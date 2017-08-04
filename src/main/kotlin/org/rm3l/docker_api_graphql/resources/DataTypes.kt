package org.rm3l.docker_api_graphql.resources

import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.messages.*

data class HostInfo(val Info: Info?, val Version: Version?)

data class ContainerFilter(
        val ancestor: String?,
        val before: String?,
        val expose: String?,
        val exited: Int?,
        val health: ContainerHealth?,
        val id: String?,
        val isolation: ContainerIsolation?,
        val is_task: Boolean?,
        val label: String?,
        val name: String?,
        val network: String?,
        val publish: String?,
        val since: String?,
        val status: List<ContainerStatus>?,
        val volume: String?
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
        val before: String?,
        val dangling: Boolean?,
        val label: String?,
        val reference: String?,
        val since: String?
)

data class VolumeFilter(
        val dangling: Boolean?,
        val driver: String?,
        val label: String?,
        val name: String?
)