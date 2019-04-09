enablePlugins(spray.boilerplate.BoilerplatePlugin, ScalaJSPlugin)

crossScalaVersions := List("2.12.8", "2.13.0-RC1")
scalaVersion := "2.12.8"
organization := "com.olvind"
version := "2.0.0"
scalacOptions ++= {
  if (scalaJSVersion.startsWith("0.6.")) Seq("-P:scalajs:sjsDefinedByDefault")
  else Nil
}

scalaJSUseMainModuleInitializer := true

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.6"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
homepage := Some(new URL("http://github.com/oyvindberg/ScalablyTyped-runtime"))
startYear := Some(2018)
pomExtra := (
  <scm>
    <connection>scm:git:github.com:oyvindberg/ScalablyTyped-runtime</connection>
    <developerConnection>scm:git:git@github.com:oyvindberg/ScalablyTyped-runtime.git</developerConnection>
    <url>github.com:oyvindberg/ScalablyTyped-runtime.git</url>
  </scm>
    <developers>
      <developer>
        <id>oyvindberg</id>
        <name>Øyvind Raddum Berg</name>
      </developer>
    </developers>
)
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
