ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "test_project"
  )

val enumeratumVersion = "1.7.2"
val catsVersion = "2.9.0"
val catsEffectVersion = "3.4.8"
val doobieVersion = "1.0.0-RC1"
val newTypeVersion = "0.4.4"
val http4sVersion = "0.23.18"

libraryDependencies ++= Seq(
  "com.beachape" %% "enumeratum" % enumeratumVersion
)
libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
)
libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
)
libraryDependencies ++= Seq(
  "org.tpolecat" %% "doobie-core"     % doobieVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion,
  "org.tpolecat" %% "doobie-hikari"   % doobieVersion,
  "io.estatico"  %% "newtype"         % newTypeVersion
)
libraryDependencies += "co.fs2" %% "fs2-core" % "3.6.1"
libraryDependencies += "co.fs2" %% "fs2-io" % "3.6.1"
libraryDependencies += "io.circe" %% "circe-generic" % "0.14.5"
libraryDependencies += "org.http4s" %% "http4s-circe" % http4sVersion

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-ember-client" % http4sVersion,
  "org.http4s" %% "http4s-ember-server" % http4sVersion,
  "org.http4s" %% "http4s-dsl"          % http4sVersion,
)

addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full)