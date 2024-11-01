package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

data object TPlugin : TValue {
    override fun format() = Representations.structRepr(
		"plugin",
	)
	override fun type(): TType = TPluginType
}
data object TPluginType : TType("plugin")
