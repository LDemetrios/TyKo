@file:Suppress("unused", "RedundantVisibilityModifier")

package org.ldemetrios.tyko.model

import kotlin.String
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.js.JSBoolean
import org.ldemetrios.js.JSNumber
import org.ldemetrios.js.JSObject
import org.ldemetrios.js.JSString
import org.ldemetrios.js.JSUndefined
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

@TInterfaceType(
    "figure",
    ["figure", "content"],
    TFigureImpl::class,
)
public interface TFigure : TContent {
    public val body: TContent

    public val placement: TAlignmentOrAutoOrNone?

    public val scope: TFigureScope?

    public val caption: TContentOrNone?

    public val kind: TAutoOrFunctionOrStr?

    public val supplement: TAutoOrContentOrFunctionOrNone?

    public val numbering: TFunctionOrNoneOrStr?

    public val gap: TLength?

    public val outlined: TBool?

    override fun func(): TElement = TFigure

    public companion object : TElementImpl("figure") {
        internal val bodyType: InternalType = ConcreteType("content")

        internal val placementType: InternalType = UnionType(ConcreteType("alignment"),
                ConcreteType("auto"), ConcreteType("none"))

        internal val scopeType: InternalType = ConcreteType("figure-scope")

        internal val captionType: InternalType = UnionType(ConcreteType("content"),
                ConcreteType("none"))

        internal val kindType: InternalType = UnionType(ConcreteType("auto"), ConcreteType("function"),
                ConcreteType("str"))

        internal val supplementType: InternalType = UnionType(ConcreteType("auto"),
                ConcreteType("content"), ConcreteType("function"), ConcreteType("none"))

        internal val numberingType: InternalType = UnionType(ConcreteType("function"),
                ConcreteType("none"), ConcreteType("str"))

        internal val gapType: InternalType = ConcreteType("length")

        internal val outlinedType: InternalType = ConcreteType("bool")
    }
}

internal data class TFigureImpl(
    @SerialName("body")
    override val body: TContent,
    @SerialName("placement")
    override val placement: TAlignmentOrAutoOrNone? = null,
    @SerialName("scope")
    override val scope: TFigureScope? = null,
    @SerialName("caption")
    override val caption: TContentOrNone? = null,
    @SerialName("kind")
    override val kind: TAutoOrFunctionOrStr? = null,
    @SerialName("supplement")
    override val supplement: TAutoOrContentOrFunctionOrNone? = null,
    @SerialName("numbering")
    override val numbering: TFunctionOrNoneOrStr? = null,
    @SerialName("gap")
    override val gap: TLength? = null,
    @SerialName("outlined")
    override val outlined: TBool? = null,
    @SerialName("label")
    override val label: TLabel? = null,
) : TFigure {
    override fun format(): String = Representations.elementRepr("figure",ArgumentEntry(false, null,
            `body`),ArgumentEntry(false, "placement", `placement`),ArgumentEntry(false, "scope",
            `scope`),ArgumentEntry(false, "caption", `caption`),ArgumentEntry(false, "kind",
            `kind`),ArgumentEntry(false, "supplement", `supplement`),ArgumentEntry(false, "numbering",
            `numbering`),ArgumentEntry(false, "gap", `gap`),ArgumentEntry(false, "outlined",
            `outlined`),ArgumentEntry(false, "label", `label`),)
}

@TypstOverloads
public fun TFigure(
    @TContentBody body: TContent,
    placement: TAlignmentOrAutoOrNone? = null,
    scope: TFigureScope? = null,
    caption: TContentOrNone? = null,
    kind: TAutoOrFunctionOrStr? = null,
    supplement: TAutoOrContentOrFunctionOrNone? = null,
    numbering: TFunctionOrNoneOrStr? = null,
    gap: TLength? = null,
    outlined: TBool? = null,
    label: TLabel? = null,
): TFigure = TFigureImpl(`body`, `placement`, `scope`, `caption`, `kind`, `supplement`, `numbering`,
        `gap`, `outlined`, `label`)

@TSetRuleType(
    "figure",
    TSetFigureImpl::class,
)
public interface TSetFigure : TSetRule {
    override val elem: String
        get() = "figure"

    public val placement: TAlignmentOrAutoOrNone?

    public val scope: TFigureScope?

    public val caption: TContentOrNone?

    public val kind: TAutoOrFunctionOrStr?

    public val supplement: TAutoOrContentOrFunctionOrNone?

    public val numbering: TFunctionOrNoneOrStr?

    public val gap: TLength?

    public val outlined: TBool?

    override fun format(): String = Representations.setRepr("figure",ArgumentEntry(false, "placement",
            `placement`),ArgumentEntry(false, "scope", `scope`),ArgumentEntry(false, "caption",
            `caption`),ArgumentEntry(false, "kind", `kind`),ArgumentEntry(false, "supplement",
            `supplement`),ArgumentEntry(false, "numbering", `numbering`),ArgumentEntry(false, "gap",
            `gap`),ArgumentEntry(false, "outlined", `outlined`),)
}

internal class TSetFigureImpl(
    @SerialName("placement")
    override val placement: TAlignmentOrAutoOrNone? = null,
    @SerialName("scope")
    override val scope: TFigureScope? = null,
    @SerialName("caption")
    override val caption: TContentOrNone? = null,
    @SerialName("kind")
    override val kind: TAutoOrFunctionOrStr? = null,
    @SerialName("supplement")
    override val supplement: TAutoOrContentOrFunctionOrNone? = null,
    @SerialName("numbering")
    override val numbering: TFunctionOrNoneOrStr? = null,
    @SerialName("gap")
    override val gap: TLength? = null,
    @SerialName("outlined")
    override val outlined: TBool? = null,
) : TSetFigure

@TypstOverloads
public fun TSetFigure(
    placement: TAlignmentOrAutoOrNone? = null,
    scope: TFigureScope? = null,
    caption: TContentOrNone? = null,
    kind: TAutoOrFunctionOrStr? = null,
    supplement: TAutoOrContentOrFunctionOrNone? = null,
    numbering: TFunctionOrNoneOrStr? = null,
    gap: TLength? = null,
    outlined: TBool? = null,
): TSetFigure = TSetFigureImpl(`placement`, `scope`, `caption`, `kind`, `supplement`, `numbering`,
        `gap`, `outlined`)
