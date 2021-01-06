package de.megaera.mongo_cats_effect

import cats.effect.{ConcurrentEffect, Sync, Timer}
import cats.implicits._
import de.megaera.mongo_cats_effect.JokesRepository.{JokesMongoRepository, JokesWebRepository}
import fs2.Stream
import io.chrisdavenport.log4cats.{Logger, SelfAwareStructuredLogger}
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.{Logger => LoggerMiddleware}
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger

import scala.concurrent.ExecutionContext.global

object Server {
  implicit def unsafeLogger[F[_]: Sync]: SelfAwareStructuredLogger[F] = Slf4jLogger.getLogger[F]

  /*
    Import jokes from internet into mongo on startup and stream via http on request
   */
  def stream[F[_]: ConcurrentEffect: Timer]: Stream[F, Nothing] = {
    for {
      httpClient <- BlazeClientBuilder[F](global).stream
      jokesCollection <- Stream.resource(MongoCollectionResource.create[F]())
      jokesMongoRepo = JokesMongoRepository[F](jokesCollection)
      jokesWebRepo = JokesWebRepository[F](httpClient)

      // import jokes
      _ <- jokesWebRepo.get.through(jokesMongoRepo.write).foldMonoid.evalMap(count => Logger[F].info(s"Imported $count"))

      helloWorldAlg = HelloWorld.impl[F]
      httpApp = (
        Routes.helloWorldRoutes[F](helloWorldAlg) <+>
        Routes.jokeRoutes[F](jokesMongoRepo)
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = LoggerMiddleware.httpApp(logHeaders = true, logBody = true)(httpApp)

      exitCode <- BlazeServerBuilder[F](global)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}
