/*
 * Copyright (c) 2017 Armel Soro
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package org.rm3l.docker_api_graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLRootResolver
import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.DockerClient.ListContainersParam.*
import com.spotify.docker.client.messages.*
import org.rm3l.docker_api_graphql.resources.*

class Query(val dockerClient: DefaultDockerClient) : GraphQLRootResolver {

    fun system() = System(dockerClient.info(), dockerClient.version())

    fun swarm() = dockerClient.inspectSwarm()

    fun containers(all: Boolean?, limit: Int?, size: Boolean?, filter: ContainerFilter?):
            List<Container> {
        val listOfParams = mutableListOf(allContainers(all ?: false))
        limit?.let { listOfParams.add(limitContainers(it)) }
        size?.let { listOfParams.add(withContainerSizes(it)) }
        filter?.let {
            it.ancestor?.forEach { listOfParams.add(filter("ancestor", it)) }
            it.before?.forEach { listOfParams.add(containersCreatedBefore(it)) }
            it.expose?.forEach { listOfParams.add(filter("expose", it)) }
            it.exited?.forEach { listOfParams.add(withExitStatus(it)) }
            it.health?.forEach { listOfParams.add(filter("health", it.name)) }
            it.id?.forEach { listOfParams.add(filter("id", it)) }
            it.isolation?.forEach { listOfParams.add(filter("isolation", it.name)) }
            it.is_task?.forEach { listOfParams.add(filter("is-task", if (it) "true" else "false")) }
            it.label?.forEach { listOfParams.add(filter("label", it)) }
            it.name?.forEach { listOfParams.add(filter("name", it)) }
            it.network?.forEach { listOfParams.add(filter("network", it)) }
            it.publish?.forEach { listOfParams.add(filter("publish", it)) }
            it.since?.forEach { listOfParams.add(containersCreatedSince(it)) }
            it.status?.forEach { listOfParams.add(filter("status", it.name)) }
            it.volume?.forEach { listOfParams.add(filter("volume", it)) }
        }
        return dockerClient.listContainers(*listOfParams.toTypedArray())
    }

    fun images(all: Boolean?, digests: Boolean?, filters: ImageFilter?): List<Image> {
        val listOfParams = mutableListOf(DockerClient.ListImagesParam.allImages(all ?: false))
        if (digests ?: false) {
            listOfParams.add(DockerClient.ListImagesParam.digests())
        }
        filters?.let {
            it.before?.forEach { listOfParams.add(DockerClient.ListImagesParam.create("before", it)) }
            it.dangling?.forEach { listOfParams.add(DockerClient.ListImagesParam.danglingImages(it)) }
            it.label?.forEach { listOfParams.add(DockerClient.ListImagesParam.create("label", it)) }
            it.reference?.forEach { listOfParams.add(DockerClient.ListImagesParam.create("reference", it)) }
            it.since?.forEach { listOfParams.add(DockerClient.ListImagesParam.create("since", it)) }
        }
        return dockerClient.listImages(*listOfParams.toTypedArray())
    }

    fun searchImagesOnDockerHub(term: String): List<ImageSearchResult> =
            dockerClient.searchImages(term)

    fun volumes(filters: VolumeFilter?): VolumeList {
        val listOfParams = mutableListOf<DockerClient.ListVolumesParam>()
        filters?.let {
            it.dangling?.forEach { listOfParams.add(DockerClient.ListVolumesParam.dangling(it)) }
            it.driver?.forEach { listOfParams.add(DockerClient.ListVolumesParam.driver(it)) }
            it.name?.forEach { listOfParams.add(DockerClient.ListVolumesParam.name(it)) }
            it.label?.forEach { listOfParams.add(DockerClient.ListVolumesParam.filter("label", it)) }
        }
        return dockerClient.listVolumes(*listOfParams.toTypedArray())
    }

    fun networks(filters: NetworkFilter?): List<Network> {
        val listOfParams = mutableListOf<DockerClient.ListNetworksParam>()
        filters?.let {
            it.id?.forEach { listOfParams.add(DockerClient.ListNetworksParam.filter("id", it)) }
            it.driver?.forEach { listOfParams.add(DockerClient.ListNetworksParam.withDriver(it)) }
            it.name?.forEach { listOfParams.add(DockerClient.ListNetworksParam.filter("name", it)) }
            it.label?.forEach { listOfParams.add(DockerClient.ListNetworksParam.filter("label", it)) }
            it.scope?.forEach { listOfParams.add(DockerClient.ListNetworksParam.filter("scope", it.name)) }
            it.type?.forEach {
                listOfParams.add(DockerClient.ListNetworksParam.withType(
                        when (it) {
                            NetworkType.builtin -> Network.Type.BUILTIN
                            NetworkType.custom -> Network.Type.CUSTOM
                        }
                ))
            }
        }
        return dockerClient.listNetworks(*listOfParams.toTypedArray())
    }
}
