package com.boan.apps.cabinet.tagger

import com.boan.apps.cabinet.tagger.models.TagExtract
import org.junit.jupiter.api.Assertions

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
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

    @TestFactory
    fun Should_translate_comment_and_source_shorthand() = listOf(
        "[[!]]" to "comment",
        "[[!|World]]" to "comment",
        "[[@]]" to "source",
        "[[@|World]]" to "source",
    )
        .map { (input, expectedShorthandTranslation) ->
            DynamicTest.dynamicTest("Should translate shorthands") {
                Assertions.assertEquals(expectedShorthandTranslation, TagExtractor.extractTags(input)[0].tagKey)
            }
        }

    @TestFactory
    fun Should_use_previous_tag() = listOf(
        "[[Hello]][[World]]" to null,
        "[[Hello]][[World]][[.]]" to null,
        "[[Hello]][[World]][[_]]" to null,
        "[[Hello]][[World]][[.|]]" to null,
        "[[Hello]][[World]][[_|]]" to null,
        "[[Hello|World]][[Goodbye|World]][[_|Bo]]" to "goodbye",
        "[[Hello|World]][[Hello|World]][[_|Bo]]" to "hello",
    )
        .map { (input, expectedShorthandTranslation) ->
            DynamicTest.dynamicTest("Should use previous tag") {
                val results: List<TagExtract> = TagExtractor.extractTags(input);
                Assertions.assertEquals(expectedShorthandTranslation, if (results.size > 2) results.get(2).tagKey else null )
            }
        }

    @Test
    fun Speciial_Should_use_previous () {

        val input = "[[Hello|World]][[.]][[.|Bo]]"
        val expected = "hello"
        val results: List<TagExtract> = TagExtractor.extractTags(input);
        Assertions.assertEquals(expected, if (results.size > 1) results.last().tagKey else null )
    }

}


