// all common settings
lazy val commonSettings = Seq(
  scalaVersion := "2.11.11",
  organization := "it.jesinity.playground",
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)

lazy val domain = project
  .settings(commonSettings: _*)
  .settings(
    name := "domain",
    PB.targets in Compile := Seq(
      scalapb.gen() -> (sourceManaged in Compile).value
    )
  )

val versionAvro = "1.8.1"

lazy val app = project
  .settings(commonSettings: _*)
  .settings(
    name := "app",
    libraryDependencies ++= Seq(
      "org.apache.avro"     % "avro"            % versionAvro,
      "com.sksamuel.avro4s" %% "avro4s-core"    % versionAvro,
      "com.twitter"         %% "bijection-avro" % "0.9.6",
      "io.spray"            %% "spray-json"     % "1.3.3"
    )
  )
  .dependsOn(domain)

lazy val playground_protobuf_scala = project
  .in(file("."))
  .settings(commonSettings: _*)
  .aggregate(domain, app)
