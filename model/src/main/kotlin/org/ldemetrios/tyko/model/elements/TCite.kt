package org.ldemetrios.tyko.model


//!https://typst.app/docs/reference/model/cite/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/cite/](https://typst.app/docs/reference/model/cite/)
 * 
 * Cite a work from the bibliography.
 * 
 * Before you starting citing, you need to add a [bibliography](https://typst.app/docs/reference/model/bibliography/) somewhere in your document.
 * 
 * **_Example_**
 * 
 * ```typ
 * This was already noted by
 * pirates long ago. @arrgh
 * 
 * Multiple sources say ...
 * @arrgh @netwok.
 * 
 * You can also call `cite`
 * explicitly. #cite(<arrgh>)
 * 
 * #bibliography("works.bib")
 * ```
 * <img src="https://typst.app/assets/docs/VelsLOKdUATbBc5AK51_FgAAAAAAAAAA.png" alt="Preview" />
 * 
 * If your source name contains certain characters such as slashes, which are not recognized by the `<>` syntax, you can explicitly call `label` instead.
 * 
 * ```typ
 * Computer Modern is an example of a modernist serif typeface.
 * #cite(label("DBLP:books/lib/Knuth86a")).
 * ```
 * 
 * **_Syntax_**
 * 
 * This function indirectly has dedicated syntax. [References](https://typst.app/docs/reference/model/ref/) can be used to cite works from the bibliography. The label then corresponds to the citation key.
 */
@SerialName("cite")
data class TCite(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/cite/](https://typst.app/docs/reference/model/cite/)
     * 
     * The citation key that identifies the entry in the bibliography that shall be cited, as a label.
     * 
     * Required, positional; Typst type: label
     */
    @all:Positional val key: TLabel,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/cite/](https://typst.app/docs/reference/model/cite/)
     * 
     * A supplement for the citation such as page or chapter number.
     * 
     * In reference syntax, the supplement can be added in square brackets:
     * 
     * Settable; Typst type: none|content
     */
    @all:Settable val supplement: Option<TContent>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/cite/](https://typst.app/docs/reference/model/cite/)
     * 
     * The kind of citation to produce. Different forms are useful in different scenarios: A normal citation is useful as a source at the end of a sentence, while a "prose" citation is more suitable for inclusion in the flow of text.
     * 
     * If set to `none`, the cited work is included in the bibliography, but nothing will be displayed.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"normal"` | Display in the standard way for the active style. |
     * | `"prose"` | Produces a citation that is suitable for inclusion in a sentence. |
     * | `"full"` | Mimics a bibliography entry, with full information about the cited work. |
     * | `"author"` | Shows only the cited work's author(s). |
     * | `"year"` | Shows only the cited work's year. |
     * 
     * Settable; Typst type: none|str
     */
    @all:Settable val form: Option<TCiteForm>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/cite/](https://typst.app/docs/reference/model/cite/)
     * 
     * The citation style.
     * 
     * This can be:
     * 
     * - `auto` to automatically use the [bibliography's style](https://typst.app/docs/reference/model/bibliography/#parameters-style) for citations.
     * - A string with the name of one of the built-in styles (see below). Some of the styles listed below appear twice, once with their full name and once with a short alias.
     * - A path string to a [CSL file](https://citationstyles.org/). For more details about paths, see the [Paths section](https://typst.app/docs/reference/syntax/#paths).
     * - Raw bytes from which a CSL style should be decoded.
     * 
     * Settable; Typst type: auto|str|bytes
     */
    @all:Settable val style: Smart<DataSourceOrPreset<TBibliographyStyle>>? = null,
    override val label: TLabel? = null
) : TSelectableContent<TCite>() {
    override fun elem(): TLocatableElement<TCite> = ELEM

    companion object {
        val ELEM = TLocatableElement<TCite>("cite")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TCite]
 */
@SerialName("set-cite")
data class TSetCite(
    override val internals: SetRuleInternals? = null,
    val supplement: Option<TContent>? = null,
    val form: Option<TCiteForm>? = null,
    val style: Smart<DataSourceOrPreset<TBibliographyStyle>>? = null
) : TSetRule()
