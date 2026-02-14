package org.ldemetrios.tyko.tests

import com.dylibso.chicory.wasi.WasiOptions.builder
import org.ldemetrios.tyko.compiler.Feature
import org.ldemetrios.tyko.compiler.FileDescriptor
import org.ldemetrios.tyko.compiler.FileError
import org.ldemetrios.tyko.compiler.FontCollection
import org.ldemetrios.tyko.compiler.FSContext
import org.ldemetrios.tyko.compiler.Library
import org.ldemetrios.tyko.compiler.Now
import org.ldemetrios.tyko.compiler.PackageError
import org.ldemetrios.tyko.compiler.PdfOptionsConfig
import org.ldemetrios.tyko.compiler.PdfStandards
import org.ldemetrios.tyko.compiler.PdfValidator
import org.ldemetrios.tyko.compiler.PdfVersion
import org.ldemetrios.tyko.compiler.RResult
import org.ldemetrios.tyko.compiler.SourceDiagnostic
import org.ldemetrios.tyko.compiler.TypstCompilerException
import org.ldemetrios.tyko.compiler.Warned
import org.ldemetrios.tyko.driver.chicory_based.ChicoryTypstCore
import org.ldemetrios.tyko.model.TAuto
import org.ldemetrios.tyko.model.TSetPage
import org.ldemetrios.tyko.model.TSetText
import org.ldemetrios.tyko.model.repr
import org.ldemetrios.tyko.model.t
import org.ldemetrios.tyko.model.pt
import org.ldemetrios.tyko.runtime.TypstRuntime
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.max
import kotlin.math.sqrt
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FreeSpec
import org.ldemetrios.tyko.runtime.withStylesOrThrow
import org.jsoup.Jsoup
import org.jsoup.nodes.Comment
import org.jsoup.nodes.DataNode
import org.jsoup.nodes.Document
import org.jsoup.nodes.DocumentType
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.nodes.XmlDeclaration
import org.jsoup.parser.Parser
import org.ldemetrios.tyko.driver.api.PointerAutoCleaningConfiguration
import org.ldemetrios.tyko.driver.api.PointerAutoCleaningMode
import org.ldemetrios.tyko.driver.api.TyKoFFIEntity
import org.ldemetrios.tyko.driver.api.TyKoInternalApi
import org.ldemetrios.tyko.driver.api.remainingPointers
import org.ldemetrios.tyko.driver.chicory_based.defaultPackagesHostPath

val SKIP = setOf(
    "image-decode-bad-svg",

    // https://github.com/typst/typst/issues/7799
    "ops-equality",

    // Float precision issues, probably (dist = 110)
    "curve-stroke-gradient-sharp",

    // Deparenthesized math.frac
    "math-frac-horizontal",
    "math-frac-horizontal-lr-paren",
    "math-frac-styles-inline",

    // Style leaking into page numbering
    "page-marginal-style-shared-initial-interaction",
    "page-marginal-style-text-call",
    "page-marginal-style-text-call-around-set-page",
    "page-marginal-style-text-call-code",

    // Internal set-rules on grid and table
    "grid-stroke-field-in-show",
    "grid-stroke-set-on-cell-and-line",
    "issue-6399-grid-cell-colspan-set-rule",
    "issue-6399-grid-cell-rowspan-set-rule",
    "grid-cell-set",
    "table-cell-set",
)

class TypstSharedFfiSuiteTest : FreeSpec({
    val pointersFreeingWarnings = mutableListOf<Pair<Array<StackTraceElement>, Long>>()
    PointerAutoCleaningConfiguration.apply {
        MODE = PointerAutoCleaningMode.WarnOnly
        CAPTURE_STACK_TRACE = true
        WARNING_CALLBACK = { stack, ptr ->
            pointersFreeingWarnings.add(stack!! to ptr)
        }
    }

    fun flush() {
        val warnings = pointersFreeingWarnings.toList()
        pointersFreeingWarnings.clear()
        if (warnings.isNotEmpty()) {
            throw AssertionError("Some native resources were leaked").apply {
                warnings
                    .map { (stack, ptr) ->
                        AssertionError("Pointer $ptr").also { it.stackTrace = stack }
                    }
                    .forEach { addSuppressed(it) }
            }
        }
    }

    val runner = TypstSharedFfiRunner()
    runner.collectTests().sortedBy { it.name }.forEach { test ->
        if (test.name in SKIP) return@forEach

        "${test.name} (${test.filePath})" {
            try {
                runner.runTest(test)
            } catch (e: Throwable) {
                val message = e.message ?: ""

                //                if (failedTests.add(message)) {
                fun sanitize(e: Throwable?) {
                    if (e == null) return
                    val sanitized = e.stackTrace.filter {
                        !it.className.startsWith("io.kotest.") &&
                                !it.className.startsWith("kotlinx.coroutines.")
                    }
                    e.stackTrace = sanitized.toTypedArray()
                    sanitize(e.cause)
                    e.suppressed?.forEach(::sanitize)
                }

                sanitize(e)

                throw e
//                }
            } finally {
                flush()
                runner.runtime.evictCache(0)
            }
        }
    }

    "Remaining resources" {
        // Sanity check:
        runner.close()
        @OptIn(TyKoFFIEntity::class)
        val ptrs = remainingPointers()
        if (ptrs.isNotEmpty()) {
            throw AssertionError("Some native resources were leaked: $ptrs").also {
                it.stackTrace = ptrs.first().creationStackTrace!!.toTypedArray()
            }
        }
    }
})

