package com.boan.apps.cabinet

import boan.pdfgongju.core.utils.Utils
import com.boan.apps.cabinet.entities.Card
import com.boan.apps.cabinet.entities.Source
import com.boan.apps.cabinet.tagger.Tagger
import java.util.*

public class MarkdownWriter {

    companion object {
        @JvmStatic
        fun ExportText(title: String, cards: MutableList<Card>): String {
            val lines = ExportLines(title, cards)
            return lines.joinToString("\n")
        }

        @JvmStatic
        fun ExportLines(title: String, cards: MutableList<Card>): List<String> {

            val lines: MutableList<String> = mutableListOf<String>()
            var counter = 0;

            // header
            lines.add("# $title\n\n")
            lines.add(Utils.getSimpleDateString(Date()).trimIndent())
            lines.add("---\n")
            lines.add("[TOC]\n")
            lines.add("")
            lines.add("---")

            for (card in cards) {

                // write note title line, also TOC header
                lines.add("## ${(counter + 1)}. ${getAlternativeTitles(card)}")

                val tagLine: String? = card.tags?.map { Tagger.TagToTagMarker(it) }?.joinToString(" ")
                if (!tagLine.isNullOrEmpty()) {
                    lines.add(tagLine)
                    lines.add("")
                }
                // source related
                val soruceLine: String? = getSourceLine(card)
                val markedText: String? = card.text
                val emphasis = if (card.importance > 0) "__" else ""
                if (!markedText.isNullOrEmpty()) {
                    lines.add("> $emphasis" + java.lang.String.join(" ",
                        *markedText.split("\\r?\\n".toRegex()).toTypedArray()) + emphasis)
                }

                // write source
                if (!markedText.isNullOrEmpty() && !soruceLine.isNullOrEmpty()) {
                    lines.add(">")
                    lines.add("> $soruceLine")
                }
                lines.add("")
                // write comments
                if (card.comments.any()) {

                    for (comment in card.comments) {

                        var originalComment: String = comment.text
                        originalComment = originalComment.replace("[\r\n]+".toRegex(), "\n")
                        originalComment = originalComment.replace("\\r?\\n".toRegex(), "**\n\n**")
                        lines.add("- **$originalComment**")
                        lines.add("")
                    }
                }

                counter++
            }
            return lines
        }

        private fun getAlternativeTitles(card: Card): String {

            val abbrevLength = 50;

            if (!card.title.isNullOrEmpty()) return card.title;

            if (card.comments.any()) {
                return card.comments.first().text.take(abbrevLength)
            };

            if (!card.text.isNullOrEmpty()) {

                return card.text.take(abbrevLength);
            }

            if (card.source != null) {
                if (card.source.title != null) {
                    return card.source.title;
                }
            }


            return "Card"
        }

        private fun getSourceLine(card: Card): String? {

            val source: Source? = card.source
            val sourceTitle: String? = card.source?.title
            val sourceFilePath: String? = card.source?.filePath

            var sourceLine: String? = null;

            if (source != null && (sourceTitle != null || sourceFilePath != null)) {
                val deFactorTitle = sourceFilePath ?: sourceTitle;
                val lastModified: String? = Utils.getSimpleDateString(source.modified)
                val pageNumber: String = (source.pageIndex ?: "").toString()


                val identifier: String? = source.uniqueId ?: "";
                val separator = if (identifier.isNullOrEmpty()) "" else "|"
                sourceLine = "`($identifier$separator$pageNumber); $deFactorTitle; $lastModified`"
            }
            return sourceLine

        }
    }
}
