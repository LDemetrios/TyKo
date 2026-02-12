package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable


@SerialName("datetime")
data class TDatetime(
    val year: Option<TInt>? = null,
    val month: Option<TInt>? = null,
    val day: Option<TInt>? = null,
    val hour: Option<TInt>? = null,
    val minute: Option<TInt>? = null,
    val second: Option<TInt>? = null,
) : TValue, Option<TDatetime>, Smart<TDatetime> {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.DATETIME
    }

    override fun repr(sb: StringBuilder) {
        sb.append("datetime(")
        if (year is TInt) sb.append("year:").append(year.value).append(",")
        if (month is TInt) sb.append("month:").append(month.value).append(",")
        if (day is TInt) sb.append("day:").append(day.value).append(",")
        if (hour is TInt) sb.append("hour:").append(hour.value).append(",")
        if (minute is TInt) sb.append("minute:").append(minute.value).append(",")
        if (second is TInt) sb.append("second:").append(second.value).append(",")
        sb.append(")")
    }
}
