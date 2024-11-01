package org.ldemetrios.typst4k.model

import org.ldemetrios.typst4k.rt.*
import org.ldemetrios.js.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import org.ldemetrios.utilities.cast
import org.ldemetrios.utilities.castUnchecked

private infix fun String.w(value: (String) -> TStr?) = this to value

internal val enumTypes = mutableMapOf<String, (String) -> TStr?>(
    "bibliography-style".w(TBibliographyStyle::of),
    "cite-form".w(TCiteForm::of),
    "cite-style".w(TCiteStyle::of),
    "figure-scope".w(TFigureScope::of),
    "par-linebreaks".w(TParLinebreaks::of),
    "par.line-numbering-scope".w(TParLineNumberingScope::of),
    "highlight-top-edge".w(THighlightTopEdge::of),
    "highlight-bottom-edge".w(THighlightBottomEdge::of),
    "text-style".w(TTextStyle::of),
    "text-weight".w(TTextWeight::of),
    "text-top-edge".w(TTextTopEdge::of),
    "text-bottom-edge".w(TTextBottomEdge::of),
    "text-number-type".w(TTextNumberType::of),
    "text-number-width".w(TTextNumberWidth::of),
    "math.class-class".w(TMathClassClass::of),
    "page-paper".w(TPagePaper::of),
    "pagebreak-to".w(TPagebreakTo::of),
    "place-scope".w(TPlaceScope::of),
    "image-format".w(TImageFormat::of),
    "image-fit".w(TImageFit::of),
    "path-fill-rule".w(TPathFillRule::of),
    "polygon-fill-rule".w(TPolygonFillRule::of),
)

internal val deserializers = mutableMapOf<String, (JSStuff, InternalType,  String) -> TValue>(
	"element" to ::deserializeElement,
    "bibliography" to ::deserializeBibliography,
    "list" to ::deserializeList,
    "list.item" to ::deserializeListItem,
    "cite" to ::deserializeCite,
    "document" to ::deserializeDocument,
    "emph" to ::deserializeEmph,
    "figure" to ::deserializeFigure,
    "figure.caption" to ::deserializeFigureCaption,
    "footnote" to ::deserializeFootnote,
    "footnote.entry" to ::deserializeFootnoteEntry,
    "heading" to ::deserializeHeading,
    "link" to ::deserializeLink,
    "position" to ::deserializePosition,
    "point" to ::deserializePoint,
    "enum" to ::deserializeEnum,
    "enum.item" to ::deserializeEnumItem,
    "outline" to ::deserializeOutline,
    "outline.entry" to ::deserializeOutlineEntry,
    "par" to ::deserializePar,
    "par.line" to ::deserializeParLine,
    "parbreak" to ::deserializeParbreak,
    "quote" to ::deserializeQuote,
    "ref" to ::deserializeRef,
    "strong" to ::deserializeStrong,
    "table" to ::deserializeTable,
    "sides" to ::deserializeSides,
    "table.cell" to ::deserializeTableCell,
    "table.hline" to ::deserializeTableHline,
    "table.vline" to ::deserializeTableVline,
    "table.header" to ::deserializeTableHeader,
    "table.footer" to ::deserializeTableFooter,
    "terms" to ::deserializeTerms,
    "terms.item" to ::deserializeTermsItem,
    "highlight" to ::deserializeHighlight,
    "linebreak" to ::deserializeLinebreak,
    "overline" to ::deserializeOverline,
    "raw" to ::deserializeRaw,
    "raw.line" to ::deserializeRawLine,
    "smallcaps" to ::deserializeSmallcaps,
    "smartquote" to ::deserializeSmartquote,
    "strike" to ::deserializeStrike,
    "sub" to ::deserializeSub,
    "super" to ::deserializeSuper,
    "text" to ::deserializeText,
    "underline" to ::deserializeUnderline,
    "math.accent" to ::deserializeMathAccent,
    "math.binom" to ::deserializeMathBinom,
    "math.cancel" to ::deserializeMathCancel,
    "math.cases" to ::deserializeMathCases,
    "math.class" to ::deserializeMathClass,
    "math.equation" to ::deserializeMathEquation,
    "math.frac" to ::deserializeMathFrac,
    "math.mat" to ::deserializeMathMat,
    "math.primes" to ::deserializeMathPrimes,
    "math.stretch" to ::deserializeMathStretch,
    "math.op" to ::deserializeMathOp,
    "math.vec" to ::deserializeMathVec,
    "align" to ::deserializeAlign,
    "block" to ::deserializeBlock,
    "box" to ::deserializeBox,
    "colbreak" to ::deserializeColbreak,
    "columns" to ::deserializeColumns,
    "grid" to ::deserializeGrid,
    "grid.cell" to ::deserializeGridCell,
    "grid.hline" to ::deserializeGridHline,
    "grid.vline" to ::deserializeGridVline,
    "grid.header" to ::deserializeGridHeader,
    "grid.footer" to ::deserializeGridFooter,
    "hide" to ::deserializeHide,
    "move" to ::deserializeMove,
    "pad" to ::deserializePad,
    "page" to ::deserializePage,
    "pagebreak" to ::deserializePagebreak,
    "place" to ::deserializePlace,
    "place.flush" to ::deserializePlaceFlush,
    "repeat" to ::deserializeRepeat,
    "rotate" to ::deserializeRotate,
    "scale" to ::deserializeScale,
    "skew" to ::deserializeSkew,
    "h" to ::deserializeH,
    "v" to ::deserializeV,
    "stack" to ::deserializeStack,
    "circle" to ::deserializeCircle,
    "ellipse" to ::deserializeEllipse,
    "image" to ::deserializeImage,
    "line" to ::deserializeLine,
    "path" to ::deserializePath,
    "polygon" to ::deserializePolygon,
    "rect" to ::deserializeRect,
    "square" to ::deserializeSquare,
    "metadata" to ::deserializeMetadata,
    "math.attach" to ::deserializeMathAttach,
    "math.scripts" to ::deserializeMathScripts,
    "math.limits" to ::deserializeMathLimits,
    "math.lr" to ::deserializeMathLr,
    "math.mid" to ::deserializeMathMid,
    "math.root" to ::deserializeMathRoot,
    "math.underline" to ::deserializeMathUnderline,
    "math.overline" to ::deserializeMathOverline,
    "math.underbrace" to ::deserializeMathUnderbrace,
    "math.overbrace" to ::deserializeMathOverbrace,
    "math.underbracket" to ::deserializeMathUnderbracket,
    "math.overbracket" to ::deserializeMathOverbracket,
    "math.underparen" to ::deserializeMathUnderparen,
    "math.overparen" to ::deserializeMathOverparen,
    "math.undershell" to ::deserializeMathUndershell,
    "math.overshell" to ::deserializeMathOvershell,
    "none" to ::deserializeNone,
    "auto" to ::deserializeAuto,
    "arguments" to ::deserializeArguments,
    "angle" to ::deserializeAngle,
    "length" to ::deserializeLength,
    "ratio" to ::deserializeRatio,
    "relative-impl" to ::deserializeRelativeImpl,
    "fraction" to ::deserializeFraction,
    "luma" to ::deserializeLuma,
    "oklab" to ::deserializeOklab,
    "oklch" to ::deserializeOklch,
    "color.linear-rgb" to ::deserializeLinearRgb,
    "rgb" to ::deserializeRgb,
    "cmyk" to ::deserializeCmyk,
    "color.hsl" to ::deserializeHsl,
    "color.hsv" to ::deserializeHsv,
    "gradient.linear" to ::deserializeLinearGradient,
    "gradient.radial" to ::deserializeRadialGradient,
    "gradient.conic" to ::deserializeConicGradient,
    "pattern" to ::deserializePattern,
    "version" to ::deserializeVersion,
    "label" to ::deserializeLabel,
    "datetime" to ::deserializeDatetime,
    "duration" to ::deserializeDuration,
    "plugin" to ::deserializePlugin,
    "alignment" to ::deserializeAlignment,
    "direction" to ::deserializeDirection,
    "regex" to ::deserializeRegex,
    "math.align-point" to ::deserializeMathAlignPoint,
    "stroke" to ::deserializeStroke,
    "element-selector" to ::deserializeElementSelector,
    "label-selector" to ::deserializeLabelSelector,
    "regex-selector" to ::deserializeRegexSelector,
    "before-selector" to ::deserializeBeforeSelector,
    "after-selector" to ::deserializeAfterSelector,
    "and-selector" to ::deserializeAndSelector,
    "or-selector" to ::deserializeOrSelector,
    "counter" to ::deserializeCounter,
    "page-counter-key" to ::deserializePageCounterKey,
    "selector-counter-key" to ::deserializeSelectorCounterKey,
    "str-counter-key" to ::deserializeStrCounterKey,
    "state" to ::deserializeState,
    "decimal" to ::deserializeDecimal,
    "styled" to ::deserializeStyled,
    "sequence" to ::deserializeSequence,
    "space" to ::deserializeSpace,
    "closure" to ::deserializeClosure,
    "with" to ::deserializeWith,
    "native-func" to ::deserializeNativeFunc,
    "show-rule" to ::deserializeShowRule,
    "context" to ::deserializeContext,
    "style-deprecated" to ::deserializeStyleDeprecated,
    "layout" to ::deserializeLayout,
    "locate" to ::deserializeLocate,
    "counter.update" to ::deserializeCounterUpdate,
    "counter.step" to ::deserializeCounterStep,
    "counter.step" to ::deserializeCounterStep,
    "state.update" to ::deserializeStateUpdate,
    "set-bibliography" to ::deserializeSetBibliography,
    "set-list" to ::deserializeSetList,
    "set-cite" to ::deserializeSetCite,
    "set-document" to ::deserializeSetDocument,
    "set-figure" to ::deserializeSetFigure,
    "set-figure.caption" to ::deserializeSetFigureCaption,
    "set-footnote" to ::deserializeSetFootnote,
    "set-footnote.entry" to ::deserializeSetFootnoteEntry,
    "set-heading" to ::deserializeSetHeading,
    "set-enum" to ::deserializeSetEnum,
    "set-enum.item" to ::deserializeSetEnumItem,
    "set-outline" to ::deserializeSetOutline,
    "set-par" to ::deserializeSetPar,
    "set-par.line" to ::deserializeSetParLine,
    "set-quote" to ::deserializeSetQuote,
    "set-ref" to ::deserializeSetRef,
    "set-strong" to ::deserializeSetStrong,
    "set-table" to ::deserializeSetTable,
    "set-table.cell" to ::deserializeSetTableCell,
    "set-table.hline" to ::deserializeSetTableHline,
    "set-table.vline" to ::deserializeSetTableVline,
    "set-table.header" to ::deserializeSetTableHeader,
    "set-table.footer" to ::deserializeSetTableFooter,
    "set-terms" to ::deserializeSetTerms,
    "set-highlight" to ::deserializeSetHighlight,
    "set-linebreak" to ::deserializeSetLinebreak,
    "set-overline" to ::deserializeSetOverline,
    "set-raw" to ::deserializeSetRaw,
    "set-smartquote" to ::deserializeSetSmartquote,
    "set-strike" to ::deserializeSetStrike,
    "set-sub" to ::deserializeSetSub,
    "set-super" to ::deserializeSetSuper,
    "set-text" to ::deserializeSetText,
    "set-underline" to ::deserializeSetUnderline,
    "set-math.accent" to ::deserializeSetMathAccent,
    "set-math.cancel" to ::deserializeSetMathCancel,
    "set-math.cases" to ::deserializeSetMathCases,
    "set-math.equation" to ::deserializeSetMathEquation,
    "set-math.mat" to ::deserializeSetMathMat,
    "set-math.stretch" to ::deserializeSetMathStretch,
    "set-math.op" to ::deserializeSetMathOp,
    "set-math.vec" to ::deserializeSetMathVec,
    "set-align" to ::deserializeSetAlign,
    "set-block" to ::deserializeSetBlock,
    "set-box" to ::deserializeSetBox,
    "set-colbreak" to ::deserializeSetColbreak,
    "set-columns" to ::deserializeSetColumns,
    "set-grid" to ::deserializeSetGrid,
    "set-grid.cell" to ::deserializeSetGridCell,
    "set-grid.hline" to ::deserializeSetGridHline,
    "set-grid.vline" to ::deserializeSetGridVline,
    "set-grid.header" to ::deserializeSetGridHeader,
    "set-grid.footer" to ::deserializeSetGridFooter,
    "set-move" to ::deserializeSetMove,
    "set-pad" to ::deserializeSetPad,
    "set-page" to ::deserializeSetPage,
    "set-pagebreak" to ::deserializeSetPagebreak,
    "set-place" to ::deserializeSetPlace,
    "set-repeat" to ::deserializeSetRepeat,
    "set-rotate" to ::deserializeSetRotate,
    "set-scale" to ::deserializeSetScale,
    "set-skew" to ::deserializeSetSkew,
    "set-h" to ::deserializeSetH,
    "set-v" to ::deserializeSetV,
    "set-stack" to ::deserializeSetStack,
    "set-circle" to ::deserializeSetCircle,
    "set-ellipse" to ::deserializeSetEllipse,
    "set-image" to ::deserializeSetImage,
    "set-line" to ::deserializeSetLine,
    "set-path" to ::deserializeSetPath,
    "set-polygon" to ::deserializeSetPolygon,
    "set-rect" to ::deserializeSetRect,
    "set-square" to ::deserializeSetSquare,
    "set-math.attach" to ::deserializeSetMathAttach,
    "set-math.limits" to ::deserializeSetMathLimits,
    "set-math.lr" to ::deserializeSetMathLr,
    "set-math.root" to ::deserializeSetMathRoot,
    "set-math.underbrace" to ::deserializeSetMathUnderbrace,
    "set-math.overbrace" to ::deserializeSetMathOverbrace,
    "set-math.underbracket" to ::deserializeSetMathUnderbracket,
    "set-math.overbracket" to ::deserializeSetMathOverbracket,
    "set-math.underparen" to ::deserializeSetMathUnderparen,
    "set-math.overparen" to ::deserializeSetMathOverparen,
    "set-math.undershell" to ::deserializeSetMathUndershell,
    "set-math.overshell" to ::deserializeSetMathOvershell,
)

internal val syntheticTypes = mutableMapOf<String, (JSObject, InternalType, String) -> TValue>(
    "position" to ::deserializePosition,
    "point" to ::deserializePoint,
    "sides" to ::deserializeSides,
)

internal val genericTypes = mutableMapOf<String, Int>(
    "bibliography" to 0,
    "list" to 0,
    "list.item" to 0,
    "cite" to 0,
    "document" to 0,
    "emph" to 0,
    "figure" to 0,
    "figure.caption" to 0,
    "footnote" to 0,
    "footnote.entry" to 0,
    "heading" to 0,
    "link" to 0,
    "position" to 0,
    "point" to 0,
    "enum" to 0,
    "enum.item" to 0,
    "outline" to 0,
    "outline.entry" to 0,
    "par" to 0,
    "par.line" to 0,
    "parbreak" to 0,
    "quote" to 0,
    "ref" to 0,
    "strong" to 0,
    "table" to 0,
    "sides" to 1,
    "table.cell" to 0,
    "table.hline" to 0,
    "table.vline" to 0,
    "table.header" to 0,
    "table.footer" to 0,
    "terms" to 0,
    "terms.item" to 0,
    "highlight" to 0,
    "linebreak" to 0,
    "overline" to 0,
    "raw" to 0,
    "raw.line" to 0,
    "smallcaps" to 0,
    "smartquote" to 0,
    "strike" to 0,
    "sub" to 0,
    "super" to 0,
    "text" to 0,
    "underline" to 0,
    "math.accent" to 0,
    "math.binom" to 0,
    "math.cancel" to 0,
    "math.cases" to 0,
    "math.class" to 0,
    "math.equation" to 0,
    "math.frac" to 0,
    "math.mat" to 0,
    "math.primes" to 0,
    "math.stretch" to 0,
    "math.op" to 0,
    "math.vec" to 0,
    "align" to 0,
    "block" to 0,
    "box" to 0,
    "colbreak" to 0,
    "columns" to 0,
    "grid" to 0,
    "grid.cell" to 0,
    "grid.hline" to 0,
    "grid.vline" to 0,
    "grid.header" to 0,
    "grid.footer" to 0,
    "hide" to 0,
    "move" to 0,
    "pad" to 0,
    "page" to 0,
    "pagebreak" to 0,
    "place" to 0,
    "place.flush" to 0,
    "repeat" to 0,
    "rotate" to 0,
    "scale" to 0,
    "skew" to 0,
    "h" to 0,
    "v" to 0,
    "stack" to 0,
    "circle" to 0,
    "ellipse" to 0,
    "image" to 0,
    "line" to 0,
    "path" to 0,
    "polygon" to 0,
    "rect" to 0,
    "square" to 0,
    "metadata" to 1,
    "math.attach" to 0,
    "math.scripts" to 0,
    "math.limits" to 0,
    "math.lr" to 0,
    "math.mid" to 0,
    "math.root" to 0,
    "math.underline" to 0,
    "math.overline" to 0,
    "math.underbrace" to 0,
    "math.overbrace" to 0,
    "math.underbracket" to 0,
    "math.overbracket" to 0,
    "math.underparen" to 0,
    "math.overparen" to 0,
    "math.undershell" to 0,
    "math.overshell" to 0,
    "none" to 0,
    "auto" to 0,
    "bool" to 0,
    "int" to 0,
    "float" to 0,
    "str" to 0,
    "array" to 1,
    "dictionary" to 1,
    "arguments" to 1,
    "content" to 0,
    "angle" to 0,
    "relative" to 0,
    "length" to 0,
    "ratio" to 0,
    "relative-impl" to 0,
    "fraction" to 0,
    "color" to 0,
    "gradient" to 0,
    "luma" to 0,
    "oklab" to 0,
    "oklch" to 0,
    "color.linear-rgb" to 0,
    "rgb" to 0,
    "cmyk" to 0,
    "color.hsl" to 0,
    "color.hsv" to 0,
    "gradient.linear" to 0,
    "gradient.radial" to 0,
    "gradient.conic" to 0,
    "pattern" to 0,
    "version" to 0,
    "label" to 0,
    "datetime" to 0,
    "duration" to 0,
    "type" to 0,
    "module" to 0,
    "plugin" to 0,
    "alignment" to 0,
    "direction" to 0,
    "regex" to 0,
    "math.align-point" to 0,
    "stroke" to 0,
    "selector" to 0,
    "element-selector" to 0,
    "label-selector" to 0,
    "regex-selector" to 0,
    "before-selector" to 0,
    "after-selector" to 0,
    "and-selector" to 0,
    "or-selector" to 0,
    "counter" to 0,
    "counter-key" to 0,
    "page-counter-key" to 0,
    "selector-counter-key" to 0,
    "str-counter-key" to 0,
    "state" to 0,
    "decimal" to 0,
    "styled" to 0,
    "sequence" to 0,
    "space" to 0,
    "function" to 0,
    "closure" to 0,
    "with" to 0,
    "native-func" to 0,
    "style" to 0,
    "show-rule" to 0,
    "context" to 0,
    "style-deprecated" to 0,
    "layout" to 0,
    "locate" to 0,
    "counter.update" to 0,
    "counter.step" to 0,
    "counter.step" to 0,
    "state.update" to 0,
)

