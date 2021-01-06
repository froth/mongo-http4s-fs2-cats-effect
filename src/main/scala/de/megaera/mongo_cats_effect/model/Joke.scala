package de.megaera.mongo_cats_effect.model

import cats.Applicative
import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import medeia.codec.{BsonCodec, BsonDocumentCodec}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

final case class Joke(joke: String) extends AnyVal
object Joke {
  implicit val jokeDecoder: Decoder[Joke] = deriveDecoder[Joke]
  implicit def jokeEntityDecoder[F[_]: Sync]: EntityDecoder[F, Joke] =
    jsonOf
  implicit val jokeEncoder: Encoder[Joke] = deriveEncoder[Joke]
  implicit def jokeEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Joke] =
    jsonEncoderOf

  implicit val jokeBsonCodec: BsonDocumentCodec[Joke] = BsonCodec.derive[Joke]
}

final case class JokeError(e: Throwable) extends RuntimeException

