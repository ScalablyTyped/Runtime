enablePlugins(spray.boilerplate.BoilerplatePlugin, ScalaJSPlugin)

crossScalaVersions := List("2.12.11", "2.13.3", "3.0.0")
scalaVersion := crossScalaVersions.value.head
organization := "com.olvind"
version := "2.4.2"
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

credentials += Credentials(
  "Sonatype Nexus Repository Manager",
  "oss.sonatype.org",
  sys.env("RUNTIME_SONATYPE_USER"),
  sys.env("RUNTIME_SONATYPE_PASSWORD")
)