internal fun deserializeBibliography(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("bibliography")
        ?: type.optionOf("content")
	check(option != null) { "`bibliography`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TBibliography (
		deserialize(value["path"], TBibliography.pathType, "$path/path").castUnchecked(),
		maybeDeser(value["title"], TBibliography.titleType, "$path/title").castUnchecked(),
		maybeDeser(value["full"], TBibliography.fullType, "$path/full").castUnchecked(),
		maybeDeser(value["style"], TBibliography.styleType, "$path/style").castUnchecked(),
	)
}

internal fun deserializeSetBibliography(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("bibliography") ?: type.optionOf("style")
           ?: failWith("`set bibliography`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetBibliography(
		(if(id == "title") maybeDeser(value["value"], TBibliography.titleType, "$path/title") else null).castUnchecked(),
		(if(id == "full") maybeDeser(value["value"], TBibliography.fullType, "$path/full") else null).castUnchecked(),
		(if(id == "style") maybeDeser(value["value"], TBibliography.styleType, "$path/style") else null).castUnchecked(),
	)
}

internal fun deserializeList(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("list")
        ?: type.optionOf("content")
	check(option != null) { "`list`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TList (
		deserialize(value["children"], TList.childrenType, "$path/children").castUnchecked(),
		maybeDeser(value["tight"], TList.tightType, "$path/tight").castUnchecked(),
		maybeDeser(value["marker"], TList.markerType, "$path/marker").castUnchecked(),
		maybeDeser(value["indent"], TList.indentType, "$path/indent").castUnchecked(),
		maybeDeser(value["body-indent"], TList.bodyIndentType, "$path/body-indent").castUnchecked(),
		maybeDeser(value["spacing"], TList.spacingType, "$path/spacing").castUnchecked(),
	)
}

internal fun deserializeSetList(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("list") ?: type.optionOf("style")
           ?: failWith("`set list`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetList(
		(if(id == "tight") maybeDeser(value["value"], TList.tightType, "$path/tight") else null).castUnchecked(),
		(if(id == "marker") maybeDeser(value["value"], TList.markerType, "$path/marker") else null).castUnchecked(),
		(if(id == "indent") maybeDeser(value["value"], TList.indentType, "$path/indent") else null).castUnchecked(),
		(if(id == "body-indent") maybeDeser(value["value"], TList.bodyIndentType, "$path/body-indent") else null).castUnchecked(),
		(if(id == "spacing") maybeDeser(value["value"], TList.spacingType, "$path/spacing") else null).castUnchecked(),
	)
}

internal fun deserializeListItem(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("list.item")
        ?: type.optionOf("content")
	check(option != null) { "`list.item`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TListItem (
		deserialize(value["body"], TListItem.bodyType, "$path/body").castUnchecked(),
	)
}

internal fun deserializeCite(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("cite")
        ?: type.optionOf("content")
	check(option != null) { "`cite`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TCite (
		deserialize(value["key"], TCite.keyType, "$path/key").castUnchecked(),
		maybeDeser(value["supplement"], TCite.supplementType, "$path/supplement").castUnchecked(),
		maybeDeser(value["form"], TCite.formType, "$path/form").castUnchecked(),
		maybeDeser(value["style"], TCite.styleType, "$path/style").castUnchecked(),
	)
}

internal fun deserializeSetCite(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("cite") ?: type.optionOf("style")
           ?: failWith("`set cite`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetCite(
		(if(id == "supplement") maybeDeser(value["value"], TCite.supplementType, "$path/supplement") else null).castUnchecked(),
		(if(id == "form") maybeDeser(value["value"], TCite.formType, "$path/form") else null).castUnchecked(),
		(if(id == "style") maybeDeser(value["value"], TCite.styleType, "$path/style") else null).castUnchecked(),
	)
}

internal fun deserializeDocument(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("document")
        ?: type.optionOf("content")
	check(option != null) { "`document`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TDocument (
		maybeDeser(value["title"], TDocument.titleType, "$path/title").castUnchecked(),
		maybeDeser(value["author"], TDocument.authorType, "$path/author").castUnchecked(),
		maybeDeser(value["keywords"], TDocument.keywordsType, "$path/keywords").castUnchecked(),
		maybeDeser(value["date"], TDocument.dateType, "$path/date").castUnchecked(),
	)
}

internal fun deserializeSetDocument(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("document") ?: type.optionOf("style")
           ?: failWith("`set document`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetDocument(
		(if(id == "title") maybeDeser(value["value"], TDocument.titleType, "$path/title") else null).castUnchecked(),
		(if(id == "author") maybeDeser(value["value"], TDocument.authorType, "$path/author") else null).castUnchecked(),
		(if(id == "keywords") maybeDeser(value["value"], TDocument.keywordsType, "$path/keywords") else null).castUnchecked(),
		(if(id == "date") maybeDeser(value["value"], TDocument.dateType, "$path/date") else null).castUnchecked(),
	)
}

internal fun deserializeEmph(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("emph")
        ?: type.optionOf("content")
	check(option != null) { "`emph`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TEmph (
		deserialize(value["body"], TEmph.bodyType, "$path/body").castUnchecked(),
	)
}

internal fun deserializeFigure(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("figure")
        ?: type.optionOf("content")
	check(option != null) { "`figure`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TFigure (
		deserialize(value["body"], TFigure.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["placement"], TFigure.placementType, "$path/placement").castUnchecked(),
		maybeDeser(value["scope"], TFigure.scopeType, "$path/scope").castUnchecked(),
		maybeDeser(value["caption"], TFigure.captionType, "$path/caption").castUnchecked(),
		maybeDeser(value["kind"], TFigure.kindType, "$path/kind").castUnchecked(),
		maybeDeser(value["supplement"], TFigure.supplementType, "$path/supplement").castUnchecked(),
		maybeDeser(value["numbering"], TFigure.numberingType, "$path/numbering").castUnchecked(),
		maybeDeser(value["gap"], TFigure.gapType, "$path/gap").castUnchecked(),
		maybeDeser(value["outlined"], TFigure.outlinedType, "$path/outlined").castUnchecked(),
	)
}

internal fun deserializeSetFigure(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("figure") ?: type.optionOf("style")
           ?: failWith("`set figure`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetFigure(
		(if(id == "placement") maybeDeser(value["value"], TFigure.placementType, "$path/placement") else null).castUnchecked(),
		(if(id == "scope") maybeDeser(value["value"], TFigure.scopeType, "$path/scope") else null).castUnchecked(),
		(if(id == "caption") maybeDeser(value["value"], TFigure.captionType, "$path/caption") else null).castUnchecked(),
		(if(id == "kind") maybeDeser(value["value"], TFigure.kindType, "$path/kind") else null).castUnchecked(),
		(if(id == "supplement") maybeDeser(value["value"], TFigure.supplementType, "$path/supplement") else null).castUnchecked(),
		(if(id == "numbering") maybeDeser(value["value"], TFigure.numberingType, "$path/numbering") else null).castUnchecked(),
		(if(id == "gap") maybeDeser(value["value"], TFigure.gapType, "$path/gap") else null).castUnchecked(),
		(if(id == "outlined") maybeDeser(value["value"], TFigure.outlinedType, "$path/outlined") else null).castUnchecked(),
	)
}

internal fun deserializeFigureCaption(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("figure.caption")
        ?: type.optionOf("content")
	check(option != null) { "`figure.caption`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TFigureCaption (
		deserialize(value["body"], TFigureCaption.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["position"], TFigureCaption.positionType, "$path/position").castUnchecked(),
		maybeDeser(value["separator"], TFigureCaption.separatorType, "$path/separator").castUnchecked(),
	)
}

internal fun deserializeSetFigureCaption(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("figure.caption") ?: type.optionOf("style")
           ?: failWith("`set figure.caption`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetFigureCaption(
		(if(id == "position") maybeDeser(value["value"], TFigureCaption.positionType, "$path/position") else null).castUnchecked(),
		(if(id == "separator") maybeDeser(value["value"], TFigureCaption.separatorType, "$path/separator") else null).castUnchecked(),
	)
}

internal fun deserializeFootnote(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("footnote")
        ?: type.optionOf("content")
	check(option != null) { "`footnote`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TFootnote (
		deserialize(value["body"], TFootnote.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["numbering"], TFootnote.numberingType, "$path/numbering").castUnchecked(),
	)
}

internal fun deserializeSetFootnote(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("footnote") ?: type.optionOf("style")
           ?: failWith("`set footnote`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetFootnote(
		(if(id == "numbering") maybeDeser(value["value"], TFootnote.numberingType, "$path/numbering") else null).castUnchecked(),
	)
}

internal fun deserializeFootnoteEntry(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("footnote.entry")
        ?: type.optionOf("content")
	check(option != null) { "`footnote.entry`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TFootnoteEntry (
		deserialize(value["note"], TFootnoteEntry.noteType, "$path/note").castUnchecked(),
		maybeDeser(value["separator"], TFootnoteEntry.separatorType, "$path/separator").castUnchecked(),
		maybeDeser(value["clearance"], TFootnoteEntry.clearanceType, "$path/clearance").castUnchecked(),
		maybeDeser(value["gap"], TFootnoteEntry.gapType, "$path/gap").castUnchecked(),
		maybeDeser(value["indent"], TFootnoteEntry.indentType, "$path/indent").castUnchecked(),
	)
}

internal fun deserializeSetFootnoteEntry(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("footnote.entry") ?: type.optionOf("style")
           ?: failWith("`set footnote.entry`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetFootnoteEntry(
		(if(id == "separator") maybeDeser(value["value"], TFootnoteEntry.separatorType, "$path/separator") else null).castUnchecked(),
		(if(id == "clearance") maybeDeser(value["value"], TFootnoteEntry.clearanceType, "$path/clearance") else null).castUnchecked(),
		(if(id == "gap") maybeDeser(value["value"], TFootnoteEntry.gapType, "$path/gap") else null).castUnchecked(),
		(if(id == "indent") maybeDeser(value["value"], TFootnoteEntry.indentType, "$path/indent") else null).castUnchecked(),
	)
}

internal fun deserializeHeading(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("heading")
        ?: type.optionOf("content")
	check(option != null) { "`heading`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return THeading (
		deserialize(value["body"], THeading.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["level"], THeading.levelType, "$path/level").castUnchecked(),
		maybeDeser(value["depth"], THeading.depthType, "$path/depth").castUnchecked(),
		maybeDeser(value["offset"], THeading.offsetType, "$path/offset").castUnchecked(),
		maybeDeser(value["numbering"], THeading.numberingType, "$path/numbering").castUnchecked(),
		maybeDeser(value["supplement"], THeading.supplementType, "$path/supplement").castUnchecked(),
		maybeDeser(value["outlined"], THeading.outlinedType, "$path/outlined").castUnchecked(),
		maybeDeser(value["bookmarked"], THeading.bookmarkedType, "$path/bookmarked").castUnchecked(),
		maybeDeser(value["hanging-indent"], THeading.hangingIndentType, "$path/hanging-indent").castUnchecked(),
	)
}

internal fun deserializeSetHeading(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("heading") ?: type.optionOf("style")
           ?: failWith("`set heading`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetHeading(
		(if(id == "level") maybeDeser(value["value"], THeading.levelType, "$path/level") else null).castUnchecked(),
		(if(id == "depth") maybeDeser(value["value"], THeading.depthType, "$path/depth") else null).castUnchecked(),
		(if(id == "offset") maybeDeser(value["value"], THeading.offsetType, "$path/offset") else null).castUnchecked(),
		(if(id == "numbering") maybeDeser(value["value"], THeading.numberingType, "$path/numbering") else null).castUnchecked(),
		(if(id == "supplement") maybeDeser(value["value"], THeading.supplementType, "$path/supplement") else null).castUnchecked(),
		(if(id == "outlined") maybeDeser(value["value"], THeading.outlinedType, "$path/outlined") else null).castUnchecked(),
		(if(id == "bookmarked") maybeDeser(value["value"], THeading.bookmarkedType, "$path/bookmarked") else null).castUnchecked(),
		(if(id == "hanging-indent") maybeDeser(value["value"], THeading.hangingIndentType, "$path/hanging-indent") else null).castUnchecked(),
	)
}

internal fun deserializeLink(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("link")
        ?: type.optionOf("content")
	check(option != null) { "`link`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TLink (
		deserialize(value["dest"], TLink.destType, "$path/dest").castUnchecked(),
		deserialize(value["body"], TLink.bodyType, "$path/body").castUnchecked(),
	)
}

internal fun deserializePosition(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("position")
	check(option != null) { "`position`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TPosition (
		deserialize(value["page"], TPosition.pageType, "$path/page").castUnchecked(),
		deserialize(value["x"], TPosition.xType, "$path/x").castUnchecked(),
		deserialize(value["y"], TPosition.yType, "$path/y").castUnchecked(),
	)
}

internal fun deserializePoint(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("point")
	check(option != null) { "`point`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TPoint (
		deserialize(value["x"], TPoint.xType, "$path/x").castUnchecked(),
		deserialize(value["y"], TPoint.yType, "$path/y").castUnchecked(),
	)
}

internal fun deserializeEnum(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("enum")
        ?: type.optionOf("content")
	check(option != null) { "`enum`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TEnum (
		deserialize(value["children"], TEnum.childrenType, "$path/children").castUnchecked(),
		maybeDeser(value["tight"], TEnum.tightType, "$path/tight").castUnchecked(),
		maybeDeser(value["numbering"], TEnum.numberingType, "$path/numbering").castUnchecked(),
		maybeDeser(value["start"], TEnum.startType, "$path/start").castUnchecked(),
		maybeDeser(value["full"], TEnum.fullType, "$path/full").castUnchecked(),
		maybeDeser(value["indent"], TEnum.indentType, "$path/indent").castUnchecked(),
		maybeDeser(value["body-indent"], TEnum.bodyIndentType, "$path/body-indent").castUnchecked(),
		maybeDeser(value["spacing"], TEnum.spacingType, "$path/spacing").castUnchecked(),
		maybeDeser(value["number-align"], TEnum.numberAlignType, "$path/number-align").castUnchecked(),
	)
}

internal fun deserializeSetEnum(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("enum") ?: type.optionOf("style")
           ?: failWith("`set enum`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetEnum(
		(if(id == "tight") maybeDeser(value["value"], TEnum.tightType, "$path/tight") else null).castUnchecked(),
		(if(id == "numbering") maybeDeser(value["value"], TEnum.numberingType, "$path/numbering") else null).castUnchecked(),
		(if(id == "start") maybeDeser(value["value"], TEnum.startType, "$path/start") else null).castUnchecked(),
		(if(id == "full") maybeDeser(value["value"], TEnum.fullType, "$path/full") else null).castUnchecked(),
		(if(id == "indent") maybeDeser(value["value"], TEnum.indentType, "$path/indent") else null).castUnchecked(),
		(if(id == "body-indent") maybeDeser(value["value"], TEnum.bodyIndentType, "$path/body-indent") else null).castUnchecked(),
		(if(id == "spacing") maybeDeser(value["value"], TEnum.spacingType, "$path/spacing") else null).castUnchecked(),
		(if(id == "number-align") maybeDeser(value["value"], TEnum.numberAlignType, "$path/number-align") else null).castUnchecked(),
	)
}

internal fun deserializeEnumItem(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("enum.item")
        ?: type.optionOf("content")
	check(option != null) { "`enum.item`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TEnumItem (
		maybeDeser(value["number"], TEnumItem.numberType, "$path/number").castUnchecked(),
		deserialize(value["body"], TEnumItem.bodyType, "$path/body").castUnchecked(),
	)
}

internal fun deserializeSetEnumItem(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("enum.item") ?: type.optionOf("style")
           ?: failWith("`set enum.item`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetEnumItem(
		(if(id == "number") maybeDeser(value["value"], TEnumItem.numberType, "$path/number") else null).castUnchecked(),
	)
}

internal fun deserializeOutline(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("outline")
        ?: type.optionOf("content")
	check(option != null) { "`outline`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TOutline (
		maybeDeser(value["title"], TOutline.titleType, "$path/title").castUnchecked(),
		maybeDeser(value["target"], TOutline.targetType, "$path/target").castUnchecked(),
		maybeDeser(value["depth"], TOutline.depthType, "$path/depth").castUnchecked(),
		maybeDeser(value["indent"], TOutline.indentType, "$path/indent").castUnchecked(),
		maybeDeser(value["fill"], TOutline.fillType, "$path/fill").castUnchecked(),
	)
}

internal fun deserializeSetOutline(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("outline") ?: type.optionOf("style")
           ?: failWith("`set outline`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetOutline(
		(if(id == "title") maybeDeser(value["value"], TOutline.titleType, "$path/title") else null).castUnchecked(),
		(if(id == "target") maybeDeser(value["value"], TOutline.targetType, "$path/target") else null).castUnchecked(),
		(if(id == "depth") maybeDeser(value["value"], TOutline.depthType, "$path/depth") else null).castUnchecked(),
		(if(id == "indent") maybeDeser(value["value"], TOutline.indentType, "$path/indent") else null).castUnchecked(),
		(if(id == "fill") maybeDeser(value["value"], TOutline.fillType, "$path/fill") else null).castUnchecked(),
	)
}

internal fun deserializeOutlineEntry(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("outline.entry")
        ?: type.optionOf("content")
	check(option != null) { "`outline.entry`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TOutlineEntry (
		deserialize(value["level"], TOutlineEntry.levelType, "$path/level").castUnchecked(),
		deserialize(value["element"], TOutlineEntry.elementType, "$path/element").castUnchecked(),
		deserialize(value["body"], TOutlineEntry.bodyType, "$path/body").castUnchecked(),
		deserialize(value["fill"], TOutlineEntry.fillType, "$path/fill").castUnchecked(),
		deserialize(value["page"], TOutlineEntry.pageType, "$path/page").castUnchecked(),
	)
}

internal fun deserializePar(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("par")
        ?: type.optionOf("content")
	check(option != null) { "`par`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TPar (
		deserialize(value["body"], TPar.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["leading"], TPar.leadingType, "$path/leading").castUnchecked(),
		maybeDeser(value["spacing"], TPar.spacingType, "$path/spacing").castUnchecked(),
		maybeDeser(value["justify"], TPar.justifyType, "$path/justify").castUnchecked(),
		maybeDeser(value["linebreaks"], TPar.linebreaksType, "$path/linebreaks").castUnchecked(),
		maybeDeser(value["first-line-indent"], TPar.firstLineIndentType, "$path/first-line-indent").castUnchecked(),
		maybeDeser(value["hanging-indent"], TPar.hangingIndentType, "$path/hanging-indent").castUnchecked(),
	)
}

