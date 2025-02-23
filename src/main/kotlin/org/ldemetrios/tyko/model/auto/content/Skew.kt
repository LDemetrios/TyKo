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
    "skew",
    ["skew", "content"],
    TSkewImpl::class,
)
public interface TSkew : TContent {
    public val body: TContent

    public val ax: TAngle?

    public val ay: TAngle?

    public val origin: TAlignment?

    public val reflow: TBool?

    override fun func(): TElement = TSkew

    public companion object : TElementImpl("skew") {
        internal val bodyType: InternalType = ConcreteType("content")

        internal val axType: InternalType = ConcreteType("angle")

        internal val ayType: InternalType = ConcreteType("angle")

        internal val originType: InternalType = ConcreteType("alignment")

        internal val reflowType: InternalType = ConcreteType("bool")
    }
}

internal data class TSkewImpl(
    @SerialName("body")
    override val body: TContent,
    @SerialName("ax")
    override val ax: TAngle? = null,
    @SerialName("ay")
    override val ay: TAngle? = null,
    @SerialName("origin")
    override val origin: TAlignment? = null,
    @SerialName("reflow")
    override val reflow: TBool? = null,
    @SerialName("label")
    override val label: TLabel? = null,
) : TSkew {
    override fun format(): String = Representations.elementRepr("skew",ArgumentEntry(false, null,
            `body`),ArgumentEntry(false, "ax", `ax`),ArgumentEntry(false, "ay", `ay`),ArgumentEntry(false,
            "origin", `origin`),ArgumentEntry(false, "reflow", `reflow`),ArgumentEntry(false, "label",
            `label`),)
}

@TypstOverloads
public fun TSkew(
    @TContentBody body: TContent,
    ax: TAngle? = null,
    ay: TAngle? = null,
    origin: TAlignment? = null,
    reflow: TBool? = null,
    label: TLabel? = null,
): TSkew = TSkewImpl(`body`, `ax`, `ay`, `origin`, `reflow`, `label`)

@TSetRuleType(
    "skew",
    TSetSkewImpl::class,
)
public interface TSetSkew : TSetRule {
    override val elem: String
        get() = "skew"

    public val ax: TAngle?

    public val ay: TAngle?

    public val origin: TAlignment?

    public val reflow: TBool?

    override fun format(): String = Representations.setRepr("skew",ArgumentEntry(false, "ax",
            `ax`),ArgumentEntry(false, "ay", `ay`),ArgumentEntry(false, "origin",
            `origin`),ArgumentEntry(false, "reflow", `reflow`),)
}

internal class TSetSkewImpl(
    @SerialName("ax")
    override val ax: TAngle? = null,
    @SerialName("ay")
    override val ay: TAngle? = null,
    @SerialName("origin")
    override val origin: TAlignment? = null,
    @SerialName("reflow")
    override val reflow: TBool? = null,
) : TSetSkew

@TypstOverloads
public fun TSetSkew(
    ax: TAngle? = null,
    ay: TAngle? = null,
    origin: TAlignment? = null,
    reflow: TBool? = null,
): TSetSkew = TSetSkewImpl(`ax`, `ay`, `origin`, `reflow`)
