FROM openjdk:17

WORKDIR /app

COPY build/libs/sbapiserver-0.0.1-SNAPSHOT.jar sbapiserver.jar


CMD ["java", "-jar", "sbapiserver.jar"]