FROM amazoncorretto:11-alpine
COPY target/application-0.0.1-SNAPSHOT.jar application-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/application-0.0.1-SNAPSHOT.jar"]