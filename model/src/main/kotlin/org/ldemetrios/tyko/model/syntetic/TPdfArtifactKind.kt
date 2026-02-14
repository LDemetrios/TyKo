package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.IntoStr
import org.ldemetrios.tyko.model.SerialName
import org.ldemetrios.tyko.model.TPdfArtifact
import org.ldemetrios.tyko.model.TStr
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.model.t


/**
 * Lists PDF artifact kinds (used at [TPdfArtifact.kind]).
 */
@SerialName("str")
enum class TPdfArtifactKind : IntoStr, Self<TPdfArtifactKind> {
    HEADER, FOOTER, PAGE, OTHER;

    override fun intoValue(): TStr = name.lowercase().t

    companion object {
        private val valuesByStr by lazy { TPdfArtifactKind.entries.associateBy { it.intoValue() } }
        fun fromValue(value: TValue): TPdfArtifactKind =
            if (value is TStr) valuesByStr[value]!! else throw AssertionError("Can't convert from $value")
    }
}