package com.boan.apps.cabinet.tagger

import com.boan.apps.cabinet.tagger.models.TagExtract

class TagExtractor {


    companion object {
        private val pattern = "\\[\\[(.+?)(?:\\|(.+?))?(?:\\|(.+?)|)?]]".toRegex()

        fun extractTags(text: String): List<TagExtract> {

            val results = Companion.pattern.findAll(text)

            return results.map { it -> getTagExtractFromMatch(it)
            }.toList()
        }

        fun getTagExtractFromMatch(matchResult: MatchResult): TagExtract {
            return TagExtract(
                matchResult.groups.get(0)!!.value ,
                matchResult.groups.get(1)?.value,
                matchResult.groups.get(2)?.value,
                matchResult.groups.get(3)?.value)
        }
    }
}
