package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.IntoDict
import org.ldemetrios.tyko.model.IntoValue
import org.ldemetrios.tyko.model.SerialName
import org.ldemetrios.tyko.model.TDict
import org.ldemetrios.tyko.model.TInt
import org.ldemetrios.tyko.model.TLabel
import org.ldemetrios.tyko.model.TLength
import org.ldemetrios.tyko.model.TLocation
import org.ldemetrios.tyko.model.TStr
import org.ldemetrios.tyko.model.TValue


sealed interface TLinkDestination : IntoValue {
    companion object {
        fun fromValue(value: TValue): TLinkDestination = when (value) {
            is TDict<*> -> LocationOnPage.fromValue(value)
            is TStr -> value
            is TLabel -> value
            is TLocation -> value
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

@SerialName("dict")
data class LocationOnPage(val page: TInt, val x: TLength, val y: TLength) : IntoDict<IntoValue>, TLinkDestination, Self<LocationOnPage> {
    override fun intoValue(): TDict<IntoValue> = TDict(
        mapOf(
            "page" to page,
            "x" to x,
            "y" to y
        )
    )

    companion object {
        fun fromValue(value: TValue) = when (value) {
            is TDict<*> -> LocationOnPage(
                value["page"]!!.intoValue() as TInt,
                value["x"]!!.intoValue() as TLength,
                value["y"]!!.intoValue() as TLength
            )

            else -> throw AssertionError("Can't convert from $value")
        }
    }
}
