package org.rm3l.docker_api_graphql.scalars

import com.fasterxml.jackson.databind.ObjectMapper
import graphql.language.ListType
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import java.util.Date

class StringSet: GraphQLScalarType("StringSet", "StringSet",
        object: Coercing<List<String?>?, Set<String?>?> {

    // value sent to the client
    override fun serialize(input: Any?): Set<String?>? {
        if (input is Collection<*>) {
            return input.map { it?.toString() }.toHashSet()
        }
        if (input is String) {
            return ObjectMapper().readValue(input, Set::class.java) as Set<String?>
        }
        return null
    }

    override fun parseValue(input: Any?): List<String?>? {
        if (input is Collection<*>) {
            return input.map { it?.toString() }.toList()
        }
        if (input is String) {
            return ObjectMapper().readValue(input, List::class.java) as List<String?>
        }
        return null
    }

    override fun parseLiteral(input: Any?): List<String?>? {
        if (input is ListType) {
            return input.children.map { if (it is StringValue) it.value else null
            }.toList()
        }
        if (input is StringValue) {
            return ObjectMapper().readValue(input.value, List::class.java) as List<String?>?
        }
        return null
    }

}) {
}