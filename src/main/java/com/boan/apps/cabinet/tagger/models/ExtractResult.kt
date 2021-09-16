package com.boan.apps.cabinet.tagger.models

import java.util.*

data class ExtractResult(
    val processedText: String,
    val tags: List<TagExtract>,
    val replaced: Boolean
) {
    val extractDate = Date()
}
