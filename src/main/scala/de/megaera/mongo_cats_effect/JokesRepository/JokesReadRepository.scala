package de.megaera.mongo_cats_effect.JokesRepository

import de.megaera.mongo_cats_effect.model.Joke
import fs2.Stream

trait JokesReadRepository[F[_]]{
  def get: Stream[F, Joke]
}