internal fun deserializeSetPar(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("par") ?: type.optionOf("style")
           ?: failWith("`set par`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetPar(
		(if(id == "leading") maybeDeser(value["value"], TPar.leadingType, "$path/leading") else null).castUnchecked(),
		(if(id == "spacing") maybeDeser(value["value"], TPar.spacingType, "$path/spacing") else null).castUnchecked(),
		(if(id == "justify") maybeDeser(value["value"], TPar.justifyType, "$path/justify") else null).castUnchecked(),
		(if(id == "linebreaks") maybeDeser(value["value"], TPar.linebreaksType, "$path/linebreaks") else null).castUnchecked(),
		(if(id == "first-line-indent") maybeDeser(value["value"], TPar.firstLineIndentType, "$path/first-line-indent") else null).castUnchecked(),
		(if(id == "hanging-indent") maybeDeser(value["value"], TPar.hangingIndentType, "$path/hanging-indent") else null).castUnchecked(),
	)
}

internal fun deserializeParLine(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("par.line")
        ?: type.optionOf("content")
	check(option != null) { "`par.line`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TParLine (
		maybeDeser(value["numbering"], TParLine.numberingType, "$path/numbering").castUnchecked(),
		maybeDeser(value["number-align"], TParLine.numberAlignType, "$path/number-align").castUnchecked(),
		maybeDeser(value["number-margin"], TParLine.numberMarginType, "$path/number-margin").castUnchecked(),
		maybeDeser(value["number-clearance"], TParLine.numberClearanceType, "$path/number-clearance").castUnchecked(),
		maybeDeser(value["numbering-scope"], TParLine.numberingScopeType, "$path/numbering-scope").castUnchecked(),
	)
}

internal fun deserializeSetParLine(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("par.line") ?: type.optionOf("style")
           ?: failWith("`set par.line`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetParLine(
		(if(id == "numbering") maybeDeser(value["value"], TParLine.numberingType, "$path/numbering") else null).castUnchecked(),
		(if(id == "number-align") maybeDeser(value["value"], TParLine.numberAlignType, "$path/number-align") else null).castUnchecked(),
		(if(id == "number-margin") maybeDeser(value["value"], TParLine.numberMarginType, "$path/number-margin") else null).castUnchecked(),
		(if(id == "number-clearance") maybeDeser(value["value"], TParLine.numberClearanceType, "$path/number-clearance") else null).castUnchecked(),
		(if(id == "numbering-scope") maybeDeser(value["value"], TParLine.numberingScopeType, "$path/numbering-scope") else null).castUnchecked(),
	)
}

internal fun deserializeParbreak(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("parbreak")
        ?: type.optionOf("content")
	check(option != null) { "`parbreak`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TParbreak (
	)
}

internal fun deserializeQuote(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("quote")
        ?: type.optionOf("content")
	check(option != null) { "`quote`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TQuote (
		deserialize(value["body"], TQuote.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["block"], TQuote.blockType, "$path/block").castUnchecked(),
		maybeDeser(value["quotes"], TQuote.quotesType, "$path/quotes").castUnchecked(),
		maybeDeser(value["attribution"], TQuote.attributionType, "$path/attribution").castUnchecked(),
	)
}

internal fun deserializeSetQuote(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("quote") ?: type.optionOf("style")
           ?: failWith("`set quote`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetQuote(
		(if(id == "block") maybeDeser(value["value"], TQuote.blockType, "$path/block") else null).castUnchecked(),
		(if(id == "quotes") maybeDeser(value["value"], TQuote.quotesType, "$path/quotes") else null).castUnchecked(),
		(if(id == "attribution") maybeDeser(value["value"], TQuote.attributionType, "$path/attribution") else null).castUnchecked(),
	)
}

internal fun deserializeRef(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("ref")
        ?: type.optionOf("content")
	check(option != null) { "`ref`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TRef (
		deserialize(value["target"], TRef.targetType, "$path/target").castUnchecked(),
		maybeDeser(value["supplement"], TRef.supplementType, "$path/supplement").castUnchecked(),
	)
}

internal fun deserializeSetRef(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("ref") ?: type.optionOf("style")
           ?: failWith("`set ref`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetRef(
		(if(id == "supplement") maybeDeser(value["value"], TRef.supplementType, "$path/supplement") else null).castUnchecked(),
	)
}

internal fun deserializeStrong(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("strong")
        ?: type.optionOf("content")
	check(option != null) { "`strong`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TStrong (
		deserialize(value["body"], TStrong.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["delta"], TStrong.deltaType, "$path/delta").castUnchecked(),
	)
}

internal fun deserializeSetStrong(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("strong") ?: type.optionOf("style")
           ?: failWith("`set strong`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetStrong(
		(if(id == "delta") maybeDeser(value["value"], TStrong.deltaType, "$path/delta") else null).castUnchecked(),
	)
}

internal fun deserializeTable(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("table")
        ?: type.optionOf("content")
	check(option != null) { "`table`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TTable (
		deserialize(value["children"], TTable.childrenType, "$path/children").castUnchecked(),
		maybeDeser(value["columns"], TTable.columnsType, "$path/columns").castUnchecked(),
		maybeDeser(value["rows"], TTable.rowsType, "$path/rows").castUnchecked(),
		maybeDeser(value["gutter"], TTable.gutterType, "$path/gutter").castUnchecked(),
		maybeDeser(value["column-gutter"], TTable.columnGutterType, "$path/column-gutter").castUnchecked(),
		maybeDeser(value["row-gutter"], TTable.rowGutterType, "$path/row-gutter").castUnchecked(),
		maybeDeser(value["fill"], TTable.fillType, "$path/fill").castUnchecked(),
		maybeDeser(value["align"], TTable.alignType, "$path/align").castUnchecked(),
		maybeDeser(value["stroke"], TTable.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["inset"], TTable.insetType, "$path/inset").castUnchecked(),
	)
}

internal fun deserializeSetTable(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("table") ?: type.optionOf("style")
           ?: failWith("`set table`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetTable(
		(if(id == "columns") maybeDeser(value["value"], TTable.columnsType, "$path/columns") else null).castUnchecked(),
		(if(id == "rows") maybeDeser(value["value"], TTable.rowsType, "$path/rows") else null).castUnchecked(),
		(if(id == "gutter") maybeDeser(value["value"], TTable.gutterType, "$path/gutter") else null).castUnchecked(),
		(if(id == "column-gutter") maybeDeser(value["value"], TTable.columnGutterType, "$path/column-gutter") else null).castUnchecked(),
		(if(id == "row-gutter") maybeDeser(value["value"], TTable.rowGutterType, "$path/row-gutter") else null).castUnchecked(),
		(if(id == "fill") maybeDeser(value["value"], TTable.fillType, "$path/fill") else null).castUnchecked(),
		(if(id == "align") maybeDeser(value["value"], TTable.alignType, "$path/align") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TTable.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "inset") maybeDeser(value["value"], TTable.insetType, "$path/inset") else null).castUnchecked(),
	)
}

internal fun deserializeSides(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("sides")
	check(option != null) { "`sides`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	val S = option.params?.getOrNull(0) ?: AnyType
	return TSides<TValue> (
		maybeDeser(value["top"], TSides.topType(S), "$path/top").castUnchecked(),
		maybeDeser(value["right"], TSides.rightType(S), "$path/right").castUnchecked(),
		maybeDeser(value["bottom"], TSides.bottomType(S), "$path/bottom").castUnchecked(),
		maybeDeser(value["left"], TSides.leftType(S), "$path/left").castUnchecked(),
	)
}

internal fun deserializeTableCell(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("table.cell")
        ?: type.optionOf("content")
	check(option != null) { "`table.cell`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TTableCell (
		deserialize(value["body"], TTableCell.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["x"], TTableCell.xType, "$path/x").castUnchecked(),
		maybeDeser(value["y"], TTableCell.yType, "$path/y").castUnchecked(),
		maybeDeser(value["colspan"], TTableCell.colspanType, "$path/colspan").castUnchecked(),
		maybeDeser(value["rowspan"], TTableCell.rowspanType, "$path/rowspan").castUnchecked(),
		maybeDeser(value["fill"], TTableCell.fillType, "$path/fill").castUnchecked(),
		maybeDeser(value["align"], TTableCell.alignType, "$path/align").castUnchecked(),
		maybeDeser(value["inset"], TTableCell.insetType, "$path/inset").castUnchecked(),
		maybeDeser(value["stroke"], TTableCell.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["breakable"], TTableCell.breakableType, "$path/breakable").castUnchecked(),
	)
}

internal fun deserializeSetTableCell(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("table.cell") ?: type.optionOf("style")
           ?: failWith("`set table.cell`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetTableCell(
		(if(id == "x") maybeDeser(value["value"], TTableCell.xType, "$path/x") else null).castUnchecked(),
		(if(id == "y") maybeDeser(value["value"], TTableCell.yType, "$path/y") else null).castUnchecked(),
		(if(id == "colspan") maybeDeser(value["value"], TTableCell.colspanType, "$path/colspan") else null).castUnchecked(),
		(if(id == "rowspan") maybeDeser(value["value"], TTableCell.rowspanType, "$path/rowspan") else null).castUnchecked(),
		(if(id == "fill") maybeDeser(value["value"], TTableCell.fillType, "$path/fill") else null).castUnchecked(),
		(if(id == "align") maybeDeser(value["value"], TTableCell.alignType, "$path/align") else null).castUnchecked(),
		(if(id == "inset") maybeDeser(value["value"], TTableCell.insetType, "$path/inset") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TTableCell.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "breakable") maybeDeser(value["value"], TTableCell.breakableType, "$path/breakable") else null).castUnchecked(),
	)
}

internal fun deserializeTableHline(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("table.hline")
        ?: type.optionOf("content")
	check(option != null) { "`table.hline`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TTableHline (
		maybeDeser(value["y"], TTableHline.yType, "$path/y").castUnchecked(),
		maybeDeser(value["start"], TTableHline.startType, "$path/start").castUnchecked(),
		maybeDeser(value["end"], TTableHline.endType, "$path/end").castUnchecked(),
		maybeDeser(value["stroke"], TTableHline.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["position"], TTableHline.positionType, "$path/position").castUnchecked(),
	)
}

internal fun deserializeSetTableHline(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("table.hline") ?: type.optionOf("style")
           ?: failWith("`set table.hline`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetTableHline(
		(if(id == "y") maybeDeser(value["value"], TTableHline.yType, "$path/y") else null).castUnchecked(),
		(if(id == "start") maybeDeser(value["value"], TTableHline.startType, "$path/start") else null).castUnchecked(),
		(if(id == "end") maybeDeser(value["value"], TTableHline.endType, "$path/end") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TTableHline.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "position") maybeDeser(value["value"], TTableHline.positionType, "$path/position") else null).castUnchecked(),
	)
}

internal fun deserializeTableVline(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("table.vline")
        ?: type.optionOf("content")
	check(option != null) { "`table.vline`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TTableVline (
		maybeDeser(value["x"], TTableVline.xType, "$path/x").castUnchecked(),
		maybeDeser(value["start"], TTableVline.startType, "$path/start").castUnchecked(),
		maybeDeser(value["end"], TTableVline.endType, "$path/end").castUnchecked(),
		maybeDeser(value["stroke"], TTableVline.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["position"], TTableVline.positionType, "$path/position").castUnchecked(),
	)
}

internal fun deserializeSetTableVline(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("table.vline") ?: type.optionOf("style")
           ?: failWith("`set table.vline`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetTableVline(
		(if(id == "x") maybeDeser(value["value"], TTableVline.xType, "$path/x") else null).castUnchecked(),
		(if(id == "start") maybeDeser(value["value"], TTableVline.startType, "$path/start") else null).castUnchecked(),
		(if(id == "end") maybeDeser(value["value"], TTableVline.endType, "$path/end") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TTableVline.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "position") maybeDeser(value["value"], TTableVline.positionType, "$path/position") else null).castUnchecked(),
	)
}

internal fun deserializeTableHeader(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("table.header")
        ?: type.optionOf("content")
	check(option != null) { "`table.header`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TTableHeader (
		deserialize(value["children"], TTableHeader.childrenType, "$path/children").castUnchecked(),
		maybeDeser(value["repeat"], TTableHeader.repeatType, "$path/repeat").castUnchecked(),
	)
}

internal fun deserializeSetTableHeader(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("table.header") ?: type.optionOf("style")
           ?: failWith("`set table.header`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetTableHeader(
		(if(id == "repeat") maybeDeser(value["value"], TTableHeader.repeatType, "$path/repeat") else null).castUnchecked(),
	)
}

internal fun deserializeTableFooter(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("table.footer")
        ?: type.optionOf("content")
	check(option != null) { "`table.footer`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TTableFooter (
		deserialize(value["children"], TTableFooter.childrenType, "$path/children").castUnchecked(),
		maybeDeser(value["repeat"], TTableFooter.repeatType, "$path/repeat").castUnchecked(),
	)
}

internal fun deserializeSetTableFooter(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("table.footer") ?: type.optionOf("style")
           ?: failWith("`set table.footer`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetTableFooter(
		(if(id == "repeat") maybeDeser(value["value"], TTableFooter.repeatType, "$path/repeat") else null).castUnchecked(),
	)
}

internal fun deserializeTerms(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("terms")
        ?: type.optionOf("content")
	check(option != null) { "`terms`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TTerms (
		deserialize(value["children"], TTerms.childrenType, "$path/children").castUnchecked(),
		maybeDeser(value["tight"], TTerms.tightType, "$path/tight").castUnchecked(),
		maybeDeser(value["separator"], TTerms.separatorType, "$path/separator").castUnchecked(),
		maybeDeser(value["indent"], TTerms.indentType, "$path/indent").castUnchecked(),
		maybeDeser(value["hanging-indent"], TTerms.hangingIndentType, "$path/hanging-indent").castUnchecked(),
		maybeDeser(value["spacing"], TTerms.spacingType, "$path/spacing").castUnchecked(),
	)
}

internal fun deserializeSetTerms(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("terms") ?: type.optionOf("style")
           ?: failWith("`set terms`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetTerms(
		(if(id == "tight") maybeDeser(value["value"], TTerms.tightType, "$path/tight") else null).castUnchecked(),
		(if(id == "separator") maybeDeser(value["value"], TTerms.separatorType, "$path/separator") else null).castUnchecked(),
		(if(id == "indent") maybeDeser(value["value"], TTerms.indentType, "$path/indent") else null).castUnchecked(),
		(if(id == "hanging-indent") maybeDeser(value["value"], TTerms.hangingIndentType, "$path/hanging-indent") else null).castUnchecked(),
		(if(id == "spacing") maybeDeser(value["value"], TTerms.spacingType, "$path/spacing") else null).castUnchecked(),
	)
}

internal fun deserializeTermsItem(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("terms.item")
        ?: type.optionOf("content")
	check(option != null) { "`terms.item`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TTermsItem (
		deserialize(value["term"], TTermsItem.termType, "$path/term").castUnchecked(),
		deserialize(value["description"], TTermsItem.descriptionType, "$path/description").castUnchecked(),
	)
}

internal fun deserializeHighlight(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("highlight")
        ?: type.optionOf("content")
	check(option != null) { "`highlight`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return THighlight (
		deserialize(value["body"], THighlight.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["fill"], THighlight.fillType, "$path/fill").castUnchecked(),
		maybeDeser(value["stroke"], THighlight.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["top-edge"], THighlight.topEdgeType, "$path/top-edge").castUnchecked(),
		maybeDeser(value["bottom-edge"], THighlight.bottomEdgeType, "$path/bottom-edge").castUnchecked(),
		maybeDeser(value["extent"], THighlight.extentType, "$path/extent").castUnchecked(),
		maybeDeser(value["radius"], THighlight.radiusType, "$path/radius").castUnchecked(),
	)
}

internal fun deserializeSetHighlight(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("highlight") ?: type.optionOf("style")
           ?: failWith("`set highlight`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetHighlight(
		(if(id == "fill") maybeDeser(value["value"], THighlight.fillType, "$path/fill") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], THighlight.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "top-edge") maybeDeser(value["value"], THighlight.topEdgeType, "$path/top-edge") else null).castUnchecked(),
		(if(id == "bottom-edge") maybeDeser(value["value"], THighlight.bottomEdgeType, "$path/bottom-edge") else null).castUnchecked(),
		(if(id == "extent") maybeDeser(value["value"], THighlight.extentType, "$path/extent") else null).castUnchecked(),
		(if(id == "radius") maybeDeser(value["value"], THighlight.radiusType, "$path/radius") else null).castUnchecked(),
	)
}

internal fun deserializeLinebreak(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("linebreak")
        ?: type.optionOf("content")
	check(option != null) { "`linebreak`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TLinebreak (
		maybeDeser(value["justify"], TLinebreak.justifyType, "$path/justify").castUnchecked(),
	)
}

internal fun deserializeSetLinebreak(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("linebreak") ?: type.optionOf("style")
           ?: failWith("`set linebreak`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetLinebreak(
		(if(id == "justify") maybeDeser(value["value"], TLinebreak.justifyType, "$path/justify") else null).castUnchecked(),
	)
}

internal fun deserializeOverline(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("overline")
        ?: type.optionOf("content")
	check(option != null) { "`overline`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TOverline (
		deserialize(value["body"], TOverline.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["stroke"], TOverline.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["offset"], TOverline.offsetType, "$path/offset").castUnchecked(),
		maybeDeser(value["extent"], TOverline.extentType, "$path/extent").castUnchecked(),
		maybeDeser(value["evade"], TOverline.evadeType, "$path/evade").castUnchecked(),
		maybeDeser(value["background"], TOverline.backgroundType, "$path/background").castUnchecked(),
	)
}

internal fun deserializeSetOverline(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("overline") ?: type.optionOf("style")
           ?: failWith("`set overline`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetOverline(
		(if(id == "stroke") maybeDeser(value["value"], TOverline.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "offset") maybeDeser(value["value"], TOverline.offsetType, "$path/offset") else null).castUnchecked(),
		(if(id == "extent") maybeDeser(value["value"], TOverline.extentType, "$path/extent") else null).castUnchecked(),
		(if(id == "evade") maybeDeser(value["value"], TOverline.evadeType, "$path/evade") else null).castUnchecked(),
		(if(id == "background") maybeDeser(value["value"], TOverline.backgroundType, "$path/background") else null).castUnchecked(),
	)
}

internal fun deserializeRaw(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("raw")
        ?: type.optionOf("content")
	check(option != null) { "`raw`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TRaw (
		deserialize(value["text"], TRaw.textType, "$path/text").castUnchecked(),
		maybeDeser(value["block"], TRaw.blockType, "$path/block").castUnchecked(),
		maybeDeser(value["lang"], TRaw.langType, "$path/lang").castUnchecked(),
		maybeDeser(value["align"], TRaw.alignType, "$path/align").castUnchecked(),
		maybeDeser(value["syntaxes"], TRaw.syntaxesType, "$path/syntaxes").castUnchecked(),
		maybeDeser(value["theme"], TRaw.themeType, "$path/theme").castUnchecked(),
		maybeDeser(value["tab-size"], TRaw.tabSizeType, "$path/tab-size").castUnchecked(),
	)
}

