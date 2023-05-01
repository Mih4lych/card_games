ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "test_project"
  )

val enumeratumVersion = "1.7.2"
val catsVersion = "2.9.0"
val catsEffectVersion = "3.4.8"

libraryDependencies ++= Seq(
  "com.beachape" %% "enumeratum" % enumeratumVersion
)
libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
)
libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
)