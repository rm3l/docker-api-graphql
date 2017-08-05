# Docker API GraphQL

<p align="center"><img src="https://raw.githubusercontent.com/rm3l/docker-api-graphql/master/assets/docker-api-graphql.png" width=96 alt="Docker API GraphQL"></p>

> A GraphQL API Server around the Docker Remote API. SpringBoot-based app, written in Kotlin.

[![Travis branch](https://img.shields.io/travis/rm3l/docker-api-graphql/master.svg)](https://travis-ci.org/rm3l/docker-api-graphql) 
[![License](https://img.shields.io/badge/license-MIT-green.svg?style=flat)](https://github.com/rm3l/docker-api-graphql/blob/master/LICENSE)
[![Coverage Status](https://coveralls.io/repos/github/rm3l/docker-api-graphql/badge.svg?branch=master)](https://coveralls.io/github/rm3l/docker-api-graphql?branch=master)  

---

<p align="center"><img src="https://raw.githubusercontent.com/rm3l/docker-api-graphql/master/assets/docker-api-graphiql.png" width=512 alt="Docker API GraphQL"></p>

TODO Add documentation here

## Running with Docker

This app can run as a lightweight Docker container.

To use it against a local Docker daemon listening on a local Unix socket: 
```bash
docker run \
 --name docker-api-graphql \
 -p 8080:8080 \
 -v /var/run/docker.sock:/var/run/docker.sock \
 rm3l/docker-api-graphql
```

<b>Note</b>: the `-v /var/run/docker.sock:/var/run/docker.sock` option can be used in Linux environments only.

For more sophisticated use cases of Docker daemons accessible over the network (and secured with client certificates):
```bash
docker run \
 --name docker-api-graphql \
 -p 8080:8080 \
 -e DOCKER_HOST=https://<host>:<port> \
 -v /path/to/my/.docker/folder/containing/my/certificates:/opt/certs \
 rm3l/docker-api-graphql
```

<b>Note</b>: the `/path/to/my/.docker/folder/containing/my/certificates` must be mounted under `/opt/certs` inside the running container, and must follow the semantics behind [the DOCKER_CERT_PATH environment variable](https://docs.docker.com/articles/https/#client-modes)  

You'll just need to access the port 8080 of the Docker engine where `docker-api-graphql` is running using your browser. 

Deploying this API to manage a Swarm cluster is just as easy ! You can directly deploy it as a service in your Docker cluster:
```bash
docker service create \
 --name docker-api-graphql \
 --publish 8080:8080 \
 --constraint 'node.role == manager' \
 --mount type=bind,src=//var/run/docker.sock,dst=/var/run/docker.sock \
 rm3l/docker-api-graphql \
 -H unix:///var/run/docker.sock
```

## API Endpoints

### /graphiql

This gives you access to a GraphiQL application in your browser, so you can play with the GraphQL API

### /graphql

This is the entrypoint for the GraphQL API.


## Building from source

You can build `docker-api-graphql` in the same way as any Gradle project on Git.

However, thanks to the [Gradle Wrapper](https://docs.gradle.org/3.3/userguide/gradle_wrapper.html) (cf. `gradlew` and `gradlew.bat` scripts), 
you do not need to have Gradle installed on your machine.

1. Clone the `docker-api-graphql` repository on your machine:
```bash
git clone https://github.com/rm3l/docker-api-graphql && cd docker-api-graphql
```
2. Switch to the appropriate branch if needed with `git checkout ...`
3. Execute a Gradle build in the directory containing the `build.gradle` file:
```bash
./gradlew build
```
4. You will find the WAR artifact under the `build/libs` directory.

## Contributing and Improving docker-api-graphql!

Contributions and issue reporting are more than welcome. 
So to help out, do feel free to fork this repo and open up a pull request. 
I'll review and merge your changes as quickly as possible.

You can use [GitHub issues](https://github.com/rm3l/docker-api-graphql/issues) to report bugs. 
However, please make sure your description is clear enough and has sufficient instructions 
to be able to reproduce the issue.

[comment]: <> (See CONTRIBUTING.md for more on contributing to this Github project.)


## TODO

* [ ] GraphQL Mutations
* [ ] GraphQL Subscriptions if possible, especially for Docker events
* [ ] Authentication / Authorization


## Open-Source Libraries and Tools

* [Kotlin](https://kotlinlang.org/), by Jetbrains
* [Spring Boot](https://projects.spring.io/spring-boot/)
* [docker-client](https://github.com/spotify/docker-client), by Spotify
* [graphql-java](https://github.com/graphql-java/graphql-java)


## Developed by

* Armel Soro
  * [keybase.io/rm3l](https://keybase.io/rm3l)
  * [rm3l.org](https://rm3l.org) - &lt;apps+docker-api-graphql@rm3l.org&gt; - [@rm3l](https://twitter.com/rm3l)
  * [paypal.me/rm3l](https://paypal.me/rm3l)
  * [coinbase.com/rm3l](https://www.coinbase.com/rm3l)


## LICENSE

    The MIT License (MIT)
    
    Copyright (c) 2017 Armel Soro
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

