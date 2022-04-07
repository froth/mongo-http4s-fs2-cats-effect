package de.megaera.mongo_cats_effect.repo

import cats.Monoid
import cats.implicits.catsSyntaxSemigroup
import de.megaera.mongo_cats_effect.model.Joke
import fs2.Pipe

trait JokesWriteRepository[F[_]] {
  def write: Pipe[F, Joke, UpdateCount]
}

case class UpdateCount(int: Int)

object UpdateCount {
  implicit val monoid: Monoid[UpdateCount] = new Monoid[UpdateCount] {
    override def empty: UpdateCount = UpdateCount(0)

    override def combine(x: UpdateCount, y: UpdateCount): UpdateCount = UpdateCount(x.int |+| y.int)
  }
}
