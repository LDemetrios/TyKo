package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.TAlternation
import org.ldemetrios.tyko.model.TCollection
import org.ldemetrios.tyko.model.TPaint

//!https://typst.app/docs/reference/text/text/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
 * 
 * Customizes the look and layout of text in a variety of ways.
 * 
 * This function is used frequently, both with set rules and directly. While the set rule is often the simpler choice, calling the `text` function directly can be useful when passing text as an argument to another function.
 * 
 * **_Example_**
 * 
 * ```typ
 * #set text(18pt)
 * With a set rule.
 * 
 * #emph(text(blue)[
 *   With a function call.
 * ])
 * ```
 * <img src="https://typst.app/assets/docs/TE1TKvqGw3ajR6jn3phXugAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("text")
data class TText(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * The text.
     * 
     * Required, positional; Typst type: str
     */
    @all:Positional val text: TStr,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * A font family descriptor or priority list of font family descriptors.
     * 
     * A font family descriptor can be a plain string representing the family name or a dictionary with the following keys:
     * 
     * - `name` (required): The font family name.
     * - `covers` (optional): Defines the Unicode codepoints for which the family shall be used. This can be:
     *   - A predefined coverage set:
     *     - `"latin-in-cjk"` covers all codepoints except for those which exist in Latin fonts, but should preferably be taken from CJK fonts.
     *   - A [regular expression](https://typst.app/docs/reference/foundations/regex/) that defines exactly which codepoints shall be covered. Accepts only the subset of regular expressions which consist of exactly one dot, letter, or character class.
     * 
     * When processing text, Typst tries all specified font families in order until it finds a font that has the necessary glyphs. In the example below, the font `Inria Serif` is preferred, but since it does not contain Arabic glyphs, the arabic text uses `Noto Sans Arabic` instead.
     * 
     * The collection of available fonts differs by platform:
     * 
     * - In the web app, you can see the list of available fonts by clicking on the "Ag" button. You can provide additional fonts by uploading `.ttf` or `.otf` files into your project. They will be discovered automatically. The priority is: project fonts > server fonts.
     * - Locally, Typst uses your installed system fonts or embedded fonts in the CLI, which are `Libertinus Serif`, `New Computer Modern`, `New Computer Modern Math`, and `DejaVu Sans Mono`. In addition, you can use the `--font-path` argument or `TYPST_FONT_PATHS` environment variable to add directories that should be scanned for fonts. The priority is: `--font-paths` > system fonts > embedded fonts. Run `typst fonts` to see the fonts that Typst has discovered on your system. Note that you can pass the `--ignore-system-fonts` parameter to the CLI to ensure Typst won't search for system fonts.
     * 
     * Settable; Typst type: str|array|dictionary
     */
    @all:Settable val font: ArrayOrSingle<TFontDescriptor>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * Whether to allow last resort font fallback when the primary font list contains no match. This lets Typst search through all available fonts for the most similar one that has the necessary glyphs.
     * 
     * *Note:* Currently, there are no warnings when fallback is disabled and no glyphs are found. Instead, your text shows up in the form of "tofus": Small boxes that indicate the lack of an appropriate glyph. In the future, you will be able to instruct Typst to issue warnings so you know something is up.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val fallback: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * The desired font style.
     * 
     * When an italic style is requested and only an oblique one is available, it is used. Similarly, the other way around, an italic style can stand in for an oblique one. When neither an italic nor an oblique style is available, Typst selects the normal style. Since most fonts are only available either in an italic or oblique style, the difference between italic and oblique style is rarely observable.
     * 
     * If you want to emphasize your text, you should do so using the [emph](https://typst.app/docs/reference/model/emph/) function instead. This makes it easy to adapt the style later if you change your mind about how to signify the emphasis.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"normal"` | The default, typically upright style. |
     * | `"italic"` | A cursive style with custom letterform. |
     * | `"oblique"` | Just a slanted version of the normal style. |
     * 
     * Settable; Typst type: str
     */
    @all:Settable val style: TStr? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * The desired thickness of the font's glyphs. Accepts an integer between `100` and `900` or one of the predefined weight names. When the desired weight is not available, Typst selects the font from the family that is closest in weight.
     * 
     * If you want to strongly emphasize your text, you should do so using the [strong](https://typst.app/docs/reference/model/strong/) function instead. This makes it easy to adapt the style later if you change your mind about how to signify the strong emphasis.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"thin"` | Thin weight (100). |
     * | `"extralight"` | Extra light weight (200). |
     * | `"light"` | Light weight (300). |
     * | `"regular"` | Regular weight (400). |
     * | `"medium"` | Medium weight (500). |
     * | `"semibold"` | Semibold weight (600). |
     * | `"bold"` | Bold weight (700). |
     * | `"extrabold"` | Extrabold weight (800). |
     * | `"black"` | Black weight (900). |
     * 
     * Settable; Typst type: int|str
     */
    @all:Settable val weight: TTextWeight? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * The desired width of the glyphs. Accepts a ratio between `50%` and `200%`. When the desired width is not available, Typst selects the font from the family that is closest in stretch. This will only stretch the text if a condensed or expanded version of the font is available.
     * 
     * If you want to adjust the amount of space between characters instead of stretching the glyphs itself, use the [`tracking`](https://typst.app/docs/reference/text/text/#parameters-tracking) property instead.
     * 
     * Settable; Typst type: ratio
     */
    @all:Settable val stretch: TRatio? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * The size of the glyphs. This value forms the basis of the `em` unit: `1em` is equivalent to the font size.
     * 
     * You can also give the font size itself in `em` units. Then, it is relative to the previous font size.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val size: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * The glyph fill paint.
     * 
     * Settable; Typst type: color|gradient|tiling
     */
    @all:Settable val fill: TPaint? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * How to stroke the text.
     * 
     * Settable; Typst type: none|length|color|gradient|stroke|tiling|dictionary
     */
    @all:Settable val stroke: Option<TStroke>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * The amount of space that should be added between characters.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val tracking: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * The amount of space between words.
     * 
     * Can be given as an absolute length, but also relative to the width of the space character in the font.
     * 
     * If you want to adjust the amount of space between characters rather than words, use the [`tracking`](https://typst.app/docs/reference/text/text/#parameters-tracking) property instead.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val spacing: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * Whether to automatically insert spacing between CJK and Latin characters.
     * 
     * Settable; Typst type: none|auto
     */
    @all:Settable val cjkLatinSpacing: Smart<TNone>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * An amount to shift the text baseline by.
     * 
     * Settable; Typst type: length
     */
    @all:Settable val baseline: TLength? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * Whether certain glyphs can hang over into the margin in justified text. This can make justification visually more pleasing.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val overhang: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * The top end of the conceptual frame around the text used for layout and positioning. This affects the size of containers that hold text.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"ascender"` | The font's ascender, which typically exceeds the height of all glyphs. |
     * | `"cap-height"` | The approximate height of uppercase letters. |
     * | `"x-height"` | The approximate height of non-ascending lowercase letters. |
     * | `"baseline"` | The baseline on which the letters rest. |
     * | `"bounds"` | The top edge of the glyph's bounding box. |
     * 
     * Settable; Typst type: length|str
     */
    @all:Settable val topEdge: TopEdge? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * The bottom end of the conceptual frame around the text used for layout and positioning. This affects the size of containers that hold text.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"baseline"` | The baseline on which the letters rest. |
     * | `"descender"` | The font's descender, which typically exceeds the depth of all glyphs. |
     * | `"bounds"` | The bottom edge of the glyph's bounding box. |
     * 
     * Settable; Typst type: length|str
     */
    @all:Settable val bottomEdge: BottomEdge? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * An [ISO 639-1/2/3 language code.](https://en.wikipedia.org/wiki/ISO_639)
     * 
     * Setting the correct language affects various parts of Typst:
     * 
     * - The text processing pipeline can make more informed choices.
     * - Hyphenation will use the correct patterns for the language.
     * - [Smart quotes](https://typst.app/docs/reference/text/smartquote/) turns into the correct quotes for the language.
     * - And all other things which are language-aware.
     * 
     * Choosing the correct language is important for accessibility. For example, screen readers will use it to choose a voice that matches the language of the text. If your document is in another language than English (the default), you should set the text language at the start of your document, before any other content. You can, for example, put it right after the `#set document(/* ... */)` rule that [sets your document's title](https://typst.app/docs/reference/model/document/#parameters-title).
     * 
     * If your document contains passages in a different language than the main language, you should locally change the text language just for those parts, either with a set rule [scoped to a block](https://typst.app/docs/reference/scripting/#blocks) or using a direct text function call such as `#text(lang: "de")[...]`.
     * 
     * If multiple codes are available for your language, you should prefer the two-letter code (ISO 639-1) over the three-letter codes (ISO 639-2/3). When you have to use a three-letter code and your language differs between ISO 639-2 and ISO 639-3, use ISO 639-2 for PDF 1.7 (Typst's default for PDF export) and below and ISO 639-3 for PDF 2.0 and HTML export.
     * 
     * The language code is case-insensitive, and will be lowercased when accessed through [context](https://typst.app/docs/reference/context/).
     * 
     * Settable; Typst type: str
     */
    @all:Settable val lang: TStr? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * An [ISO 3166-1 alpha-2 region code.](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2)
     * 
     * This lets the text processing pipeline make more informed choices.
     * 
     * The region code is case-insensitive, and will be uppercased when accessed through [context](https://typst.app/docs/reference/context/).
     * 
     * Settable; Typst type: none|str
     */
    @all:Settable val region: Option<TStr>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * The OpenType writing script.
     * 
     * The combination of `lang` and `script` determine how font features, such as glyph substitution, are implemented. Frequently the value is a modified (all-lowercase) ISO 15924 script identifier, and the `math` writing script is used for features appropriate for mathematical symbols.
     * 
     * When set to `auto`, the default and recommended setting, an appropriate script is chosen for each block of characters sharing a common Unicode script property.
     * 
     * Settable; Typst type: auto|str
     */
    @all:Settable val script: Smart<TStr>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * The dominant direction for text and inline objects. Possible values are:
     * 
     * - `auto`: Automatically infer the direction from the `lang` property.
     * - `ltr`: Layout text from left to right.
     * - `rtl`: Layout text from right to left.
     * 
     * When writing in right-to-left scripts like Arabic or Hebrew, you should set the [text language](https://typst.app/docs/reference/text/text/#parameters-lang) or direction. While individual runs of text are automatically layouted in the correct direction, setting the dominant direction gives the bidirectional reordering algorithm the necessary information to correctly place punctuation and inline objects. Furthermore, setting the direction affects the alignment values `start` and `end`, which are equivalent to `left` and `right` in `ltr` text and the other way around in `rtl` text.
     * 
     * If you set this to `rtl` and experience bugs or in some way bad looking output, please get in touch with us through the [Forum](https://forum.typst.app/), [Discord server](https://discord.gg/2uDybryKPe), or our [contact form](https://typst.app/contact).
     * 
     * Settable; Typst type: auto|direction
     */
    @all:Settable val dir: Smart<TDirection>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * Whether to hyphenate text to improve line breaking. When `auto`, text will be hyphenated if and only if justification is enabled.
     * 
     * Setting the [text language](https://typst.app/docs/reference/text/text/#parameters-lang) ensures that the correct hyphenation patterns are used.
     * 
     * Settable; Typst type: auto|bool
     */
    @all:Settable val hyphenate: Smart<TBool>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * The "cost" of various choices when laying out text. A higher cost means the layout engine will make the choice less often. Costs are specified as a ratio of the default cost, so `50%` will make text layout twice as eager to make a given choice, while `200%` will make it half as eager.
     * 
     * Currently, the following costs can be customized:
     * 
     * - `hyphenation`: splitting a word across multiple lines
     * - `runt`: ending a paragraph with a line with a single word
     * - `widow`: leaving a single line of paragraph on the next page
     * - `orphan`: leaving single line of paragraph on the previous page
     * 
     * Hyphenation is generally avoided by placing the whole word on the next line, so a higher hyphenation cost can result in awkward justification spacing. Note: Hyphenation costs will only be applied when the [`linebreaks`](https://typst.app/docs/reference/model/par/#parameters-linebreaks) are set to "optimized". (For example by default implied by [`justify`](https://typst.app/docs/reference/model/par/#parameters-justify).)
     * 
     * Runts are avoided by placing more or fewer words on previous lines, so a higher runt cost can result in more awkward in justification spacing.
     * 
     * Text layout prevents widows and orphans by default because they are generally discouraged by style guides. However, in some contexts they are allowed because the prevention method, which moves a line to the next page, can result in an uneven number of lines between pages. The `widow` and `orphan` costs allow disabling these modifications. (Currently, `0%` allows widows/orphans; anything else, including the default of `100%`, prevents them. More nuanced cost specification for these modifications is planned for the future.)
     * 
     * Settable; Typst type: dictionary
     */
    @all:Settable val costs: TDict<TValue>? = null, // TODO <*>
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * Whether to apply kerning.
     * 
     * When enabled, specific letter pairings move closer together or further apart for a more visually pleasing result. The example below demonstrates how decreasing the gap between the "T" and "o" results in a more natural look. Setting this to `false` disables kerning by turning off the OpenType `kern` font feature.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val kerning: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * Whether to apply stylistic alternates.
     * 
     * Sometimes fonts contain alternative glyphs for the same codepoint. Setting this to `true` switches to these by enabling the OpenType `salt` font feature.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val alternates: TAlternation? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * Which stylistic sets to apply. Font designers can categorize alternative glyphs forms into stylistic sets. As this value is highly font-specific, you need to consult your font to know which sets are available.
     * 
     * This can be set to an integer or an array of integers, all of which must be between `1` and `20`, enabling the corresponding OpenType feature(s) from `ss01` to `ss20`. Setting this to `none` will disable all stylistic sets.
     * 
     * Settable; Typst type: none|int|array
     */
    @all:Settable val stylisticSet: Option<ArrayOrSingle<TInt>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * Whether standard ligatures are active.
     * 
     * Certain letter combinations like "fi" are often displayed as a single merged glyph called a *ligature.* Setting this to `false` disables these ligatures by turning off the OpenType `liga` and `clig` font features.
     * 
     * Note that some programming fonts use other OpenType font features to implement "ligatures," including the contextual alternates (`calt`) feature, which is also enabled by default. Use the general [`features`](https://typst.app/docs/reference/text/text/#parameters-features) parameter to control such features.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val ligatures: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * Whether ligatures that should be used sparingly are active. Setting this to `true` enables the OpenType `dlig` font feature.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val discretionaryLigatures: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * Whether historical ligatures are active. Setting this to `true` enables the OpenType `hlig` font feature.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val historicalLigatures: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * Which kind of numbers / figures to select. When set to `auto`, the default numbers for the font are used.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"lining"` | Numbers that fit well with capital text (the OpenType `lnum` font feature). |
     * | `"old-style"` | Numbers that fit well into a flow of upper- and lowercase text (the OpenType `onum` font feature). |
     * 
     * Settable; Typst type: auto|str
     */
    @all:Settable val numberType: Smart<TStr>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * The width of numbers / figures. When set to `auto`, the default numbers for the font are used.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"proportional"` | Numbers with glyph-specific widths (the OpenType `pnum` font feature). |
     * | `"tabular"` | Numbers of equal width (the OpenType `tnum` font feature). |
     * 
     * Settable; Typst type: auto|str
     */
    @all:Settable val numberWidth: Smart<TStr>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * Whether to have a slash through the zero glyph. Setting this to `true` enables the OpenType `zero` font feature.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val slashedZero: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * Whether to turn numbers into fractions. Setting this to `true` enables the OpenType `frac` font feature.
     * 
     * It is not advisable to enable this property globally as it will mess with all appearances of numbers after a slash (e.g., in URLs). Instead, enable it locally when you want a fraction.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val fractions: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * Raw OpenType features to apply.
     * 
     * - If given an array of strings, sets the features identified by the strings to `1`.
     * - If given a dictionary mapping to numbers, sets the features identified by the keys to the values.
     * 
     * Settable; Typst type: array|dictionary
     */
    @all:Settable val features: TCollection<TInt>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/text/](https://typst.app/docs/reference/text/text/)
     * 
     * Content in which all text is styled according to the other arguments.
     * 
     * Typst type: content
     */
    val body: TContent? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("text")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TText]
 */
@SerialName("set-text")
data class TSetText(
    override val internals: SetRuleInternals? = null,
    val font: ArrayOrSingle<TFontDescriptor>? = null,
    val fallback: TBool? = null,
    val style: TStr? = null,
    val weight: TTextWeight? = null,
    val stretch: TRatio? = null,
    val size: TLength? = null,
    val fill: TPaint? = null,
    val stroke: Option<TStroke>? = null,
    val tracking: TLength? = null,
    val spacing: TRelative? = null,
    val cjkLatinSpacing: Smart<TNone>? = null,
    val baseline: TLength? = null,
    val overhang: TBool? = null,
    val topEdge: TopEdge? = null,
    val bottomEdge: BottomEdge? = null,
    val lang: TStr? = null,
    val region: Option<TStr>? = null,
    val script: Smart<TStr>? = null,
    val dir: Smart<TDirection>? = null,
    val hyphenate: Smart<TBool>? = null,
    val costs: TDict<TValue>? = null, // TODO <*>
    val kerning: TBool? = null,
    val alternates: TAlternation? = null,
    val stylisticSet: Option<ArrayOrSingle<TInt>>? = null,
    val ligatures: TBool? = null,
    val discretionaryLigatures: TBool? = null,
    val historicalLigatures: TBool? = null,
    val numberType: Smart<TStr>? = null,
    val numberWidth: Smart<TStr>? = null,
    val slashedZero: TBool? = null,
    val fractions: TBool? = null,
    val features: TCollection<TInt>? = null
) : TSetRule()
