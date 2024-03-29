package de.megaera.mongo_cats_effect.repo

import cats.implicits._
import cats.effect.kernel.Concurrent
import de.megaera.mongo_cats_effect.model.{Joke, JokeError}
import fs2.Stream
import org.http4s.Method.GET
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.implicits._

object JokesWebRepository {
  def apply[F[_]: Concurrent](C: Client[F]): JokesReadRepository[F] = new JokesReadRepository[F]{
    val dsl = new Http4sClientDsl[F]{}
    import dsl._
    def get: Stream[F, Joke] = {
      val response = C.expect[Joke](GET(uri"https://icanhazdadjoke.com/")).adaptError{ case t => JokeError(t)} // Prevent Client Json Decoding Failure Leaking
      Stream.eval(response).repeatN(50)
    }
  }
}
