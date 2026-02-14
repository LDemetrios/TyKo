package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.IntoDict
import org.ldemetrios.tyko.model.IntoStr
import org.ldemetrios.tyko.model.IntoValue
import org.ldemetrios.tyko.model.SerialName
import org.ldemetrios.tyko.model.TAuto
import org.ldemetrios.tyko.model.TDict
import org.ldemetrios.tyko.model.TImage
import org.ldemetrios.tyko.model.TInt
import org.ldemetrios.tyko.model.TStr
import org.ldemetrios.tyko.model.TValue
import org.ldemetrios.tyko.model.t


sealed interface TImageFormat {
    companion object {
        fun fromValue(value: TValue) = when (value) {
            is TDict<*> -> TImageFormatDict.fromValue(value)
            TAuto -> TAuto
            is TStr -> TImageFormatPreset.fromValue(value)
            else -> throw AssertionError("Can't convert from $value")
        }
    }
}

/**
 * Lists raw image encoding formats (used at [TImageFormatDict.encoding]).
 */
@SerialName("str")
enum class TImageFormatEncoding(val value: String) : IntoStr, Self<TImageFormatEncoding> {
    RGB8("rgb8"),
    RGBA8("rgba8"),
    LUMA8("luma8"),
    LUMAA8("lumaa8");

    override fun intoValue(): TStr = TStr(value)

    companion object {
        private val valuesByStr by lazy { entries.associateBy { it.value } }
        fun fromValue(value: TValue) =
            if (value is TStr) valuesByStr[value.value]!! else throw AssertionError("Can't convert from $value")
    }
}

@SerialName("dict")
data class TImageFormatDict(
    val encoding: TImageFormatEncoding,
    val width: TInt,
    val height: TInt,
) : IntoDict<IntoValue>, TImageFormat, Self<TImageFormatDict> {
    override fun intoValue(): TDict<IntoValue> = TDict(
        mapOf(
            "encoding" to encoding.intoValue(),
            "width" to width,
            "height" to height,
        )
    )

    companion object {
        fun fromValue(value: TValue) = if (value is TDict<*>) TImageFormatDict(
            encoding = TImageFormatEncoding.fromValue(value["encoding"]!!.intoValue()),
            width = value["width"]!!.intoValue() as TInt,
            height = value["height"]!!.intoValue() as TInt,
        ) else throw AssertionError("Can't convert from $value")
    }
}

/**
 * Lists built-in image format presets (used at [TImage.format]).
 */
@SerialName("str")
enum class TImageFormatPreset : IntoStr, TImageFormat, Self<TImageFormatPreset> {
    PNG, JPG, GIF, SVG, PDF, WEBP;

    override fun intoValue(): TStr = name.lowercase().t

    companion object {
        private val valuesByStr by lazy { entries.associateBy { it.intoValue() } }
        fun fromValue(value: TValue) =
            if (value is TStr) valuesByStr[value]!! else throw AssertionError("Can't convert from $value")
    }
}
