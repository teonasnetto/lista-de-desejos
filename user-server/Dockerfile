FROM bellsoft/liberica-openjdk-alpine-musl:17
COPY target/user-server-1.0.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]