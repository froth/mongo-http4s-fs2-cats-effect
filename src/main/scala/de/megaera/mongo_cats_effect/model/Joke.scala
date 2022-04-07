package de.megaera.mongo_cats_effect.model

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import medeia.codec.{BsonCodec, BsonDocumentCodec}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import cats.effect.kernel.Concurrent

final case class Joke(joke: String) extends AnyVal
object Joke {
  implicit val jokeDecoder: Decoder[Joke] = deriveDecoder[Joke]
  implicit def jokeEntityDecoder[F[_]: Concurrent]: EntityDecoder[F, Joke] =
    jsonOf
  implicit val jokeEncoder: Encoder[Joke] = deriveEncoder[Joke]
  implicit def jokeEntityEncoder[F[_]]: EntityEncoder[F, Joke] =
    jsonEncoderOf

  implicit val jokeBsonCodec: BsonDocumentCodec[Joke] = BsonCodec.derive[Joke]
}

final case class JokeError(e: Throwable) extends RuntimeException

