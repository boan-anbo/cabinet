package com.boan.apps.cabinet.tagger

import com.boan.apps.cabinet.tagger.models.TagExtract

class TagExtractor {


    companion object {
        private val pattern = "\\[\\[(.+?)(?<!\\|)(?:\\|(.+?))?(?:\\|(.+?)|)?]]".toRegex()

        fun extractTags(text: String): List<TagExtract> {

            val results = Companion.pattern.findAll(text)

            var lastTag: TagExtract? = null;
            return results
                    // extract tag information
                .map {
                val currentTag: TagExtract? = getTagExtractFromMatch(it, lastTag);

                    if (currentTag != null) lastTag = currentTag;

                return@map currentTag
            }
                .filterNotNull() // filter null tags (mainly those repeating tags without new information. See below.)
                .toList()
        }

        fun getTagExtractFromMatch(matchResult: MatchResult, previousTag: TagExtract? =null): TagExtract? {
            val tagMarker = matchResult.groups.get(0)!!.value
            val tagValue = matchResult.groups.get(2)?.value
            val tagNote = matchResult.groups.get(3)?.value

            val hasNoteOrValue = !tagValue.isNullOrBlank() || !tagNote.isNullOrEmpty()

            val tagKey = getKeyValue(matchResult.groups.get(1)?.value, previousTag, hasNoteOrValue) ?: return null;
            return TagExtract(
                tagMarker.trim(),
                tagKey.trim(),
                tagValue?.trim(),
                tagNote?.trim()
            )
        }

        fun getKeyValue(keyValue: String?,  previousTag: TagExtract?, hasValueOrNote:Boolean): String? {

            val inputValue = keyValue?.trim() ?: ""

            val isRepreatingTag = when (inputValue) {
                "_", "." -> true
                else -> false
            }

            // when the tag repeats the last tag and does not provide value or note, i.e. addint new information, e.g. [[Hello]][[.]] or [[Hello|World]][[!]], then it will not be considered.
            if (isRepreatingTag && !hasValueOrNote) {
                return null
            }

            val resultValue = when (inputValue) {
                "!" -> "comment"
                "@" -> "source"
                "_", "." -> previousTag?.tagKey ?: "tag"
                else -> inputValue
            }

            return resultValue
        }
    }
}
