package org.rm3l.docker_api_graphql.resources

import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.messages.*
import org.rm3l.docker_api_graphql.scalars.StringStringMap

data class HostInfo(val Info: Info?, val Version: Version?)

data class ContainerDetails(
        var dockerClient: DefaultDockerClient? = null,
        var info: Container? = null,
        var details: ContainerInfo? = null,
        var processes: TopResults? = null,
        var stats: ContainerStats? = null,
        var logs: String? = null
) {
    fun getProcesses(psArgs: String?): TopResults? {
        if (psArgs.isNullOrBlank()) {
            return this.processes
        }
        return dockerClient?.topContainer(info?.id(), psArgs)
    }

    fun getLogs(follow: Boolean?,
                stdout: Boolean?,
                stderr: Boolean?,
                since: Int?,
                timestamps: Boolean?,
                lines: Int?,
                customParams: Map<String, String>?): String? {
        val logsParam = mutableListOf<DockerClient.LogsParam>()
        if (follow?:false) {
            logsParam.add(DockerClient.LogsParam.follow(follow!!))
        }
        if (stdout?:false) {
            logsParam.add(DockerClient.LogsParam.stdout(stdout!!))
        }
        if (stderr?:false) {
            logsParam.add(DockerClient.LogsParam.stderr(stderr!!))
        }
        since?.let { logsParam.add(DockerClient.LogsParam.since(it)) }
        if (timestamps?:false) {
            logsParam.add(DockerClient.LogsParam.timestamps(timestamps!!))
        }
        lines?.let { logsParam.add(DockerClient.LogsParam.tail(it)) }
        customParams?.forEach { key, value -> logsParam.add(DockerClient.LogsParam.create(key, value))}
        return dockerClient?.logs(info?.id(), *logsParam.toTypedArray())?.readFully()
    }
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