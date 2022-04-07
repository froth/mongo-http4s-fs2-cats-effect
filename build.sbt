val Http4sVersion = "0.23.11"
val CirceVersion = "0.14.1"
val MunitVersion = "0.7.29"
val LogbackVersion = "1.2.11"
val MunitCatsEffectVersion = "0.12.0"
val MongoScalaDriverVersion = "4.5.1"
val MedeiaVersion = "0.8.0"
val Log4CatsVersion = "2.2.0"

lazy val root = (project in file("."))
  .settings(
    organization := "de.megaera",
    name := "mongo-http4s-fs2-cats-effect",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.8",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-ember-server" % Http4sVersion,
      "org.http4s" %% "http4s-ember-client" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "org.typelevel" %% "cats-effect-kernel" % "3.3.9",
      "org.typelevel" %% "cats-effect-std"    % "3.3.9",
      "org.typelevel" %% "cats-effect"        % "3.3.9", 
      "co.fs2" %% "fs2-reactive-streams" % "3.2.7",
      "org.scalameta" %% "munit" % MunitVersion % Test,
      "org.typelevel" %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "org.mongodb.scala" %% "mongo-scala-driver" % MongoScalaDriverVersion,
      "de.megaera" %% "medeia" % MedeiaVersion,
      "org.typelevel" %% "log4cats-core"    % Log4CatsVersion,  // Only if you want to Support Any Backend
      "org.typelevel" %% "log4cats-slf4j"   % Log4CatsVersion  // Direct Slf4j Support - Recommended
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework")
  )
