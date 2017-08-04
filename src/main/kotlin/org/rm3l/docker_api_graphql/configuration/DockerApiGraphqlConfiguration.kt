package org.rm3l.docker_api_graphql.configuration

import com.coxautodev.graphql.tools.SchemaParser
import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerCertificates
import com.spotify.docker.client.messages.ContainerInfo
import com.spotify.docker.client.messages.swarm.Version
import graphql.schema.GraphQLSchema
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import graphql.servlet.SimpleGraphQLServlet
import org.rm3l.docker_api_graphql.resolver.ContainerResolver
import org.rm3l.docker_api_graphql.resolver.Query
import org.rm3l.docker_api_graphql.scalars.Date
import org.rm3l.docker_api_graphql.scalars.StringSet
import org.rm3l.docker_api_graphql.scalars.StringStringMap
import org.springframework.beans.factory.annotation.Value
import java.io.File
import java.net.URI
import java.nio.file.Paths

@Configuration
class DockerApiGraphqlConfiguration {

    @Value("\${DOCKER_HOST:unix:///var/run/docker.sock}")
    private lateinit var dockerHost: String

    @Value("\${DOCKER_CERT_PATH:}")
    private lateinit var dockerCertificatePath: String

    @Value("\${CONNECTION_POOL_SIZE:100}")
    private var connectionPoolSize: Int = 100

    @Bean
    fun dockerClient(): DefaultDockerClient {
        //Initialize connector to the API
        val dockerCLientBuilder = DefaultDockerClient.builder().uri(URI.create(dockerHost))
                .connectionPoolSize(connectionPoolSize)
        if (!dockerCertificatePath.isBlank()) {
            dockerCLientBuilder.dockerCertificates(
                    DockerCertificates(Paths.get(dockerCertificatePath)))
        }
        return dockerCLientBuilder.build()
    }

    @Bean
    fun graphQLSchema(dockerClient: DefaultDockerClient): GraphQLSchema {
        return SchemaParser.newParser()
                .file("schema${File.separator}docker.graphqls")
                .dictionary("SwarmClusterVersion", Version::class.java)
                .dictionary("ContainerFullDetails", ContainerInfo::class.java)
                .scalars(Date(), StringStringMap(), StringSet())
                .resolvers(Query(dockerClient),
                        ContainerResolver(dockerClient))
                .build()
                .makeExecutableSchema()
    }

    @Bean
    fun graphQLServletRegistrationBean(schema: GraphQLSchema): ServletRegistrationBean {
        return ServletRegistrationBean(SimpleGraphQLServlet(schema), "/graphql")
    }

}
