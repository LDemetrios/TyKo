package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.Representations

abstract class TSetRule(
    val elem: String,
) : TStyle {
    override fun type(): TType = TStyle
    companion object : TType("style") {
    }
}


