package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

open class TType(val name: String) : TValue{
    override fun type() : TType = TType
    override fun format() = Representations.reprOf(this)
    companion object: TType("type") {
    }   
}
