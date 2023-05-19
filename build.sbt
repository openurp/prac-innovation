import org.openurp.parent.Settings._
import org.openurp.parent.Dependencies._
import org.beangle.tools.sbt.Sas

ThisBuild / organization := "org.openurp.prac.innovation"
ThisBuild / version := "0.0.8-SNAPSHOT"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/openurp/prac-innovation"),
    "scm:git@github.com:openurp/prac-innovation.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id    = "chaostone",
    name  = "Tihua Duan",
    email = "duantihua@gmail.com",
    url   = url("http://github.com/duantihua")
  )
)

ThisBuild / description := "OpenURP Prac Innovation"
ThisBuild / homepage := Some(url("http://openurp.github.io/prac-innovation/index.html"))

val apiVer = "0.32.1"
val starterVer = "0.2.16"
val openurp_prac_api = "org.openurp.prac" % "openurp-prac-api" % apiVer
val openurp_stater_web = "org.openurp.starter" % "openurp-starter-web" % starterVer

lazy val root = (project in file("."))
  .enablePlugins(WarPlugin,TomcatPlugin)
  .settings(
    name := "openurp-prac-innovation-webapp",
    common,
    libraryDependencies ++= Seq(beangle_commons_file,openurp_stater_web),
    libraryDependencies ++= Seq(openurp_prac_api,beangle_ems_app,beangle_commons_file)
  )
