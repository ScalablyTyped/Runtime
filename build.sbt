enablePlugins(spray.boilerplate.BoilerplatePlugin, ScalaJSPlugin)

crossScalaVersions := List("2.12.11", "2.13.3", "3.0.0-M2")
scalaVersion := crossScalaVersions.value.head
organization := "com.olvind"
version := "2.2.0"
name := "scalablytyped-runtime"
scalaJSUseMainModuleInitializer := true

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
homepage := Some(new URL("http://github.com/ScalablyTyped/Runtime"))
startYear := Some(2018)
pomExtra := (
  <scm>
    <connection>scm:git:github.com:ScalablyTyped/Runtime</connection>
    <developerConnection>scm:git:git@github.com:ScalablyTyped/Runtime.git</developerConnection>
    <url>github.com:ScalablyTyped/Runtime.git</url>
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