internal fun deserializeSetRaw(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("raw") ?: type.optionOf("style")
           ?: failWith("`set raw`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetRaw(
		(if(id == "block") maybeDeser(value["value"], TRaw.blockType, "$path/block") else null).castUnchecked(),
		(if(id == "lang") maybeDeser(value["value"], TRaw.langType, "$path/lang") else null).castUnchecked(),
		(if(id == "align") maybeDeser(value["value"], TRaw.alignType, "$path/align") else null).castUnchecked(),
		(if(id == "syntaxes") maybeDeser(value["value"], TRaw.syntaxesType, "$path/syntaxes") else null).castUnchecked(),
		(if(id == "theme") maybeDeser(value["value"], TRaw.themeType, "$path/theme") else null).castUnchecked(),
		(if(id == "tab-size") maybeDeser(value["value"], TRaw.tabSizeType, "$path/tab-size") else null).castUnchecked(),
	)
}

internal fun deserializeRawLine(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("raw.line")
        ?: type.optionOf("content")
	check(option != null) { "`raw.line`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TRawLine (
		deserialize(value["number"], TRawLine.numberType, "$path/number").castUnchecked(),
		deserialize(value["count"], TRawLine.countType, "$path/count").castUnchecked(),
		deserialize(value["text"], TRawLine.textType, "$path/text").castUnchecked(),
		deserialize(value["body"], TRawLine.bodyType, "$path/body").castUnchecked(),
	)
}

internal fun deserializeSmallcaps(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("smallcaps")
        ?: type.optionOf("content")
	check(option != null) { "`smallcaps`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TSmallcaps (
		deserialize(value["body"], TSmallcaps.bodyType, "$path/body").castUnchecked(),
	)
}

internal fun deserializeSmartquote(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("smartquote")
        ?: type.optionOf("content")
	check(option != null) { "`smartquote`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TSmartquote (
		maybeDeser(value["double"], TSmartquote.doubleType, "$path/double").castUnchecked(),
		maybeDeser(value["enabled"], TSmartquote.enabledType, "$path/enabled").castUnchecked(),
		maybeDeser(value["alternative"], TSmartquote.alternativeType, "$path/alternative").castUnchecked(),
		maybeDeser(value["quotes"], TSmartquote.quotesType, "$path/quotes").castUnchecked(),
	)
}

internal fun deserializeSetSmartquote(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("smartquote") ?: type.optionOf("style")
           ?: failWith("`set smartquote`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetSmartquote(
		(if(id == "double") maybeDeser(value["value"], TSmartquote.doubleType, "$path/double") else null).castUnchecked(),
		(if(id == "enabled") maybeDeser(value["value"], TSmartquote.enabledType, "$path/enabled") else null).castUnchecked(),
		(if(id == "alternative") maybeDeser(value["value"], TSmartquote.alternativeType, "$path/alternative") else null).castUnchecked(),
		(if(id == "quotes") maybeDeser(value["value"], TSmartquote.quotesType, "$path/quotes") else null).castUnchecked(),
	)
}

internal fun deserializeStrike(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("strike")
        ?: type.optionOf("content")
	check(option != null) { "`strike`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TStrike (
		deserialize(value["body"], TStrike.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["stroke"], TStrike.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["offset"], TStrike.offsetType, "$path/offset").castUnchecked(),
		maybeDeser(value["extent"], TStrike.extentType, "$path/extent").castUnchecked(),
		maybeDeser(value["background"], TStrike.backgroundType, "$path/background").castUnchecked(),
	)
}

internal fun deserializeSetStrike(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("strike") ?: type.optionOf("style")
           ?: failWith("`set strike`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetStrike(
		(if(id == "stroke") maybeDeser(value["value"], TStrike.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "offset") maybeDeser(value["value"], TStrike.offsetType, "$path/offset") else null).castUnchecked(),
		(if(id == "extent") maybeDeser(value["value"], TStrike.extentType, "$path/extent") else null).castUnchecked(),
		(if(id == "background") maybeDeser(value["value"], TStrike.backgroundType, "$path/background") else null).castUnchecked(),
	)
}

internal fun deserializeSub(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("sub")
        ?: type.optionOf("content")
	check(option != null) { "`sub`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TSub (
		deserialize(value["body"], TSub.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["typographic"], TSub.typographicType, "$path/typographic").castUnchecked(),
		maybeDeser(value["baseline"], TSub.baselineType, "$path/baseline").castUnchecked(),
		maybeDeser(value["size"], TSub.sizeType, "$path/size").castUnchecked(),
	)
}

internal fun deserializeSetSub(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("sub") ?: type.optionOf("style")
           ?: failWith("`set sub`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetSub(
		(if(id == "typographic") maybeDeser(value["value"], TSub.typographicType, "$path/typographic") else null).castUnchecked(),
		(if(id == "baseline") maybeDeser(value["value"], TSub.baselineType, "$path/baseline") else null).castUnchecked(),
		(if(id == "size") maybeDeser(value["value"], TSub.sizeType, "$path/size") else null).castUnchecked(),
	)
}

internal fun deserializeSuper(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("super")
        ?: type.optionOf("content")
	check(option != null) { "`super`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TSuper (
		deserialize(value["body"], TSuper.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["typographic"], TSuper.typographicType, "$path/typographic").castUnchecked(),
		maybeDeser(value["baseline"], TSuper.baselineType, "$path/baseline").castUnchecked(),
		maybeDeser(value["size"], TSuper.sizeType, "$path/size").castUnchecked(),
	)
}

internal fun deserializeSetSuper(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("super") ?: type.optionOf("style")
           ?: failWith("`set super`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetSuper(
		(if(id == "typographic") maybeDeser(value["value"], TSuper.typographicType, "$path/typographic") else null).castUnchecked(),
		(if(id == "baseline") maybeDeser(value["value"], TSuper.baselineType, "$path/baseline") else null).castUnchecked(),
		(if(id == "size") maybeDeser(value["value"], TSuper.sizeType, "$path/size") else null).castUnchecked(),
	)
}

internal fun deserializeText(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("text")
        ?: type.optionOf("content")
	check(option != null) { "`text`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TText (
		maybeDeser(value["body"], TText.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["text"], TText.textType, "$path/text").castUnchecked(),
		maybeDeser(value["font"], TText.fontType, "$path/font").castUnchecked(),
		maybeDeser(value["fallback"], TText.fallbackType, "$path/fallback").castUnchecked(),
		maybeDeser(value["style"], TText.styleType, "$path/style").castUnchecked(),
		maybeDeser(value["weight"], TText.weightType, "$path/weight").castUnchecked(),
		maybeDeser(value["stretch"], TText.stretchType, "$path/stretch").castUnchecked(),
		maybeDeser(value["size"], TText.sizeType, "$path/size").castUnchecked(),
		maybeDeser(value["fill"], TText.fillType, "$path/fill").castUnchecked(),
		maybeDeser(value["stroke"], TText.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["tracking"], TText.trackingType, "$path/tracking").castUnchecked(),
		maybeDeser(value["spacing"], TText.spacingType, "$path/spacing").castUnchecked(),
		maybeDeser(value["cjk-latin-spacing"], TText.cjkLatinSpacingType, "$path/cjk-latin-spacing").castUnchecked(),
		maybeDeser(value["baseline"], TText.baselineType, "$path/baseline").castUnchecked(),
		maybeDeser(value["overhang"], TText.overhangType, "$path/overhang").castUnchecked(),
		maybeDeser(value["top-edge"], TText.topEdgeType, "$path/top-edge").castUnchecked(),
		maybeDeser(value["bottom-edge"], TText.bottomEdgeType, "$path/bottom-edge").castUnchecked(),
		maybeDeser(value["lang"], TText.langType, "$path/lang").castUnchecked(),
		maybeDeser(value["region"], TText.regionType, "$path/region").castUnchecked(),
		maybeDeser(value["script"], TText.scriptType, "$path/script").castUnchecked(),
		maybeDeser(value["dir"], TText.dirType, "$path/dir").castUnchecked(),
		maybeDeser(value["hyphenate"], TText.hyphenateType, "$path/hyphenate").castUnchecked(),
		maybeDeser(value["costs"], TText.costsType, "$path/costs").castUnchecked(),
		maybeDeser(value["kerning"], TText.kerningType, "$path/kerning").castUnchecked(),
		maybeDeser(value["alternates"], TText.alternatesType, "$path/alternates").castUnchecked(),
		maybeDeser(value["stylistic-set"], TText.stylisticSetType, "$path/stylistic-set").castUnchecked(),
		maybeDeser(value["ligatures"], TText.ligaturesType, "$path/ligatures").castUnchecked(),
		maybeDeser(value["discretionary-ligatures"], TText.discretionaryLigaturesType, "$path/discretionary-ligatures").castUnchecked(),
		maybeDeser(value["historical-ligatures"], TText.historicalLigaturesType, "$path/historical-ligatures").castUnchecked(),
		maybeDeser(value["number-type"], TText.numberTypeType, "$path/number-type").castUnchecked(),
		maybeDeser(value["number-width"], TText.numberWidthType, "$path/number-width").castUnchecked(),
		maybeDeser(value["slashed-zero"], TText.slashedZeroType, "$path/slashed-zero").castUnchecked(),
		maybeDeser(value["fractions"], TText.fractionsType, "$path/fractions").castUnchecked(),
		maybeDeser(value["features"], TText.featuresType, "$path/features").castUnchecked(),
	)
}

internal fun deserializeSetText(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("text") ?: type.optionOf("style")
           ?: failWith("`set text`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetText(
		(if(id == "font") maybeDeser(value["value"], TText.fontType, "$path/font") else null).castUnchecked(),
		(if(id == "fallback") maybeDeser(value["value"], TText.fallbackType, "$path/fallback") else null).castUnchecked(),
		(if(id == "style") maybeDeser(value["value"], TText.styleType, "$path/style") else null).castUnchecked(),
		(if(id == "weight") maybeDeser(value["value"], TText.weightType, "$path/weight") else null).castUnchecked(),
		(if(id == "stretch") maybeDeser(value["value"], TText.stretchType, "$path/stretch") else null).castUnchecked(),
		(if(id == "size") maybeDeser(value["value"], TText.sizeType, "$path/size") else null).castUnchecked(),
		(if(id == "fill") maybeDeser(value["value"], TText.fillType, "$path/fill") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TText.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "tracking") maybeDeser(value["value"], TText.trackingType, "$path/tracking") else null).castUnchecked(),
		(if(id == "spacing") maybeDeser(value["value"], TText.spacingType, "$path/spacing") else null).castUnchecked(),
		(if(id == "cjk-latin-spacing") maybeDeser(value["value"], TText.cjkLatinSpacingType, "$path/cjk-latin-spacing") else null).castUnchecked(),
		(if(id == "baseline") maybeDeser(value["value"], TText.baselineType, "$path/baseline") else null).castUnchecked(),
		(if(id == "overhang") maybeDeser(value["value"], TText.overhangType, "$path/overhang") else null).castUnchecked(),
		(if(id == "top-edge") maybeDeser(value["value"], TText.topEdgeType, "$path/top-edge") else null).castUnchecked(),
		(if(id == "bottom-edge") maybeDeser(value["value"], TText.bottomEdgeType, "$path/bottom-edge") else null).castUnchecked(),
		(if(id == "lang") maybeDeser(value["value"], TText.langType, "$path/lang") else null).castUnchecked(),
		(if(id == "region") maybeDeser(value["value"], TText.regionType, "$path/region") else null).castUnchecked(),
		(if(id == "script") maybeDeser(value["value"], TText.scriptType, "$path/script") else null).castUnchecked(),
		(if(id == "dir") maybeDeser(value["value"], TText.dirType, "$path/dir") else null).castUnchecked(),
		(if(id == "hyphenate") maybeDeser(value["value"], TText.hyphenateType, "$path/hyphenate") else null).castUnchecked(),
		(if(id == "costs") maybeDeser(value["value"], TText.costsType, "$path/costs") else null).castUnchecked(),
		(if(id == "kerning") maybeDeser(value["value"], TText.kerningType, "$path/kerning") else null).castUnchecked(),
		(if(id == "alternates") maybeDeser(value["value"], TText.alternatesType, "$path/alternates") else null).castUnchecked(),
		(if(id == "stylistic-set") maybeDeser(value["value"], TText.stylisticSetType, "$path/stylistic-set") else null).castUnchecked(),
		(if(id == "ligatures") maybeDeser(value["value"], TText.ligaturesType, "$path/ligatures") else null).castUnchecked(),
		(if(id == "discretionary-ligatures") maybeDeser(value["value"], TText.discretionaryLigaturesType, "$path/discretionary-ligatures") else null).castUnchecked(),
		(if(id == "historical-ligatures") maybeDeser(value["value"], TText.historicalLigaturesType, "$path/historical-ligatures") else null).castUnchecked(),
		(if(id == "number-type") maybeDeser(value["value"], TText.numberTypeType, "$path/number-type") else null).castUnchecked(),
		(if(id == "number-width") maybeDeser(value["value"], TText.numberWidthType, "$path/number-width") else null).castUnchecked(),
		(if(id == "slashed-zero") maybeDeser(value["value"], TText.slashedZeroType, "$path/slashed-zero") else null).castUnchecked(),
		(if(id == "fractions") maybeDeser(value["value"], TText.fractionsType, "$path/fractions") else null).castUnchecked(),
		(if(id == "features") maybeDeser(value["value"], TText.featuresType, "$path/features") else null).castUnchecked(),
	)
}

internal fun deserializeUnderline(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("underline")
        ?: type.optionOf("content")
	check(option != null) { "`underline`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TUnderline (
		deserialize(value["body"], TUnderline.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["stroke"], TUnderline.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["offset"], TUnderline.offsetType, "$path/offset").castUnchecked(),
		maybeDeser(value["extent"], TUnderline.extentType, "$path/extent").castUnchecked(),
		maybeDeser(value["evade"], TUnderline.evadeType, "$path/evade").castUnchecked(),
		maybeDeser(value["background"], TUnderline.backgroundType, "$path/background").castUnchecked(),
	)
}

internal fun deserializeSetUnderline(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("underline") ?: type.optionOf("style")
           ?: failWith("`set underline`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetUnderline(
		(if(id == "stroke") maybeDeser(value["value"], TUnderline.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "offset") maybeDeser(value["value"], TUnderline.offsetType, "$path/offset") else null).castUnchecked(),
		(if(id == "extent") maybeDeser(value["value"], TUnderline.extentType, "$path/extent") else null).castUnchecked(),
		(if(id == "evade") maybeDeser(value["value"], TUnderline.evadeType, "$path/evade") else null).castUnchecked(),
		(if(id == "background") maybeDeser(value["value"], TUnderline.backgroundType, "$path/background") else null).castUnchecked(),
	)
}

internal fun deserializeMathAccent(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.accent")
        ?: type.optionOf("content")
	check(option != null) { "`math.accent`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathAccent (
		deserialize(value["base"], TMathAccent.baseType, "$path/base").castUnchecked(),
		deserialize(value["accent"], TMathAccent.accentType, "$path/accent").castUnchecked(),
		maybeDeser(value["size"], TMathAccent.sizeType, "$path/size").castUnchecked(),
	)
}

internal fun deserializeSetMathAccent(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.accent") ?: type.optionOf("style")
           ?: failWith("`set math.accent`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathAccent(
		(if(id == "size") maybeDeser(value["value"], TMathAccent.sizeType, "$path/size") else null).castUnchecked(),
	)
}

internal fun deserializeMathBinom(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.binom")
        ?: type.optionOf("content")
	check(option != null) { "`math.binom`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathBinom (
		deserialize(value["upper"], TMathBinom.upperType, "$path/upper").castUnchecked(),
		deserialize(value["lower"], TMathBinom.lowerType, "$path/lower").castUnchecked(),
	)
}

internal fun deserializeMathCancel(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.cancel")
        ?: type.optionOf("content")
	check(option != null) { "`math.cancel`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathCancel (
		deserialize(value["body"], TMathCancel.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["length"], TMathCancel.lengthType, "$path/length").castUnchecked(),
		maybeDeser(value["inverted"], TMathCancel.invertedType, "$path/inverted").castUnchecked(),
		maybeDeser(value["cross"], TMathCancel.crossType, "$path/cross").castUnchecked(),
		maybeDeser(value["angle"], TMathCancel.angleType, "$path/angle").castUnchecked(),
		maybeDeser(value["stroke"], TMathCancel.strokeType, "$path/stroke").castUnchecked(),
	)
}

internal fun deserializeSetMathCancel(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.cancel") ?: type.optionOf("style")
           ?: failWith("`set math.cancel`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathCancel(
		(if(id == "length") maybeDeser(value["value"], TMathCancel.lengthType, "$path/length") else null).castUnchecked(),
		(if(id == "inverted") maybeDeser(value["value"], TMathCancel.invertedType, "$path/inverted") else null).castUnchecked(),
		(if(id == "cross") maybeDeser(value["value"], TMathCancel.crossType, "$path/cross") else null).castUnchecked(),
		(if(id == "angle") maybeDeser(value["value"], TMathCancel.angleType, "$path/angle") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TMathCancel.strokeType, "$path/stroke") else null).castUnchecked(),
	)
}

internal fun deserializeMathCases(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.cases")
        ?: type.optionOf("content")
	check(option != null) { "`math.cases`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathCases (
		deserialize(value["children"], TMathCases.childrenType, "$path/children").castUnchecked(),
		maybeDeser(value["delim"], TMathCases.delimType, "$path/delim").castUnchecked(),
		maybeDeser(value["reverse"], TMathCases.reverseType, "$path/reverse").castUnchecked(),
		maybeDeser(value["gap"], TMathCases.gapType, "$path/gap").castUnchecked(),
	)
}

internal fun deserializeSetMathCases(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.cases") ?: type.optionOf("style")
           ?: failWith("`set math.cases`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathCases(
		(if(id == "delim") maybeDeser(value["value"], TMathCases.delimType, "$path/delim") else null).castUnchecked(),
		(if(id == "reverse") maybeDeser(value["value"], TMathCases.reverseType, "$path/reverse") else null).castUnchecked(),
		(if(id == "gap") maybeDeser(value["value"], TMathCases.gapType, "$path/gap") else null).castUnchecked(),
	)
}

internal fun deserializeMathClass(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.class")
        ?: type.optionOf("content")
	check(option != null) { "`math.class`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathClass (
		deserialize(value["class"], TMathClass.clazzType, "$path/class").castUnchecked(),
		deserialize(value["body"], TMathClass.bodyType, "$path/body").castUnchecked(),
	)
}

internal fun deserializeMathEquation(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.equation")
        ?: type.optionOf("content")
	check(option != null) { "`math.equation`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathEquation (
		deserialize(value["body"], TMathEquation.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["block"], TMathEquation.blockType, "$path/block").castUnchecked(),
		maybeDeser(value["numbering"], TMathEquation.numberingType, "$path/numbering").castUnchecked(),
		maybeDeser(value["number-align"], TMathEquation.numberAlignType, "$path/number-align").castUnchecked(),
		maybeDeser(value["supplement"], TMathEquation.supplementType, "$path/supplement").castUnchecked(),
	)
}

