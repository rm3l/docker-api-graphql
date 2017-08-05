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