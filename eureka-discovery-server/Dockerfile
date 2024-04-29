FROM bellsoft/liberica-openjdk-alpine-musl:17
WORKDIR /app
COPY target/eureka-discovery-server-1.0.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]