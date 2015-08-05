name := """grader-prototype"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "commons-io" % "commons-io" % "2.4"
)

val appDependencies = Seq(
  // Add your project dependencies here,

  "mysql" % "mysql-connector-java" % "5.1.18"

)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

mappings in Universal <++= (packageBin in Compile) map { jar =>
  val scriptsDir = new java.io.File("template/")
  scriptsDir.listFiles.toSeq.map { f =>

    f -> ("template/" + f.getName)
  }
}

mappings in Universal <++= (packageBin in Compile) map { jar =>
  val scriptsDir = new java.io.File("testcase/")
  scriptsDir.listFiles.toSeq.map { f =>

    f -> ("testcase/" + f.getName)
  }
}