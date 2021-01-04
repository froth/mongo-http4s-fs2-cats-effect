package de.megaera.mongo_cats_effect

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    Server.stream[IO].compile.drain.as(ExitCode.Success)
}
