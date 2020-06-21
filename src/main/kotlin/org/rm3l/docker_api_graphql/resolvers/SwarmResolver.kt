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

import graphql.kickstart.tools.GraphQLResolver
import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.messages.swarm.Node
import com.spotify.docker.client.messages.swarm.Swarm
import org.rm3l.docker_api_graphql.resources.NodeFilter

class SwarmResolver(val dockerClient: DefaultDockerClient) :
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
