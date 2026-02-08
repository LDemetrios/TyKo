package org.ldemetrios.docgen.org.ldemetrios.tyko.docs


sealed interface KDocNode {
    fun render(builder: StringBuilder)
}

data class KDocText(val text: String) : KDocNode {
    override fun render(builder: StringBuilder) {
        builder.append(text)
    }
}

data class KDocEmphasis(val content: List<KDocNode>) : KDocNode {
    override fun render(builder: StringBuilder) {
        builder.append('*')
        content.forEach { it.render(builder) }
        builder.append('*')
    }
}

data class KDocHeader(val text: KDocNode, val level: Int) : KDocNode {
    override fun render(builder: StringBuilder) {
        builder.append("**_")
        text.render(builder)
        builder.append("_**")
    }
}

data class KDocLink(val text: KDocNode, val url: String) : KDocNode {
    override fun render(builder: StringBuilder) {
        builder.append('[')
        text.render(builder)
        builder.append("](")
        builder.append(url)
        builder.append(')')
    }
}

data class KDocBlockCode(val code: String) : KDocNode {
    override fun render(builder: StringBuilder) {
        builder.append("```typ\n")
        builder.append(code)
        builder.append("\n```\n")
    }
}

data class KDocInlineCode(val code: String) : KDocNode {
    override fun render(builder: StringBuilder) {
        builder.append("`")
        builder.append(code)
        builder.append("`")
    }
}

data class KDocImage(val altText: KDocNode, val url: String) : KDocNode {
    override fun render(builder: StringBuilder) {
        builder.append("<img src=\"")
        builder.append(url)
        builder.append("\" alt=\"")
        altText.render(builder)
        builder.append("\" />")
    }
}

data class KDocFold(val summary: List<KDocNode>, val body: List<KDocParagraph>) : KDocNode {
    override fun render(builder: StringBuilder) {
        return
        builder.append("<details><summary>")
        summary.forEach { it.render(builder) }
        builder.append("</summary>\n\n")
        body.forEachIndexed { index, paragraph ->
            builder.append(renderParagraph(paragraph))
            if (index != body.lastIndex) {
                builder.append("\n\n")
            } else {
                builder.append("\n")
            }
        }
        builder.append("</details>")
    }
}

data class KDocSequence(val content: List<KDocNode>) : KDocNode {
    override fun render(builder: StringBuilder) {
        content.forEach { it.render(builder) }
    }
}

data class KDocListItem(val content: List<KDocNode>) : KDocNode {
    override fun render(builder: StringBuilder) {
        KDocList(listOf(this), ordered = false).render(builder)
    }
}

data class KDocList(val items: List<KDocListItem>, val ordered: Boolean) : KDocNode {
    override fun render(builder: StringBuilder) {
        renderList(builder, 0)
    }

    private fun renderList(builder: StringBuilder, indent: Int) {
        items.forEachIndexed { index, item ->
            if (index > 0) builder.append("\n")
            renderItem(builder, item, indent)
        }
    }

    private fun renderItem(builder: StringBuilder, item: KDocListItem, indent: Int) {
        val inlineBuilder = StringBuilder()
        val nestedLists = mutableListOf<KDocList>()

        item.content.forEach { node ->
            when (node) {
                is KDocList -> nestedLists.add(node)
                else -> node.render(inlineBuilder)
            }
        }

        val marker = if (ordered) "1." else "-"
        builder.append(" ".repeat(indent))
        builder.append(marker)
        builder.append(" ")
        builder.append(inlineBuilder.toString().trimEnd())

        nestedLists.forEach { nested ->
            builder.append("\n")
            nested.renderList(builder, indent + 2)
        }
    }
}

data class KDocTableCell(val content: List<KDocNode>, val isHeader: Boolean = false)

data class KDocTableRow(val cells: List<KDocTableCell>)

data class KDocTable(val header: List<KDocTableRow>, val body: List<KDocTableRow>) : KDocNode {
    override fun render(builder: StringBuilder) {
        val rows = header + body
        if (rows.isEmpty()) return

        val columnCount = rows.maxOfOrNull { it.cells.size } ?: 0
        if (columnCount == 0) return

        fun renderRow(row: KDocTableRow): String {
            val cells = (0 until columnCount).map { index ->
                val cell = row.cells.getOrNull(index)
                if (cell == null) {
                    ""
                } else {
                    buildString {
                        cell.content.forEach { it.render(this) }
                    }.trim()
                }
            }
            return "| " + cells.joinToString(" | ") + " |"
        }

        if (header.isNotEmpty()) {
            val headerRow = header.first()
            builder.append(renderRow(headerRow))
            builder.append("\n")
            builder.append("|")
            repeat(columnCount) {
                builder.append(" --- |")
            }
            builder.append("\n")
            body.forEachIndexed { index, row ->
                builder.append(renderRow(row))
                if (index != body.lastIndex) {
                    builder.append("\n")
                }
            }
        } else {
            rows.forEachIndexed { index, row ->
                builder.append(renderRow(row))
                if (index != rows.lastIndex) {
                    builder.append("\n")
                }
            }
        }
    }
}

data class KDocParagraph(val content: List<KDocNode>)

data class KDocDocument(val paragraphs: List<KDocParagraph>) {
    fun render(): String {
        return paragraphs
            .joinToString("\n\n") { renderParagraph(it) }
            .replace(Regex("\n\n\n+"), "\n\n")
            .split("\n")
            .joinToString("\n", "/**\n", "\n */") { " * $it" }
    }
}

private fun renderParagraph(paragraph: KDocParagraph): String {
    val builder = StringBuilder()
    paragraph.content.forEach { it.render(builder) }
    return builder.toString().trimEnd()
}