internal fun deserializeSetMathEquation(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.equation") ?: type.optionOf("style")
           ?: failWith("`set math.equation`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathEquation(
		(if(id == "block") maybeDeser(value["value"], TMathEquation.blockType, "$path/block") else null).castUnchecked(),
		(if(id == "numbering") maybeDeser(value["value"], TMathEquation.numberingType, "$path/numbering") else null).castUnchecked(),
		(if(id == "number-align") maybeDeser(value["value"], TMathEquation.numberAlignType, "$path/number-align") else null).castUnchecked(),
		(if(id == "supplement") maybeDeser(value["value"], TMathEquation.supplementType, "$path/supplement") else null).castUnchecked(),
	)
}

internal fun deserializeMathFrac(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.frac")
        ?: type.optionOf("content")
	check(option != null) { "`math.frac`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathFrac (
		deserialize(value["num"], TMathFrac.numType, "$path/num").castUnchecked(),
		deserialize(value["denom"], TMathFrac.denomType, "$path/denom").castUnchecked(),
	)
}

internal fun deserializeMathMat(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.mat")
        ?: type.optionOf("content")
	check(option != null) { "`math.mat`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathMat (
		deserialize(value["rows"], TMathMat.rowsType, "$path/rows").castUnchecked(),
		maybeDeser(value["delim"], TMathMat.delimType, "$path/delim").castUnchecked(),
		maybeDeser(value["align"], TMathMat.alignType, "$path/align").castUnchecked(),
		maybeDeser(value["augment"], TMathMat.augmentType, "$path/augment").castUnchecked(),
		maybeDeser(value["gap"], TMathMat.gapType, "$path/gap").castUnchecked(),
		maybeDeser(value["row-gap"], TMathMat.rowGapType, "$path/row-gap").castUnchecked(),
		maybeDeser(value["column-gap"], TMathMat.columnGapType, "$path/column-gap").castUnchecked(),
	)
}

internal fun deserializeSetMathMat(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.mat") ?: type.optionOf("style")
           ?: failWith("`set math.mat`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathMat(
		(if(id == "delim") maybeDeser(value["value"], TMathMat.delimType, "$path/delim") else null).castUnchecked(),
		(if(id == "align") maybeDeser(value["value"], TMathMat.alignType, "$path/align") else null).castUnchecked(),
		(if(id == "augment") maybeDeser(value["value"], TMathMat.augmentType, "$path/augment") else null).castUnchecked(),
		(if(id == "gap") maybeDeser(value["value"], TMathMat.gapType, "$path/gap") else null).castUnchecked(),
		(if(id == "row-gap") maybeDeser(value["value"], TMathMat.rowGapType, "$path/row-gap") else null).castUnchecked(),
		(if(id == "column-gap") maybeDeser(value["value"], TMathMat.columnGapType, "$path/column-gap") else null).castUnchecked(),
	)
}

internal fun deserializeMathPrimes(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.primes")
        ?: type.optionOf("content")
	check(option != null) { "`math.primes`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathPrimes (
		deserialize(value["count"], TMathPrimes.countType, "$path/count").castUnchecked(),
	)
}

internal fun deserializeMathStretch(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.stretch")
        ?: type.optionOf("content")
	check(option != null) { "`math.stretch`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathStretch (
		deserialize(value["body"], TMathStretch.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["size"], TMathStretch.sizeType, "$path/size").castUnchecked(),
	)
}

internal fun deserializeSetMathStretch(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.stretch") ?: type.optionOf("style")
           ?: failWith("`set math.stretch`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathStretch(
		(if(id == "size") maybeDeser(value["value"], TMathStretch.sizeType, "$path/size") else null).castUnchecked(),
	)
}

internal fun deserializeMathOp(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.op")
        ?: type.optionOf("content")
	check(option != null) { "`math.op`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathOp (
		deserialize(value["text"], TMathOp.textType, "$path/text").castUnchecked(),
		maybeDeser(value["limits"], TMathOp.limitsType, "$path/limits").castUnchecked(),
	)
}

internal fun deserializeSetMathOp(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.op") ?: type.optionOf("style")
           ?: failWith("`set math.op`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathOp(
		(if(id == "limits") maybeDeser(value["value"], TMathOp.limitsType, "$path/limits") else null).castUnchecked(),
	)
}

internal fun deserializeMathVec(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.vec")
        ?: type.optionOf("content")
	check(option != null) { "`math.vec`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathVec (
		deserialize(value["children"], TMathVec.childrenType, "$path/children").castUnchecked(),
		maybeDeser(value["delim"], TMathVec.delimType, "$path/delim").castUnchecked(),
		maybeDeser(value["align"], TMathVec.alignType, "$path/align").castUnchecked(),
		maybeDeser(value["gap"], TMathVec.gapType, "$path/gap").castUnchecked(),
	)
}

internal fun deserializeSetMathVec(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.vec") ?: type.optionOf("style")
           ?: failWith("`set math.vec`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathVec(
		(if(id == "delim") maybeDeser(value["value"], TMathVec.delimType, "$path/delim") else null).castUnchecked(),
		(if(id == "align") maybeDeser(value["value"], TMathVec.alignType, "$path/align") else null).castUnchecked(),
		(if(id == "gap") maybeDeser(value["value"], TMathVec.gapType, "$path/gap") else null).castUnchecked(),
	)
}

internal fun deserializeAlign(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("align")
        ?: type.optionOf("content")
	check(option != null) { "`align`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TAlign (
		maybeDeser(value["alignment"], TAlign.alignmentType, "$path/alignment").castUnchecked(),
		deserialize(value["body"], TAlign.bodyType, "$path/body").castUnchecked(),
	)
}

internal fun deserializeSetAlign(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("align") ?: type.optionOf("style")
           ?: failWith("`set align`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetAlign(
		(if(id == "alignment") maybeDeser(value["value"], TAlign.alignmentType, "$path/alignment") else null).castUnchecked(),
	)
}

internal fun deserializeBlock(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("block")
        ?: type.optionOf("content")
	check(option != null) { "`block`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TBlock (
		maybeDeser(value["body"], TBlock.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["width"], TBlock.widthType, "$path/width").castUnchecked(),
		maybeDeser(value["height"], TBlock.heightType, "$path/height").castUnchecked(),
		maybeDeser(value["breakable"], TBlock.breakableType, "$path/breakable").castUnchecked(),
		maybeDeser(value["fill"], TBlock.fillType, "$path/fill").castUnchecked(),
		maybeDeser(value["stroke"], TBlock.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["radius"], TBlock.radiusType, "$path/radius").castUnchecked(),
		maybeDeser(value["inset"], TBlock.insetType, "$path/inset").castUnchecked(),
		maybeDeser(value["outset"], TBlock.outsetType, "$path/outset").castUnchecked(),
		maybeDeser(value["spacing"], TBlock.spacingType, "$path/spacing").castUnchecked(),
		maybeDeser(value["above"], TBlock.aboveType, "$path/above").castUnchecked(),
		maybeDeser(value["below"], TBlock.belowType, "$path/below").castUnchecked(),
		maybeDeser(value["clip"], TBlock.clipType, "$path/clip").castUnchecked(),
		maybeDeser(value["sticky"], TBlock.stickyType, "$path/sticky").castUnchecked(),
	)
}

internal fun deserializeSetBlock(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("block") ?: type.optionOf("style")
           ?: failWith("`set block`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetBlock(
		(if(id == "body") maybeDeser(value["value"], TBlock.bodyType, "$path/body") else null).castUnchecked(),
		(if(id == "width") maybeDeser(value["value"], TBlock.widthType, "$path/width") else null).castUnchecked(),
		(if(id == "height") maybeDeser(value["value"], TBlock.heightType, "$path/height") else null).castUnchecked(),
		(if(id == "breakable") maybeDeser(value["value"], TBlock.breakableType, "$path/breakable") else null).castUnchecked(),
		(if(id == "fill") maybeDeser(value["value"], TBlock.fillType, "$path/fill") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TBlock.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "radius") maybeDeser(value["value"], TBlock.radiusType, "$path/radius") else null).castUnchecked(),
		(if(id == "inset") maybeDeser(value["value"], TBlock.insetType, "$path/inset") else null).castUnchecked(),
		(if(id == "outset") maybeDeser(value["value"], TBlock.outsetType, "$path/outset") else null).castUnchecked(),
		(if(id == "spacing") maybeDeser(value["value"], TBlock.spacingType, "$path/spacing") else null).castUnchecked(),
		(if(id == "above") maybeDeser(value["value"], TBlock.aboveType, "$path/above") else null).castUnchecked(),
		(if(id == "below") maybeDeser(value["value"], TBlock.belowType, "$path/below") else null).castUnchecked(),
		(if(id == "clip") maybeDeser(value["value"], TBlock.clipType, "$path/clip") else null).castUnchecked(),
		(if(id == "sticky") maybeDeser(value["value"], TBlock.stickyType, "$path/sticky") else null).castUnchecked(),
	)
}

internal fun deserializeBox(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("box")
        ?: type.optionOf("content")
	check(option != null) { "`box`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TBox (
		maybeDeser(value["body"], TBox.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["width"], TBox.widthType, "$path/width").castUnchecked(),
		maybeDeser(value["height"], TBox.heightType, "$path/height").castUnchecked(),
		maybeDeser(value["baseline"], TBox.baselineType, "$path/baseline").castUnchecked(),
		maybeDeser(value["fill"], TBox.fillType, "$path/fill").castUnchecked(),
		maybeDeser(value["stroke"], TBox.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["radius"], TBox.radiusType, "$path/radius").castUnchecked(),
		maybeDeser(value["inset"], TBox.insetType, "$path/inset").castUnchecked(),
		maybeDeser(value["outset"], TBox.outsetType, "$path/outset").castUnchecked(),
		maybeDeser(value["clip"], TBox.clipType, "$path/clip").castUnchecked(),
	)
}

internal fun deserializeSetBox(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("box") ?: type.optionOf("style")
           ?: failWith("`set box`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetBox(
		(if(id == "body") maybeDeser(value["value"], TBox.bodyType, "$path/body") else null).castUnchecked(),
		(if(id == "width") maybeDeser(value["value"], TBox.widthType, "$path/width") else null).castUnchecked(),
		(if(id == "height") maybeDeser(value["value"], TBox.heightType, "$path/height") else null).castUnchecked(),
		(if(id == "baseline") maybeDeser(value["value"], TBox.baselineType, "$path/baseline") else null).castUnchecked(),
		(if(id == "fill") maybeDeser(value["value"], TBox.fillType, "$path/fill") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TBox.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "radius") maybeDeser(value["value"], TBox.radiusType, "$path/radius") else null).castUnchecked(),
		(if(id == "inset") maybeDeser(value["value"], TBox.insetType, "$path/inset") else null).castUnchecked(),
		(if(id == "outset") maybeDeser(value["value"], TBox.outsetType, "$path/outset") else null).castUnchecked(),
		(if(id == "clip") maybeDeser(value["value"], TBox.clipType, "$path/clip") else null).castUnchecked(),
	)
}

internal fun deserializeColbreak(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("colbreak")
        ?: type.optionOf("content")
	check(option != null) { "`colbreak`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TColbreak (
		maybeDeser(value["weak"], TColbreak.weakType, "$path/weak").castUnchecked(),
	)
}

internal fun deserializeSetColbreak(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("colbreak") ?: type.optionOf("style")
           ?: failWith("`set colbreak`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetColbreak(
		(if(id == "weak") maybeDeser(value["value"], TColbreak.weakType, "$path/weak") else null).castUnchecked(),
	)
}

internal fun deserializeColumns(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("columns")
        ?: type.optionOf("content")
	check(option != null) { "`columns`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TColumns (
		maybeDeser(value["count"], TColumns.countType, "$path/count").castUnchecked(),
		deserialize(value["body"], TColumns.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["gutter"], TColumns.gutterType, "$path/gutter").castUnchecked(),
	)
}

internal fun deserializeSetColumns(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("columns") ?: type.optionOf("style")
           ?: failWith("`set columns`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetColumns(
		(if(id == "count") maybeDeser(value["value"], TColumns.countType, "$path/count") else null).castUnchecked(),
		(if(id == "gutter") maybeDeser(value["value"], TColumns.gutterType, "$path/gutter") else null).castUnchecked(),
	)
}

internal fun deserializeGrid(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("grid")
        ?: type.optionOf("content")
	check(option != null) { "`grid`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TGrid (
		deserialize(value["children"], TGrid.childrenType, "$path/children").castUnchecked(),
		maybeDeser(value["columns"], TGrid.columnsType, "$path/columns").castUnchecked(),
		maybeDeser(value["rows"], TGrid.rowsType, "$path/rows").castUnchecked(),
		maybeDeser(value["gutter"], TGrid.gutterType, "$path/gutter").castUnchecked(),
		maybeDeser(value["column-gutter"], TGrid.columnGutterType, "$path/column-gutter").castUnchecked(),
		maybeDeser(value["row-gutter"], TGrid.rowGutterType, "$path/row-gutter").castUnchecked(),
		maybeDeser(value["fill"], TGrid.fillType, "$path/fill").castUnchecked(),
		maybeDeser(value["align"], TGrid.alignType, "$path/align").castUnchecked(),
		maybeDeser(value["stroke"], TGrid.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["inset"], TGrid.insetType, "$path/inset").castUnchecked(),
	)
}

internal fun deserializeSetGrid(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("grid") ?: type.optionOf("style")
           ?: failWith("`set grid`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetGrid(
		(if(id == "columns") maybeDeser(value["value"], TGrid.columnsType, "$path/columns") else null).castUnchecked(),
		(if(id == "rows") maybeDeser(value["value"], TGrid.rowsType, "$path/rows") else null).castUnchecked(),
		(if(id == "gutter") maybeDeser(value["value"], TGrid.gutterType, "$path/gutter") else null).castUnchecked(),
		(if(id == "column-gutter") maybeDeser(value["value"], TGrid.columnGutterType, "$path/column-gutter") else null).castUnchecked(),
		(if(id == "row-gutter") maybeDeser(value["value"], TGrid.rowGutterType, "$path/row-gutter") else null).castUnchecked(),
		(if(id == "fill") maybeDeser(value["value"], TGrid.fillType, "$path/fill") else null).castUnchecked(),
		(if(id == "align") maybeDeser(value["value"], TGrid.alignType, "$path/align") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TGrid.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "inset") maybeDeser(value["value"], TGrid.insetType, "$path/inset") else null).castUnchecked(),
	)
}

internal fun deserializeGridCell(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("grid.cell")
        ?: type.optionOf("content")
	check(option != null) { "`grid.cell`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TGridCell (
		deserialize(value["body"], TGridCell.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["x"], TGridCell.xType, "$path/x").castUnchecked(),
		maybeDeser(value["y"], TGridCell.yType, "$path/y").castUnchecked(),
		maybeDeser(value["colspan"], TGridCell.colspanType, "$path/colspan").castUnchecked(),
		maybeDeser(value["rowspan"], TGridCell.rowspanType, "$path/rowspan").castUnchecked(),
		maybeDeser(value["fill"], TGridCell.fillType, "$path/fill").castUnchecked(),
		maybeDeser(value["align"], TGridCell.alignType, "$path/align").castUnchecked(),
		maybeDeser(value["inset"], TGridCell.insetType, "$path/inset").castUnchecked(),
		maybeDeser(value["stroke"], TGridCell.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["breakable"], TGridCell.breakableType, "$path/breakable").castUnchecked(),
	)
}

internal fun deserializeSetGridCell(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("grid.cell") ?: type.optionOf("style")
           ?: failWith("`set grid.cell`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetGridCell(
		(if(id == "x") maybeDeser(value["value"], TGridCell.xType, "$path/x") else null).castUnchecked(),
		(if(id == "y") maybeDeser(value["value"], TGridCell.yType, "$path/y") else null).castUnchecked(),
		(if(id == "colspan") maybeDeser(value["value"], TGridCell.colspanType, "$path/colspan") else null).castUnchecked(),
		(if(id == "rowspan") maybeDeser(value["value"], TGridCell.rowspanType, "$path/rowspan") else null).castUnchecked(),
		(if(id == "fill") maybeDeser(value["value"], TGridCell.fillType, "$path/fill") else null).castUnchecked(),
		(if(id == "align") maybeDeser(value["value"], TGridCell.alignType, "$path/align") else null).castUnchecked(),
		(if(id == "inset") maybeDeser(value["value"], TGridCell.insetType, "$path/inset") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TGridCell.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "breakable") maybeDeser(value["value"], TGridCell.breakableType, "$path/breakable") else null).castUnchecked(),
	)
}

internal fun deserializeGridHline(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("grid.hline")
        ?: type.optionOf("content")
	check(option != null) { "`grid.hline`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TGridHline (
		maybeDeser(value["y"], TGridHline.yType, "$path/y").castUnchecked(),
		maybeDeser(value["start"], TGridHline.startType, "$path/start").castUnchecked(),
		maybeDeser(value["end"], TGridHline.endType, "$path/end").castUnchecked(),
		maybeDeser(value["stroke"], TGridHline.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["position"], TGridHline.positionType, "$path/position").castUnchecked(),
	)
}

internal fun deserializeSetGridHline(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("grid.hline") ?: type.optionOf("style")
           ?: failWith("`set grid.hline`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetGridHline(
		(if(id == "y") maybeDeser(value["value"], TGridHline.yType, "$path/y") else null).castUnchecked(),
		(if(id == "start") maybeDeser(value["value"], TGridHline.startType, "$path/start") else null).castUnchecked(),
		(if(id == "end") maybeDeser(value["value"], TGridHline.endType, "$path/end") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TGridHline.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "position") maybeDeser(value["value"], TGridHline.positionType, "$path/position") else null).castUnchecked(),
	)
}

internal fun deserializeGridVline(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("grid.vline")
        ?: type.optionOf("content")
	check(option != null) { "`grid.vline`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TGridVline (
		maybeDeser(value["x"], TGridVline.xType, "$path/x").castUnchecked(),
		maybeDeser(value["start"], TGridVline.startType, "$path/start").castUnchecked(),
		maybeDeser(value["end"], TGridVline.endType, "$path/end").castUnchecked(),
		maybeDeser(value["stroke"], TGridVline.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["position"], TGridVline.positionType, "$path/position").castUnchecked(),
	)
}

internal fun deserializeSetGridVline(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("grid.vline") ?: type.optionOf("style")
           ?: failWith("`set grid.vline`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetGridVline(
		(if(id == "x") maybeDeser(value["value"], TGridVline.xType, "$path/x") else null).castUnchecked(),
		(if(id == "start") maybeDeser(value["value"], TGridVline.startType, "$path/start") else null).castUnchecked(),
		(if(id == "end") maybeDeser(value["value"], TGridVline.endType, "$path/end") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TGridVline.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "position") maybeDeser(value["value"], TGridVline.positionType, "$path/position") else null).castUnchecked(),
	)
}

