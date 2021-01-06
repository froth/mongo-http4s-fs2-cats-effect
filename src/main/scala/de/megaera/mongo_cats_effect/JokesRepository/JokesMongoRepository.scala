package de.megaera.mongo_cats_effect.JokesRepository

import cats.effect.{ConcurrentEffect, Sync}
import de.megaera.mongo_cats_effect.model.Joke
import fs2.{Pipe, Stream}
import fs2.interop.reactivestreams._
import org.mongodb.scala.{Document, MongoCollection}

trait JokesMongoRepository[F[_]] extends JokesReadRepository[F] with JokesWriteRepository[F]

object JokesMongoRepository {
  def apply[F[_] : ConcurrentEffect](C: MongoCollection[Document]): JokesMongoRepository[F] = new JokesMongoRepository[F] {
    override def get: Stream[F, Joke] = {
      val find = Sync[F].delay(C.find())
      Stream.eval(find).flatMap(_.toStream).map(_ => Joke("test"))
    }

    override def write: Pipe[F, Joke, UpdateCount] = in => in.map(_ => UpdateCount(1))
  }
}

