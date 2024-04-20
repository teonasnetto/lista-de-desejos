FROM openjdk:17-jdk-slim

EXPOSE 8080

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn

COPY pom.xml .

RUN ./mvnw dependency:go-offline -B

COPY src src

# removido o -DskipTests para gerar o relatório do JaCoCo
RUN ./mvnw package

COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]