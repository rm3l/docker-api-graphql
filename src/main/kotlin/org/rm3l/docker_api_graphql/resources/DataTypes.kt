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

package org.rm3l.docker_api_graphql.resources

import com.spotify.docker.client.messages.Info
import com.spotify.docker.client.messages.Version

data class System(val Info: Info?, val Version: Version?)

data class ContainerFilter(
        val ancestor: List<String>?,
        val before: List<String>?,
        val expose: List<String>?,
        val exited: List<Int>?,
        val health: List<ContainerHealth>?,
        val id: List<String>?,
        val isolation: List<ContainerIsolation>?,
        val is_task: List<Boolean>?,
        val label: List<String>?,
        val name: List<String>?,
        val network: List<String>?,
        val publish: List<String>?,
        val since: List<String>?,
        val status: List<ContainerStatus>?,
        val volume: List<String>?
)

enum class ContainerHealth {
    starting,
    healthy,
    unhealthy,
    none
}

enum class ContainerIsolation {
    default,
    process,
    hyperv
}

enum class ContainerStatus {
    created,
    restarting,
    running,
    removing,
    paused,
    exited,
    dead
}

data class ImageFilter(
        val before: List<String>?,
        val dangling: List<Boolean>?,
        val label: List<String>?,
        val reference: List<String>?,
        val since: List<String>?
)

data class VolumeFilter(
        val dangling: List<Boolean>?,
        val driver: List<String>?,
        val label: List<String>?,
        val name: List<String>?
)

data class NetworkFilter(
        val driver: List<String>?,
        val id: List<String>?,
        val label: List<String>?,
        val name: List<String>?,
        val scope: List<NetworkScope>?,
        val type: List<NetworkType>?
)

enum class NetworkScope {
    swarm,
    global,
    local
}

enum class NetworkType {
    custom,
    builtin
}

data class NodeFilter(
        val id: List<String>?,
        val label: List<String>?,
        val membership: List<NodeMembership>?,
        val name: List<String>?,
        val role: List<NodeRole>?
)

enum class NodeMembership {
    accepted,
    pending
}

enum class NodeRole {
    manager,
    worker
}