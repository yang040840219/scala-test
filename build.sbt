logLevel := Level.Debug

name := "scala-test"

version := "1.0"

scalaVersion := "2.10.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.3.14"

libraryDependencies += "com.typesafe.akka" % "akka-remote_2.10" % "2.3.14"

libraryDependencies += "com.typesafe.akka" %% "akka-persistence-experimental" % "2.3.14"

libraryDependencies += "org.apache.zookeeper" % "zookeeper" % "3.4.6"

libraryDependencies += "org.apache.kafka" % "kafka-clients" % "0.8.2.0"

libraryDependencies += "org.apache.kafka" % "kafka_2.10" % "0.8.2.0"

libraryDependencies += "com.codahale.metrics" % "metrics-core" % "3.0.2"

libraryDependencies += "com.codahale.metrics" % "metrics-servlets" % "3.0.2"

libraryDependencies += "io.dropwizard.metrics" % "metrics-healthchecks" % "3.1.2"

libraryDependencies += "org.eclipse.jetty" % "jetty-server" % "8.1.14.v20131031"

libraryDependencies += "org.eclipse.jetty" % "jetty-servlet" % "8.1.14.v20131031"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.12"

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.12"

libraryDependencies += "org.elasticsearch" % "metrics-elasticsearch-reporter" % "2.0"

libraryDependencies += "org.elasticsearch" % "elasticsearch" % "2.0.0"

libraryDependencies += "com.twitter" % "finagle-redis_2.10" % "6.31.0"

libraryDependencies += "com.twitter" %% "util-collection" % "6.27.0"

libraryDependencies += "org.scala-lang" % "scala-actors" % "2.10.4"



