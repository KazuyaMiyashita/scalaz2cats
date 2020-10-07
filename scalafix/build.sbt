lazy val V = _root_.scalafix.sbt.BuildInfo

inThisBuild(
  List(
    scalaVersion := V.scala213,
    addCompilerPlugin(scalafixSemanticdb),
    scalacOptions ++= List(
      "-Yrangepos"
    )
  )
)


lazy val rules = project.settings(
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
  )

lazy val input = project.settings(
    libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.30"
  )

lazy val output = project.settings(
  libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0",
  libraryDependencies += "org.typelevel" %% "mouse" % "0.16"
)

lazy val tests = project
  .settings(
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full,
    scalafixTestkitOutputSourceDirectories :=
      sourceDirectories.in(output, Compile).value,
    scalafixTestkitInputSourceDirectories :=
      sourceDirectories.in(input, Compile).value,
    scalafixTestkitInputClasspath :=
      fullClasspath.in(input, Compile).value
  )
  .dependsOn(rules)
  .enablePlugins(ScalafixTestkitPlugin)