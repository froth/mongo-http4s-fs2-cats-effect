val Http4sVersion = "0.21.15"
val CirceVersion = "0.13.0"
val MunitVersion = "0.7.20"
val LogbackVersion = "1.2.3"
val MunitCatsEffectVersion = "0.12.0"
val MongoScalaDriverVersion = "4.1.1"
val MedeiaVersion = "0.4.2"

lazy val root = (project in file("."))
  .settings(
    organization := "de.megaera",
    name := "mongo-http4s-fs2-cats-effect",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.4",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "org.scalameta" %% "munit" % MunitVersion % Test,
      "org.typelevel" %% "munit-cats-effect-2" % MunitCatsEffectVersion % Test,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "org.mongodb.scala" %% "mongo-scala-driver" % MongoScalaDriverVersion,
      "co.fs2" %% "fs2-reactive-streams" % "2.5.0",
      "de.megaera" %% "medeia" % MedeiaVersion,
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework")
  )
