package de.megaera.mongo_cats_effect.JokesRepository

import cats.effect.{ConcurrentEffect, Sync}
import de.megaera.mongo_cats_effect.model.Joke
import fs2.{Pipe, Stream}
import fs2.interop.reactivestreams._
import org.mongodb.scala.{Document, MongoCollection}
import medeia.syntax._

trait JokesMongoRepository[F[_]] extends JokesReadRepository[F] with JokesWriteRepository[F]

object JokesMongoRepository {
  def apply[F[_] : ConcurrentEffect](C: MongoCollection[Document]): JokesMongoRepository[F] = new JokesMongoRepository[F] {
    override def get: Stream[F, Joke] = {
      for {
        document <- C.find().toStream
        joke <- Stream.fromEither(document.fromBson[Joke].left.map(_.head))
      } yield joke
    }

    override def write: Pipe[F, Joke, UpdateCount] = in => in.chunkN(10, allowFewer = true).flatMap(joke => writeBatch(joke.toList))

    private[this] def writeBatch(jokes: List[Joke]): Stream[F, UpdateCount] = {
      val jokeDocuments: List[Document] = jokes.map(_.toBsonDocument)
      C.insertMany(jokeDocuments).toStream.map(updateResult => UpdateCount(updateResult.getInsertedIds.size()))
    }
  }
}

