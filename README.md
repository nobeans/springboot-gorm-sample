# Spring Boot - Book Rest API Sample

## How to run

You can build and run this sample using Gradle wrapper:

```
$ ./gradlew run
```

Then access the app via a browser (or curl) on http://localhost:8080/api/books.


## How to build a JAR file

If you want to run the application outside of Gradle, then first build the JARs
and then use the `java` command:

```
$ gradle build
$ java -jar build/libs/*.jar
```

