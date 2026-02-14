package org.ldemetrios.docgen.org.ldemetrios.tyko.docs

import java.net.URI
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.select.Elements
import kotlin.collections.takeWhile

context(urlContext: UrlContext)
fun composeDoc(html: Document, source: LinkComment): ClassDoc {
    val documentation = html.select("body > div.main-grid > main")
    val fragment = urlContext.fragment()

    data class SectionConfig(
        val header: List<Element>,
        val paramHeadingTag: String,
        val paramIdPrefix: String,
        val subDefinitionPrefix: String?,
    )

    val section = if (fragment != null) {
        val start = documentation.select("#$fragment").firstOrNull()
            ?: throw IllegalStateException("Fragment '#$fragment' not found in ${source.url}")
        val nodes = listOf(start) + start.nextElementSiblings()
        val headerNodes = nodes.takeWhile { !(it.tagName() == "h4" && it.id().startsWith(fragment + "-")) }
        SectionConfig(headerNodes, "h4", "$fragment-", fragment + "-definitions-")
    } else {
        val nodes = documentation.select("h1")[0]
            .nextElementSiblings()
            .takeWhile { it.tagName() != "h2" || it.attr("id") != "parameters" }
        SectionConfig(nodes, "h3", "parameters-", null)
    }

    var parameters = collectParameters(
        documentation = documentation,
        paramHeadingTag = section.paramHeadingTag,
        paramIdPrefix = section.paramIdPrefix,
        subDefinitionPrefix = section.subDefinitionPrefix,
    )

    // Some type pages expose fields under "Constructor" with ids like "constructor-year",
    // without a dedicated "parameters-*" section.
    if (fragment == null && parameters.isEmpty()) {
        parameters = collectParameters(
            documentation = documentation,
            paramHeadingTag = "h4",
            paramIdPrefix = "constructor-",
            subDefinitionPrefix = null,
        )
    }

    return ClassDoc(
        classDoc = headerKdoc(
            section.header.takeWhile { !isClassDocBoundary(it) }
        ),
        params = parameters,
        source = source,
    )
}

context(urlContext: UrlContext)
private fun collectParameters(
    documentation: Elements,
    paramHeadingTag: String,
    paramIdPrefix: String,
    subDefinitionPrefix: String?,
): Map<String, KDocDocument> {
    val paramHeadingLevel = headingLevel(paramHeadingTag) ?: 3
    return documentation.select("$paramHeadingTag[id^=$paramIdPrefix]")
        .filter { subDefinitionPrefix == null || !it.id().startsWith(subDefinitionPrefix) }
        .map { paramSection ->
            listOf(paramSection) + paramSection.nextElementSiblings().takeWhile {
                !isParamSectionBoundary(it, paramHeadingLevel, paramIdPrefix)
            }
        }
        .associate {
            val name = it.first().select("code").first()!!.text().trim()
            name to paramKdoc(it)
        }
}

context(urlContext: UrlContext)
fun headerKdoc(header: List<Element>): KDocDocument {
    return KDocDocument(header.mapNotNull { paragraphKdoc(it) })
}

context(urlContext: UrlContext)
fun paramKdoc(elements: List<Element>): KDocDocument {
    val addInfo = elements.first().select("div.additional-info").first()!!.children()
    val type = addInfo.first()!!.text()
    val markers = addInfo.drop(1).map { it.text() }
    val appendage = if (markers.isNotEmpty()) {
        markers.joinToString { it.split(" ").first().lowercase() }.replaceFirstChar { it.uppercase() } +
                "; "
    } else ""
    return KDocDocument(
        elements
            .drop(1)
            .filter { !it.text().trim().startsWith("Default: ") }
            .takeWhile { it.className() != "page-end-buttons" && !it.id().endsWith("definitions") }
            .mapNotNull { paragraphKdoc(it) } +
                KDocParagraph(
                    listOf(
                        KDocText(
                            appendage + "Typst type: " + type.replace(" or ", "|")
                        )
                    )
                )
    )
}

context(urlContext: UrlContext)
fun paragraphKdoc(element: Element): KDocParagraph? {
    return when (element.tagName()) {
        "h1", "h2", "h3", "h4", "h5", "h6" -> {
            if (element.hasClass("scoped-function")) return null
            val level = element.tagName().removePrefix("h").toIntOrNull() ?: 1
            KDocParagraph(listOf(KDocHeader(KDocText(headingText(element)), level)))
        }

        "details", "table", "thead", "tbody", "tr", "ul", "ol" -> KDocParagraph(listOfNotNull(node(element)))
        "div" -> when {
            element.className().startsWith("code code-definition") -> null
            element.className() == "footnote-definition" -> null
            else -> KDocParagraph(element.childNodes().mapNotNull { node(it) })
        }

        else -> KDocParagraph(element.childNodes().mapNotNull { node(it) })
    }
}

context(urlContext: UrlContext)
fun node(it: Node): KDocNode? = when (it) {
    is TextNode -> {
        val text = it.text()
        if (text.isBlank()) null else KDocText(text)
    }

    is Element -> elementNode(it)
    else -> null
}

