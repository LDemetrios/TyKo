package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable

@SerialName("str")
enum class TBibliographyStyle(val value: String) : DataSourceOrPreset<TBibliographyStyle>, IntoStr, Smart<TBibliographyStyle> {
    ALPHANUMERIC("alphanumeric"),
    AMERICAN_ANTHROPOLOGICAL_ASSOCIATION("american-anthropological-association"),
    AMERICAN_CHEMICAL_SOCIETY("american-chemical-society"),
    AMERICAN_GEOPHYSICAL_UNION("american-geophysical-union"),
    AMERICAN_INSTITUTE_OF_AERONAUTICS_AND_ASTRONAUTICS("american-institute-of-aeronautics-and-astronautics"),
    AMERICAN_INSTITUTE_OF_PHYSICS("american-institute-of-physics"),
    AMERICAN_MEDICAL_ASSOCIATION("american-medical-association"),
    AMERICAN_METEOROLOGICAL_SOCIETY("american-meteorological-society"),
    AMERICAN_PHYSICS_SOCIETY("american-physics-society"),
    AMERICAN_PHYSIOLOGICAL_SOCIETY("american-physiological-society"),
    AMERICAN_POLITICAL_SCIENCE_ASSOCIATION("american-political-science-association"),
    AMERICAN_PSYCHOLOGICAL_ASSOCIATION("american-psychological-association"),
    APA("apa"),
    AMERICAN_SOCIETY_FOR_MICROBIOLOGY("american-society-for-microbiology"),
    AMERICAN_SOCIETY_OF_CIVIL_ENGINEERS("american-society-of-civil-engineers"),
    AMERICAN_SOCIETY_OF_MECHANICAL_ENGINEERS("american-society-of-mechanical-engineers"),
    AMERICAN_SOCIOLOGICAL_ASSOCIATION("american-sociological-association"),
    ANGEWANDTE_CHEMIE("angewandte-chemie"),
    ANNUAL_REVIEWS("annual-reviews"),
    ANNUAL_REVIEWS_AUTHOR_DATE("annual-reviews-author-date"),
    ASSOCIACAO_BRASILEIRA_DE_NORMAS_TECNICAS("associacao-brasileira-de-normas-tecnicas"),
    ASSOCIATION_FOR_COMPUTING_MACHINERY("association-for-computing-machinery"),
    BIOMED_CENTRAL("biomed-central"),
    BRISTOL_UNIVERSITY_PRESS("bristol-university-press"),
    BRITISH_MEDICAL_JOURNAL("british-medical-journal"),
    BMJ("bmj"),
    CELL("cell"),
    CHICAGO_AUTHOR_DATE("chicago-author-date"),
    CHICAGO_NOTES("chicago-notes"),
    CHICAGO_FULLNOTES("chicago-fullnotes"),
    CHICAGO_SHORTENED_NOTES("chicago-shortened-notes"),
    COPERNICUS("copernicus"),
    COUNCIL_OF_SCIENCE_EDITORS("council-of-science-editors"),
    COUNCIL_OF_SCIENCE_EDITORS_AUTHOR_DATE("council-of-science-editors-author-date"),
    CURRENT_OPINION("current-opinion"),
    DEUTSCHE_GESELLSCHAFT_FÜR_PSYCHOLOGIE("deutsche-gesellschaft-für-psychologie"),
    DEUTSCHE_SPRACHE("deutsche-sprache"),
    ELSEVIER_HARVARD("elsevier-harvard"),
    ELSEVIER_VANCOUVER("elsevier-vancouver"),
    ELSEVIER_WITH_TITLES("elsevier-with-titles"),
    FRONTIERS("frontiers"),
    FUTURE_MEDICINE("future-medicine"),
    FUTURE_SCIENCE("future-science"),
    GB_7714_2005_NUMERIC("gb-7714-2005-numeric"),
    GB_7714_2015_AUTHOR_DATE("gb-7714-2015-author-date"),
    GB_7714_2015_NOTE("gb-7714-2015-note"),
    GB_7714_2015_NUMERIC("gb-7714-2015-numeric"),
    GOST_R_705_2008_NUMERIC("gost-r-705-2008-numeric"),
    HARVARD_CITE_THEM_RIGHT("harvard-cite-them-right"),
    INSTITUTE_OF_ELECTRICAL_AND_ELECTRONICS_ENGINEERS("institute-of-electrical-and-electronics-engineers"),
    IEEE("ieee"),
    INSTITUTE_OF_PHYSICS_NUMERIC("institute-of-physics-numeric"),
    ISO_690_AUTHOR_DATE("iso-690-author-date"),
    ISO_690_NUMERIC("iso-690-numeric"),
    KARGER("karger"),
    MARY_ANN_LIEBERT_VANCOUVER("mary-ann-liebert-vancouver"),
    MODERN_HUMANITIES_RESEARCH_ASSOCIATION_NOTES("modern-humanities-research-association-notes"),
    MODERN_HUMANITIES_RESEARCH_ASSOCIATION("modern-humanities-research-association"),
    MODERN_LANGUAGE_ASSOCIATION("modern-language-association"),
    MLA("mla"),
    MODERN_LANGUAGE_ASSOCIATION_8("modern-language-association-8"),
    MLA_8("mla-8"),
    MULTIDISCIPLINARY_DIGITAL_PUBLISHING_INSTITUTE("multidisciplinary-digital-publishing-institute"),
    NATURE("nature"),
    PENSOFT("pensoft"),
    PUBLIC_LIBRARY_OF_SCIENCE("public-library-of-science"),
    PLOS("plos"),
    ROYAL_SOCIETY_OF_CHEMISTRY("royal-society-of-chemistry"),
    SAGE_VANCOUVER("sage-vancouver"),
    SIST02("sist02"),
    SPIE("spie"),
    SPRINGER_BASIC("springer-basic"),
    SPRINGER_BASIC_AUTHOR_DATE("springer-basic-author-date"),
    SPRINGER_FACHZEITSCHRIFTEN_MEDIZIN_PSYCHOLOGIE("springer-fachzeitschriften-medizin-psychologie"),
    SPRINGER_HUMANITIES_AUTHOR_DATE("springer-humanities-author-date"),
    SPRINGER_LECTURE_NOTES_IN_COMPUTER_SCIENCE("springer-lecture-notes-in-computer-science"),
    SPRINGER_MATHPHYS("springer-mathphys"),
    SPRINGER_SOCPSYCH_AUTHOR_DATE("springer-socpsych-author-date"),
    SPRINGER_VANCOUVER("springer-vancouver"),
    TAYLOR_AND_FRANCIS_CHICAGO_AUTHOR_DATE("taylor-and-francis-chicago-author-date"),
    TAYLOR_AND_FRANCIS_NATIONAL_LIBRARY_OF_MEDICINE("taylor-and-francis-national-library-of-medicine"),
    THE_INSTITUTION_OF_ENGINEERING_AND_TECHNOLOGY("the-institution-of-engineering-and-technology"),
    THE_LANCET("the-lancet"),
    THIEME("thieme"),
    TRENDS("trends"),
    TURABIAN_AUTHOR_DATE("turabian-author-date"),
    TURABIAN_FULLNOTE_8("turabian-fullnote-8"),
    VANCOUVER("vancouver"),
    VANCOUVER_SUPERSCRIPT("vancouver-superscript");

    override fun intoValue(): TStr {
        return value.t
    }

    companion object {
        private val valuesByStr by lazy { entries.associateBy { it.value } }
        fun fromValue(value: TValue) = if(value is TStr) valuesByStr[value.value]!! else throw AssertionError("Can't convert from $value")
    }
}

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
@SerialName("set-bibliography")
data class TSetBibliography(
    override val internals: SetRuleInternals? = null,
    val title: Smart<Option<TContent>>? = null,
    val full: TBool? = null,
    val style: DataSourceOrPreset<TBibliographyStyle>? = null
) : TSetRule()
