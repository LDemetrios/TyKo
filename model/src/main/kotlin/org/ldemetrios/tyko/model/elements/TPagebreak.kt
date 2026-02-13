package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/layout/pagebreak/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/layout/pagebreak/](https://typst.app/docs/reference/layout/pagebreak/)
 * 
 * A manual page break.
 * 
 * Must not be used inside any containers.
 * 
 * **_Example_**
 * 
 * ```typ
 * The next page contains
 * more details on compound theory.
 * #pagebreak()
 * 
 * == Compound Theory
 * In 1984, the first ...
 * ```
 * <img src="https://typst.app/assets/docs/MJju6am_GVBgtJWStEY3AwAAAAAAAAAA.png" alt="Preview" /><img src="https://typst.app/assets/docs/MJju6am_GVBgtJWStEY3AwAAAAAAAAAB.png" alt="Preview" />
 * 
 * Even without manual page breaks, content will be automatically paginated based on the configured page size. You can set [the page height](https://typst.app/docs/reference/layout/page/#parameters-height) to `auto` to let the page grow dynamically until a manual page break occurs.
 * 
 * Pagination tries to avoid single lines of text at the top or bottom of a page (these are called *widows* and *orphans*). You can adjust the [`text.costs`](https://typst.app/docs/reference/text/text/#parameters-costs) parameter to disable this behavior.
 */
@SerialName("pagebreak")
data class TPagebreak(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/pagebreak/](https://typst.app/docs/reference/layout/pagebreak/)
     * 
     * If `true`, the page break is skipped if the current page is already empty.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val weak: TBool? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/layout/pagebreak/](https://typst.app/docs/reference/layout/pagebreak/)
     * 
     * If given, ensures that the next page will be an even/odd page, with an empty page in between if necessary.
     * 
     * | Variant | Details |
     * | --- | --- |
     * | `"even"` | Next page will be an even page. |
     * | `"odd"` | Next page will be an odd page. |
     * 
     * Settable; Typst type: none|str
     */
    @all:Settable val to: Option<TStr>? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("pagebreak")
    }
}


@SerialName("set-pagebreak")
data class TSetPagebreak(
    override val internals: SetRuleInternals? = null,
    val weak: TBool? = null,
    val to: Option<TStr>? = null
) : TSetRule()
