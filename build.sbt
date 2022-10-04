libraryDependencies += "com.github.jsqlparser" % "jsqlparser" % "3.2"
libraryDependencies += "org.mongodb" % "mongo-java-driver" % "3.12.11"

libraryDependencies ++= Seq(
  "net.aichler" % "jupiter-interface" % JupiterKeys.jupiterVersion.value % Test
)

