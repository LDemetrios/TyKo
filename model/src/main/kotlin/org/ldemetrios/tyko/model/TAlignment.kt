package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

//!https://typst.app/docs/reference/layout/alignment/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/alignment/](https://typst.app/docs/reference/layout/alignment/)
 * 
 * Where to align something along an axis.
 * 
 * Possible values are:
 * 
 * - `start`: Aligns at the [start](https://typst.app/docs/reference/layout/direction/#definitions-start) of the [text direction](https://typst.app/docs/reference/text/text/#parameters-dir).
 * - `end`: Aligns at the [end](https://typst.app/docs/reference/layout/direction/#definitions-end) of the [text direction](https://typst.app/docs/reference/text/text/#parameters-dir).
 * - `left`: Align at the left.
 * - `center`: Aligns in the middle, horizontally.
 * - `right`: Aligns at the right.
 * - `top`: Aligns at the top.
 * - `horizon`: Aligns in the middle, vertically.
 * - `bottom`: Align at the bottom.
 * 
 * These values are available globally and also in the alignment type's scope, so you can write either of the following two:
 * 
 * ```typ
 * #align(center)[Hi]
 * #align(alignment.center)[Hi]
 * ```
 * <img src="https://typst.app/assets/docs/ZprGjLBPSUJ5f2a4fil8IAAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_2D alignments_**
 * 
 * To align along both axes at the same time, add the two alignments using the `+` operator. For example, `top + right` aligns the content to the top right corner.
 * 
 * ```typ
 * #set page(height: 3cm)
 * #align(center + bottom)[Hi]
 * ```
 * <img src="https://typst.app/assets/docs/X3ZrV0nn1RgePWtIVMB4XgAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Fields_**
 * 
 * The `x` and `y` fields hold the alignment's horizontal and vertical components, respectively (as yet another `alignment`). They may be `none`.
 * 
 * ```typ
 * #(top + right).x \
 * #left.x \
 * #left.y (none)
 * ```
 * <img src="https://typst.app/assets/docs/ecr8JX7jRnRHSrOlOhwwRwAAAAAAAAAA.png" alt="Preview" />
 */
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

/**
 * Lists horizontal alignment options.
 */
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

/**
 * Lists vertical alignment options.
 */
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

/**
 * Combines horizontal and vertical alignment into a composite alignment value.
 */
operator fun THAlignment.plus(v: TVAlignment): TAlignment = TCompositeAlignment(this, v)
/**
 * Combines vertical and horizontal alignment into a composite alignment value.
 */
operator fun TVAlignment.plus(h: THAlignment): TAlignment = TCompositeAlignment(h, this)
