package org.rm3l.docker_api_graphql.resolver

import com.coxautodev.graphql.tools.GraphQLResolver
import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.messages.*

class ContainerResolver(val dockerClient: DefaultDockerClient):
        GraphQLResolver<Container> {

    fun details(container: Container) = dockerClient.inspectContainer(container.id())

    fun processes(container: Container, ps_args: String?): TopResults? {
        if (ps_args.isNullOrBlank()) {
            return dockerClient.topContainer(container.id())
        }
        return dockerClient.topContainer(container.id(), ps_args)
    }

    fun stats(container: Container) = dockerClient.stats(container.id())

    fun logs(container: Container,
                follow: Boolean?,
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
        if (stdout == null && stderr == null) {
            //By default, a source is needed => use stdout
            logsParam.add(DockerClient.LogsParam.stdout())
        } else {
            if (stdout ?: false) {
                logsParam.add(DockerClient.LogsParam.stdout(stdout!!))
            }
            if (stderr ?: false) {
                logsParam.add(DockerClient.LogsParam.stderr(stderr!!))
            }
        }
        since?.let { logsParam.add(DockerClient.LogsParam.since(it)) }
        if (timestamps?:false) {
            logsParam.add(DockerClient.LogsParam.timestamps(timestamps!!))
        }
        lines?.let { logsParam.add(DockerClient.LogsParam.tail(it)) }
        customParams?.forEach { key, value -> logsParam.add(DockerClient.LogsParam.create(key, value))}

        return dockerClient.logs(container.id(), *logsParam.toTypedArray())?.readFully()
    }

    fun changes(container: Container) = dockerClient.inspectContainerChanges(container.id())

}