FROM openjdk:17
MAINTAINER org.madu
COPY target/*.jar app.jar
CMD ["java", "-XX:MaxRAMPercentage=80.0", "-XX:+UseParallelGC", "-jar", "/app.jar"]