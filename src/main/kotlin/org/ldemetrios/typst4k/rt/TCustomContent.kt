@file:Suppress("PackageDirectoryMismatch")

package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.repr
import org.ldemetrios.typst4k.rt.t

data class TCustomContent(val function: String, val positional: List<TValue>, val named: Map<String, TValue>, override val label: TLabel? = null) :
    TContent() {
    override fun format(): String {
        val sb = StringBuilder(function)
        sb.append("(")
        for (arg in positional) {
            sb.append(arg.repr())
            sb.append(", ")
        }
        for ((key, value) in named) {
            sb.append(key)
            sb.append(": ")
            sb.append(value.repr())
            sb.append(", ")
        }
        sb.setLength(sb.length - 2)
        sb.append(")")
        return sb.toString()
    }

    override fun func() = TElement(function)
}