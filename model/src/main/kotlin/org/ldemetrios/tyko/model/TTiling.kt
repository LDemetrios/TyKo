package org.ldemetrios.tyko.model

import kotlinx.serialization.Serializable


@SerialName("tiling")
data class TTiling(
    @all:Body @all:Positional val body: TContent,
    val size: Smart<Point<TLength>> = TAuto,
    val spacing: Point<TLength> = Point(.0.t.pt, .0.t.pt),
    @all:SerialName("relative") val relativeTo: Smart<PaintRelativeTo> = TAuto,
) : TPaint, Smart<TTiling>, TValue, Option<TTiling>, ArrayOrSingle<TTiling> {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.TILING
    }
}
