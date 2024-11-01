package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data class TPlace(
	val alignment: TAlignmentOrAuto? = null,
	val body: TContent,
	val scope: TPlaceScope? = null,
	val float: TBool? = null,
	val clearance: TLength? = null,
	val dx: TRelative? = null,
	val dy: TRelative? = null,
	override val label:TLabel? = null,
) : TContent() {
    override fun format() = Representations.elementRepr(
		"place",
		ArgumentEntry(false, null, alignment),
		ArgumentEntry(false, null, body),
		ArgumentEntry(false, "scope", scope),
		ArgumentEntry(false, "float", float),
		ArgumentEntry(false, "clearance", clearance),
		ArgumentEntry(false, "dx", dx),
		ArgumentEntry(false, "dy", dy),
	)
	override fun func() = TPlace
    companion object : TElement("place") {
        internal val alignmentType : InternalType = UnionType(ConcreteType("alignment"), ConcreteType("auto"))
        internal val bodyType : InternalType = ConcreteType("content")
        internal val scopeType : InternalType = ConcreteType("place-scope")
        internal val floatType : InternalType = ConcreteType("bool")
        internal val clearanceType : InternalType = ConcreteType("length")
        internal val dxType : InternalType = ConcreteType("relative")
        internal val dyType : InternalType = ConcreteType("relative")

    }
}

data class TSetPlace(
	val alignment: TAlignmentOrAuto? = null,
	val scope: TPlaceScope? = null,
	val float: TBool? = null,
	val clearance: TLength? = null,
	val dx: TRelative? = null,
	val dy: TRelative? = null,
) : TSetRule("place") {
    override fun format() = Representations.setRepr(
		"place",
		ArgumentEntry(false, null, alignment),
		ArgumentEntry(false, "scope", scope),
		ArgumentEntry(false, "float", float),
		ArgumentEntry(false, "clearance", clearance),
		ArgumentEntry(false, "dx", dx),
		ArgumentEntry(false, "dy", dy),
	)
}

