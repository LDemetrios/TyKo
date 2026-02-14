package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.IntoStr
import org.ldemetrios.tyko.model.Option
import org.ldemetrios.tyko.model.SerialName
import org.ldemetrios.tyko.model.TPdfAttach
import org.ldemetrios.tyko.model.TStr
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.model.t


/**
 * Lists PDF attachment relationship presets (used at [TPdfAttach.relationship]).
 */
@SerialName("str")
enum class TPdfAttachRelationship : IntoStr, Self<TPdfAttachRelationship> {
    SOURCE, DATA, ALTERNATIVE, SUPPLEMENT;

    override fun intoValue(): TStr = toString().lowercase().t

    companion object {
        fun fromValue(value: TValue): TPdfAttachRelationship {
            val candidate = (value as? TStr)?.value?.uppercase()
            return entries.find { it.name == candidate }
                ?: throw AssertionError("Can't convert from $value")
        }
    }
}
