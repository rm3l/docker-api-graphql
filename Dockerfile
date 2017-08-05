FROM java:8-jdk-alpine AS appBuild
VOLUME "/usr/src/docker-api-graphql/.gradle"
WORKDIR /usr/src/docker-api-graphql
COPY . .
RUN ./gradlew clean build --stacktrace

FROM java:8-jre-alpine
VOLUME "/opt/certs"
ENV DOCKER_HOST unix:///var/run/docker.sock
ENV DOCKER_CERT_PATH /opt/certs
WORKDIR /app
COPY --from=appBuild /usr/src/docker-api-graphql/build/libs/docker-api-graphql-0.0.1-SNAPSHOT.war ./docker-api-graphql.war
ENTRYPOINT ["java", "-jar", "/app/docker-api-graphql.war"]
EXPOSE 8080
