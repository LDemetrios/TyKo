package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TImage(
	val path: TStr,
	val format: TAutoOrImageFormat? = null,
	val width: TAutoOrRelative? = null,
	val height: TAutoOrFractionOrRelative? = null,
	val alt: TNoneOrStr? = null,
	val fit: TImageFit? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"image",
		ArgumentEntry(false, null, path),
		ArgumentEntry(false, "format", format),
		ArgumentEntry(false, "width", width),
		ArgumentEntry(false, "height", height),
		ArgumentEntry(false, "alt", alt),
		ArgumentEntry(false, "fit", fit),
	)
	override fun func() = TImage
    companion object : TElement("image") {
        internal val pathType : InternalType = ConcreteType("str")
        internal val formatType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("image-format"))
        internal val widthType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("relative"))
        internal val heightType : InternalType = UnionType(ConcreteType("auto"), ConcreteType("fraction"), ConcreteType("relative"))
        internal val altType : InternalType = UnionType(ConcreteType("none"), ConcreteType("str"))
        internal val fitType : InternalType = ConcreteType("image-fit")

    }
}

data class TSetImage(
	val format: TAutoOrImageFormat? = null,
	val width: TAutoOrRelative? = null,
	val height: TAutoOrFractionOrRelative? = null,
	val alt: TNoneOrStr? = null,
	val fit: TImageFit? = null,
) : TSetRule("image") {
    override fun format() = Representations.setRepr(
		"image",
		ArgumentEntry(false, "format", format),
		ArgumentEntry(false, "width", width),
		ArgumentEntry(false, "height", height),
		ArgumentEntry(false, "alt", alt),
		ArgumentEntry(false, "fit", fit),
	)
}

