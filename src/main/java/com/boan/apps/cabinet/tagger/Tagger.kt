package com.boan.apps.cabinet.tagger

import com.boan.apps.cabinet.tagger.models.ExtractResult
import com.boan.apps.cabinet.tagger.models.TagExtract


class Tagger {


    companion object {
        @JvmStatic
        @JvmOverloads
        fun process(text:String, replace: Boolean=true): ExtractResult {
            val tags = TagExtractor.extractTags(text);

            var replacedText = text

            if (replace && tags.isNotEmpty()) {
                replacedText = this.replaceTags(replacedText, tags)
            }

            return ExtractResult(
                replacedText,
                tags,
                replace
            )
        }

        private fun replaceTags(text: String, tags: List<TagExtract>): String {
            var replacedText = text;
            tags.forEach{
                replacedText = replacedText.replace(it.tagMarker, "")
            }
            return replacedText.trim()
        }
    }

}
