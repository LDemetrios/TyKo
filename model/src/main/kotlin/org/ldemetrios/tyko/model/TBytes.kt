package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.DataSource

//!https://typst.app/docs/reference/foundations/bytes/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/bytes/](https://typst.app/docs/reference/foundations/bytes/)
 * 
 * A sequence of bytes.
 * 
 * This is conceptually similar to an array of [integers](https://typst.app/docs/reference/foundations/int/) between `0` and `255`, but represented much more efficiently. You can iterate over it using a [for loop](https://typst.app/docs/reference/scripting/#loops).
 * 
 * You can convert
 * 
 * - a [string](https://typst.app/docs/reference/foundations/str/) or an [array](https://typst.app/docs/reference/foundations/array/) of integers to bytes with the [`bytes`](https://typst.app/docs/reference/foundations/bytes/) constructor
 * - bytes to a string with the [`str`](https://typst.app/docs/reference/foundations/str/) constructor, with UTF-8 encoding
 * - bytes to an array of integers with the [`array`](https://typst.app/docs/reference/foundations/array/) constructor
 * 
 * When [reading](https://typst.app/docs/reference/data-loading/read/) data from a file, you can decide whether to load it as a string or as raw bytes.
 * 
 * ```typ
 * #bytes((123, 160, 22, 0)) \
 * #bytes("Hello 😃")
 * 
 * #let data = read(
 *   "rhino.png",
 *   encoding: none,
 * )
 * 
 * // Magic bytes.
 * #array(data.slice(0, 4)) \
 * #str(data.slice(1, 4))
 * ```
 * <img src="https://typst.app/assets/docs/sJtYFgVyQkDZELEHje5ywwAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("bytes")
data class TBytes(val value: ByteArray) : TValue, DataSource {
    override fun type(): TType = TYPE

    override fun equals(other: Any?): Boolean =
        this === other || other is TBytes && value.contentEquals(other.value)

    override fun hashCode(): Int = value.contentHashCode()

    companion object {
        val TYPE = TType.BYTES
    }

    override fun repr(sb: StringBuilder) {
        sb.append("bytes((")
        value.forEach {
            sb.append("0x")
            sb.append(it.toInt().and(255).toString(16).padStart(2, '0'))
            sb.append(",")
        }
        sb.append("))")
    }
}

/**
 * Convenience extension converting `ByteArray` into the corresponding Typst value.
 */
val ByteArray.t get() = TBytes(this)
