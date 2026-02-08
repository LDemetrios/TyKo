package org.ldemetrios.tyko.model

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Suppress("UNCHECKED_CAST")
internal fun <T> Any?.castUnchecked(): T = this as T

sealed interface IntoValue {
    fun intoValue(): TValue

    companion object {
        fun <T : IntoValue> fromValue(value: TValue, type: KType): T {
            return value.into(type) as T
        }
    }
}

sealed interface IntoStr : IntoValue {
    override fun intoValue(): TStr

    companion object {
        fun fromValue(value: TValue, type: KType): TStr {
            return value as TStr
        }
    }
}

sealed interface IntoCollection<out T : IntoValue> : IntoValue {
    override fun intoValue(): TCollection<T>

    companion object {
        inline fun <reified T : IntoValue> fromValue(value: TValue): IntoCollection<T> = when (value) {
            is TArray<*> -> TArray.fromValue<T>(value)
            is TDict<*> -> TDict.fromValue<T>(value)
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

sealed interface IntoDict<out T : IntoValue> : IntoCollection<T> {
    override fun intoValue(): TDict<T>
}

@SerialName("str")
enum class TLineCloseMode(val value: String) : IntoStr {
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

@SerialName("array")

data class Point<out T : IntoValue>(val x: T, val y: T) : IntoValue, Smart<Point<T>>, TCurveControl<T>,
    Option<Point<T>> {
    override fun intoValue(): TArray<T> = TArray(listOf(x, y))

    companion object {
        inline fun <reified T : IntoValue> fromValue(value: TValue): Point<T> {
            val array = value as TArray<*>
            require(array.size == 2) { "Point requires 2 items, got ${array.size}" }
            val x = (array[0] as TValue).into<T>()
            val y = (array[1] as TValue).into<T>()
            return Point(x, y)
        }

        fun fromValue(value: TValue, type: KType): Point<*> {
            val arg = type.arguments.firstOrNull()?.type
            val array = value as TArray<*>
            require(array.size == 2) { "Point requires 2 items, got ${array.size}" }
            return if (arg == null) {
                Point(array[0] as IntoValue, array[1] as IntoValue)
            } else {
                val x = (array[0] as TValue).into(arg)
                val y = (array[1] as TValue).into(arg)
                Point(x, y)
            }
        }
    }
}

@SerialName("str")
enum class PaintRelativeTo : IntoStr, Smart<PaintRelativeTo> {
    @SerialName("self")
    SELF,

    @SerialName("parent")
    PARENT;

    override fun intoValue(): TStr = TStr(name.lowercase())

    companion object {
        val valuesByStr by lazy { entries.associateBy { it.intoValue() } }
        fun fromValue(value: TValue): PaintRelativeTo = when (value) {
            is TStr -> valuesByStr[value]!!
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

sealed interface Margin<out M : IntoValue> : IntoValue {
    val sides: Sides<M>
    val twoSided: TBool

    companion object {
        inline fun <reified M : IntoValue> fromValue(value: TValue) = when (value) {
            is TDict<*> -> MarginImpl.fromValue(value)
            is M -> value as MarginSplat<M>
            else -> throw AssertionError("Can't convert from $value")
        }

        fun fromValue(value: TValue, type: KType): Margin<*> = when (value) {
            is TDict<*> -> MarginImpl.fromValue(value, type)
            else -> {
                val arg = type.arguments.firstOrNull()?.type
                if (arg == null) value as MarginSplat<*>
                else value.into(arg) as MarginSplat<*>
            }
        }
    }
}

sealed interface MarginSplat<out Self : MarginSplat<Self>> : SidesSplat<Self>, Margin<Self> {
    override val sides: Sides<Self> get() = this
    override val twoSided: TBool get() = false.t
}

@SerialName("dict")

data class MarginImpl<out M : IntoValue>(override val sides: Sides<M>, override val twoSided: TBool) : IntoDict<M>,
    Margin<M>, Option<MarginImpl<M>>, Smart<MarginImpl<M>>, ArrayOrSingle<MarginImpl<M>> {
    override fun intoValue(): TDict<M> = if (twoSided.value) {
        TDict(
            mapOfNotNullValues(
                "top" to sides.top,
                "bottom" to sides.bottom,
                "inside" to sides.left,
                "outside" to sides.right,
            )
        )
    } else {
        TDict(
            mapOfNotNullValues(
                "top" to sides.top,
                "bottom" to sides.bottom,
                "left" to sides.left,
                "right" to sides.right,
            )
        )
    }

    companion object {
        inline fun <reified M : IntoValue> fromValue(value: TValue): MarginImpl<M> {
            val dict = value as TDict<*>
            val top = ((dict["top"] ?: dict["y"] ?: dict["rest"]) as TValue?)?.into<M>()
            val bottom = ((dict["bottom"] ?: dict["y"] ?: dict["rest"]) as TValue?)?.into<M>()
            val inside = dict["inside"]
            val outside = dict["outside"]
            return if (inside != null || outside != null) {
                val left = ((inside ?: dict["x"] ?: dict["rest"]) as TValue?)?.into<M>()
                val right = ((outside ?: dict["x"] ?: dict["rest"]) as TValue?)?.into<M>()
                MarginImpl(SidesImpl(top, right, bottom, left), true.t)
            } else {
                val left = ((dict["left"] ?: dict["x"] ?: dict["rest"]) as TValue?)?.into<M>()
                val right = ((dict["right"] ?: dict["x"] ?: dict["rest"]) as TValue?)?.into<M>()
                MarginImpl(SidesImpl(top, right, bottom, left), false.t)
            }
        }

        fun fromValue(value: TValue, type: KType): MarginImpl<*> {
            val arg = type.arguments.firstOrNull()?.type
            val dict = value as TDict<*>
            val top = ((dict["top"] ?: dict["y"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
            val bottom = ((dict["bottom"] ?: dict["y"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
            val inside = dict["inside"]
            val outside = dict["outside"]
            return if (inside != null || outside != null) {
                val left = ((inside ?: dict["x"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
                val right = ((outside ?: dict["x"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
                MarginImpl(SidesImpl(top, right, bottom, left), true.t)
            } else {
                val left = ((dict["left"] ?: dict["x"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
                val right = ((dict["right"] ?: dict["x"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
                MarginImpl(SidesImpl(top, right, bottom, left), false.t)
            }
        }
    }
}

fun <M : IntoValue> Margin(top: M, bottom: M, left: M, right: M): Margin<M> =
    MarginImpl(Sides(top, bottom, left, right), false.t)

sealed interface Corners<out T : IntoValue> : IntoValue {
    val topLeft: T?
    val topRight: T?
    val bottomRight: T?
    val bottomLeft: T?


    companion object {
        inline fun <reified T : IntoValue> fromValue(value: TValue): Corners<T> = when (value) {
            is TDict<*> -> CornersImpl.fromValue(value)
            is T -> value as CornersSplat<T>
            else -> throw AssertionError("Can't convert from $value")
        }

        fun fromValue(value: TValue, type: KType): Corners<*> = when (value) {
            is TDict<*> -> CornersImpl.fromValue(value, type)
            else -> {
                val arg = type.arguments.firstOrNull()?.type
                if (arg == null) value as CornersSplat<*>
                else value.into(arg) as CornersSplat<*>
            }
        }
    }
}

sealed interface CornersSplat<out Self : CornersSplat<Self>> : Corners<Self> {
    override val topLeft: Self get() = this as Self
    override val topRight: Self get() = this as Self
    override val bottomRight: Self get() = this as Self
    override val bottomLeft: Self get() = this as Self
}

@SerialName("dict")

data class CornersImpl<out S : IntoValue>(
    @all:SerialName("top-left") override val topLeft: S?,
    @all:SerialName("top-right") override val topRight: S?,
    @all:SerialName("bottom-right") override val bottomRight: S?,
    @all:SerialName("bottom-left") override val bottomLeft: S?,
) : IntoValue,
    IntoDict<S>, Corners<S>, ArrayOrSingle<CornersImpl<S>>, Option<CornersImpl<S>>, Smart<CornersImpl<S>> {
    override fun intoValue(): TDict<S> = TDict(
        mapOfNotNullValues(
            "top-left" to topLeft,
            "top-right" to topRight,
            "bottom-right" to bottomRight,
            "bottom-left" to bottomLeft,
        )
    )

    companion object {
        fun <T : IntoValue> splat(x: T) = Sides(x, x, x, x)

        inline fun <reified T : IntoValue> fromValue(value: TValue): CornersImpl<T> {
            val dict = value as TDict<*>
            val topLeft = ((dict["top-left"] ?: dict["top"] ?: dict["left"] ?: dict["rest"]) as TValue?)?.into<T>()
            val topRight = ((dict["top-right"] ?: dict["top"] ?: dict["right"] ?: dict["rest"]) as TValue?)?.into<T>()
            val bottomRight =
                ((dict["bottom-right"] ?: dict["bottom"] ?: dict["right"] ?: dict["rest"]) as TValue?)?.into<T>()
            val bottomLeft =
                ((dict["bottom-left"] ?: dict["bottom"] ?: dict["left"] ?: dict["rest"]) as TValue?)?.into<T>()
            return CornersImpl(topLeft, topRight, bottomRight, bottomLeft)
        }

        fun fromValue(value: TValue, type: KType): CornersImpl<*> {
            val arg = type.arguments.firstOrNull()?.type
            val dict = value as TDict<*>
            val topLeft = ((dict["top-left"] ?: dict["top"] ?: dict["left"] ?: dict["rest"]) as TValue?)
                ?.into(arg ?: typeOf<IntoValue>())
            val topRight = ((dict["top-right"] ?: dict["top"] ?: dict["right"] ?: dict["rest"]) as TValue?)
                ?.into(arg ?: typeOf<IntoValue>())
            val bottomRight = ((dict["bottom-right"] ?: dict["bottom"] ?: dict["right"] ?: dict["rest"]) as TValue?)
                    ?.into(arg ?: typeOf<IntoValue>())
            val bottomLeft = ((dict["bottom-left"] ?: dict["bottom"] ?: dict["left"] ?: dict["rest"]) as TValue?)
                ?.into(arg ?: typeOf<IntoValue>())
            return CornersImpl(topLeft, topRight, bottomRight, bottomLeft)
        }
    }
}

sealed interface Sides<out S : IntoValue> : IntoValue {
    val top: S?
    val right: S?
    val bottom: S?
    val left: S?


    companion object {
        inline fun <reified S : IntoValue> fromValue(value: TValue): Sides<S> = when (value) {
            is TDict<*> -> SidesImpl.fromValue(value)
            is S -> value as SidesSplat<S>
            else -> throw AssertionError("Can't convert from $value")
        }

        fun fromValue(value: TValue, type: KType): Sides<*> = when (value) {
            is TDict<*> -> SidesImpl.fromValue(value, type)
            else -> {
                val arg = type.arguments.firstOrNull()?.type
                if (arg == null) value as SidesSplat<*>
                else value.into(arg) as SidesSplat<*>
            }
        }
    }
}

sealed interface SidesSplat<out Self : SidesSplat<Self>> : Sides<Self> {
    override val top: Self get() = this as Self
    override val right: Self get() = this as Self
    override val bottom: Self get() = this as Self
    override val left: Self get() = this as Self
}

@SerialName("dict")
data class SidesImpl<out S : IntoValue>(
    override val top: S?,
    override val right: S?,
    override val bottom: S?,
    override val left: S?,
) : IntoValue,
    IntoDict<S>, Sides<S>, ArrayOrSingle<SidesImpl<S>>, Option<SidesImpl<S>>, Smart<SidesImpl<S>> {
    override fun intoValue(): TDict<S> = TDict(
        mapOfNotNullValues(
            "top" to top,
            "right" to right,
            "bottom" to bottom,
            "left" to left,
        )
    )

    companion object {
        fun <T : IntoValue> splat(x: T) = Sides(x, x, x, x)

        inline fun <reified T : IntoValue> fromValue(value: TValue): SidesImpl<T> {
            val dict = value as TDict<*>
            val top = ((dict["top"] ?: dict["y"] ?: dict["rest"]) as TValue?)?.into<T>()
            val right = ((dict["right"] ?: dict["x"] ?: dict["rest"]) as TValue?)?.into<T>()
            val bottom = ((dict["bottom"] ?: dict["y"] ?: dict["rest"]) as TValue?)?.into<T>()
            val left = ((dict["left"] ?: dict["x"] ?: dict["rest"]) as TValue?)?.into<T>()
            return SidesImpl(top, right, bottom, left)
        }

        fun fromValue(value: TValue, type: KType): SidesImpl<*> {
            val arg = type.arguments.firstOrNull()?.type
            val dict = value as TDict<*>
            val top = ((dict["top"] ?: dict["y"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
            val right = ((dict["right"] ?: dict["x"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
            val bottom = ((dict["bottom"] ?: dict["y"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
            val left = ((dict["left"] ?: dict["x"] ?: dict["rest"]) as TValue?)?.into(arg ?: typeOf<IntoValue>())
            return SidesImpl(top, right, bottom, left)
        }
    }
}

fun <S : IntoValue> Sides(top: S, right: S, bottom: S, left: S) = SidesImpl(top, right, bottom, left)

sealed interface TopEdge : IntoValue {
    companion object {
        fun fromValue(value: TValue) = when (value) {
            is TLength -> value
            is TStr -> TopEdgePreset.fromValue(value)
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

sealed interface BottomEdge : IntoValue {
    companion object {
        fun fromValue(value: TValue) = when (value) {
            is TLength -> value
            is TStr -> BottomEdgePreset.fromValue(value)
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

@SerialName("str")

enum class TopEdgePreset(val value: String) : IntoStr, TopEdge {
    ASCENDER("ascender"),
    CAP_HEIGHT("cap-height"),
    X_HEIGHT("x-height"),
    BASELINE("baseline"),
    BOUNDS("bounds");

    override fun intoValue(): TStr = TStr(value)

    companion object {
        fun fromValue(value: TValue): TopEdgePreset {
            val str = (value as TStr).value
            return entries.first { it.value == str }
        }
    }
}

@SerialName("str")

enum class BottomEdgePreset(val value: String) : IntoStr, BottomEdge {
    DESCENDER("descender"),
    BASELINE("baseline"),
    BOUNDS("bounds");

    override fun intoValue(): TStr = TStr(value)

    companion object {
        fun fromValue(value: TValue): BottomEdgePreset {
            val str = (value as TStr).value
            return entries.first { it.value == str }
        }
    }
}

@SerialName("str")

enum class TTextWeightPreset(val value: String) : IntoStr, TTextWeight {
    THIN("thin"),
    EXTRALIGHT("extralight"),
    LIGHT("light"),
    REGULAR("regular"),
    MEDIUM("medium"),
    SEMIBOLD("semibold"),
    BOLD("bold"),
    EXTRABOLD("extrabold"),
    BLACK("black");

    override fun intoValue(): TStr = TStr(value)

    companion object {
        fun fromValue(value: TValue): TTextWeightPreset {
            val str = (value as TStr).value
            return entries.first { it.value == str }
        }
    }
}

sealed interface IntoArr<E : IntoValue> : IntoValue, IntoCollection<E> {
    override fun intoValue(): TArray<E>
}

fun IntoValue.type() = intoValue().type()

inline fun <reified T : IntoValue> TValue.into(): T {
    return this.into(typeOf<T>()) as T
}

fun TValue.into(t: KType): IntoValue {
    val typeArgs = t.arguments
    when (t.classifier) {
        IntoValue::class -> return this as IntoValue
        IntoStr::class -> return this as IntoStr
        TLineCloseMode::class -> return TLineCloseMode.fromValue(this)
        PaintRelativeTo::class -> return PaintRelativeTo.fromValue(this)
        TopEdgePreset::class -> return TopEdgePreset.fromValue(this)
        BottomEdgePreset::class -> return BottomEdgePreset.fromValue(this)
        TTextWeightPreset::class -> return TTextWeightPreset.fromValue(this)
        TFillRule::class -> return TFillRule.fromValue(this)
        LineCap::class -> return LineCap.fromValue(this)
        LineJoin::class -> return LineJoin.fromValue(this)
        LineWidth::class -> return if (this == "dot".t) LineWidth as IntoValue else throw AssertionError("Can't convert from $this")
        TImageFormatEncoding::class -> return TImageFormatEncoding.fromValue(this)
        TImageFormatPreset::class -> return TImageFormatPreset.fromValue(this)
        TPdfArtifactKind::class -> return TPdfArtifactKind.fromValue(this)
        TFontCoveragePreset::class -> return TFontCoveragePreset.fromValue(this)
        TFigureScope::class -> return TFigureScope.fromValue(this)
        TCiteForm::class -> return TCiteForm.fromValue(this)
        TBibliographyStyle::class -> return TBibliographyStyle.fromValue(this)
        IntoCollection::class -> return TCollection.fromValue(this, t)
        IntoDict::class -> return TDict.fromValue(this, t)
        MarginImpl::class -> return MarginImpl.fromValue(this, t)
        CornersImpl::class -> return CornersImpl.fromValue(this, t)
        SidesImpl::class -> return SidesImpl.fromValue(this, t)
        TDict::class -> return TDict.fromValue(this, t)
        DashPattern::class -> return DashPattern.fromValue(this)
        SymVariant::class -> return SymVariant.fromValue(this)
        TSmartquoteSymbols::class -> return TSmartquoteSymbols.fromValue(this)
        TImageFormatDict::class -> return TImageFormatDict.fromValue(this)
        LocationOnPage::class -> return LocationOnPage.fromValue(this)
        TFontDescriptorImpl::class -> return TFontDescriptorImpl.fromValue(this)
        TMatAugmentDict::class -> return TMatAugmentDict.fromValue(this)
        IntoArr::class -> return TArray.fromValue(this, typeArgs[0].type!!)
        TOpenCloseQuotes::class -> return TOpenCloseQuotes.fromValue(this)
        TCollection::class -> return TCollection.fromValue(this, t)
        TArray::class -> return TArray.fromValue(this, t)
        Point::class -> return Point.fromValue(this, t)
        Margin::class -> return Margin.fromValue(this, t)
        MarginSplat::class -> return this.into(typeArgs[0].type!!)
        Corners::class -> return Corners.fromValue(this, t)
        CornersSplat::class -> return this.into(typeArgs[0].type!!)
        Sides::class -> return Sides.fromValue(this, t)
        SidesSplat::class -> return this.into(typeArgs[0].type!!)
        TopEdge::class -> return TopEdge.fromValue(this)
        BottomEdge::class -> return BottomEdge.fromValue(this)
        DataSourceOrPreset::class -> return DataSourceOrPreset.fromValue(this, typeArgs[0].type!!)
        DataSource::class -> return DataSource.fromValue(this)
        TPath::class -> return TPath((this as TStr).value)
        Smart::class -> return Smart.fromValue(this, typeArgs[0].type!!)
        ArrayOrSingle::class -> return ArrayOrSingle.fromValue(this, typeArgs[0].type!!)
        Progression::class -> return Progression.fromValue(this, typeArgs[0].type!!)
        Option::class -> return Option.fromValue(this, typeArgs[0].type!!)
        Computable::class -> return Computable.fromValue(this, typeArgs[0].type!!)
        TTextWeight::class -> return TTextWeight.fromValue(this)
        TGradientStop::class -> return TGradientStop.fromValue(this)
        Dash::class -> return Dash.fromValue(this)
        DashLength::class -> return DashLength.fromValue(this)
        TSmartquoteSpec::class -> return TSmartquoteSpec.fromValue(this)
        TLinkDestination::class -> return TLinkDestination.fromValue(this)
        TFontDescriptor::class -> return TFontDescriptor.fromValue(this)
        TFontCoverage::class -> return TFontCoverage.fromValue(this)
        TMatAugment::class -> return TMatAugment.fromValue(this)
        FirstLineIndent::class -> return FirstLineIndent.fromValue(this)
        FirstLineIndentImpl::class -> return FirstLineIndentImpl.fromValue(this)
        TCurveControl::class -> return TCurveControl.fromValue(this, t)

        else -> return if (TValue::class.java.isAssignableFrom((t.classifier as KClass<*>).java)) this
        else throw IllegalArgumentException(
            "Unsupported IntoValue conversion for $t"
        )
    }
}
