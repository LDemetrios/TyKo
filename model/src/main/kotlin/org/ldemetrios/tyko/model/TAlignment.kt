package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


sealed interface TAlignment : TValue, Smart<TAlignment>, ArrayOrSingle<TAlignment> {
    override fun type(): TType = TYPE
    abstract val h: THAlignment?
    abstract val v: TVAlignment?

    companion object {
        val TYPE = TType.ALIGNMENT
    }
}


@SerialName("alignment")
internal class TCompositeAlignment(override val h: THAlignment, override val v: TVAlignment) : TAlignment {
    override fun repr(sb: StringBuilder) {
        sb.sumOfNotNull(h, v)
    }
}

@SerialName("h-alignment")
enum class THAlignment : TAlignment {
    @SerialName("left")
    LEFT,
    @SerialName("center")
    CENTER,
    @SerialName("right")
    RIGHT,
    @SerialName("start")
    START,
    @SerialName("end")
    END;

    override val h: THAlignment get() = this
    override val v: TVAlignment? get() = null

    override fun repr(sb: StringBuilder) {
        sb.append(name.lowercase())
    }
}

@SerialName("v-alignment")
enum class TVAlignment : TAlignment {
    @SerialName("top")
    TOP,
    @SerialName("horizon")
    HORIZON,
    @SerialName("bottom")
    BOTTOM;

    override val h: THAlignment? get() = null
    override val v: TVAlignment get() = this

    override fun repr(sb: StringBuilder) {
        sb.append(name.lowercase())
    }
}

operator fun THAlignment.plus(v: TVAlignment): TAlignment = TCompositeAlignment(this, v)
operator fun TVAlignment.plus(h: THAlignment): TAlignment = TCompositeAlignment(h, this)
