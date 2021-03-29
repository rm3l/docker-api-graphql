#
# Copyright (c) 2017 Armel Soro
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
#  of this software and associated documentation files (the "Software"), to deal
#  in the Software without restriction, including without limitation the rights
#  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
#  copies of the Software, and to permit persons to whom the Software is
#  furnished to do so, subject to the following conditions:
#
#  The above copyright notice and this permission notice shall be included in all
#  copies or substantial portions of the Software.
#
#  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
#  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
#  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
#  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
#  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
#  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
#  SOFTWARE.
#
FROM adoptopenjdk:16-jdk-openj9 AS appBuild
MAINTAINER Armel Soro <armel+docker_api_graphql@rm3l.org>
VOLUME "/usr/src/docker-api-graphql/.gradle"
WORKDIR /usr/src/docker-api-graphql
COPY . .
RUN ./gradlew clean build --stacktrace

FROM adoptopenjdk:16-jdk-openj9
MAINTAINER Armel Soro <armel+docker_api_graphql@rm3l.org>
LABEL org.rm3l.docker-api-graphql.is-beta="true" \
    org.rm3l.docker-api-graphql.version="0.1.1" \
    org.rm3l.docker-api-graphql.vcs.url="https://github.com/rm3l/docker-api-graphql"
VOLUME "/etc/docker"
ENV DOCKER_HOST unix:///var/run/docker.sock
ENV DOCKER_CERT_PATH /etc/docker
WORKDIR /app
COPY --from=appBuild "/usr/src/docker-api-graphql/build/libs/docker-api-graphql-0.1.1.jar" "./docker-api-graphql.jar"
ENTRYPOINT ["java", "-Dserver.port=8080", "-jar", "/app/docker-api-graphql.jar"]
EXPOSE 8080
