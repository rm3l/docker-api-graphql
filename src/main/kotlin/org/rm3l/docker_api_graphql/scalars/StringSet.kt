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

package org.rm3l.docker_api_graphql.scalars

import com.fasterxml.jackson.databind.ObjectMapper
import graphql.language.ListType
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType

class StringSet : GraphQLScalarType("StringSet", "StringSet",
        object : Coercing<List<String?>?, Set<String?>?> {

            // value sent to the client
            @Suppress("UNCHECKED_CAST")
            override fun serialize(input: Any?): Set<String?>? {
                if (input is Collection<*>) {
                    return input.map { it?.toString() }.toHashSet()
                }
                if (input is String) {
                    return ObjectMapper().readValue(input, Set::class.java) as Set<String?>
                }
                return null
            }

            @Suppress("UNCHECKED_CAST")
            override fun parseValue(input: Any?): List<String?>? {
                if (input is Collection<*>) {
                    return input.map { it?.toString() }.toList()
                }
                if (input is String) {
                    return ObjectMapper().readValue(input, List::class.java) as List<String?>
                }
                return null
            }

            @Suppress("UNCHECKED_CAST")
            override fun parseLiteral(input: Any?): List<String?>? {
                if (input is ListType) {
                    return input.children.map {
                        if (it is StringValue) it.value else null
                    }.toList()
                }
                if (input is StringValue) {
                    return ObjectMapper().readValue(input.value, List::class.java) as List<String?>?
                }
                return null
            }

        }) {
}