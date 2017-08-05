package org.rm3l.docker_api_graphql.scalars

import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType
import java.util.Date

class Date : GraphQLScalarType("Date", "Date Scalar",
        object : Coercing<Long?, Date?> {

            // value sent to the client
            override fun serialize(input: Any?): Date? {
                if (input is Long) {
                    return Date(input)
                }
                if (input is String) {
                    return Date(input.toLong())
                }
                return null
            }

            override fun parseValue(input: Any?): Long? {
                if (input is Date) {
                    return input.time
                } else if (input is String) {
                    return input.toLong()
                }
                return null
            }

            override fun parseLiteral(input: Any?): Long? {
                if (input is StringValue) {
                    return input.value.toLong()
                }
                return null
            }

        })