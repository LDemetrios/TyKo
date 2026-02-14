package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.TAttachment


//!https://typst.app/docs/reference/math/accent/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/math/accent/](https://typst.app/docs/reference/math/accent/)
 * 
 * Attaches an accent to a base.
 * 
 * **_Example_**
 * 
 * ```typ
 * $grave(a) = accent(a, `)$ \
 * $arrow(a) = accent(a, arrow)$ \
 * $tilde(a) = accent(a, \u{0303})$
 * ```
 * <img src="https://typst.app/assets/docs/wdLZED2cvtXKAU75vKtAKwAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("math.accent")
data class TMathAccent(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/accent/](https://typst.app/docs/reference/math/accent/)
     * 
     * The base to which the accent is applied. May consist of multiple letters.
     * 
     * Required, positional; Typst type: content
     */
    @all:Positional val base: TContent,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/accent/](https://typst.app/docs/reference/math/accent/)
     * 
     * The accent to apply to the base.
     * 
     * Supported accents include:
     * 
     * | Accent | Name | Codepoint |
     * | --- | --- | --- |
     * | Grave | `grave` | ``` |
     * | Acute | `acute` | `´` |
     * | Circumflex | `hat` | `^` |
     * | Tilde | `tilde` | `~` |
     * | Macron | `macron` | `¯` |
     * | Dash | `dash` | `‾` |
     * | Breve | `breve` | `˘` |
     * | Dot | `dot` | `.` |
     * | Double dot, Diaeresis | `dot.double`, `diaer` | `¨` |
     * | Triple dot | `dot.triple` | `⃛` |
     * | Quadruple dot | `dot.quad` | `⃜` |
     * | Circle | `circle` | `∘` |
     * | Double acute | `acute.double` | `˝` |
     * | Caron | `caron` | `ˇ` |
     * | Right arrow | `arrow`, `->` | `→` |
     * | Left arrow | `arrow.l`, `<-` | `←` |
     * | Left/Right arrow | `arrow.l.r` | `↔` |
     * | Right harpoon | `harpoon` | `⇀` |
     * | Left harpoon | `harpoon.lt` | `↼` |
     * 
     * Required, positional; Typst type: str|content
     */
    @all:Positional val accent: TAttachment,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/accent/](https://typst.app/docs/reference/math/accent/)
     * 
     * The size of the accent, relative to the width of the base.
     * 
     * Settable; Typst type: relative
     */
    @all:Settable val size: TRelative? = null,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/math/accent/](https://typst.app/docs/reference/math/accent/)
     * 
     * Whether to remove the dot on top of lowercase i and j when adding a top accent.
     * 
     * This enables the `dtls` OpenType feature.
     * 
     * Settable; Typst type: bool
     */
    @all:Settable val dotless: TBool? = null,
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("math.accent")
    }
}


/**
 * Represents [`set`-rule](https://typst.app/docs/reference/styling/#set-rules) for [TMathAccent]
 */
@SerialName("set-math.accent")
data class TSetMathAccent(
    override val internals: SetRuleInternals? = null,
    val size: TRelative? = null,
    val dotless: TBool? = null
) : TSetRule()
