package org.rm3l.docker_api_graphql.resources

import com.spotify.docker.client.messages.Container
import com.spotify.docker.client.messages.ContainerInfo
import com.spotify.docker.client.messages.Info
import com.spotify.docker.client.messages.Version

data class HostInfo(val Info: Info?, val Version: Version?)

data class ContainerDetails(
        val info: Container?,
        val details: ContainerInfo?
) {
    constructor(info: Container?): this(info, null)
}

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
        val status: ContainerStatus?,
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