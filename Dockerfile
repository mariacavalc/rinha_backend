FROM openjdk:17
MAINTAINER org.madu
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]