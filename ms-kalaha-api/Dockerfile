FROM openjdk:18-slim-buster

WORKDIR /code/

COPY --chown=appuser ./build/libs/ms-kalaha-api.jar .
RUN chmod 400 ms-kalaha-api.jar

ENTRYPOINT ["java", "-jar", "ms-kalaha-api.jar", "--server.port=8080"]