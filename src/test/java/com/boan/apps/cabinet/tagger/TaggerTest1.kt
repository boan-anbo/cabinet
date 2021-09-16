package com.boan.apps.cabinet.tagger

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TaggerTest {


    @Test
    fun Should_Extract_And_Replace_Text() {
        val textText = "[[Hello]] World!"
        val result = Tagger.process(textText)
        assertTrue(result.tags.isNotEmpty())
        assertEquals(result.tags[0].tagKey, "hello")
        assertEquals(result.processedText, "World!")
        println(result.extractDate)
    }
}