internal fun deserializeGridHeader(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("grid.header")
        ?: type.optionOf("content")
	check(option != null) { "`grid.header`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TGridHeader (
		deserialize(value["children"], TGridHeader.childrenType, "$path/children").castUnchecked(),
		maybeDeser(value["repeat"], TGridHeader.repeatType, "$path/repeat").castUnchecked(),
	)
}

internal fun deserializeSetGridHeader(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("grid.header") ?: type.optionOf("style")
           ?: failWith("`set grid.header`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetGridHeader(
		(if(id == "repeat") maybeDeser(value["value"], TGridHeader.repeatType, "$path/repeat") else null).castUnchecked(),
	)
}

internal fun deserializeGridFooter(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("grid.footer")
        ?: type.optionOf("content")
	check(option != null) { "`grid.footer`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TGridFooter (
		deserialize(value["children"], TGridFooter.childrenType, "$path/children").castUnchecked(),
		maybeDeser(value["repeat"], TGridFooter.repeatType, "$path/repeat").castUnchecked(),
	)
}

internal fun deserializeSetGridFooter(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("grid.footer") ?: type.optionOf("style")
           ?: failWith("`set grid.footer`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetGridFooter(
		(if(id == "repeat") maybeDeser(value["value"], TGridFooter.repeatType, "$path/repeat") else null).castUnchecked(),
	)
}

internal fun deserializeHide(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("hide")
        ?: type.optionOf("content")
	check(option != null) { "`hide`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return THide (
		deserialize(value["body"], THide.bodyType, "$path/body").castUnchecked(),
	)
}

internal fun deserializeMove(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("move")
        ?: type.optionOf("content")
	check(option != null) { "`move`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMove (
		deserialize(value["body"], TMove.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["dx"], TMove.dxType, "$path/dx").castUnchecked(),
		maybeDeser(value["dy"], TMove.dyType, "$path/dy").castUnchecked(),
	)
}

internal fun deserializeSetMove(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("move") ?: type.optionOf("style")
           ?: failWith("`set move`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMove(
		(if(id == "dx") maybeDeser(value["value"], TMove.dxType, "$path/dx") else null).castUnchecked(),
		(if(id == "dy") maybeDeser(value["value"], TMove.dyType, "$path/dy") else null).castUnchecked(),
	)
}

internal fun deserializePad(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("pad")
        ?: type.optionOf("content")
	check(option != null) { "`pad`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TPad (
		deserialize(value["body"], TPad.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["left"], TPad.leftType, "$path/left").castUnchecked(),
		maybeDeser(value["top"], TPad.topType, "$path/top").castUnchecked(),
		maybeDeser(value["right"], TPad.rightType, "$path/right").castUnchecked(),
		maybeDeser(value["bottom"], TPad.bottomType, "$path/bottom").castUnchecked(),
		maybeDeser(value["x"], TPad.xType, "$path/x").castUnchecked(),
		maybeDeser(value["y"], TPad.yType, "$path/y").castUnchecked(),
		maybeDeser(value["rest"], TPad.restType, "$path/rest").castUnchecked(),
	)
}

internal fun deserializeSetPad(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("pad") ?: type.optionOf("style")
           ?: failWith("`set pad`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetPad(
		(if(id == "left") maybeDeser(value["value"], TPad.leftType, "$path/left") else null).castUnchecked(),
		(if(id == "top") maybeDeser(value["value"], TPad.topType, "$path/top") else null).castUnchecked(),
		(if(id == "right") maybeDeser(value["value"], TPad.rightType, "$path/right") else null).castUnchecked(),
		(if(id == "bottom") maybeDeser(value["value"], TPad.bottomType, "$path/bottom") else null).castUnchecked(),
		(if(id == "x") maybeDeser(value["value"], TPad.xType, "$path/x") else null).castUnchecked(),
		(if(id == "y") maybeDeser(value["value"], TPad.yType, "$path/y") else null).castUnchecked(),
		(if(id == "rest") maybeDeser(value["value"], TPad.restType, "$path/rest") else null).castUnchecked(),
	)
}

internal fun deserializePage(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("page")
        ?: type.optionOf("content")
	check(option != null) { "`page`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TPage (
		deserialize(value["body"], TPage.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["paper"], TPage.paperType, "$path/paper").castUnchecked(),
		maybeDeser(value["width"], TPage.widthType, "$path/width").castUnchecked(),
		maybeDeser(value["height"], TPage.heightType, "$path/height").castUnchecked(),
		maybeDeser(value["flipped"], TPage.flippedType, "$path/flipped").castUnchecked(),
		maybeDeser(value["margin"], TPage.marginType, "$path/margin").castUnchecked(),
		maybeDeser(value["binding"], TPage.bindingType, "$path/binding").castUnchecked(),
		maybeDeser(value["columns"], TPage.columnsType, "$path/columns").castUnchecked(),
		maybeDeser(value["fill"], TPage.fillType, "$path/fill").castUnchecked(),
		maybeDeser(value["numbering"], TPage.numberingType, "$path/numbering").castUnchecked(),
		maybeDeser(value["number-align"], TPage.numberAlignType, "$path/number-align").castUnchecked(),
		maybeDeser(value["header"], TPage.headerType, "$path/header").castUnchecked(),
		maybeDeser(value["header-ascent"], TPage.headerAscentType, "$path/header-ascent").castUnchecked(),
		maybeDeser(value["footer"], TPage.footerType, "$path/footer").castUnchecked(),
		maybeDeser(value["footer-descent"], TPage.footerDescentType, "$path/footer-descent").castUnchecked(),
		maybeDeser(value["background"], TPage.backgroundType, "$path/background").castUnchecked(),
		maybeDeser(value["foreground"], TPage.foregroundType, "$path/foreground").castUnchecked(),
	)
}

internal fun deserializeSetPage(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("page") ?: type.optionOf("style")
           ?: failWith("`set page`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetPage(
		(if(id == "paper") maybeDeser(value["value"], TPage.paperType, "$path/paper") else null).castUnchecked(),
		(if(id == "width") maybeDeser(value["value"], TPage.widthType, "$path/width") else null).castUnchecked(),
		(if(id == "height") maybeDeser(value["value"], TPage.heightType, "$path/height") else null).castUnchecked(),
		(if(id == "flipped") maybeDeser(value["value"], TPage.flippedType, "$path/flipped") else null).castUnchecked(),
		(if(id == "margin") maybeDeser(value["value"], TPage.marginType, "$path/margin") else null).castUnchecked(),
		(if(id == "binding") maybeDeser(value["value"], TPage.bindingType, "$path/binding") else null).castUnchecked(),
		(if(id == "columns") maybeDeser(value["value"], TPage.columnsType, "$path/columns") else null).castUnchecked(),
		(if(id == "fill") maybeDeser(value["value"], TPage.fillType, "$path/fill") else null).castUnchecked(),
		(if(id == "numbering") maybeDeser(value["value"], TPage.numberingType, "$path/numbering") else null).castUnchecked(),
		(if(id == "number-align") maybeDeser(value["value"], TPage.numberAlignType, "$path/number-align") else null).castUnchecked(),
		(if(id == "header") maybeDeser(value["value"], TPage.headerType, "$path/header") else null).castUnchecked(),
		(if(id == "header-ascent") maybeDeser(value["value"], TPage.headerAscentType, "$path/header-ascent") else null).castUnchecked(),
		(if(id == "footer") maybeDeser(value["value"], TPage.footerType, "$path/footer") else null).castUnchecked(),
		(if(id == "footer-descent") maybeDeser(value["value"], TPage.footerDescentType, "$path/footer-descent") else null).castUnchecked(),
		(if(id == "background") maybeDeser(value["value"], TPage.backgroundType, "$path/background") else null).castUnchecked(),
		(if(id == "foreground") maybeDeser(value["value"], TPage.foregroundType, "$path/foreground") else null).castUnchecked(),
	)
}

internal fun deserializePagebreak(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("pagebreak")
        ?: type.optionOf("content")
	check(option != null) { "`pagebreak`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TPagebreak (
		maybeDeser(value["weak"], TPagebreak.weakType, "$path/weak").castUnchecked(),
		maybeDeser(value["to"], TPagebreak.toType, "$path/to").castUnchecked(),
	)
}

internal fun deserializeSetPagebreak(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("pagebreak") ?: type.optionOf("style")
           ?: failWith("`set pagebreak`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetPagebreak(
		(if(id == "weak") maybeDeser(value["value"], TPagebreak.weakType, "$path/weak") else null).castUnchecked(),
		(if(id == "to") maybeDeser(value["value"], TPagebreak.toType, "$path/to") else null).castUnchecked(),
	)
}

internal fun deserializePlace(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("place")
        ?: type.optionOf("content")
	check(option != null) { "`place`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TPlace (
		maybeDeser(value["alignment"], TPlace.alignmentType, "$path/alignment").castUnchecked(),
		deserialize(value["body"], TPlace.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["scope"], TPlace.scopeType, "$path/scope").castUnchecked(),
		maybeDeser(value["float"], TPlace.floatType, "$path/float").castUnchecked(),
		maybeDeser(value["clearance"], TPlace.clearanceType, "$path/clearance").castUnchecked(),
		maybeDeser(value["dx"], TPlace.dxType, "$path/dx").castUnchecked(),
		maybeDeser(value["dy"], TPlace.dyType, "$path/dy").castUnchecked(),
	)
}

internal fun deserializeSetPlace(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("place") ?: type.optionOf("style")
           ?: failWith("`set place`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetPlace(
		(if(id == "alignment") maybeDeser(value["value"], TPlace.alignmentType, "$path/alignment") else null).castUnchecked(),
		(if(id == "scope") maybeDeser(value["value"], TPlace.scopeType, "$path/scope") else null).castUnchecked(),
		(if(id == "float") maybeDeser(value["value"], TPlace.floatType, "$path/float") else null).castUnchecked(),
		(if(id == "clearance") maybeDeser(value["value"], TPlace.clearanceType, "$path/clearance") else null).castUnchecked(),
		(if(id == "dx") maybeDeser(value["value"], TPlace.dxType, "$path/dx") else null).castUnchecked(),
		(if(id == "dy") maybeDeser(value["value"], TPlace.dyType, "$path/dy") else null).castUnchecked(),
	)
}

internal fun deserializePlaceFlush(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("place.flush")
        ?: type.optionOf("content")
	check(option != null) { "`place.flush`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TPlaceFlush (
	)
}

internal fun deserializeRepeat(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("repeat")
        ?: type.optionOf("content")
	check(option != null) { "`repeat`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TRepeat (
		deserialize(value["body"], TRepeat.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["gap"], TRepeat.gapType, "$path/gap").castUnchecked(),
		maybeDeser(value["justify"], TRepeat.justifyType, "$path/justify").castUnchecked(),
	)
}

internal fun deserializeSetRepeat(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("repeat") ?: type.optionOf("style")
           ?: failWith("`set repeat`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetRepeat(
		(if(id == "gap") maybeDeser(value["value"], TRepeat.gapType, "$path/gap") else null).castUnchecked(),
		(if(id == "justify") maybeDeser(value["value"], TRepeat.justifyType, "$path/justify") else null).castUnchecked(),
	)
}

internal fun deserializeRotate(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("rotate")
        ?: type.optionOf("content")
	check(option != null) { "`rotate`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TRotate (
		maybeDeser(value["angle"], TRotate.angleType, "$path/angle").castUnchecked(),
		deserialize(value["body"], TRotate.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["origin"], TRotate.originType, "$path/origin").castUnchecked(),
		maybeDeser(value["reflow"], TRotate.reflowType, "$path/reflow").castUnchecked(),
	)
}

internal fun deserializeSetRotate(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("rotate") ?: type.optionOf("style")
           ?: failWith("`set rotate`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetRotate(
		(if(id == "angle") maybeDeser(value["value"], TRotate.angleType, "$path/angle") else null).castUnchecked(),
		(if(id == "origin") maybeDeser(value["value"], TRotate.originType, "$path/origin") else null).castUnchecked(),
		(if(id == "reflow") maybeDeser(value["value"], TRotate.reflowType, "$path/reflow") else null).castUnchecked(),
	)
}

internal fun deserializeScale(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("scale")
        ?: type.optionOf("content")
	check(option != null) { "`scale`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TScale (
		maybeDeser(value["factor"], TScale.factorType, "$path/factor").castUnchecked(),
		deserialize(value["body"], TScale.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["x"], TScale.xType, "$path/x").castUnchecked(),
		maybeDeser(value["y"], TScale.yType, "$path/y").castUnchecked(),
		maybeDeser(value["origin"], TScale.originType, "$path/origin").castUnchecked(),
		maybeDeser(value["reflow"], TScale.reflowType, "$path/reflow").castUnchecked(),
	)
}

internal fun deserializeSetScale(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("scale") ?: type.optionOf("style")
           ?: failWith("`set scale`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetScale(
		(if(id == "factor") maybeDeser(value["value"], TScale.factorType, "$path/factor") else null).castUnchecked(),
		(if(id == "x") maybeDeser(value["value"], TScale.xType, "$path/x") else null).castUnchecked(),
		(if(id == "y") maybeDeser(value["value"], TScale.yType, "$path/y") else null).castUnchecked(),
		(if(id == "origin") maybeDeser(value["value"], TScale.originType, "$path/origin") else null).castUnchecked(),
		(if(id == "reflow") maybeDeser(value["value"], TScale.reflowType, "$path/reflow") else null).castUnchecked(),
	)
}

internal fun deserializeSkew(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("skew")
        ?: type.optionOf("content")
	check(option != null) { "`skew`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TSkew (
		deserialize(value["body"], TSkew.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["ax"], TSkew.axType, "$path/ax").castUnchecked(),
		maybeDeser(value["ay"], TSkew.ayType, "$path/ay").castUnchecked(),
		maybeDeser(value["origin"], TSkew.originType, "$path/origin").castUnchecked(),
		maybeDeser(value["reflow"], TSkew.reflowType, "$path/reflow").castUnchecked(),
	)
}

internal fun deserializeSetSkew(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("skew") ?: type.optionOf("style")
           ?: failWith("`set skew`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetSkew(
		(if(id == "ax") maybeDeser(value["value"], TSkew.axType, "$path/ax") else null).castUnchecked(),
		(if(id == "ay") maybeDeser(value["value"], TSkew.ayType, "$path/ay") else null).castUnchecked(),
		(if(id == "origin") maybeDeser(value["value"], TSkew.originType, "$path/origin") else null).castUnchecked(),
		(if(id == "reflow") maybeDeser(value["value"], TSkew.reflowType, "$path/reflow") else null).castUnchecked(),
	)
}

internal fun deserializeH(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("h")
        ?: type.optionOf("content")
	check(option != null) { "`h`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TH (
		deserialize(value["amount"], TH.amountType, "$path/amount").castUnchecked(),
		maybeDeser(value["weak"], TH.weakType, "$path/weak").castUnchecked(),
	)
}

internal fun deserializeSetH(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("h") ?: type.optionOf("style")
           ?: failWith("`set h`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetH(
		(if(id == "weak") maybeDeser(value["value"], TH.weakType, "$path/weak") else null).castUnchecked(),
	)
}

internal fun deserializeV(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("v")
        ?: type.optionOf("content")
	check(option != null) { "`v`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TV (
		deserialize(value["amount"], TV.amountType, "$path/amount").castUnchecked(),
		maybeDeser(value["weak"], TV.weakType, "$path/weak").castUnchecked(),
	)
}

internal fun deserializeSetV(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("v") ?: type.optionOf("style")
           ?: failWith("`set v`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetV(
		(if(id == "weak") maybeDeser(value["value"], TV.weakType, "$path/weak") else null).castUnchecked(),
	)
}

internal fun deserializeStack(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("stack")
        ?: type.optionOf("content")
	check(option != null) { "`stack`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TStack (
		deserialize(value["children"], TStack.childrenType, "$path/children").castUnchecked(),
		maybeDeser(value["dir"], TStack.dirType, "$path/dir").castUnchecked(),
		maybeDeser(value["spacing"], TStack.spacingType, "$path/spacing").castUnchecked(),
	)
}

internal fun deserializeSetStack(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("stack") ?: type.optionOf("style")
           ?: failWith("`set stack`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetStack(
		(if(id == "dir") maybeDeser(value["value"], TStack.dirType, "$path/dir") else null).castUnchecked(),
		(if(id == "spacing") maybeDeser(value["value"], TStack.spacingType, "$path/spacing") else null).castUnchecked(),
	)
}

internal fun deserializeCircle(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("circle")
        ?: type.optionOf("content")
	check(option != null) { "`circle`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TCircle (
		maybeDeser(value["body"], TCircle.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["radius"], TCircle.radiusType, "$path/radius").castUnchecked(),
		maybeDeser(value["width"], TCircle.widthType, "$path/width").castUnchecked(),
		maybeDeser(value["height"], TCircle.heightType, "$path/height").castUnchecked(),
		maybeDeser(value["fill"], TCircle.fillType, "$path/fill").castUnchecked(),
		maybeDeser(value["stroke"], TCircle.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["inset"], TCircle.insetType, "$path/inset").castUnchecked(),
		maybeDeser(value["outset"], TCircle.outsetType, "$path/outset").castUnchecked(),
	)
}

internal fun deserializeSetCircle(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("circle") ?: type.optionOf("style")
           ?: failWith("`set circle`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetCircle(
		(if(id == "body") maybeDeser(value["value"], TCircle.bodyType, "$path/body") else null).castUnchecked(),
		(if(id == "radius") maybeDeser(value["value"], TCircle.radiusType, "$path/radius") else null).castUnchecked(),
		(if(id == "width") maybeDeser(value["value"], TCircle.widthType, "$path/width") else null).castUnchecked(),
		(if(id == "height") maybeDeser(value["value"], TCircle.heightType, "$path/height") else null).castUnchecked(),
		(if(id == "fill") maybeDeser(value["value"], TCircle.fillType, "$path/fill") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TCircle.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "inset") maybeDeser(value["value"], TCircle.insetType, "$path/inset") else null).castUnchecked(),
		(if(id == "outset") maybeDeser(value["value"], TCircle.outsetType, "$path/outset") else null).castUnchecked(),
	)
}

internal fun deserializeEllipse(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("ellipse")
        ?: type.optionOf("content")
	check(option != null) { "`ellipse`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TEllipse (
		maybeDeser(value["body"], TEllipse.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["width"], TEllipse.widthType, "$path/width").castUnchecked(),
		maybeDeser(value["height"], TEllipse.heightType, "$path/height").castUnchecked(),
		maybeDeser(value["fill"], TEllipse.fillType, "$path/fill").castUnchecked(),
		maybeDeser(value["stroke"], TEllipse.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["inset"], TEllipse.insetType, "$path/inset").castUnchecked(),
		maybeDeser(value["outset"], TEllipse.outsetType, "$path/outset").castUnchecked(),
	)
}

