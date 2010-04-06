import sbt._
import de.element34.sbteclipsify._
import fi.jawsy.sbtplugins.jrebel.JRebelWebPlugin

class Project(info: ProjectInfo) extends DefaultWebProject(info) with SbtEclipsifyPlugin with JRebelWebPlugin {
  val scalaToolsSnapshots = "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"
  val smackRepo = "m2-repository-smack" at "http://maven.reucon.com/public"
   
  val liftCore = "net.liftweb" % "lift-core" % "2.0-scala280-SNAPSHOT"
  val servletApi = "javax.servlet" % "servlet-api" % "2.5" % "provided"
  
  val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.14" % "test"
  val scalaCompiler = "org.scala-lang" % "scala-compiler" % "2.8.0.Beta1" % "test"
  val scalatest = "org.scalatest" % "scalatest" % "1.0.1-for-scala-2.8.0.Beta1-with-test-interfaces-0.3-SNAPSHOT" % "test"

  override def scanDirectories = Nil
}
