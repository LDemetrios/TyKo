package org.ldemetrios.docgen.org.ldemetrios.tyko.docs

import org.jsoup.Jsoup.connect
import java.nio.file.Path
import java.nio.file.Paths

data class GeneratorConfig(
    val inputRoot: Path,
)

data class LinkComment(
    val sourceFile: Path,
    val lineNumber: Int,
    val url: String,
)

data class ClassDoc(
    val classDoc: KDocDocument,
    val params: Map<String, KDocDocument>,
    val source: LinkComment,
)

fun main(args: Array<String>) {
    val path = args
        .getOrNull(0)
        ?.let(Paths::get)
        ?: Path.of("/Users/ldemis/Workspace/Projects/TyKo/model/src/main/kotlin")

    scanLinks(path)
        .asSequence()
        .map { link ->
            with(UrlContext(link.url)) {
                composeDoc(
                    connect(link.url).userAgent("tyko-docs-generator").get(),
                    link,
                )
            }
        }
        .groupBy { it.source.sourceFile }
        .values
        .forEach(::writeDocs)
}
