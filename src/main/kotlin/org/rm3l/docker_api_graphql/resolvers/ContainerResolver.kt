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

import com.coxautodev.graphql.tools.GraphQLResolver
import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.messages.Container
import com.spotify.docker.client.messages.TopResults

class ContainerResolver(val dockerClient: DefaultDockerClient) :
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
        if (follow ?: false) {
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
        if (timestamps ?: false) {
            logsParam.add(DockerClient.LogsParam.timestamps(timestamps!!))
        }
        lines?.let { logsParam.add(DockerClient.LogsParam.tail(it)) }
        customParams?.forEach { key, value -> logsParam.add(DockerClient.LogsParam.create(key, value)) }

        return dockerClient.logs(container.id(), *logsParam.toTypedArray())?.readFully()
    }

    fun changes(container: Container) = dockerClient.inspectContainerChanges(container.id())

}