FROM bellsoft/liberica-openjdk-alpine-musl:17
COPY target/config-server-1.0.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]