package org.rm3l.docker_api_graphql.resolver

import com.coxautodev.graphql.tools.GraphQLRootResolver
import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.DockerClient.ListContainersParam.allContainers
import com.spotify.docker.client.DockerClient.ListContainersParam.containersCreatedBefore
import com.spotify.docker.client.DockerClient.ListContainersParam.containersCreatedSince
import com.spotify.docker.client.DockerClient.ListContainersParam.filter
import com.spotify.docker.client.DockerClient.ListContainersParam.limitContainers
import com.spotify.docker.client.DockerClient.ListContainersParam.withContainerSizes
import com.spotify.docker.client.DockerClient.ListContainersParam.withExitStatus
import com.spotify.docker.client.messages.Container
import org.rm3l.docker_api_graphql.configuration.DockerApiGraphqlConfiguration
import org.rm3l.docker_api_graphql.resources.ContainerDetails
import org.rm3l.docker_api_graphql.resources.ContainerFilter
import org.rm3l.docker_api_graphql.resources.HostInfo
import org.slf4j.LoggerFactory

class Query(val dockerClient: DefaultDockerClient): GraphQLRootResolver {

    private val logger = LoggerFactory.getLogger(DockerApiGraphqlConfiguration::class.java)

    fun host(): HostInfo {
        logger.trace("host()")
        return HostInfo(dockerClient.info(), dockerClient.version())
    }

    fun containers(all: Boolean?, limit: Int?, size: Boolean?, filter: ContainerFilter?):
            List<Container> {
        logger.trace("containers()")
        val listOfParams = mutableListOf(allContainers(all ?: false))
        limit?.let { listOfParams.add(limitContainers(it)) }
        size?.let { listOfParams.add(withContainerSizes(it)) }
        filter?.let {
            it.ancestor?.let { listOfParams.add(filter("ancestor", it)) }
            it.before?.let { listOfParams.add(containersCreatedBefore(it)) }
            it.expose?.let { listOfParams.add(filter("expose", it)) }
            it.exited?.let { listOfParams.add(withExitStatus(it)) }
            it.health?.let { listOfParams.add(filter("health", it.name)) }
            it.id?.let { listOfParams.add(filter("id", it)) }
            it.isolation?.let { listOfParams.add(filter("isolation", it.name)) }
            it.is_task?.let { listOfParams.add(filter("is-task", if (it) "true" else "false")) }
            it.label?.let { listOfParams.add(filter("label", it)) }
            it.name?.let { listOfParams.add(filter("name", it)) }
            it.network?.let { listOfParams.add(filter("network", it)) }
            it.publish?.let { listOfParams.add(filter("publish", it)) }
            it.since?.let { listOfParams.add(containersCreatedSince(it)) }
            it.status?.forEach { listOfParams.add(filter("status", it.name)) }
            it.volume?.let { listOfParams.add(filter("volume", it)) }
        }
        return dockerClient.listContainers(*listOfParams.toTypedArray())
    }
}
