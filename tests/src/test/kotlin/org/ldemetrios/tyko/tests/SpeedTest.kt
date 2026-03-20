package org.ldemetrios.tyko.tests

import io.kotest.core.spec.style.FreeSpec
import org.ldemetrios.tyko.compiler.Feature
import org.ldemetrios.tyko.compiler.FontCollectionPointer
import org.ldemetrios.tyko.compiler.orThrow
import org.ldemetrios.tyko.compiler.outputOrThrow
import org.ldemetrios.tyko.driver.chicory_based.ChicoryTypstCore
import org.ldemetrios.tyko.driver.chicory_based.defaultWasiOptions
import org.ldemetrios.tyko.model.TAuto
import org.ldemetrios.tyko.model.TSetPage
import org.ldemetrios.tyko.model.TSetText
import org.ldemetrios.tyko.model.pt
import org.ldemetrios.tyko.model.t
import org.ldemetrios.tyko.runtime.FontCollection
import org.ldemetrios.tyko.runtime.TypstRuntime
import org.ldemetrios.tyko.runtime.buildWorldSpecification
import java.io.File

class SpeedTest : FreeSpec({
    "Bootstraping takes reasonable time" {
        val repoRoot = File("/Users/ldemis/Workspace/Projects/TyKo")
        val beginTime = System.currentTimeMillis()
        val runtime = TypstRuntime(
            buildWorldSpecification {
                library {
                    fresh(setOf(Feature.Html, Feature.A11yExtras))
                }
            }
        ) {
            ChicoryTypstCore(
                defaultWasiOptions()
            )
        }

        val bootstrap = System.currentTimeMillis()

        val fonts: FontCollection = runtime.fontCollection(
            includeSystem = true,
            includeEmbedded = true,
            fontPaths = listOf(
                File(repoRoot, "typst-dev-assets/files/fonts").absolutePath,
                File(repoRoot, "typst-assets/files/fonts").absolutePath,
            )
        )

        val library = runtime.library(setOf(Feature.Html, Feature.A11yExtras))
            .alterStyles(
                TSetPage(width = 120.0.t.pt),
                TSetPage(height = TAuto),
                TSetPage(margin = 10.0.t.pt),
                TSetText(size = 10.0.t.pt),
                fonts = fonts,
                append = true,
            ).orThrow()

        val setup = System.currentTimeMillis()

        val document = runtime.compileHtml {
            fonts(fonts)
            library(library)
            files {
                singleFile("/main.typ", "Hello, world!")
            }
        }.outputOrThrow()

        println(document)
        val endTime = System.currentTimeMillis()
        println("Time taken: ${bootstrap - beginTime}ms for bootstraping, ${setup - bootstrap}ms for setting up, ${endTime - setup}ms for compilation")
        runtime.close()
    }
})
