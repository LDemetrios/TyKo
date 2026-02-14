package org.ldemetrios.tyko.model

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * An object that can be converted into Typst value.
 * This could be a value with additional contract or unusual behaviour.
 */
sealed interface IntoValue : Argument {
    /**
     * Convert into generic Typst value
     */
    fun intoValue(): TValue

    companion object {
        fun <T : IntoValue> fromValue(value: TValue, type: KType): T {
            return value.into(type) as T
        }
    }
}

/**
 * Specialization of `IntoValue` for string-backed values.
 */
sealed interface IntoStr : IntoValue {
    override fun intoValue(): TStr

    companion object {
        fun fromValue(value: TValue, type: KType): TStr {
            return value as TStr
        }
    }
}

/**
 * Specialization of `IntoValue` for collection-backed values (either array or dictionary).
 */
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

/**
 * Specialization of `IntoValue` for dict-backed values.
 */
sealed interface IntoDict<out T : IntoValue> : IntoCollection<T> {
    override fun intoValue(): TDict<T>
}

/**
 * Specialization of `IntoValue` for array-backed values.
 */
sealed interface IntoArr<E : IntoValue> : IntoValue, IntoCollection<E> {
    override fun intoValue(): TArray<E>
}

/**
 * Returns the Typst type of this value.
 */
fun IntoValue.type() = intoValue().type()

/**
 * Converts `TValue` into a possibly specialized value using reified type information.
 */
inline fun <reified T : IntoValue> TValue.into(): T {
    return this.into(typeOf<T>()) as T
}

/**
 * Converts raw `TValue` into a possibly specialized value using the type token.
 */
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
        TPoint::class -> return TPoint.fromValue(this, t)
        Margin::class -> return Margin.fromValue(this, t)
        MarginSplat::class -> return this.into(typeArgs[0].type!!)
        Corners::class -> return Corners.fromValue(this, t)
        CornersSplat::class -> return this.into(typeArgs[0].type!!)
        Sides::class -> return Sides.fromValue(this, t)
        SidesSplat::class -> return this.into(typeArgs[0].type!!)
        TopEdge::class -> return TopEdge.fromValue(this)
        BottomEdge::class -> return BottomEdge.fromValue(this)
        DataSourceOrPreset::class -> return DataSourceOrPreset.fromValue(this, typeArgs[0].type!!)
        Smart::class -> return Smart.fromValue(this, typeArgs[0].type!!)
        ArrayOrSingle::class -> return ArrayOrSingle.fromValue(this, typeArgs[0].type!!)
        Progression::class -> return Progression.fromValue(this, typeArgs[0].type!!)
        Option::class -> return Option.fromValue(this, typeArgs[0].type!!)
        Computable::class -> return Computable.fromValue(this, typeArgs[0].type!!)
        TTextWeight::class -> return TTextWeight.fromValue(this)
        TGradientStop::class -> return TGradientStop.fromValue(this)
        Dash::class -> return Dash.fromValue(this)
        DashLength::class -> return DashLength.fromValue(this)
        TSmartquoteQuotes::class -> return TSmartquoteQuotes.fromValue(this)
        TLinkDestination::class -> return TLinkDestination.fromValue(this)
        TFontDescriptor::class -> return TFontDescriptor.fromValue(this)
        TFontCoverage::class -> return TFontCoverage.fromValue(this)
        TMatAugment::class -> return TMatAugment.fromValue(this)
        FirstLineIndent::class -> return FirstLineIndent.fromValue(this)
        FirstLineIndentImpl::class -> return FirstLineIndentImpl.fromValue(this)
        TCurveControl::class -> return TCurveControl.fromValue(this, t)
        TPdfAttachRelationship::class -> return TPdfAttachRelationship.fromValue(this)

        else -> return if (TValue::class.java.isAssignableFrom((t.classifier as KClass<*>).java)) this
        else throw IllegalArgumentException(
            "Unsupported IntoValue conversion for $t"
        )
    }
}
