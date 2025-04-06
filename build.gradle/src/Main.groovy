plugins {
  id 'java'
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(8)) // Ensure Java 8 compatibility
  }
}

repositories {
  mavenCentral()
}

dependencies {
  // External Libraries (converted from Eclipse classpath)
  implementation files('lib/commons-compress-1.0.jar')
  implementation files('lib/commons-lang3-3.4.jar')
  implementation files('lib/gson-2.2.4.jar')
  implementation files('lib/guava-18.0.jar')
  implementation files('lib/jackson-annotations-2.1.1.jar')
  implementation files('lib/jackson-core-2.1.1.jar')
  implementation files('lib/jackson-core-asl-1.9.13.jar')
  implementation files('lib/jackson-databind-2.1.1.jar')
  implementation files('lib/jackson-dataformat-csv-2.1.1.jar')
  implementation files('lib/jackson-mapper-asl-1.9.13.jar')
  implementation files('lib/JDA-4.0.0_39-javadoc.jar')
  implementation files('lib/JDA-4.0.0_39-sources.jar')
  implementation files('lib/JDA-4.0.0_39-withDependencies.jar')
  implementation files('lib/motiservice-api-1-0-2.jar')
  implementation files('lib/Motivote-server.jar')
  implementation files('lib/mvgate3.jar')
  implementation files('lib/mysql.jar')
  implementation files('lib/netty-3.5.8.Final.jar')
  implementation files('lib/teamgames-api-v1.jar')
  implementation files('lib/xpp3_min-1.1.4c.jar')
  implementation files('lib/xpp3-1.1.4c.jar')
  implementation files('lib/jsoup-1.13.1.jar')
  implementation files('lib/google-api-client-2.0.0.jar')
  implementation files('lib/checker-qual-3.12.0.jar')
  implementation files('lib/commons-codec-1.15.jar')
  implementation files('lib/commons-logging-1.2.jar')
  implementation files('lib/error_prone_annotations-2.11.0.jar')
  implementation files('lib/failureaccess-1.0.1.jar')
  implementation files('lib/google-api-client-2.2.0.jar')
  implementation files('lib/google-api-services-customsearch-v1-rev20230319-2.0.0.jar')
  implementation files('lib/google-http-client-1.42.3.jar')
  implementation files('lib/google-http-client-apache-v2-1.42.3.jar')
  implementation files('lib/google-http-client-gson-1.42.3.jar')
  implementation files('lib/google-oauth-client-1.34.1.jar')
  implementation files('lib/grpc-context-1.27.2.jar')
  implementation files('lib/gson-2.10.jar')
  implementation files('lib/guava-31.1-jre.jar')
  implementation files('lib/httpclient-4.5.14.jar')
  implementation files('lib/httpcore-4.4.16.jar')
  implementation files('lib/j2objc-annotations-1.3.jar')
  implementation files('lib/jsr305-3.0.2.jar')
  implementation files('lib/listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar')
  implementation files('lib/opencensus-api-0.31.1.jar')
  implementation files('lib/opencensus-contrib-http-util-0.31.1.jar')
  implementation files('lib/lombok.jar')

  // External JAR from a specific path
  implementation files('C:/Users/Administrator/Downloads/everythingrs-api.jar')
}

// Define where compiled output should go
sourceSets {
  main {
    java {
      srcDirs = ['src']
    }
  }
}

tasks.withType(JavaCompile) {
  options.encoding = 'UTF-8'
}

