package org.rm3l.docker_api_graphql.resolver

import com.coxautodev.graphql.tools.GraphQLRootResolver
import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient.ListContainersParam.allContainers
import com.spotify.docker.client.DockerClient.ListContainersParam.limitContainers
import com.spotify.docker.client.DockerClient.ListContainersParam.withContainerSizes
import com.spotify.docker.client.DockerClient.ListContainersParam.create
import com.spotify.docker.client.messages.Container
import org.rm3l.docker_api_graphql.configuration.DockerApiGraphqlConfiguration
import org.rm3l.docker_api_graphql.resources.HostInfo
import org.slf4j.LoggerFactory

class Query(val dockerClient: DefaultDockerClient): GraphQLRootResolver {

    private val logger = LoggerFactory.getLogger(DockerApiGraphqlConfiguration::class.java)

    fun host(): HostInfo {
        logger.trace("host()")
        return HostInfo(dockerClient.info(), dockerClient.version())
    }

    fun containers(all: Boolean?, limit: Int?, size: Boolean?, filter: Map<String,String>?):
            List<Container> {
        logger.trace("containers()")
        val listOfParams = mutableListOf(allContainers(all ?: false))
        limit?.let { listOfParams.add(limitContainers(it)) }
        size?.let { listOfParams.add(withContainerSizes(it)) }
        filter?.forEach { key, value -> listOfParams.add(create(key,value))}
        return dockerClient.listContainers(*listOfParams.toTypedArray())
    }
}
