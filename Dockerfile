FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/pos-system-0.0.1-SNAPSHOT.jar pos-app.jar
ENV PORT=8081
EXPOSE $PORT
CMD ["java", "-jar", "pos-app.jar"]