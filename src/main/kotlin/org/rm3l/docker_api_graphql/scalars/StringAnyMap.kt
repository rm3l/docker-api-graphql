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
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType

class StringAnyMap :
        GraphQLScalarType("StringAnyMap", "Map<String,String>", StringAnyMapCoercing())

private class StringAnyMapCoercing : Coercing<Map<String, Any>?, Map<String, Any>?> {

    @Suppress("UNCHECKED_CAST")
    private fun convertImpl(input: Any?): Map<String, Any>? {
        if (input is String) {
            try {
                return ObjectMapper().readValue(input, Map::class.java) as Map<String, Any>?
            } catch (e: Exception) {
                return null
            }
        } else if (input is Map<*, *>) {
            try {
                return convertImpl(ObjectMapper().writeValueAsString(input))
            } catch (e: Exception) {
                return null
            }
        }
        return null
    }

    // value sent to the client
    override fun serialize(input: Any?): Map<String, Any>? {
        return convertImpl(input) ?:
                throw IllegalArgumentException("Invalid input '$input' for Map<String,Any>")
    }

    // value from the client
    override fun parseValue(input: Any?): Map<String, Any>? {
        return convertImpl(input) ?:
                throw IllegalArgumentException("Invalid input '$input' for Map<String,Any>")
    }

    @Suppress("UNCHECKED_CAST")
    override fun parseLiteral(input: Any?): Map<String, Any>? {
        if (input is StringValue) {
            return convertImpl(input.value)
        }
        return null
    }
}