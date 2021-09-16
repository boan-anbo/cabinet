package com.boan.apps.cabinet.tagger

import org.junit.jupiter.api.Assertions

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

internal class Extractor_Text() {

    @TestFactory
    fun Should_Extract_Tags() = listOf(
        "[[Hello]]" to 1,
        "[[Hello]][[Good Morning|World]]" to 2,
        "[[Hello|World]]" to 1,
        "[[Hello|World|Bo|Test]]" to 1,
        "[[]]" to 0,
        "Hello" to 0,
        "[[Hello]][[Good Morning|World]][[NoWay]]" to 3,
    )
        .map { (input, expectedTags) ->
            DynamicTest.dynamicTest("Should extract tags") {
                Assertions.assertEquals(expectedTags, TagExtractor.extractTags(input).size)
            }
        }

    @TestFactory
    fun Should_lower_tag_key() = listOf(
        "[[Hello]]" to "hello",
        "[[Good Morning|World]]" to "good morning",
    )
        .map { (input, expectedLoweredCase) ->
            DynamicTest.dynamicTest("Should lower tag key") {
                Assertions.assertEquals(expectedLoweredCase, TagExtractor.extractTags(input)[0].tagKey)
            }
        }
}