private class TypstSharedFfiRunner {
    private val repoRoot = findRepoRoot()
        ?: error("Failed to locate repo root (expected typst-shared-library + typst-dev-assets).")


    val runtime = TypstRuntime(
        ChicoryTypstCore(
            builder()
                .inheritSystem()
                .withEnvironment("RUST_BACKTRACE", "full")
                .withEnvironment("RUST_LIB_BACKTRACE", "1")
                .withDirectory("/repo", repoRoot.toPath())
                .withDirectory("/packages", defaultPackagesHostPath())
                .build()
        ),
        defaultFeatures = setOf(Feature.Html, Feature.A11yExtras),
    )

    private val suiteRoot = File(repoRoot, "typst-shared-library/tests")
    private val suiteDir = File(suiteRoot, "suite")
    private val refDir = File(suiteRoot, "ref")
    private val packagesDir = File(suiteRoot, "packages")
    private val assetsDir = File(repoRoot, "typst-dev-assets/files")
    private val outputDir = File(repoRoot, "tests/build/typst-shared-ffi-output")
    private val typstVersion: String = run {
        val cargo = File(repoRoot, "typst-shared-library/Cargo.toml")
        if (!cargo.isFile) return@run "VERSION"
        val text = cargo.readText()
        val match = Regex("""(?m)^version\s*=\s*"([^"]+)"""").find(text)
        match?.groupValues?.get(1) ?: "VERSION"
    }

    private val fonts: FontCollection = runtime.fontCollection(
        includeSystem = true,
        includeEmbedded = true,
        fontPaths = listOf(
            "/repo/typst-dev-assets/files/fonts",
            "/repo/typst-assets/files/fonts",
        )
    )

    @OptIn(TyKoInternalApi::class)
    private val libraryPrototype: Library = runtime.library(setOf(Feature.Html, Feature.A11yExtras))
        .withTestDefinitions(true)
        .withStylesOrThrow(
            TSetPage(width = 120.0.t.pt),
            TSetPage(height = TAuto),
            TSetPage(margin = 10.0.t.pt),
            TSetText(size = 10.0.t.pt),
            fonts = fonts,
            closePrevious = true,
            append = true,
        )

    private val now = Now.Fixed(0, 0)

    init {
        if (!suiteDir.isDirectory) {
            fail("Missing suite directory: ${suiteDir.absolutePath}")
        }
        if (outputDir.exists()) outputDir.deleteRecursively()
    }

    fun runTest(test: TestCase) {
        println(test.name)
        println(test.notes.joinToString("\n"))
        println(test.content)
        val library = libraryPrototype
        withTestContext(test) { context, main ->
            val diagnostics = mutableListOf<EmittedNote>()

            fun <T> recordDiagnostics(result: Warned<RResult<T, List<SourceDiagnostic>>>) {
                diagnostics.addAll(result.warnings.flatMap { EmittedNote.fromDiagnostic(it, test) })
                val output = result.output
                if (output is RResult.Err) {
                    diagnostics.addAll(output.error.flatMap { EmittedNote.fromDiagnostic(it, test) })
                }
            }

            val hasErrors = test.notes.any { it.kind == NoteKind.Error }

            if (test.attrs.eval) {
                val eval = runtime.evalRaw(
                    context,
                    main,
                    fonts = fonts,
                    stdlib = library,
                    now = now,
                )
                recordDiagnostics(eval)
            }

            if (test.attrs.paged) {
                @OptIn(TyKoInternalApi::class)
                val render = runtime.compileMergedWithLinksRaw(
                    context,
                    main,
                    fonts = fonts,
                    stdlib = library,
                    now = now,
                    ppi = 72.0
                )
                val renderBytes = if (render.output is RResult.Ok) {
                    (render.output as RResult.Ok<ByteArray>).value
                } else {
                    null
                }
                val renderPages = runtime.compilePngRaw(
                    context,
                    main,
                    fonts = fonts,
                    stdlib = library,
                    now = now,
                    from = 0,
                    to = Int.MAX_VALUE,
                    ppi = 72.0
                ).let { result ->
                    if (result.output is RResult.Ok) {
                        (result.output as RResult.Ok<List<ByteArray>>).value
                    } else {
                        null
                    }
                }
                if (renderBytes != null) {
                    compareRenderRef(test, renderBytes)
                }

                val doc = runtime.precompilePagedRaw(
                    context,
                    main,
                    fonts = fonts,
                    stdlib = library,
                    now = now,
                )
                recordDiagnostics(doc)

                val pagedDocument = doc.output
                if (pagedDocument is RResult.Ok) pagedDocument.value.use {
                    it.enforceTitle(test.name)

                    val pdfOptions = test.attrs.pdfStandard?.toOptions()

                    val pdf = it.renderPdfRaw(
                        options = pdfOptions
                    )
                    recordDiagnostics(Warned(pdf, listOf()))

                    val svg = it.renderSvg()

                    if (!hasErrors && renderPages != null) {
                        roundTripCompare(
                            test,
                            context,
                            main,
                            renderPages,
                            RoundTripTarget.Render,
                            pdfOptions,
                            library,
                        )
                    }
                }
            }

            if (test.attrs.html) {
                val html = runtime.compileHtmlRaw(
                    context,
                    main,
                    fonts = fonts,
                    stdlib = library,
                    now = now
                )
                recordDiagnostics(html)
                if (html.output is RResult.Ok) {
                    val htmlValue = (html.output as RResult.Ok<String>).value
                    compareHtmlRef(test, htmlValue)

                    if (!hasErrors) {
                        roundTripCompare(
                            test,
                            context,
                            main,
                            htmlValue,
                            RoundTripTarget.Html,
                            null,
                            library,
                        )
                    }
                }
            }

            verifyDiagnostics(test, diagnostics, typstVersion)
        }

    }

    private fun roundTripCompare(
        test: TestCase,
        context: FSContext,
        main: FileDescriptor,
        reference: Any,
        target: RoundTripTarget,
        pdfOptions: PdfOptionsConfig?,
        library: Library,
    ) {
        val result = runtime.fileContext {
            println("$it ?? $main")
            if (main == it) RResult.Ok(test.content.toByteArray())
            else context.original(it)
        }.use {
            runtime.evalRaw(
                it,
                main,
                stdlib = library,
                fonts = fonts,
            )
        }.output

        val evaled = when (result) {
            is RResult.Ok -> result.value
            is RResult.Err -> throw TypstCompilerException.construct(result.error)
        }

        val repr = evaled.repr()
        val reprContent = "#$repr"

        val formatted = runtime.formatSource(reprContent, 80, 4)
        println(formatted)

        val recompiledValue = withTestContext(
            test.copy(content = reprContent)
        ) { ctx, reprMain ->
            when (target) {
                RoundTripTarget.Render -> runtime.compilePng(
                    ctx,
                    reprMain,
                    fonts = fonts,
                    stdlib = library,
                    now = now,
                    from = 0,
                    to = Int.MAX_VALUE,
                    ppi = 72.0
                )

                RoundTripTarget.Pdf -> runtime.precompilePaged(
                    ctx,
                    reprMain,
                    fonts = fonts,
                    stdlib = library,
                    now = now,
                ).also { it.enforceTitle(test.name) }.renderPdf(
                    options = pdfOptions
                )

                RoundTripTarget.Svg -> runtime.compileSvg(
                    ctx,
                    reprMain,
                    fonts = fonts,
                    stdlib = library,
                    now = now
                )

                RoundTripTarget.Html -> runtime.compileHtml(
                    ctx,
                    reprMain,
                    fonts = fonts,
                    stdlib = library,
                    now = now
                )
            }
        }

        when (target) {
            RoundTripTarget.Render -> {
                try {
                    compareRenderRoundTrip(
                        test,
                        reference as List<ByteArray>,
                        recompiledValue as List<ByteArray>
                    )
                } catch (e: AssertionError) {
                    saveText(test, "roundtrip-initial.typ", test.content)
                    saveText(test, "roundtrip-repr.typ", formatted)
                    throw e
                }
            }

            RoundTripTarget.Pdf -> {
                if (!(reference as ByteArray).contentEquals(recompiledValue as ByteArray)) {
                    saveBytes(test, "roundtrip-pdf-reference.pdf", reference)
                    saveBytes(test, "roundtrip-pdf-recompiled.pdf", recompiledValue)
                    throw AssertionError("PDF round-trip mismatch for ${test.name}")
                }
            }

            RoundTripTarget.Svg -> {
                val refList = reference as List<String>
                val recompList = recompiledValue as List<String>
                if (refList != recompList) {
                    saveText(test, "roundtrip-svg-reference.svg", refList.joinToString("\n"))
                    saveText(test, "roundtrip-svg-recompiled.svg", recompList.joinToString("\n"))
                    throw AssertionError("SVG round-trip mismatch for ${test.name}")
                }
            }

            RoundTripTarget.Html -> {
                if (htmlSignature(parseHtml(reference as String)) != htmlSignature(parseHtml(recompiledValue as String))) {
                    saveText(test, "roundtrip-html-reference.html", reference)
                    saveText(test, "roundtrip-html-recompiled.html", recompiledValue)
                    throw AssertionError("HTML round-trip mismatch for ${test.name}")
                }
            }
        }
    }

    private fun compareRenderRoundTrip(
        test: TestCase,
        reference: List<ByteArray>,
        recompiled: List<ByteArray>
    ) {
        if (reference.size != recompiled.size) {
            val maxPages = max(reference.size, recompiled.size)
            for (pageIndex in 0 until maxPages) {
                if (pageIndex < reference.size) {
                    saveBytes(test, "roundtrip-render-reference-page-${pageIndex + 1}.png", reference[pageIndex])
                }
                if (pageIndex < recompiled.size) {
                    saveBytes(test, "roundtrip-render-recompiled-page-${pageIndex + 1}.png", recompiled[pageIndex])
                }
            }
            throw AssertionError(
                "Render round-trip page count mismatch for ${test.name} " +
                        "(reference=${reference.size}, recompiled=${recompiled.size})"
            )
        }

        for (pageIndex in reference.indices) {
            val (diffImage, maxDist) = diff(
                reference[pageIndex].toImage(),
                recompiled[pageIndex].toImage()
            )
            if (maxDist < 3) continue
            saveImage(test, "roundtrip-render-page-${pageIndex + 1}-diff.png", diffImage)
            saveBytes(test, "roundtrip-render-reference-page-${pageIndex + 1}.png", reference[pageIndex])
            saveBytes(test, "roundtrip-render-recompiled-page-${pageIndex + 1}.png", recompiled[pageIndex])
            throw AssertionError(
                "Render round-trip mismatch for ${test.name} page ${pageIndex + 1} (maxDist=$maxDist)"
            )
        }
    }

    private fun compareRenderRef(test: TestCase, actual: ByteArray) {
        val refFile = File(refDir, "render/${test.name}.png")
        if (!refFile.isFile) return
        val refBytes = refFile.readBytes()
        val referenceImage = refBytes.toImage()
        val actualImage = quantizeToIndexed(actual.toImage())
        val (diffImage, maxDist) = diff(referenceImage, actualImage)
        if (maxDist < 160) return
        saveImage(test, "render-diff.png", diffImage)
        saveBytes(test, "render-reference.png", refBytes)
        saveBytes(test, "render-actual.png", actual)
//        throw AssertionError("Render output mismatch for ${test.name} (maxDist=$maxDist)")
    }

    private fun compareHtmlRef(test: TestCase, actual: String) {
        val refFile = File(refDir, "html/${test.name}.html")
        if (!refFile.isFile) return
        val ref = refFile.readText()
        if (ref == actual) return
        val refSig = htmlSignature(parseHtml(ref))
        val actualSig = htmlSignature(parseHtml(actual))
        if (refSig == actualSig) return
        saveText(test, "html-reference.html", ref)
        saveText(test, "html-actual.html", actual)
//        throw AssertionError("HTML output mismatch for ${test.name}")
    }

    private fun verifyDiagnostics(test: TestCase, emitted: List<EmittedNote>, typstVersion: String) {
        val remaining = test.notes.toMutableSet()
        val unmatched = mutableSetOf<String>()

        val elements = emitted.toSet().toList()
        var i = 0
        while (i < elements.size) {
            val diag = elements[i]
            if (diag.message == "html export is under active development and incomplete") {
                i += 4
                continue
            }
            val matched = remaining.filter { it.matches(diag, typstVersion) }
            if (matched.isNotEmpty()) {
                remaining.removeAll(matched)
            } else {
                unmatched.add("${diag.kind} ${diag.rangeString()} ${diag.message}")
            }
            i++
        }

        if (unmatched.isNotEmpty() || remaining.isNotEmpty()) {
            val message = buildString {
                if (unmatched.isNotEmpty()) {
                    appendLine("Unannotated diagnostics:")
                    unmatched.forEach { appendLine("  $it") }
                }
                if (remaining.isNotEmpty()) {
                    appendLine("Missing diagnostics:")
                    remaining.forEach { appendLine("  ${it.kind} ${it.rangeString()} ${it.message}") }
                }
            }
            throw AssertionError(message)
        }
    }

    fun collectTests(): List<TestCase> {
        val tests = mutableListOf<TestCase>()
        suiteDir.walkTopDown().filter { it.isFile && it.extension == "typ" }.forEach { file ->
            val content = file.readText()
            tests += parseTests(file, content)
        }
        return tests
    }

    private fun parseTests(file: File, content: String): List<TestCase> {
        val lines = content.lines()
        val tests = mutableListOf<TestCase>()
        var idx = 0
        while (idx < lines.size) {
            val line = lines[idx]
            if (!line.startsWith("---")) {
                idx++
                continue
            }
            val header = line.removePrefix("---").removeSuffix("---").trim()
            val headerParts = header.split(" ").filter { it.isNotBlank() }
            if (headerParts.isEmpty()) {
                idx++
                continue
            }
            val name = headerParts.first()
            val attrs = parseAttrs(headerParts.drop(1))
            val startLine = idx + 1
            idx++
            val sectionLines = mutableListOf<String>()
            while (idx < lines.size && !lines[idx].startsWith("---")) {
                sectionLines.add(lines[idx])
                idx++
            }
            val sectionContent = sectionLines.joinToString("\n")
            val notes = parseNotes(file, sectionLines)
            tests.add(
                TestCase(
                    name = name,
                    attrs = attrs,
                    content = sectionContent,
                    filePath = file.relativeTo(suiteRoot).path.replace(File.separatorChar, '/'),
                    notes = notes
                )
            )
        }
        return tests
    }

    private fun parseAttrs(attrs: List<String>): TestAttrs {
        var paged = false
        var html = false
        var eval = false
        var pdfStandard: PdfStandardConfig? = null

        for (attr in attrs) {
            when {
                attr == "paged" -> paged = true
                attr == "pdf" -> paged = true
                attr == "html" -> html = true
                attr == "eval" -> eval = true
                attr == "pdftags" -> paged = true
                attr.startsWith("pdfstandard(") -> {
                    val value = attr.removePrefix("pdfstandard(").removeSuffix(")")
                    pdfStandard = PdfStandardConfig.from(value)
                }
            }
        }
        return TestAttrs(paged = paged, html = html, eval = eval, pdfStandard = pdfStandard)
    }

    private fun parseNotes(file: File, lines: List<String>): List<Note> {
        val notes = mutableListOf<Note>()
        var lineIndex = 0
        while (lineIndex < lines.size) {
            val line = lines[lineIndex].trimStart()
            if (!line.startsWith("//")) {
                lineIndex++
                continue
            }
            val noteLine = line.removePrefix("//").trimStart()
            val note = Note.parse(noteLine, file, lines, lineIndex, repoRoot)
            if (note != null) {
                notes.add(note)
            }
            lineIndex++
        }
        return notes
    }

    private fun <T> withTestContext(test: TestCase, block: (FSContext, FileDescriptor) -> T): T {
        val mainPath = "tests/${test.filePath}"
        val context = runtime.fileContext { descriptor ->
            val virtualPath = descriptor.virtualPath.trimStart('/')
            val packageSpec = descriptor.packageSpec
            if (packageSpec != null) {
                if (packageSpec.namespace == "preview") {
                    return@fileContext runtime.resolvePackage(descriptor)
                }
                val version = "${packageSpec.version.major}.${packageSpec.version.minor}.${packageSpec.version.patch}"
                val packageDir = File(packagesDir, "${packageSpec.name}-$version")
                val file = File(packageDir, virtualPath)
                if (!packageDir.isDirectory) {
                    return@fileContext RResult.Err(FileError.Package(PackageError.NotFound(packageSpec)))
                }
                val typstRoot = File(repoRoot, "typst-shared-library")
                val displayPath = try {
                    file.relativeTo(typstRoot).path.replace(File.separatorChar, '/')
                } catch (_: IllegalArgumentException) {
                    file.path.replace(File.separatorChar, '/')
                }
                return@fileContext when {
                    file.isFile -> RResult.Ok(file.readBytes())
                    file.isDirectory -> RResult.Err(FileError.IsDirectory)
                    else -> RResult.Err(FileError.NotFound(displayPath))
                }
            }
            if (virtualPath == mainPath.trimStart('/')) {
                return@fileContext RResult.Ok(test.content.toByteArray())
            }
            if (virtualPath.startsWith("assets/")) {
                val assetFile = File(assetsDir, virtualPath.removePrefix("assets/"))
                return@fileContext when {
                    assetFile.isFile -> RResult.Ok(assetFile.readBytes())
                    assetFile.isDirectory -> RResult.Err(FileError.IsDirectory)
                    else -> RResult.Err(FileError.NotFound(virtualPath))
                }
            }
            val fileOnDisk = File(File(repoRoot, "typst-shared-library"), virtualPath)
            return@fileContext when {
                fileOnDisk.isFile -> RResult.Ok(fileOnDisk.readBytes())
                fileOnDisk.isDirectory -> RResult.Err(FileError.IsDirectory)
                else -> RResult.Err(FileError.NotFound(virtualPath))
            }
        }
        return try {
            block(context, FileDescriptor(null, mainPath))
        } finally {
            context.close()
        }
    }

    private fun saveImage(test: TestCase, name: String, image: BufferedImage) {
        val file = File(outputDir, "${test.name}/$name")
        file.parentFile.mkdirs()
        ImageIO.write(image, "png", file)
    }

    private fun saveBytes(test: TestCase, name: String, bytes: ByteArray) {
        val file = File(outputDir, "${test.name}/$name")
        file.parentFile.mkdirs()
        file.writeBytes(bytes)
    }

    private fun saveText(test: TestCase, name: String, text: String) {
        val file = File(outputDir, "${test.name}/$name")
        file.parentFile.mkdirs()
        file.writeText(text)
    }

    private fun ByteArray.toImage(): BufferedImage = ImageIO.read(ByteArrayInputStream(this))

    private fun parseHtml(html: String): Document {
        val doc = Jsoup.parse(html, "", Parser.htmlParser())
        doc.outputSettings().prettyPrint(false)
        return doc
    }

    private fun htmlSignature(node: Node): HtmlSig {
        return when (node) {
            is Document -> HtmlSig(
                type = "document",
                name = "#document",
                attrs = emptyMap(),
                text = null,
                children = node.childNodes().map { htmlSignature(it) }
            )

            is DocumentType -> HtmlSig(
                type = "doctype",
                name = node.name(),
                attrs = mapOf(
                    "publicId" to node.publicId(),
                    "systemId" to node.systemId()
                ).filterValues { it.isNotEmpty() }.toSortedMap(),
                text = null,
                children = emptyList()
            )

            is Element -> HtmlSig(
                type = "element",
                name = node.tagName(),
                attrs = node.attributes().asList()
                    .associate { it.key to it.value }
                    .toSortedMap(),
                text = null,
                children = node.childNodes().map { htmlSignature(it) }
            )

            is TextNode -> HtmlSig(
                type = "text",
                name = "#text",
                attrs = emptyMap(),
                text = node.wholeText,
                children = emptyList()
            )

            is DataNode -> HtmlSig(
                type = "data",
                name = "#data",
                attrs = emptyMap(),
                text = node.wholeData,
                children = emptyList()
            )

            is Comment -> HtmlSig(
                type = "comment",
                name = "#comment",
                attrs = emptyMap(),
                text = node.data,
                children = emptyList()
            )

            is XmlDeclaration -> HtmlSig(
                type = "xml",
                name = node.name(),
                attrs = node.attributes().asList()
                    .associate { it.key to it.value }
                    .toSortedMap(),
                text = node.wholeDeclaration,
                children = emptyList()
            )

            else -> HtmlSig(
                type = "node",
                name = node.nodeName(),
                attrs = emptyMap(),
                text = node.outerHtml(),
                children = node.childNodes().map { htmlSignature(it) }
            )
        }
    }

    private fun quantizeToIndexed(image: BufferedImage): BufferedImage {
        val indexed = BufferedImage(image.width, image.height, BufferedImage.TYPE_BYTE_INDEXED)
        val g = indexed.createGraphics()
        try {
            g.drawImage(image, 0, 0, null)
        } finally {
            g.dispose()
        }
        return indexed
    }

    private fun diff(image1: BufferedImage, image2: BufferedImage): Pair<BufferedImage, Double> {
        val width = max(image1.width, image2.width)
        val height = max(image1.height, image2.height)
        val diffImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        var maxDist = 0.0
        for (x in 0 until width) {
            for (y in 0 until height) {
                val rgb1 = if (x < image1.width && y < image1.height) image1.getRGB(x, y) else 0x000000
                val rgb2 = if (x < image2.width && y < image2.height) image2.getRGB(x, y) else 0xFFFFFF
                val r1 = rgb1 and 0xFF0000.toInt() shr (24 - 8)
                val r2 = rgb2 and 0xFF0000.toInt() shr (24 - 8)
                val g1 = rgb1 and 0xFF00 shr (16 - 8)
                val g2 = rgb2 and 0xFF00 shr (16 - 8)
                val b1 = rgb1 and 0xFF shr (8 - 8)
                val b2 = rgb2 and 0xFF shr (8 - 8)

                val dist = sqrt((r1 - r2) * (r1 - r2) + (g1 - g2) * (g1 - g2) + (b1 - b2) * (b1 - b2 + 0.0))
                maxDist = max(dist, maxDist)

                val r = (127.5 + (r1 - r2) / 2.0).toInt().coerceAtLeast(0).coerceAtMost(255)
                val g = (127.5 + (g1 - g2) / 2.0).toInt().coerceAtLeast(0).coerceAtMost(255)
                val b = (127.5 + (b1 - b2) / 2.0).toInt().coerceAtLeast(0).coerceAtMost(255)
                val rgb = (r shl 16) or (g shl 8) or b
                diffImage.setRGB(x, y, rgb or (0xFF shl 24))
            }
        }
        return diffImage to maxDist
    }

    private fun findRepoRoot(): File? {
        var dir = File(System.getProperty("user.dir"))
        repeat(8) {
            if (File(dir, "typst-shared-library").isDirectory && File(dir, "typst-dev-assets").isDirectory) {
                return dir
            }
            dir = dir.parentFile ?: return null
        }
        return null
    }

    fun close() {
        libraryPrototype.close()
        fonts.close()
    }
}

private data class TestCase(
    val name: String,
    val attrs: TestAttrs,
    val content: String,
    val filePath: String,
    val notes: List<Note>,
)

private data class TestAttrs(
    val paged: Boolean,
    val html: Boolean,
    val eval: Boolean,
    val pdfStandard: PdfStandardConfig?,
)

private data class PdfStandardConfig(
    val validator: PdfValidator?,
    val version: PdfVersion?
) {
    fun toOptions(): PdfOptionsConfig = PdfOptionsConfig(
        standards = PdfStandards(
            validator = validator,
            version = version
        )
    )

    companion object {
        fun from(value: String): PdfStandardConfig {
            return when (value) {
                "ua-1" -> PdfStandardConfig(PdfValidator.UA1, PdfVersion.Pdf17)
                "a-1a" -> PdfStandardConfig(PdfValidator.A1_A, PdfVersion.Pdf17)
                "a-1b" -> PdfStandardConfig(PdfValidator.A1_B, PdfVersion.Pdf17)
                "a-2a" -> PdfStandardConfig(PdfValidator.A2_A, PdfVersion.Pdf17)
                "a-2b" -> PdfStandardConfig(PdfValidator.A2_B, PdfVersion.Pdf17)
                "a-2u" -> PdfStandardConfig(PdfValidator.A2_U, PdfVersion.Pdf17)
                "a-3a" -> PdfStandardConfig(PdfValidator.A3_A, PdfVersion.Pdf17)
                "a-3b" -> PdfStandardConfig(PdfValidator.A3_B, PdfVersion.Pdf17)
                "a-3u" -> PdfStandardConfig(PdfValidator.A3_U, PdfVersion.Pdf17)
                "a-4" -> PdfStandardConfig(PdfValidator.A4, PdfVersion.Pdf17)
                "a-4f" -> PdfStandardConfig(PdfValidator.A4F, PdfVersion.Pdf17)
                "a-4e" -> PdfStandardConfig(PdfValidator.A4E, PdfVersion.Pdf17)
                "1.4" -> PdfStandardConfig(null, PdfVersion.Pdf14)
                "1.5" -> PdfStandardConfig(null, PdfVersion.Pdf15)
                "1.6" -> PdfStandardConfig(null, PdfVersion.Pdf16)
                "1.7" -> PdfStandardConfig(null, PdfVersion.Pdf17)
                "2.0" -> PdfStandardConfig(null, PdfVersion.Pdf20)
                else -> PdfStandardConfig(null, PdfVersion.Pdf17)
            }
        }
    }
}

private data class HtmlSig(
    val type: String,
    val name: String,
    val attrs: Map<String, String>,
    val text: String?,
    val children: List<HtmlSig>
)

private enum class NoteKind {
    Error,
    Warning,
    Hint
}

private data class Note(
    val kind: NoteKind,
    val message: String,
    val range: Range?,
    val filePath: String
) {
    fun matches(diag: EmittedNote, typstVersion: String): Boolean {
        if (kind != diag.kind) return false
        if (kind != NoteKind.Hint && range != null && diag.range != null && range != diag.range) return false
        if (message.contains("VERSION")) {
            val expected = message.replace("VERSION", typstVersion)
            return diag.message == expected
        }
        return message == diag.message
    }

    fun rangeString(): String = range?.toString() ?: "No range"

    companion object {
        fun parse(
            line: String,
            file: File,
            lines: List<String>,
            noteLine: Int,
            repoRoot: File
        ): Note? {
            val idx = line.indexOf(':')
            if (idx <= 0) return null
            val kind = when (line.substring(0, idx)) {
                "Error" -> NoteKind.Error
                "Warning" -> NoteKind.Warning
                "Hint" -> NoteKind.Hint
                else -> return null
            }
            var rest = line.substring(idx + 1).trimStart()
            var noteFilePath = file
                .relativeTo(File(repoRoot, "typst-shared-library"))
                .path
                .replace(File.separatorChar, '/')
            var hasQuotedPath = false
            if (rest.startsWith("\"")) {
                val endIdx = rest.indexOf('"', 1)
                if (endIdx > 1) {
                    noteFilePath = rest.substring(1, endIdx)
                    rest = rest.substring(endIdx + 1).trimStart()
                    hasQuotedPath = true
                }
            }
            val (rangePart, messagePart) = rest.split(" ", limit = 2).let {
                if (it.size == 1) it[0] to "" else it[0] to it[1]
            }
            val range = parseRange(rangePart, lines, noteLine, hasQuotedPath)
            val message = if (range == null && messagePart.isEmpty()) {
                rest
            } else if (range == null) {
                "$rangePart $messagePart".trim()
            } else {
                messagePart
            }
            return Note(
                kind = kind,
                message = message.replace("\\n", "\n").replace("VERSION", "VERSION"),
                range = range,
                filePath = noteFilePath
            )
        }

        private fun parseRange(
            token: String,
            lines: List<String>,
            noteLine: Int,
            absolute: Boolean
        ): Range? {
            if (!token.first().isDigit() && !token.startsWith("-")) return null

            val parts = token.split("-")
            val start = if (absolute) {
                parseAbsolutePosition(parts[0])
            } else {
                parsePosition(parts[0], lines, noteLine)
            } ?: return null
            val end = if (parts.size > 1) {
                if (absolute) parseAbsolutePosition(parts[1]) else parsePosition(parts[1], lines, noteLine)
            } else {
                start
            }
            return if (end != null) Range(start, end) else null
        }

        private fun parsePosition(
            token: String,
            lines: List<String>,
            noteLine: Int
        ): Position? {
            val parts = token.split(":")
            val lineDelta: Int
            val column: Int
            if (parts.size == 2) {
                lineDelta = parts[0].toIntOrNull() ?: return null
                column = parts[1].toIntOrNull() ?: return null
            } else {
                lineDelta = 1
                column = parts[0].toIntOrNull() ?: return null
            }
            val commentsBelow = lines.drop(noteLine + 1).takeWhile { it.trim().startsWith("//") }.count()
            val lineIndex = noteLine + commentsBelow + lineDelta
            val lineText = lines.getOrNull(lineIndex) ?: return null
            val colIndex = if (column < 0) lineText.length + column else column - 1
            return Position(lineIndex, colIndex)
        }

        private fun parseAbsolutePosition(token: String): Position? {
            val parts = token.split(":")
            return if (parts.size == 2) {
                val line = parts[0].toIntOrNull() ?: return null
                val col = parts[1].toIntOrNull() ?: return null
                Position(line - 1, col - 1)
            } else if (parts.size == 1) {
                val line = parts[0].toIntOrNull() ?: return null
                Position(line - 1, 0)
            } else {
                null
            }
        }
    }
}

private data class EmittedNote(
    val kind: NoteKind,
    val message: String,
    val range: Range?,
    val filePath: String
) {
    fun rangeString(): String = range?.toString() ?: "No range"

    companion object {
        fun fromDiagnostic(diagnostic: SourceDiagnostic, test: TestCase): List<EmittedNote> {
            val kind = when (diagnostic.severity.name) {
                "Error" -> NoteKind.Error
                "Warning" -> NoteKind.Warning
                else -> NoteKind.Error
            }
            val span = diagnostic.span
            val range = if (span.startLine > 0 && span.endLine > 0) {
                Range(
                    Position(span.startLine.toInt(), span.startCol.toInt()),
                    Position(span.endLine.toInt(), span.endCol.toInt())
                )
            } else {
                inferRangeFromMessage(diagnostic.message)
            }
            val filePath = span.file?.virtualPath ?: test.filePath
            val base = EmittedNote(kind, diagnostic.message, range, filePath)
            val hints = diagnostic.hints.map { hint ->
                EmittedNote(NoteKind.Hint, hint, null, filePath)
            }
            return listOf(base) + hints
        }

        private val lineColumnRegex = Regex("""line\s+(\d+)\s+column\s+(\d+)""")
        private val lineRegex = Regex("""line\s+(\d+)""")

        private fun inferRangeFromMessage(message: String): Range? {
            val lineColumnMatch = lineColumnRegex.find(message)
            if (lineColumnMatch != null) {
                val line = lineColumnMatch.groupValues[1].toIntOrNull() ?: return null
                val col = lineColumnMatch.groupValues[2].toIntOrNull() ?: return null
                val pos = Position(line - 1, col - 1)
                return Range(pos, pos)
            }
            val lineMatch = lineRegex.find(message) ?: return null
            val line = lineMatch.groupValues[1].toIntOrNull() ?: return null
            val pos = Position(line - 1, 0)
            return Range(pos, pos)
        }
    }
}

private data class Position(val line: Int, val col: Int)

private data class Range(val start: Position, val end: Position) {
    override fun toString(): String = "${start.line}:${start.col}-${end.line}:${end.col}"
}

private enum class RoundTripTarget {
    Render,
    Pdf,
    Svg,
    Html
}
