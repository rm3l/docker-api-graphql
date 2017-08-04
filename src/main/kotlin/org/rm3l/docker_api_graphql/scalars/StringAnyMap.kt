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