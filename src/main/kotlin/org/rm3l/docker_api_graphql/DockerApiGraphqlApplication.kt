package org.rm3l.docker_api_graphql

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class DockerApiGraphqlApplication

fun main(args: Array<String>) {
    SpringApplication.run(DockerApiGraphqlApplication::class.java, *args)
}
