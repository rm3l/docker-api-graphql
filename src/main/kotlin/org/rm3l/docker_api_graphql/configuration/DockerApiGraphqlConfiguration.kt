package org.rm3l.docker_api_graphql.configuration

import com.coxautodev.graphql.tools.SchemaParser
import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerCertificates
import com.spotify.docker.client.messages.ContainerInfo
import com.spotify.docker.client.messages.ImageInfo
import graphql.schema.GraphQLSchema
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import graphql.servlet.SimpleGraphQLServlet
import org.rm3l.docker_api_graphql.resolver.*
import org.rm3l.docker_api_graphql.scalars.Date
import org.rm3l.docker_api_graphql.scalars.StringSet
import org.rm3l.docker_api_graphql.scalars.StringAnyMap
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
                .dictionary("HostVersion", com.spotify.docker.client.messages.Version::class.java)
                .dictionary("ContainerDetails", ContainerInfo::class.java)
                .dictionary("ImageDetails", ImageInfo::class.java)
                .dictionary("SwarmVersion", com.spotify.docker.client.messages.swarm.Version::class.java)
                .dictionary("SwarmNode", com.spotify.docker.client.messages.swarm.Node::class.java)
                .scalars(Date(), StringAnyMap(), StringSet())
                .resolvers(Query(dockerClient),
                        ContainerResolver(dockerClient),
                        ImageResolver(dockerClient),
                        SwarmResolver(dockerClient),
                        NodeResolver(dockerClient))
                .build()
                .makeExecutableSchema()
    }

    @Bean
    fun graphQLServletRegistrationBean(schema: GraphQLSchema): ServletRegistrationBean {
        return ServletRegistrationBean(SimpleGraphQLServlet(schema), "/graphql")
    }

}
