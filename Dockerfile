FROM openjdk:8-alpine
COPY ./target/semesteroverview-1.0.0.jar /opt/dhbw-overview/semesteroverview-1.0.0.jar

ENTRYPOINT ["java", "-jar", "/opt/dhbw-overview/semesteroverview-1.0.0.jar"]