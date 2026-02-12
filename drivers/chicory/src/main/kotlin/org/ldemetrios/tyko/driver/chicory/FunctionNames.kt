package org.ldemetrios.tyko.driver.chicory

import java.io.BufferedReader

internal object FunctionNames {
    private const val RESOURCE = "typst-shared-function-names.txt"

    private val namesByIndex: Map<Int, String> by lazy {
        val stream = FunctionNames::class.java.classLoader.getResourceAsStream(RESOURCE) ?: return@lazy emptyMap()
        BufferedReader(stream.reader()).useLines { lines ->
            lines.mapNotNull { line ->
                if (line.isBlank()) {
                    null
                } else {
                    val tab = line.indexOf('\t')
                    if (tab <= 0 || tab == line.length - 1) {
                        null
                    } else {
                        val idx = line.substring(0, tab).toIntOrNull() ?: return@mapNotNull null
                        idx to line.substring(tab + 1)
                    }
                }
            }.toMap()
        }
    }

    fun nameOf(idx: Int): String? = namesByIndex[idx]
}
