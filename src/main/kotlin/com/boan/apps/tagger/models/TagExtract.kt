package com.boan.apps.cabinet.tagger.models

data class TagExtract(
    val tagMarker: String,
    private val _tagKey: String?="",
    val tagValue: String?="",
    val tagNote: String?=""
    )
{

    val tagKey: String
    init {
        tagKey = _tagKey?.lowercase() ?: ""
    }
}



