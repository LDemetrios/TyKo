package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("type")
data class TType(@all:Positional val name: TStr) : TValue, Smart<TType> {
    constructor(name: String) : this(name.t)

    override fun type(): TType = TYPE

    companion object {
        val STATE: TType = TType("state")
        val NONE = TType("none")
        val AUTO = TType("auto")
        val BOOL = TType("bool")
        val COUNTER: TType = TType("counter")
        val INT = TType("int")
        val FLOAT = TType("float")
        val DECIMAL = TType("decimal")
        val FRACTION = TType("fraction")
        val LENGTH = TType("length")
        val RELATIVE = TType("relative")
        val RATIO = TType("ratio")
        val ANGLE = TType("angle")
        val DIRECTION = TType("direction")
        val ALIGNMENT = TType("alignment")
        val STROKE = TType("stroke")
        val COLOR = TType("color")
        val GRADIENT = TType("gradient")
        val TILING = TType("tiling")
        val DURATION = TType("duration")
        val DATETIME = TType("datetime")
        val BYTES = TType("bytes")
        val STR = TType("str")
        val SYMBOL = TType("symbol")
        val REGEX = TType("regex")
        val LABEL = TType("label")
        val CONTENT = TType("content")
        val ARRAY = TType("array")
        val DICT = TType("dict")
        val ARGS = TType("args")
        val FUNC = TType("func")
        val TYPE = TType("type")
        val MODULE = TType("module")
        val VERSION = TType("version")
        val LOCATION = TType("location")
        val SELECTOR = TType("selector")
        val STYLE = TType("style")
        val PATH = TType("path")
    }

    override fun repr(sb: StringBuilder) {
        sb.append(name.value)
    }
}
