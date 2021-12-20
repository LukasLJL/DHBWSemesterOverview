FROM maven:3.8-openjdk-17 AS builder
COPY . /src
WORKDIR /src
RUN mvn clean package

FROM openjdk:17-alpine
COPY --from=builder /src/target/semesteroverview-*.jar ./opt/semesteroverview.jar

ENTRYPOINT ["java", "-jar", "/opt/semesteroverview.jar"]