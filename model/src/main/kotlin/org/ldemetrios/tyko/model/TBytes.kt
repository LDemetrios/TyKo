package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


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

val ByteArray.t get() = TBytes(this)
