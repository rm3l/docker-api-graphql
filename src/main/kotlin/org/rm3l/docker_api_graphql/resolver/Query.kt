package org.rm3l.docker_api_graphql.resolver

import com.coxautodev.graphql.tools.GraphQLRootResolver
import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.DockerClient.ListContainersParam.allContainers
import com.spotify.docker.client.DockerClient.ListContainersParam.limitContainers
import com.spotify.docker.client.DockerClient.ListContainersParam.withContainerSizes
import com.spotify.docker.client.DockerClient.ListContainersParam.create
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

    fun containers(details: Boolean?,
                   all: Boolean?, limit: Int?, size: Boolean?, filter: ContainerFilter?):
            List<ContainerDetails> {
        logger.trace("containers()")
        val listOfParams = mutableListOf(allContainers(all ?: false))
        limit?.let { listOfParams.add(limitContainers(it)) }
        size?.let { listOfParams.add(withContainerSizes(it)) }
        filter?.let {
            it.ancestor?.let { listOfParams.add(DockerClient.ListContainersParam.filter("ancestor", it)) }
            it.before?.let { listOfParams.add(DockerClient.ListContainersParam.containersCreatedBefore(it)) }
            it.expose?.let { listOfParams.add(DockerClient.ListContainersParam.filter("expose", it)) }
            it.exited?.let { listOfParams.add(DockerClient.ListContainersParam.withExitStatus(it)) }
            it.health?.let { listOfParams.add(DockerClient.ListContainersParam.filter("health", it.name)) }
            it.id?.let { listOfParams.add(DockerClient.ListContainersParam.filter("id", it)) }
            it.isolation?.let { listOfParams.add(DockerClient.ListContainersParam.filter("isolation", it.name)) }
            it.is_task?.let { listOfParams.add(DockerClient.ListContainersParam.filter("is-task",
                    if (it) "true" else "false")) }
            it.label?.let { listOfParams.add(DockerClient.ListContainersParam.filter("label", it)) }
            it.name?.let { listOfParams.add(DockerClient.ListContainersParam.filter("name", it)) }
            it.network?.let { listOfParams.add(DockerClient.ListContainersParam.filter("network", it)) }
            it.publish?.let { listOfParams.add(DockerClient.ListContainersParam.filter("publish", it)) }
            it.since?.let { listOfParams.add(DockerClient.ListContainersParam.containersCreatedSince(it)) }
            it.status?.let { listOfParams.add(DockerClient.ListContainersParam.filter("status", it.name)) }
            it.volume?.let { listOfParams.add(DockerClient.ListContainersParam.filter("volume", it)) }
        }
        val containers = dockerClient.listContainers(*listOfParams.toTypedArray())
        val containerDetails = mutableListOf<ContainerDetails>()
        containers?.forEach {
            val containerInfo = if (details?:false) dockerClient.inspectContainer(it.id()) else null
            containerDetails.add(ContainerDetails(it, containerInfo))
        }
        return listOf(*containerDetails.toTypedArray())
    }
}
