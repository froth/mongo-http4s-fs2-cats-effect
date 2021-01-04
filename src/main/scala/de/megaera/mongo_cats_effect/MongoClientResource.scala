package de.megaera.mongo_cats_effect

import cats.effect.{Resource, Sync}
import org.mongodb.scala.{MongoClient, MongoClientSettings}

object MongoClientResource {
  def create[F[_]]()(implicit F: Sync[F]) : Resource[F, MongoClient] =
    Resource.make(F.delay(MongoClient(mongoBuilder.build()))){ client => F.delay(client.close())}

  private[this] def mongoBuilder: MongoClientSettings.Builder = {
    MongoClientSettings.builder()
  }
}
