package org.ldemetrios.tyko.model

import kotlinx.serialization.Serializable
import org.ldemetrios.tyko.model.DataSourceOrPreset
import org.ldemetrios.tyko.model.IntoStr
import org.ldemetrios.tyko.model.Option
import org.ldemetrios.tyko.model.SerialName
import org.ldemetrios.tyko.model.TCite
import org.ldemetrios.tyko.model.TStr
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.model.t

/**
 * Lists built-in citation forms (used at [TCite.form]).
 */
@SerialName("str")
enum class TCiteForm : IntoStr, Self<TCiteForm>, DataSourceOrPreset<TCiteForm> {
    NORMAL, PROSE, FULL, AUTHOR, YEAR;

    override fun intoValue(): TStr = name.lowercase().t

    companion object {
        private val valuesByStr by lazy { entries.associateBy { it.intoValue() } }
        fun fromValue(value: TValue) = if(value is TStr) valuesByStr[value]!! else throw AssertionError("Can't convert from $value")
    }
}