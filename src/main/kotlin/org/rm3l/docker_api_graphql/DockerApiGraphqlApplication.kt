package org.rm3l.docker_api_graphql

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.support.SpringBootServletInitializer

@SpringBootApplication
class DockerApiGraphqlApplication: SpringBootServletInitializer() {

    override fun configure(builder: SpringApplicationBuilder?): SpringApplicationBuilder =
        builder?.sources(DockerApiGraphqlApplication::class.java)?:super.configure(builder)
}

fun main(args: Array<String>) {
    SpringApplication.run(DockerApiGraphqlApplication::class.java, *args)
}
