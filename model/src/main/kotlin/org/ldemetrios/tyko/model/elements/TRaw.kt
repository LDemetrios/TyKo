package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.DataSource


//!https://typst.app/docs/reference/text/raw/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/text/raw/](https://typst.app/docs/reference/text/raw/)
 * 
 * Raw text with optional syntax highlighting.
 * 
 * Displays the text verbatim and in a monospace font. This is typically used to embed computer code into your document.
 * 
 * Note that text given to this element cannot contain arbitrary formatting, such as `*strong*` or `_emphasis_`, as it is displayed verbatim. If you'd like to display any kind of content with a monospace font, instead of using [`raw`](https://typst.app/docs/reference/text/raw/), you should change its font to a monospace font using the [`text`](https://typst.app/docs/reference/text/text/) function.
 * 
 * **_Example_**
 * 
 * ```typ
 * Adding `rbx` to `rcx` gives
 * the desired result.
 * 
 * What is ```rust fn main()``` in Rust
 * would be ```c int main()``` in C.
 * 
 * ```rust
 * fn main() {
 *     println!("Hello World!");
 * }
 * ```
 * 
 * This has ``` `backticks` ``` in it
 * (but the spaces are trimmed). And
 * ``` here``` the leading space is
 * also trimmed.
 * ```
 * <img src="https://typst.app/assets/docs/HG5qpETGRO7ndBI1Qrek9gAAAAAAAAAA.png" alt="Preview" />
 * 
 * You can also construct a [`raw`](https://typst.app/docs/reference/text/raw/) element programmatically from a string (and provide the language tag via the optional [`lang`](https://typst.app/docs/reference/text/raw/#parameters-lang) argument).
 * 
 * ```typ
 * #raw("fn " + "main() {}", lang: "rust")
 * ```
 * <img src="https://typst.app/assets/docs/MNABiMKxTxPPaXzIwfuPPQAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Syntax_**
 * 
 * This function also has dedicated syntax. You can enclose text in 1 or 3+ backticks (```) to make it raw. Two backticks produce empty raw text. This works both in markup and code.
 * 
 * When you use three or more backticks, you can additionally specify a language tag for syntax highlighting directly after the opening backticks. Within raw blocks, everything (except for the language tag, if applicable) is rendered as is, in particular, there are no escape sequences.
 * 
 * The language tag is an identifier that directly follows the opening backticks only if there are three or more backticks. If your text starts with something that looks like an identifier, but no syntax highlighting is needed, start the text with a single space (which will be trimmed) or use the single backtick syntax. If your text should start or end with a backtick, put a space before or after it (it will be trimmed).
 * 
 * If no syntax highlighting is available by default for your specified language tag (or if you want to override the built-in definition), you may provide a custom syntax specification file to the [`syntaxes`](https://typst.app/docs/reference/text/raw/#parameters-syntaxes) field.
 * 
 * **_Styling_**
 * 
 * By default, the `raw` element uses the `DejaVu Sans Mono` font (included with Typst), with a smaller font size of `0.8em` (that is, 80% of the global font size). This is because monospace fonts tend to be visually larger than non-monospace fonts.
 * 
 * You can customize these properties with show-set rules:
 * 
 * ```typ
 * // Switch to Cascadia Code for both
 * // inline and block raw.
 * #show raw: set text(font: "Cascadia Code")
 * 
 * // Reset raw blocks to the same size as normal text,
 * // but keep inline raw at the reduced size.
 * #show raw.where(block: true): set text(1em / 0.8)
 * 
 * Now using the `Cascadia Code` font for raw text.
 * Here's some Python code. It looks larger now:
 * 
 * ```py
 * def python():
 *   return 5 + 5
 * ```
 * ```
 * <img src="https://typst.app/assets/docs/HLQOx_K2s_h6sA-OgbggOAAAAAAAAAAA.png" alt="Preview" />
 * 
 * In addition, you can customize the syntax highlighting colors by setting a custom theme through the [`theme`](https://typst.app/docs/reference/text/raw/#parameters-theme) field.
 * 
 * For complete customization of the appearance of a raw block, a show rule on [`raw.line`](https://typst.app/docs/reference/text/raw/#definitions-line) could be helpful, such as to add line numbers.
 * 
 * Note that, in raw text, typesetting features like [hyphenation](https://typst.app/docs/reference/text/text/#parameters-hyphenate), [overhang](https://typst.app/docs/reference/text/text/#parameters-overhang), [CJK-Latin spacing](https://typst.app/docs/reference/text/text/#parameters-cjk-latin-spacing) (and [justification](https://typst.app/docs/reference/model/par/#parameters-justify) for [raw blocks](https://typst.app/docs/reference/text/raw/#parameters-block)) will be disabled by default.
 */
@SerialName("raw")
data class TRaw(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/raw/](https://typst.app/docs/reference/text/raw/)
     * 
     * The raw text.
     * 
     * You can also use raw blocks creatively to create custom syntaxes for your automations.
     * 
     * Required, positional; Typst type: str
     */
    @all:Positional val text: TStr,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/raw/](https://typst.app/docs/reference/text/raw/)
     * 
     * Whether the raw text is displayed as a separate block.
     * 
     * In markup mode, using one-backtick notation makes this `false`. Using three-backtick notation makes it `true` if the enclosed content contains at least one line break.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val block: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/raw/](https://typst.app/docs/reference/text/raw/)
     * 
     * The language to syntax-highlight in.
     * 
     * Apart from typical language tags known from Markdown, this supports the `"typ"`, `"typc"`, and `"typm"` tags for [Typst markup](https://typst.app/docs/reference/syntax/#markup), [Typst code](https://typst.app/docs/reference/syntax/#code), and [Typst math](https://typst.app/docs/reference/syntax/#math), respectively.
     * 
     * Settable; Typst type: none|str
     */
    @all:Settable val lang: Option<TStr>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/raw/](https://typst.app/docs/reference/text/raw/)
     * 
     * The horizontal alignment that each line in a raw block should have. This option is ignored if this is not a raw block (if specified `block: false` or single backticks were used in markup mode).
     * 
     * By default, this is set to `start`, meaning that raw text is aligned towards the start of the text direction inside the block by default, regardless of the current context's alignment (allowing you to center the raw block itself without centering the text inside it, for example).
     * 
     * Settable; Typst type: alignment
     */
    @all:Settable val align: TAlignment? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/raw/](https://typst.app/docs/reference/text/raw/)
     * 
     * Additional syntax definitions to load. The syntax definitions should be in the [`sublime-syntax` file format](https://www.sublimetext.com/docs/syntax.html).
     * 
     * You can pass any of the following values:
     * 
     * - A path string to load a syntax file from the given path. For more details about paths, see the [Paths section](https://typst.app/docs/reference/syntax/#paths).
     * - Raw bytes from which the syntax should be decoded.
     * - An array where each item is one of the above.
     * 
     * Settable; Typst type: str|bytes|array
     */
    @all:Settable val syntaxes: ArrayOrSingle<DataSource>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/raw/](https://typst.app/docs/reference/text/raw/)
     * 
     * The theme to use for syntax highlighting. Themes should be in the [`tmTheme` file format](https://www.sublimetext.com/docs/color_schemes_tmtheme.html).
     * 
     * You can pass any of the following values:
     * 
     * - `none`: Disables syntax highlighting.
     * - `auto`: Highlights with Typst's default theme.
     * - A path string to load a theme file from the given path. For more details about paths, see the [Paths section](https://typst.app/docs/reference/syntax/#paths).
     * - Raw bytes from which the theme should be decoded.
     * 
     * Applying a theme only affects the color of specifically highlighted text. It does not consider the theme's foreground and background properties, so that you retain control over the color of raw text. You can apply the foreground color yourself with the [`text`](https://typst.app/docs/reference/text/text/) function and the background with a [filled block](https://typst.app/docs/reference/layout/block/#parameters-fill). You could also use the [`xml`](https://typst.app/docs/reference/data-loading/xml/) function to extract these properties from the theme.
     * 
     * Settable; Typst type: none|auto|str|bytes
     */
    @all:Settable val theme: Smart<Option<DataSource>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/text/raw/](https://typst.app/docs/reference/text/raw/)
     * 
     * The size for a tab stop in spaces. A tab is replaced with enough spaces to align with the next multiple of the size.
     * 
     * Settable; Typst type: int
     */
    @all:Settable val tabSize: TInt? = null,
    override val label: TLabel? = null
) : TSelectableContent<TRaw>() {
    override fun elem(): TLocatableElement<TRaw> = ELEM

    companion object {
        val ELEM = TLocatableElement<TRaw>("raw")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TRaw]
 */
@SerialName("set-raw")
data class TSetRaw(
    override val internals: SetRuleInternals? = null,
    val block: TBool? = null,
    val lang: Option<TStr>? = null,
    val align: TAlignment? = null,
    val syntaxes: ArrayOrSingle<DataSource>? = null,
    val theme: Smart<Option<DataSource>>? = null,
    val tabSize: TInt? = null
) : TSetRule()
