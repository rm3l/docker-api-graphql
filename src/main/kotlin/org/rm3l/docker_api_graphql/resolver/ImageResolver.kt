package org.rm3l.docker_api_graphql.resolver

import com.coxautodev.graphql.tools.GraphQLResolver
import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.messages.*

class ImageResolver(val dockerClient: DefaultDockerClient):
        GraphQLResolver<Image> {

    fun details(image: Image) = dockerClient.inspectImage(image.id())

    fun history(image: Image) = dockerClient.history(image.id())
}