internal fun deserializeSetEllipse(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("ellipse") ?: type.optionOf("style")
           ?: failWith("`set ellipse`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetEllipse(
		(if(id == "body") maybeDeser(value["value"], TEllipse.bodyType, "$path/body") else null).castUnchecked(),
		(if(id == "width") maybeDeser(value["value"], TEllipse.widthType, "$path/width") else null).castUnchecked(),
		(if(id == "height") maybeDeser(value["value"], TEllipse.heightType, "$path/height") else null).castUnchecked(),
		(if(id == "fill") maybeDeser(value["value"], TEllipse.fillType, "$path/fill") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TEllipse.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "inset") maybeDeser(value["value"], TEllipse.insetType, "$path/inset") else null).castUnchecked(),
		(if(id == "outset") maybeDeser(value["value"], TEllipse.outsetType, "$path/outset") else null).castUnchecked(),
	)
}

internal fun deserializeImage(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("image")
        ?: type.optionOf("content")
	check(option != null) { "`image`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TImage (
		deserialize(value["path"], TImage.pathType, "$path/path").castUnchecked(),
		maybeDeser(value["format"], TImage.formatType, "$path/format").castUnchecked(),
		maybeDeser(value["width"], TImage.widthType, "$path/width").castUnchecked(),
		maybeDeser(value["height"], TImage.heightType, "$path/height").castUnchecked(),
		maybeDeser(value["alt"], TImage.altType, "$path/alt").castUnchecked(),
		maybeDeser(value["fit"], TImage.fitType, "$path/fit").castUnchecked(),
	)
}

internal fun deserializeSetImage(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("image") ?: type.optionOf("style")
           ?: failWith("`set image`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetImage(
		(if(id == "format") maybeDeser(value["value"], TImage.formatType, "$path/format") else null).castUnchecked(),
		(if(id == "width") maybeDeser(value["value"], TImage.widthType, "$path/width") else null).castUnchecked(),
		(if(id == "height") maybeDeser(value["value"], TImage.heightType, "$path/height") else null).castUnchecked(),
		(if(id == "alt") maybeDeser(value["value"], TImage.altType, "$path/alt") else null).castUnchecked(),
		(if(id == "fit") maybeDeser(value["value"], TImage.fitType, "$path/fit") else null).castUnchecked(),
	)
}

internal fun deserializeLine(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("line")
        ?: type.optionOf("content")
	check(option != null) { "`line`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TLine (
		maybeDeser(value["start"], TLine.startType, "$path/start").castUnchecked(),
		maybeDeser(value["end"], TLine.endType, "$path/end").castUnchecked(),
		maybeDeser(value["length"], TLine.lengthType, "$path/length").castUnchecked(),
		maybeDeser(value["angle"], TLine.angleType, "$path/angle").castUnchecked(),
		maybeDeser(value["stroke"], TLine.strokeType, "$path/stroke").castUnchecked(),
	)
}

internal fun deserializeSetLine(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("line") ?: type.optionOf("style")
           ?: failWith("`set line`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetLine(
		(if(id == "start") maybeDeser(value["value"], TLine.startType, "$path/start") else null).castUnchecked(),
		(if(id == "end") maybeDeser(value["value"], TLine.endType, "$path/end") else null).castUnchecked(),
		(if(id == "length") maybeDeser(value["value"], TLine.lengthType, "$path/length") else null).castUnchecked(),
		(if(id == "angle") maybeDeser(value["value"], TLine.angleType, "$path/angle") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TLine.strokeType, "$path/stroke") else null).castUnchecked(),
	)
}

internal fun deserializePath(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("path")
        ?: type.optionOf("content")
	check(option != null) { "`path`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TPath (
		deserialize(value["vertices"], TPath.verticesType, "$path/vertices").castUnchecked(),
		maybeDeser(value["fill"], TPath.fillType, "$path/fill").castUnchecked(),
		maybeDeser(value["fill-rule"], TPath.fillRuleType, "$path/fill-rule").castUnchecked(),
		maybeDeser(value["stroke"], TPath.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["closed"], TPath.closedType, "$path/closed").castUnchecked(),
	)
}

internal fun deserializeSetPath(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("path") ?: type.optionOf("style")
           ?: failWith("`set path`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetPath(
		(if(id == "fill") maybeDeser(value["value"], TPath.fillType, "$path/fill") else null).castUnchecked(),
		(if(id == "fill-rule") maybeDeser(value["value"], TPath.fillRuleType, "$path/fill-rule") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TPath.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "closed") maybeDeser(value["value"], TPath.closedType, "$path/closed") else null).castUnchecked(),
	)
}

internal fun deserializePolygon(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("polygon")
        ?: type.optionOf("content")
	check(option != null) { "`polygon`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TPolygon (
		deserialize(value["vertices"], TPolygon.verticesType, "$path/vertices").castUnchecked(),
		maybeDeser(value["fill"], TPolygon.fillType, "$path/fill").castUnchecked(),
		maybeDeser(value["fill-rule"], TPolygon.fillRuleType, "$path/fill-rule").castUnchecked(),
		maybeDeser(value["stroke"], TPolygon.strokeType, "$path/stroke").castUnchecked(),
	)
}

internal fun deserializeSetPolygon(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("polygon") ?: type.optionOf("style")
           ?: failWith("`set polygon`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetPolygon(
		(if(id == "fill") maybeDeser(value["value"], TPolygon.fillType, "$path/fill") else null).castUnchecked(),
		(if(id == "fill-rule") maybeDeser(value["value"], TPolygon.fillRuleType, "$path/fill-rule") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TPolygon.strokeType, "$path/stroke") else null).castUnchecked(),
	)
}

internal fun deserializeRect(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("rect")
        ?: type.optionOf("content")
	check(option != null) { "`rect`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TRect (
		maybeDeser(value["body"], TRect.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["width"], TRect.widthType, "$path/width").castUnchecked(),
		maybeDeser(value["height"], TRect.heightType, "$path/height").castUnchecked(),
		maybeDeser(value["fill"], TRect.fillType, "$path/fill").castUnchecked(),
		maybeDeser(value["stroke"], TRect.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["radius"], TRect.radiusType, "$path/radius").castUnchecked(),
		maybeDeser(value["inset"], TRect.insetType, "$path/inset").castUnchecked(),
		maybeDeser(value["outset"], TRect.outsetType, "$path/outset").castUnchecked(),
	)
}

internal fun deserializeSetRect(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("rect") ?: type.optionOf("style")
           ?: failWith("`set rect`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetRect(
		(if(id == "body") maybeDeser(value["value"], TRect.bodyType, "$path/body") else null).castUnchecked(),
		(if(id == "width") maybeDeser(value["value"], TRect.widthType, "$path/width") else null).castUnchecked(),
		(if(id == "height") maybeDeser(value["value"], TRect.heightType, "$path/height") else null).castUnchecked(),
		(if(id == "fill") maybeDeser(value["value"], TRect.fillType, "$path/fill") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TRect.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "radius") maybeDeser(value["value"], TRect.radiusType, "$path/radius") else null).castUnchecked(),
		(if(id == "inset") maybeDeser(value["value"], TRect.insetType, "$path/inset") else null).castUnchecked(),
		(if(id == "outset") maybeDeser(value["value"], TRect.outsetType, "$path/outset") else null).castUnchecked(),
	)
}

internal fun deserializeSquare(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("square")
        ?: type.optionOf("content")
	check(option != null) { "`square`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TSquare (
		maybeDeser(value["body"], TSquare.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["size"], TSquare.sizeType, "$path/size").castUnchecked(),
		maybeDeser(value["width"], TSquare.widthType, "$path/width").castUnchecked(),
		maybeDeser(value["height"], TSquare.heightType, "$path/height").castUnchecked(),
		maybeDeser(value["fill"], TSquare.fillType, "$path/fill").castUnchecked(),
		maybeDeser(value["stroke"], TSquare.strokeType, "$path/stroke").castUnchecked(),
		maybeDeser(value["radius"], TSquare.radiusType, "$path/radius").castUnchecked(),
		maybeDeser(value["inset"], TSquare.insetType, "$path/inset").castUnchecked(),
		maybeDeser(value["outset"], TSquare.outsetType, "$path/outset").castUnchecked(),
	)
}

internal fun deserializeSetSquare(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("square") ?: type.optionOf("style")
           ?: failWith("`set square`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetSquare(
		(if(id == "body") maybeDeser(value["value"], TSquare.bodyType, "$path/body") else null).castUnchecked(),
		(if(id == "size") maybeDeser(value["value"], TSquare.sizeType, "$path/size") else null).castUnchecked(),
		(if(id == "width") maybeDeser(value["value"], TSquare.widthType, "$path/width") else null).castUnchecked(),
		(if(id == "height") maybeDeser(value["value"], TSquare.heightType, "$path/height") else null).castUnchecked(),
		(if(id == "fill") maybeDeser(value["value"], TSquare.fillType, "$path/fill") else null).castUnchecked(),
		(if(id == "stroke") maybeDeser(value["value"], TSquare.strokeType, "$path/stroke") else null).castUnchecked(),
		(if(id == "radius") maybeDeser(value["value"], TSquare.radiusType, "$path/radius") else null).castUnchecked(),
		(if(id == "inset") maybeDeser(value["value"], TSquare.insetType, "$path/inset") else null).castUnchecked(),
		(if(id == "outset") maybeDeser(value["value"], TSquare.outsetType, "$path/outset") else null).castUnchecked(),
	)
}

