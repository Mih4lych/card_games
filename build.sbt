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