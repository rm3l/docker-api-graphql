package org.rm3l.docker_api_graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLResolver
import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.messages.swarm.Node

class NodeResolver(val dockerClient: DefaultDockerClient):
        GraphQLResolver<Node> {

    fun details(node: Node) = dockerClient.inspectNode(node.id())
}