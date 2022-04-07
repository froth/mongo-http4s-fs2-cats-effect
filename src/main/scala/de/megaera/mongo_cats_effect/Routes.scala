package de.megaera.mongo_cats_effect

import cats.effect.Sync
import cats.implicits._
import de.megaera.mongo_cats_effect.repo.JokesReadRepository
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object Routes {

  def jokeRoutes[F[_]: Sync](J: JokesReadRepository[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "joke" =>
        Ok(J.get)
    }
  }

  def helloWorldRoutes[F[_]: Sync](H: HelloWorld[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(HelloWorld.Name(name))
          resp <- Ok(greeting)
        } yield resp
    }
  }
}