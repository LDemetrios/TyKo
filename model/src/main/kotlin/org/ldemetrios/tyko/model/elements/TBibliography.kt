package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.DataSource


//!https://typst.app/docs/reference/model/bibliography/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/model/bibliography/](https://typst.app/docs/reference/model/bibliography/)
 * 
 * A bibliography / reference listing.
 * 
 * You can create a new bibliography by calling this function with a path to a bibliography file in either one of two formats:
 * 
 * - A Hayagriva `.yaml`/`.yml` file. Hayagriva is a new bibliography file format designed for use with Typst. Visit its [documentation](https://github.com/typst/hayagriva/blob/main/docs/file-format.md) for more details.
 * - A BibLaTeX `.bib` file.
 * 
 * As soon as you add a bibliography somewhere in your document, you can start citing things with reference syntax (`@key`) or explicit calls to the [citation](https://typst.app/docs/reference/model/cite/) function (`#cite(<key>)`). The bibliography will only show entries for works that were referenced in the document.
 * 
 * **_Styles_**
 * 
 * Typst offers a wide selection of built-in [citation and bibliography styles](https://typst.app/docs/reference/model/bibliography/#parameters-style). Beyond those, you can add and use custom [CSL](https://citationstyles.org/) (Citation Style Language) files. Wondering which style to use? Here are some good defaults based on what discipline you're working in:
 * 
 * | Fields | Typical Styles |
 * | --- | --- |
 * | Engineering, IT | `"ieee"` |
 * | Psychology, Life Sciences | `"apa"` |
 * | Social sciences | `"chicago-author-date"` |
 * | Humanities | `"mla"`, `"chicago-notes"`, `"harvard-cite-them-right"` |
 * | Economics | `"harvard-cite-them-right"` |
 * | Physics | `"american-physics-society"` |
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
 * #bibliography("works.bib")
 * ```
 * <img src="https://typst.app/assets/docs/IJ3xnmEzh6yEddeM44ev3wAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("bibliography")
data class TBibliography(
    // field is not variadic -- it's not variadic for repr, but param is -- for overload generator,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/bibliography/](https://typst.app/docs/reference/model/bibliography/)
     * 
     * One or multiple paths to or raw bytes for Hayagriva `.yaml` and/or BibLaTeX `.bib` files.
     * 
     * This can be a:
     * 
     * - A path string to load a bibliography file from the given path. For more details about paths, see the [Paths section](https://typst.app/docs/reference/syntax/#paths).
     * - Raw bytes from which the bibliography should be decoded.
     * - An array where each item is one of the above.
     * 
     * Required, positional; Typst type: str|bytes|array
     */
    @all:Positional @param:Variadic val sources: TArray<DataSource>,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/bibliography/](https://typst.app/docs/reference/model/bibliography/)
     * 
     * The title of the bibliography.
     * 
     * - When set to `auto`, an appropriate title for the [text language](https://typst.app/docs/reference/text/text/#parameters-lang) will be used. This is the default.
     * - When set to `none`, the bibliography will not have a title.
     * - A custom title can be set by passing content.
     * 
     * The bibliography's heading will not be numbered by default, but you can force it to be with a show-set rule: `show bibliography: set heading(numbering: "1.")`
     * 
     * Settable; Typst type: none|auto|content
     */
    @all:Settable val title: Smart<Option<TContent>>? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/bibliography/](https://typst.app/docs/reference/model/bibliography/)
     * 
     * Whether to include all works from the given bibliography files, even those that weren't cited in the document.
     * 
     * To selectively add individual cited works without showing them, you can also use the `cite` function with [`form`](https://typst.app/docs/reference/model/cite/#parameters-form) set to `none`.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val full: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/model/bibliography/](https://typst.app/docs/reference/model/bibliography/)
     * 
     * The bibliography style.
     * 
     * This can be:
     * 
     * - A string with the name of one of the built-in styles (see below). Some of the styles listed below appear twice, once with their full name and once with a short alias.
     * - A path string to a [CSL file](https://citationstyles.org/). For more details about paths, see the [Paths section](https://typst.app/docs/reference/syntax/#paths).
     * - Raw bytes from which a CSL style should be decoded.
     * 
     * Settable; Typst type: str|bytes
     */
    @all:Settable val style: DataSourceOrPreset<TBibliographyStyle>? = null,
    override val label: TLabel? = null
) : TSelectableContent<TBibliography>() {
    override fun elem(): TLocatableElement<TBibliography> = ELEM
    companion object {
        val ELEM = TLocatableElement<TBibliography>("bibliography")
    }
}
/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TBibliography]
 */
@SerialName("set-bibliography")
data class TSetBibliography(
    override val internals: SetRuleInternals? = null,
    val title: Smart<Option<TContent>>? = null,
    val full: TBool? = null,
    val style: DataSourceOrPreset<TBibliographyStyle>? = null
) : TSetRule()
