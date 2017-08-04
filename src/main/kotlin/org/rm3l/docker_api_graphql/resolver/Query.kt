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
import com.spotify.docker.client.messages.Image
import com.spotify.docker.client.messages.ImageSearchResult
import com.spotify.docker.client.messages.VolumeList
import org.rm3l.docker_api_graphql.resources.ContainerFilter
import org.rm3l.docker_api_graphql.resources.HostInfo
import org.rm3l.docker_api_graphql.resources.ImageFilter
import org.rm3l.docker_api_graphql.resources.VolumeFilter

class Query(val dockerClient: DefaultDockerClient): GraphQLRootResolver {

    fun host(): HostInfo =
            HostInfo(dockerClient.info(), dockerClient.version())

    fun containers(all: Boolean?, limit: Int?, size: Boolean?, filter: List<ContainerFilter>?):
            List<Container> {
        val listOfParams = mutableListOf(allContainers(all ?: false))
        limit?.let { listOfParams.add(limitContainers(it)) }
        size?.let { listOfParams.add(withContainerSizes(it)) }
        filter?.forEach {
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

    fun images(all: Boolean?, digests: Boolean?, filters: List<ImageFilter>?): List<Image> {
        val listOfParams = mutableListOf(DockerClient.ListImagesParam.allImages(all ?: false))
        if (digests?:false) {
            listOfParams.add(DockerClient.ListImagesParam.digests())
        }
        filters?.forEach {
            it.before?.let { listOfParams.add(DockerClient.ListImagesParam.create("before", it)) }
            it.dangling?.let { listOfParams.add(DockerClient.ListImagesParam.danglingImages(it)) }
            it.label?.let { listOfParams.add(DockerClient.ListImagesParam.create("label", it)) }
            it.reference?.let { listOfParams.add(DockerClient.ListImagesParam.create("reference", it)) }
            it.since?.let { listOfParams.add(DockerClient.ListImagesParam.create("since", it)) }
        }
        return dockerClient.listImages(*listOfParams.toTypedArray())
    }

    fun searchImagesOnDockerHub(term: String): List<ImageSearchResult> =
            dockerClient.searchImages(term)

    fun volumes(filters: List<VolumeFilter>?): VolumeList {
        val listOfParams = mutableListOf<DockerClient.ListVolumesParam>()
        filters?.forEach {
            it.dangling?.let { listOfParams.add(DockerClient.ListVolumesParam.dangling(it)) }
            it.driver?.let { listOfParams.add(DockerClient.ListVolumesParam.driver(it)) }
            it.name?.let { listOfParams.add(DockerClient.ListVolumesParam.name(it)) }
            it.label?.let { listOfParams.add(DockerClient.ListVolumesParam.filter("label", it)) }
        }
        return dockerClient.listVolumes(*listOfParams.toTypedArray())
    }
}