context(urlContext: UrlContext)
fun elementNode(el: Element): KDocNode = when (el.tagName()) {
    "p" -> KDocSequence(el.childNodes().mapNotNull { node(it) })
    "div" -> when (el.className()) {
        "code code-definition", "footnote-definition" -> KDocText("")
        else -> KDocSequence(el.childNodes().mapNotNull { node(it) })
    }
    "sup" -> when (el.className()) {
        "footnote-reference" -> KDocText("")
        else -> KDocSequence(el.childNodes().mapNotNull { node(it) })
    }

    "pre" -> KDocBlockCode(el.text())
    "img" -> KDocImage(KDocText(el.attr("alt")), urlContext.resolve(el.attr("src")))
    "em" -> KDocEmphasis(el.childNodes().mapNotNull { node(it) })
    "strong" -> KDocStrong(el.childNodes().mapNotNull { node(it) })
    "a" -> KDocLink(KDocSequence(el.childNodes().mapNotNull { node(it) }), urlContext.resolve(el.attr("href")))
    "h2" -> KDocHeader(KDocText(el.text()), 2)
    "h3" -> KDocHeader(KDocText(el.text()), 3)
    "h4" -> KDocHeader(KDocText(el.text()), 4)
    "h5" -> KDocHeader(KDocText(el.text()), 5)
    "h6" -> KDocHeader(KDocText(el.text()), 6)
    "code" -> {
        val text = el.text()
        if (text.contains('\n')) {
            KDocBlockCode(text.trimEnd())
        } else {
            KDocInlineCode(text)
        }
    }
    "li" -> KDocListItem(el.childNodes().mapNotNull { node(it) })
    "ul" -> KDocList(
        el.children().filter { it.tagName() == "li" }.map { li ->
            KDocListItem(li.childNodes().mapNotNull { node(it) })
        },
        ordered = false,
    )

    "ol" -> KDocList(
        el.children().filter { it.tagName() == "li" }.map { li ->
            KDocListItem(li.childNodes().mapNotNull { node(it) })
        },
        ordered = true,
    )

    "details" -> detailsNode(el)
    "summary" -> KDocSequence(el.childNodes().mapNotNull { node(it) })
    "br" -> KDocSequence(el.childNodes().mapNotNull { node(it) })
    "table" -> tableNode(el)
    "thead" -> KDocTable(el.children().filter { it.tagName() == "tr" }.map { tableRow(it) }, emptyList())
    "tbody" -> KDocTable(emptyList(), el.children().filter { it.tagName() == "tr" }.map { tableRow(it) })
    "tr" -> KDocTable(listOf(tableRow(el)), emptyList())
    "th" -> KDocTable(listOf(KDocTableRow(listOf(tableCell(el)))), emptyList())
    "td" -> KDocTable(emptyList(), listOf(KDocTableRow(listOf(tableCell(el)))))
    "small" -> KDocText("")
    else -> throw AssertionError("Unprepared for ${el.tagName()} : $el")
}

private fun headingText(element: Element): String {
    val code = element.selectFirst("code")?.text()?.trim()
    if (!code.isNullOrBlank()) return code
    val clone = element.clone()
    clone.select("small, .tooltip-context, svg, title").remove()
    return clone.text().trim()
}

context(urlContext: UrlContext)
fun detailsNode(it: Element): KDocNode {
    val summaryElement = it.children().firstOrNull { child -> child.tagName() == "summary" }
    val summary = summaryElement?.childNodes()?.mapNotNull { node(it) } ?: emptyList()
    val bodyElements = it.children().filter { child -> child.tagName() != "summary" }
    val body = bodyElements.map { paragraphKdoc(it) }
    return KDocFold(summary, body.filterNotNull())
}

context(urlContext: UrlContext)
fun tableNode(it: Element): KDocTable {
    val thead = it.children().firstOrNull { child -> child.tagName() == "thead" }
    val tbody = it.children().firstOrNull { child -> child.tagName() == "tbody" }

    val headerRows = thead?.children()
        ?.filter { child -> child.tagName() == "tr" }
        ?.map { row -> tableRow(row) }
        ?: emptyList()
    val bodyRows = tbody?.children()
        ?.filter { child -> child.tagName() == "tr" }
        ?.map { row -> tableRow(row) }
        ?: it.children()
            .filter { child -> child.tagName() == "tr" }
            .map { row -> tableRow(row) }

    return KDocTable(headerRows, bodyRows)
}

context(urlContext: UrlContext)
fun tableRow(it: Element): KDocTableRow =
    KDocTableRow(it.children().filter { it.tagName() == "th" || it.tagName() == "td" }.map { tableCell(it) })

context(urlContext: UrlContext)
fun tableCell(it: Element): KDocTableCell =
    KDocTableCell(it.childNodes().mapNotNull { node(it) }, isHeader = it.tagName() == "th")

private fun headingLevel(tagName: String): Int? =
    tagName.lowercase().removePrefix("h").toIntOrNull()

private fun isClassDocBoundary(element: Element): Boolean {
    if (element.className() == "page-end-buttons" || element.id().endsWith("definitions")) return true
    if (headingLevel(element.tagName()) == null) return false
    return headingText(element).equals("Constructor", ignoreCase = true)
}

private fun isParamSectionBoundary(element: Element, paramHeadingLevel: Int, paramIdPrefix: String): Boolean {
    val level = headingLevel(element.tagName()) ?: return false
    if (level < paramHeadingLevel) return true
    if (level > paramHeadingLevel) return false
    return element.id().startsWith(paramIdPrefix) ||
        element.id().startsWith(paramIdPrefix.removeSuffix("-") + "-definitions")
}

data class UrlContext(val base: String) {
    fun resolve(url: String): String {
        if (url.isBlank()) return url
        return try {
            URI(base).resolve(url).toString()
        } catch (_: Exception) {
            url
        }
    }

    fun fragment(): String? = try {
        URI(base).fragment
    } catch (_: Exception) {
        null
    }
}
