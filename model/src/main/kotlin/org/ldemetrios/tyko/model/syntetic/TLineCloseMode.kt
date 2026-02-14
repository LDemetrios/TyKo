package org.ldemetrios.tyko.model


/**
 * Lists supported line close modes.
 */
@SerialName("str")
enum class TLineCloseMode(val value: String) : IntoStr, Self<TLineCloseMode> {
    @SerialName("smooth")
    SMOOTH("smooth"),

    @SerialName("straight")
    STRAIGHT("straight");

    override fun intoValue(): TStr = TStr(value)

    companion object {
        fun fromValue(value: TValue): TLineCloseMode {
            val str = (value as TStr).value
            return entries.first { it.value == str }
        }
    }
}

