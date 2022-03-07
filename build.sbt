import org.openurp.parent.Settings._
import org.openurp.parent.Dependencies._
import org.beangle.tools.sbt.Sas

ThisBuild / organization := "org.openurp.prac.innovation"
ThisBuild / version := "0.0.6-SNAPSHOT"

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

ThisBuild / description := "OpenURP Starter"
ThisBuild / homepage := Some(url("http://openurp.github.io/prac-innovation/index.html"))

val apiVer = "0.23.3"
val starterVer = "0.0.12"
val baseVer = "0.1.21"
val openurp_base_api = "org.openurp.base" % "openurp-base-api" % apiVer
val openurp_std_api = "org.openurp.std" % "openurp-std-api" % apiVer
val openurp_stater_web = "org.openurp.starter" % "openurp-starter-web" % starterVer
val openurp_base_tag = "org.openurp.base" % "openurp-base-tag" % baseVer

lazy val root = (project in file("."))
  .settings()
  .aggregate(core,webapp)

lazy val core = (project in file("core"))
  .settings(
    name := "openurp-prac-innovation-core",
    common,
    libraryDependencies ++= Seq(openurp_base_api,openurp_std_api,beangle_ems_app,beangle_commons_file)
  )

lazy val webapp = (project in file("webapp"))
  .enablePlugins(WarPlugin)
  .settings(
    name := "openurp-prac-innovation-webapp",
    common,
    libraryDependencies ++= Seq(beangle_commons_file,openurp_stater_web),
    libraryDependencies ++= Seq(Sas.Tomcat % "test")
  ).dependsOn(core)

publish / skip := true
