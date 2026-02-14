package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

//!https://typst.app/docs/reference/foundations/duration/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/foundations/duration/](https://typst.app/docs/reference/foundations/duration/)
 * 
 * Represents a positive or negative span of time.
 */
@SerialName("duration")
data class TDuration(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/foundations/duration/](https://typst.app/docs/reference/foundations/duration/)
     * 
     * The number of weeks.
     * 
     * Typst type: int
     */
   val weeks : TInt? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/foundations/duration/](https://typst.app/docs/reference/foundations/duration/)
     * 
     * The number of days.
     * 
     * Typst type: int
     */
   val days : TInt? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/foundations/duration/](https://typst.app/docs/reference/foundations/duration/)
     * 
     * The number of hours.
     * 
     * Typst type: int
     */
   val hours : TInt? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/foundations/duration/](https://typst.app/docs/reference/foundations/duration/)
     * 
     * The number of minutes.
     * 
     * Typst type: int
     */
   val minutes : TInt? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/foundations/duration/](https://typst.app/docs/reference/foundations/duration/)
     * 
     * The number of seconds.
     * 
     * Typst type: int
     */
   val seconds : TInt? = null,
) : TValue {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.DURATION
    }
}
