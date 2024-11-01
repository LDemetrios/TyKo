package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.model.TFunction
import org.ldemetrios.typst4k.rt.Representations
import org.ldemetrios.typst4k.rt.Representations.reprOf
import org.ldemetrios.typst4k.rt.t

open class TElement(val name: TStr) : TFunction, TSelector {
    constructor (name: String) : this(name.t)
    override fun format(): String = reprOf(this)
    override fun type(): TType = super<TFunction>.type()
}