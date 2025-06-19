import org.openurp.parent.Dependencies.*
import org.openurp.parent.Settings.*

ThisBuild / organization := "org.openurp.prac.innovation"
ThisBuild / version := "0.0.9-SNAPSHOT"

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

val apiVer = "0.44.0"
val starterVer = "0.3.58"
val baseVer = "0.4.51"
val openurp_prac_api = "org.openurp.prac" % "openurp-prac-api" % apiVer
val openurp_stater_web = "org.openurp.starter" % "openurp-starter-web" % starterVer
val openurp_base_tag = "org.openurp.base" % "openurp-base-tag" % baseVer

lazy val root = (project in file("."))
  .enablePlugins(WarPlugin,TomcatPlugin)
  .settings(
    name := "openurp-prac-innovation-webapp",
    common,
    libraryDependencies ++= Seq(openurp_stater_web),
    libraryDependencies ++= Seq(openurp_prac_api,beangle_ems_app,openurp_base_tag)
  )
