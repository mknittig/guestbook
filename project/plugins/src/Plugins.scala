import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val jawsyMavenReleases = "Jawsy.fi M2 releases" at "http://oss.jawsy.fi/maven2/releases"
  val jrebelPlugin = "fi.jawsy" % "sbt-jrebel-plugin" % "0.1.0"
  
  lazy val eclipsify = "de.element34" % "sbt-eclipsify" % "0.5.0"
}