internal fun deserializeMetadata(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("metadata")
        ?: type.optionOf("content")
	check(option != null) { "`metadata`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	val D = option.params?.getOrNull(0) ?: AnyType
	return TMetadata<TValue> (
		deserialize(value["value"], TMetadata.valueType(D), "$path/value").castUnchecked(),
	)
}

internal fun deserializeMathAttach(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.attach")
        ?: type.optionOf("content")
	check(option != null) { "`math.attach`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathAttach (
		deserialize(value["base"], TMathAttach.baseType, "$path/base").castUnchecked(),
		maybeDeser(value["t"], TMathAttach.tType, "$path/t").castUnchecked(),
		maybeDeser(value["b"], TMathAttach.bType, "$path/b").castUnchecked(),
		maybeDeser(value["tl"], TMathAttach.tlType, "$path/tl").castUnchecked(),
		maybeDeser(value["bl"], TMathAttach.blType, "$path/bl").castUnchecked(),
		maybeDeser(value["tr"], TMathAttach.trType, "$path/tr").castUnchecked(),
		maybeDeser(value["br"], TMathAttach.brType, "$path/br").castUnchecked(),
	)
}

internal fun deserializeSetMathAttach(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.attach") ?: type.optionOf("style")
           ?: failWith("`set math.attach`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathAttach(
		(if(id == "t") maybeDeser(value["value"], TMathAttach.tType, "$path/t") else null).castUnchecked(),
		(if(id == "b") maybeDeser(value["value"], TMathAttach.bType, "$path/b") else null).castUnchecked(),
		(if(id == "tl") maybeDeser(value["value"], TMathAttach.tlType, "$path/tl") else null).castUnchecked(),
		(if(id == "bl") maybeDeser(value["value"], TMathAttach.blType, "$path/bl") else null).castUnchecked(),
		(if(id == "tr") maybeDeser(value["value"], TMathAttach.trType, "$path/tr") else null).castUnchecked(),
		(if(id == "br") maybeDeser(value["value"], TMathAttach.brType, "$path/br") else null).castUnchecked(),
	)
}

internal fun deserializeMathScripts(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.scripts")
        ?: type.optionOf("content")
	check(option != null) { "`math.scripts`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathScripts (
		deserialize(value["body"], TMathScripts.bodyType, "$path/body").castUnchecked(),
	)
}

internal fun deserializeMathLimits(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.limits")
        ?: type.optionOf("content")
	check(option != null) { "`math.limits`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathLimits (
		deserialize(value["body"], TMathLimits.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["inline"], TMathLimits.inlineType, "$path/inline").castUnchecked(),
	)
}

internal fun deserializeSetMathLimits(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.limits") ?: type.optionOf("style")
           ?: failWith("`set math.limits`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathLimits(
		(if(id == "inline") maybeDeser(value["value"], TMathLimits.inlineType, "$path/inline") else null).castUnchecked(),
	)
}

internal fun deserializeMathLr(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.lr")
        ?: type.optionOf("content")
	check(option != null) { "`math.lr`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathLr (
		deserialize(value["body"], TMathLr.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["size"], TMathLr.sizeType, "$path/size").castUnchecked(),
	)
}

internal fun deserializeSetMathLr(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.lr") ?: type.optionOf("style")
           ?: failWith("`set math.lr`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathLr(
		(if(id == "size") maybeDeser(value["value"], TMathLr.sizeType, "$path/size") else null).castUnchecked(),
	)
}

internal fun deserializeMathMid(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.mid")
        ?: type.optionOf("content")
	check(option != null) { "`math.mid`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathMid (
		deserialize(value["body"], TMathMid.bodyType, "$path/body").castUnchecked(),
	)
}

internal fun deserializeMathRoot(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.root")
        ?: type.optionOf("content")
	check(option != null) { "`math.root`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathRoot (
		maybeDeser(value["index"], TMathRoot.indexType, "$path/index").castUnchecked(),
		deserialize(value["radicand"], TMathRoot.radicandType, "$path/radicand").castUnchecked(),
	)
}

internal fun deserializeSetMathRoot(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.root") ?: type.optionOf("style")
           ?: failWith("`set math.root`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathRoot(
		(if(id == "index") maybeDeser(value["value"], TMathRoot.indexType, "$path/index") else null).castUnchecked(),
	)
}

internal fun deserializeMathUnderline(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.underline")
        ?: type.optionOf("content")
	check(option != null) { "`math.underline`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathUnderline (
		deserialize(value["body"], TMathUnderline.bodyType, "$path/body").castUnchecked(),
	)
}

internal fun deserializeMathOverline(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.overline")
        ?: type.optionOf("content")
	check(option != null) { "`math.overline`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathOverline (
		deserialize(value["body"], TMathOverline.bodyType, "$path/body").castUnchecked(),
	)
}

internal fun deserializeMathUnderbrace(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.underbrace")
        ?: type.optionOf("content")
	check(option != null) { "`math.underbrace`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathUnderbrace (
		deserialize(value["body"], TMathUnderbrace.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["annotation"], TMathUnderbrace.annotationType, "$path/annotation").castUnchecked(),
	)
}

internal fun deserializeSetMathUnderbrace(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.underbrace") ?: type.optionOf("style")
           ?: failWith("`set math.underbrace`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathUnderbrace(
		(if(id == "annotation") maybeDeser(value["value"], TMathUnderbrace.annotationType, "$path/annotation") else null).castUnchecked(),
	)
}

internal fun deserializeMathOverbrace(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.overbrace")
        ?: type.optionOf("content")
	check(option != null) { "`math.overbrace`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathOverbrace (
		deserialize(value["body"], TMathOverbrace.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["annotation"], TMathOverbrace.annotationType, "$path/annotation").castUnchecked(),
	)
}

internal fun deserializeSetMathOverbrace(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.overbrace") ?: type.optionOf("style")
           ?: failWith("`set math.overbrace`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathOverbrace(
		(if(id == "annotation") maybeDeser(value["value"], TMathOverbrace.annotationType, "$path/annotation") else null).castUnchecked(),
	)
}

internal fun deserializeMathUnderbracket(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.underbracket")
        ?: type.optionOf("content")
	check(option != null) { "`math.underbracket`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathUnderbracket (
		deserialize(value["body"], TMathUnderbracket.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["annotation"], TMathUnderbracket.annotationType, "$path/annotation").castUnchecked(),
	)
}

internal fun deserializeSetMathUnderbracket(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.underbracket") ?: type.optionOf("style")
           ?: failWith("`set math.underbracket`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathUnderbracket(
		(if(id == "annotation") maybeDeser(value["value"], TMathUnderbracket.annotationType, "$path/annotation") else null).castUnchecked(),
	)
}

internal fun deserializeMathOverbracket(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.overbracket")
        ?: type.optionOf("content")
	check(option != null) { "`math.overbracket`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathOverbracket (
		deserialize(value["body"], TMathOverbracket.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["annotation"], TMathOverbracket.annotationType, "$path/annotation").castUnchecked(),
	)
}

internal fun deserializeSetMathOverbracket(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.overbracket") ?: type.optionOf("style")
           ?: failWith("`set math.overbracket`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathOverbracket(
		(if(id == "annotation") maybeDeser(value["value"], TMathOverbracket.annotationType, "$path/annotation") else null).castUnchecked(),
	)
}

internal fun deserializeMathUnderparen(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.underparen")
        ?: type.optionOf("content")
	check(option != null) { "`math.underparen`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathUnderparen (
		deserialize(value["body"], TMathUnderparen.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["annotation"], TMathUnderparen.annotationType, "$path/annotation").castUnchecked(),
	)
}

internal fun deserializeSetMathUnderparen(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.underparen") ?: type.optionOf("style")
           ?: failWith("`set math.underparen`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathUnderparen(
		(if(id == "annotation") maybeDeser(value["value"], TMathUnderparen.annotationType, "$path/annotation") else null).castUnchecked(),
	)
}

internal fun deserializeMathOverparen(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.overparen")
        ?: type.optionOf("content")
	check(option != null) { "`math.overparen`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathOverparen (
		deserialize(value["body"], TMathOverparen.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["annotation"], TMathOverparen.annotationType, "$path/annotation").castUnchecked(),
	)
}

internal fun deserializeSetMathOverparen(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.overparen") ?: type.optionOf("style")
           ?: failWith("`set math.overparen`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathOverparen(
		(if(id == "annotation") maybeDeser(value["value"], TMathOverparen.annotationType, "$path/annotation") else null).castUnchecked(),
	)
}

internal fun deserializeMathUndershell(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.undershell")
        ?: type.optionOf("content")
	check(option != null) { "`math.undershell`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathUndershell (
		deserialize(value["body"], TMathUndershell.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["annotation"], TMathUndershell.annotationType, "$path/annotation").castUnchecked(),
	)
}

internal fun deserializeSetMathUndershell(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.undershell") ?: type.optionOf("style")
           ?: failWith("`set math.undershell`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathUndershell(
		(if(id == "annotation") maybeDeser(value["value"], TMathUndershell.annotationType, "$path/annotation") else null).castUnchecked(),
	)
}

internal fun deserializeMathOvershell(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.overshell")
        ?: type.optionOf("content")
	check(option != null) { "`math.overshell`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathOvershell (
		deserialize(value["body"], TMathOvershell.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["annotation"], TMathOvershell.annotationType, "$path/annotation").castUnchecked(),
	)
}

internal fun deserializeSetMathOvershell(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.overshell") ?: type.optionOf("style")
           ?: failWith("`set math.overshell`, indicated by the type descriptor, is not present in `$type` options (at path $path)" )
    val id = value["id"].cast<JSString>().str
	return TSetMathOvershell(
		(if(id == "annotation") maybeDeser(value["value"], TMathOvershell.annotationType, "$path/annotation") else null).castUnchecked(),
	)
}

internal fun deserializeNone(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("none")
	check(option != null) { "`none`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TNone 
}

internal fun deserializeAuto(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("auto")
	check(option != null) { "`auto`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TAuto 
}

internal fun deserializeArguments(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("arguments")
	check(option != null) { "`arguments`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	val A = option.params?.getOrNull(0) ?: AnyType
	return TArguments<TValue> (
		deserialize(value["positional"], TArguments.positionalType(A), "$path/positional").castUnchecked(),
		deserialize(value["named"], TArguments.namedType(A), "$path/named").castUnchecked(),
	)
}

internal fun deserializeAngle(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("angle")
	check(option != null) { "`angle`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TAngle (
		deserialize(value["deg"], TAngle.degType, "$path/deg").castUnchecked(),
	)
}

internal fun deserializeLength(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("length")
        ?: type.optionOf("relative")
	check(option != null) { "`length`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TLength (
		maybeDeser(value["pt"], TLength.ptType, "$path/pt").castUnchecked(),
		maybeDeser(value["em"], TLength.emType, "$path/em").castUnchecked(),
	)
}

internal fun deserializeRatio(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("ratio")
        ?: type.optionOf("relative")
	check(option != null) { "`ratio`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TRatio (
		deserialize(value["value"], TRatio.valueType, "$path/value").castUnchecked(),
	)
}

internal fun deserializeRelativeImpl(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("relative-impl")
        ?: type.optionOf("relative")
	check(option != null) { "`relative-impl`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TRelativeImpl (
		maybeDeser(value["rel"], TRelativeImpl.relType, "$path/rel").castUnchecked(),
		maybeDeser(value["abs"], TRelativeImpl.absType, "$path/abs").castUnchecked(),
	)
}

internal fun deserializeFraction(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("fraction")
	check(option != null) { "`fraction`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TFraction (
		deserialize(value["value"], TFraction.valueType, "$path/value").castUnchecked(),
	)
}

internal fun deserializeLuma(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("luma")
        ?: type.optionOf("color")
	check(option != null) { "`luma`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TLuma (
		deserialize(value["lightness"], TLuma.lightnessType, "$path/lightness").castUnchecked(),
		maybeDeser(value["alpha"], TLuma.alphaType, "$path/alpha").castUnchecked(),
	)
}

internal fun deserializeOklab(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("oklab")
        ?: type.optionOf("color")
	check(option != null) { "`oklab`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TOklab (
		deserialize(value["lightness"], TOklab.lightnessType, "$path/lightness").castUnchecked(),
		deserialize(value["a"], TOklab.aType, "$path/a").castUnchecked(),
		deserialize(value["b"], TOklab.bType, "$path/b").castUnchecked(),
		maybeDeser(value["alpha"], TOklab.alphaType, "$path/alpha").castUnchecked(),
	)
}

internal fun deserializeOklch(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("oklch")
        ?: type.optionOf("color")
	check(option != null) { "`oklch`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TOklch (
		deserialize(value["lightness"], TOklch.lightnessType, "$path/lightness").castUnchecked(),
		deserialize(value["chroma"], TOklch.chromaType, "$path/chroma").castUnchecked(),
		deserialize(value["hue"], TOklch.hueType, "$path/hue").castUnchecked(),
		maybeDeser(value["alpha"], TOklch.alphaType, "$path/alpha").castUnchecked(),
	)
}

internal fun deserializeLinearRgb(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("color.linear-rgb")
        ?: type.optionOf("color")
	check(option != null) { "`color.linear-rgb`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TLinearRgb (
		deserialize(value["red"], TLinearRgb.redType, "$path/red").castUnchecked(),
		deserialize(value["green"], TLinearRgb.greenType, "$path/green").castUnchecked(),
		deserialize(value["blue"], TLinearRgb.blueType, "$path/blue").castUnchecked(),
		maybeDeser(value["alpha"], TLinearRgb.alphaType, "$path/alpha").castUnchecked(),
	)
}

internal fun deserializeRgb(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("rgb")
        ?: type.optionOf("color")
	check(option != null) { "`rgb`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TRgb (
		deserialize(value["hex"], TRgb.hexType, "$path/hex").castUnchecked(),
	)
}

internal fun deserializeCmyk(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("cmyk")
        ?: type.optionOf("color")
	check(option != null) { "`cmyk`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TCmyk (
		deserialize(value["cyan"], TCmyk.cyanType, "$path/cyan").castUnchecked(),
		deserialize(value["magenta"], TCmyk.magentaType, "$path/magenta").castUnchecked(),
		deserialize(value["yellow"], TCmyk.yellowType, "$path/yellow").castUnchecked(),
		deserialize(value["key"], TCmyk.keyType, "$path/key").castUnchecked(),
	)
}

internal fun deserializeHsl(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("color.hsl")
        ?: type.optionOf("color")
	check(option != null) { "`color.hsl`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return THsl (
		deserialize(value["hue"], THsl.hueType, "$path/hue").castUnchecked(),
		deserialize(value["saturation"], THsl.saturationType, "$path/saturation").castUnchecked(),
		deserialize(value["lightness"], THsl.lightnessType, "$path/lightness").castUnchecked(),
		maybeDeser(value["alpha"], THsl.alphaType, "$path/alpha").castUnchecked(),
	)
}

internal fun deserializeHsv(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("color.hsv")
        ?: type.optionOf("color")
	check(option != null) { "`color.hsv`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return THsv (
		deserialize(value["hue"], THsv.hueType, "$path/hue").castUnchecked(),
		deserialize(value["saturation"], THsv.saturationType, "$path/saturation").castUnchecked(),
		deserialize(value["value"], THsv.valueType, "$path/value").castUnchecked(),
		maybeDeser(value["alpha"], THsv.alphaType, "$path/alpha").castUnchecked(),
	)
}

internal fun deserializeLinearGradient(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("gradient.linear")
        ?: type.optionOf("gradient")
	check(option != null) { "`gradient.linear`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TLinearGradient (
		deserialize(value["stops"], TLinearGradient.stopsType, "$path/stops").castUnchecked(),
		maybeDeser(value["relative"], TLinearGradient.relativeType, "$path/relative").castUnchecked(),
		maybeDeser(value["dir"], TLinearGradient.dirType, "$path/dir").castUnchecked(),
		maybeDeser(value["angle"], TLinearGradient.angleType, "$path/angle").castUnchecked(),
	)
}

internal fun deserializeRadialGradient(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("gradient.radial")
        ?: type.optionOf("gradient")
	check(option != null) { "`gradient.radial`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TRadialGradient (
		deserialize(value["stops"], TRadialGradient.stopsType, "$path/stops").castUnchecked(),
		maybeDeser(value["relative"], TRadialGradient.relativeType, "$path/relative").castUnchecked(),
		maybeDeser(value["center"], TRadialGradient.centerType, "$path/center").castUnchecked(),
		maybeDeser(value["radius"], TRadialGradient.radiusType, "$path/radius").castUnchecked(),
		maybeDeser(value["focal-center"], TRadialGradient.focalCenterType, "$path/focal-center").castUnchecked(),
		maybeDeser(value["focal-radius"], TRadialGradient.focalRadiusType, "$path/focal-radius").castUnchecked(),
	)
}

internal fun deserializeConicGradient(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("gradient.conic")
        ?: type.optionOf("gradient")
	check(option != null) { "`gradient.conic`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TConicGradient (
		deserialize(value["stops"], TConicGradient.stopsType, "$path/stops").castUnchecked(),
		maybeDeser(value["angle"], TConicGradient.angleType, "$path/angle").castUnchecked(),
		maybeDeser(value["relative"], TConicGradient.relativeType, "$path/relative").castUnchecked(),
		maybeDeser(value["center"], TConicGradient.centerType, "$path/center").castUnchecked(),
	)
}

internal fun deserializePattern(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("pattern")
	check(option != null) { "`pattern`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TPattern (
		maybeDeser(value["body"], TPattern.bodyType, "$path/body").castUnchecked(),
		maybeDeser(value["size"], TPattern.sizeType, "$path/size").castUnchecked(),
		maybeDeser(value["spacing"], TPattern.spacingType, "$path/spacing").castUnchecked(),
		maybeDeser(value["relative"], TPattern.relativeType, "$path/relative").castUnchecked(),
	)
}

internal fun deserializeVersion(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("version")
	check(option != null) { "`version`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TVersion (
		deserialize(value["value"], TVersion.valueType, "$path/value").castUnchecked(),
	)
}

internal fun deserializeLabel(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("label")
        ?: type.optionOf("selector")
	check(option != null) { "`label`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TLabel (
		deserialize(value["name"], TLabel.nameType, "$path/name").castUnchecked(),
	)
}

internal fun deserializeDatetime(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("datetime")
	check(option != null) { "`datetime`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TDatetime (
		maybeDeser(value["year"], TDatetime.yearType, "$path/year").castUnchecked(),
		maybeDeser(value["month"], TDatetime.monthType, "$path/month").castUnchecked(),
		maybeDeser(value["day"], TDatetime.dayType, "$path/day").castUnchecked(),
		maybeDeser(value["hour"], TDatetime.hourType, "$path/hour").castUnchecked(),
		maybeDeser(value["minute"], TDatetime.minuteType, "$path/minute").castUnchecked(),
		maybeDeser(value["second"], TDatetime.secondType, "$path/second").castUnchecked(),
	)
}

internal fun deserializeDuration(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("duration")
	check(option != null) { "`duration`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TDuration (
		maybeDeser(value["seconds"], TDuration.secondsType, "$path/seconds").castUnchecked(),
		maybeDeser(value["minutes"], TDuration.minutesType, "$path/minutes").castUnchecked(),
		maybeDeser(value["hours"], TDuration.hoursType, "$path/hours").castUnchecked(),
		maybeDeser(value["days"], TDuration.daysType, "$path/days").castUnchecked(),
		maybeDeser(value["weeks"], TDuration.weeksType, "$path/weeks").castUnchecked(),
	)
}

internal fun deserializePlugin(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("plugin")
	check(option != null) { "`plugin`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TPlugin 
}

internal fun deserializeAlignment(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("alignment")
	check(option != null) { "`alignment`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TAlignment (
		maybeDeser(value["horizontal"], TAlignment.horizontalType, "$path/horizontal").castUnchecked(),
		maybeDeser(value["vertical"], TAlignment.verticalType, "$path/vertical").castUnchecked(),
	)
}

internal fun deserializeDirection(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("direction")
	check(option != null) { "`direction`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TDirection (
		deserialize(value["value"], TDirection.valueType, "$path/value").castUnchecked(),
	)
}

internal fun deserializeRegex(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("regex")
        ?: type.optionOf("selector")
	check(option != null) { "`regex`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TRegex (
		maybeDeser(value["regex"], TRegex.regexType, "$path/regex").castUnchecked(),
	)
}

internal fun deserializeMathAlignPoint(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("math.align-point")
        ?: type.optionOf("content")
	check(option != null) { "`math.align-point`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TMathAlignPoint (
	)
}

internal fun deserializeStroke(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("stroke")
	check(option != null) { "`stroke`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TStroke (
		maybeDeser(value["paint"], TStroke.paintType, "$path/paint").castUnchecked(),
		maybeDeser(value["thickness"], TStroke.thicknessType, "$path/thickness").castUnchecked(),
		maybeDeser(value["cap"], TStroke.capType, "$path/cap").castUnchecked(),
		maybeDeser(value["join"], TStroke.joinType, "$path/join").castUnchecked(),
		maybeDeser(value["dash"], TStroke.dashType, "$path/dash").castUnchecked(),
		maybeDeser(value["miter-limit"], TStroke.miterLimitType, "$path/miter-limit").castUnchecked(),
	)
}

internal fun deserializeElementSelector(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("element-selector")
        ?: type.optionOf("selector")
	check(option != null) { "`element-selector`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TElementSelector (
		deserialize(value["element"], TElementSelector.elementType, "$path/element").castUnchecked(),
		maybeDeser(value["where"], TElementSelector.whereType, "$path/where").castUnchecked(),
	)
}

internal fun deserializeLabelSelector(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("label-selector")
        ?: type.optionOf("selector")
	check(option != null) { "`label-selector`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TLabelSelector (
		deserialize(value["label"], TLabelSelector.labelType, "$path/label").castUnchecked(),
	)
}

internal fun deserializeRegexSelector(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("regex-selector")
        ?: type.optionOf("selector")
	check(option != null) { "`regex-selector`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TRegexSelector (
		deserialize(value["regex"], TRegexSelector.regexType, "$path/regex").castUnchecked(),
	)
}

internal fun deserializeBeforeSelector(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("before-selector")
        ?: type.optionOf("selector")
	check(option != null) { "`before-selector`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TBeforeSelector (
		deserialize(value["selector"], TBeforeSelector.selectorType, "$path/selector").castUnchecked(),
		deserialize(value["end"], TBeforeSelector.endType, "$path/end").castUnchecked(),
		maybeDeser(value["inclusive"], TBeforeSelector.inclusiveType, "$path/inclusive").castUnchecked(),
	)
}

internal fun deserializeAfterSelector(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("after-selector")
        ?: type.optionOf("selector")
	check(option != null) { "`after-selector`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TAfterSelector (
		deserialize(value["selector"], TAfterSelector.selectorType, "$path/selector").castUnchecked(),
		deserialize(value["start"], TAfterSelector.startType, "$path/start").castUnchecked(),
		maybeDeser(value["inclusive"], TAfterSelector.inclusiveType, "$path/inclusive").castUnchecked(),
	)
}

internal fun deserializeAndSelector(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("and-selector")
        ?: type.optionOf("selector")
	check(option != null) { "`and-selector`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TAndSelector (
		deserialize(value["variants"], TAndSelector.variantsType, "$path/variants").castUnchecked(),
	)
}

internal fun deserializeOrSelector(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("or-selector")
        ?: type.optionOf("selector")
	check(option != null) { "`or-selector`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TOrSelector (
		deserialize(value["variants"], TOrSelector.variantsType, "$path/variants").castUnchecked(),
	)
}

internal fun deserializeCounter(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("counter")
	check(option != null) { "`counter`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TCounter (
		deserialize(value["value"], TCounter.valueType, "$path/value").castUnchecked(),
	)
}

internal fun deserializePageCounterKey(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("page-counter-key")
        ?: type.optionOf("counter-key")
	check(option != null) { "`page-counter-key`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TPageCounterKey 
}

internal fun deserializeSelectorCounterKey(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("selector-counter-key")
        ?: type.optionOf("counter-key")
	check(option != null) { "`selector-counter-key`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TSelectorCounterKey (
		deserialize(value["selector"], TSelectorCounterKey.selectorType, "$path/selector").castUnchecked(),
	)
}

internal fun deserializeStrCounterKey(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("str-counter-key")
        ?: type.optionOf("counter-key")
	check(option != null) { "`str-counter-key`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TStrCounterKey (
		deserialize(value["str"], TStrCounterKey.strType, "$path/str").castUnchecked(),
	)
}

internal fun deserializeState(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("state")
	check(option != null) { "`state`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TState (
		deserialize(value["key"], TState.keyType, "$path/key").castUnchecked(),
		maybeDeser(value["init"], TState.initType, "$path/init").castUnchecked(),
	)
}

internal fun deserializeDecimal(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("decimal")
	check(option != null) { "`decimal`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TDecimal (
		maybeDeser(value["value"], TDecimal.valueType, "$path/value").castUnchecked(),
	)
}

internal fun deserializeStyled(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("styled")
        ?: type.optionOf("content")
	check(option != null) { "`styled`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TStyled (
		deserialize(value["styles"], TStyled.stylesType, "$path/styles").castUnchecked(),
		deserialize(value["child"], TStyled.childType, "$path/child").castUnchecked(),
	)
}

internal fun deserializeSequence(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("sequence")
        ?: type.optionOf("content")
	check(option != null) { "`sequence`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TSequence (
		deserialize(value["children"], TSequence.childrenType, "$path/children").castUnchecked(),
	)
}

internal fun deserializeSpace(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("space")
        ?: type.optionOf("content")
	check(option != null) { "`space`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TSpace (
	)
}

internal fun deserializeClosure(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("closure")
        ?: type.optionOf("function")
	check(option != null) { "`closure`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TClosure (
		deserialize(value["node"], TClosure.nodeType, "$path/node").castUnchecked(),
		deserialize(value["defaults"], TClosure.defaultsType, "$path/defaults").castUnchecked(),
		deserialize(value["captured"], TClosure.capturedType, "$path/captured").castUnchecked(),
		deserialize(value["num-pos-params"], TClosure.numPosParamsType, "$path/num-pos-params").castUnchecked(),
	)
}

internal fun deserializeWith(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("with")
        ?: type.optionOf("function")
	check(option != null) { "`with`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TWith (
		deserialize(value["origin"], TWith.originType, "$path/origin").castUnchecked(),
		deserialize(value["args"], TWith.argsType, "$path/args").castUnchecked(),
	)
}

internal fun deserializeNativeFunc(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("native-func")
        ?: type.optionOf("function")
	check(option != null) { "`native-func`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TNativeFunc (
		deserialize(value["name"], TNativeFunc.nameType, "$path/name").castUnchecked(),
	)
}

internal fun deserializeShowRule(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("show-rule")
        ?: type.optionOf("style")
	check(option != null) { "`show-rule`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TShowRule (
		deserialize(value["selector"], TShowRule.selectorType, "$path/selector").castUnchecked(),
		deserialize(value["transform"], TShowRule.transformType, "$path/transform").castUnchecked(),
	)
}

internal fun deserializeContext(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("context")
        ?: type.optionOf("content")
	check(option != null) { "`context`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TContext (
		deserialize(value["func"], TContext.funcType, "$path/func").castUnchecked(),
	)
}

internal fun deserializeStyleDeprecated(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("style-deprecated")
        ?: type.optionOf("content")
	check(option != null) { "`style-deprecated`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TStyleDeprecated (
		deserialize(value["func"], TStyleDeprecated.funcType, "$path/func").castUnchecked(),
	)
}

internal fun deserializeLayout(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("layout")
        ?: type.optionOf("content")
	check(option != null) { "`layout`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TLayout (
		deserialize(value["func"], TLayout.funcType, "$path/func").castUnchecked(),
	)
}

internal fun deserializeLocate(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("locate")
        ?: type.optionOf("content")
	check(option != null) { "`locate`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TLocate (
		deserialize(value["func"], TLocate.funcType, "$path/func").castUnchecked(),
	)
}

internal fun deserializeStateUpdate(value: JSStuff, type: InternalType, path: String) : TValue {
    val option = type.optionOf("state.update")
        ?: type.optionOf("content")
	check(option != null) { "`state.update`, indicated by the type descriptor, is not present in `$type` options (at path $path)" }
	return TStateUpdate (
		deserialize(value["key"], TStateUpdate.keyType, "$path/key").castUnchecked(),
		deserialize(value["update"], TStateUpdate.updateType, "$path/update").castUnchecked(),
	)
}

