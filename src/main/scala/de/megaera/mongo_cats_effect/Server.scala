package de.megaera.mongo_cats_effect

import cats.effect._
import cats.syntax.all._
import com.comcast.ip4s._
import fs2.Stream
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import de.megaera.mongo_cats_effect.repo._
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.Logger
import org.http4s.server.middleware.{Logger => LoggerMiddleware} 

object Server {
  implicit def unsafeLogger[F[_]: Sync] = Slf4jLogger.getLogger[F]

  /*
    Import jokes from internet into mongo on startup and stream via http on request
   */
  def stream[F[_]: Async]: Stream[F, Nothing] = {
    for {
      httpClient <- Stream.resource(EmberClientBuilder.default[F].build)
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

      exitCode <- Stream.resource(EmberServerBuilder.default[F]
         .withHost(ipv4"0.0.0.0")
         .withPort(port"8080")
        .withHttpApp(finalHttpApp)
        .build >> Resource.eval(Async[F].never)
      )
    } yield exitCode
  }.drain
}
