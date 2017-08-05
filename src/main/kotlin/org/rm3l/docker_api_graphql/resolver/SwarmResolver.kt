package org.rm3l.docker_api_graphql.resolver

import com.coxautodev.graphql.tools.GraphQLResolver
import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.messages.swarm.Node
import com.spotify.docker.client.messages.swarm.Swarm
import org.rm3l.docker_api_graphql.resources.NodeFilter

class SwarmResolver(val dockerClient: DefaultDockerClient):
        GraphQLResolver<Swarm> {

    fun unlockKey(swarm: Swarm) = dockerClient.unlockKey()

    fun nodes(swarm: Swarm, filter: NodeFilter?): List<Node> {
        if (filter == null) {
            return dockerClient.listNodes()
        }

        val criteriaBuilder = Node.find()
        filter.id?.forEach { criteriaBuilder.nodeId(it) }
        filter.label?.forEach { criteriaBuilder.label(it) }
        filter.membership?.forEach { criteriaBuilder.membership(it.name) }
        filter.name?.forEach { criteriaBuilder.nodeName(it) }
        filter.role?.forEach { criteriaBuilder.nodeRole(it.name) }

        return dockerClient.listNodes(criteriaBuilder.build())
    }
}