package org.ldemetrios.tyko.e2e

import io.kotest.core.spec.style.FreeSpec
import org.ldemetrios.tyko.driver.api.TypstCore
import org.ldemetrios.tyko.runtime.TypstRuntime
import java.nio.file.Paths

open class E2ETest(typstCore: TypstCore) : FreeSpec({
    "Works as expected" {
        val runtime = TypstRuntime(typstCore)
        val fonts = runtime.fontCollection(includeSystem = true, includeEmbedded = true, listOf())
        runtime.close()
    }
})