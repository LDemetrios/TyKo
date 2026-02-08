package org.ldemetrios.tyko.e2e

import com.dylibso.chicory.wasi.WasiOptions
import org.ldemetrios.tyko.driver.chicory.ChicoryTypstCore
import java.nio.file.Paths

class ChicoryE2ETest : E2ETest(
    ChicoryTypstCore(
        WasiOptions.builder()
            .inheritSystem()
            .withEnvironment("RUST_BACKTRACE", "full")
            .withDirectory("/", Paths.get("/"))
            .build()
    )
)
