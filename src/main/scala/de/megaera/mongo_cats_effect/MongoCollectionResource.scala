package de.megaera.mongo_cats_effect

import cats.effect.{Resource, Sync}
import org.mongodb.scala.{Document, MongoClient, MongoClientSettings, MongoCollection}

object MongoCollectionResource {
  def create[F[_]]()(implicit F: Sync[F]) : Resource[F, MongoCollection[Document]] = {
    Resource.make(F.delay(MongoClient(mongoBuilder.build()))){ client => F.delay(client.close())}.map(_.getDatabase("jokes").getCollection("jokes"))
  }

  private[this] def mongoBuilder: MongoClientSettings.Builder = {
    MongoClientSettings.builder()
  }
}
