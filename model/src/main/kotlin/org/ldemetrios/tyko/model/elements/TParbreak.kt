package org.ldemetrios.tyko.model


import kotlinx.serialization.Serializable



//!https://typst.app/docs/reference/model/parbreak/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * A paragraph break.
 * 
 * This starts a new paragraph. Especially useful when used within code like [for loops](https://typst.app/docs/reference/scripting/#loops). Multiple consecutive paragraph breaks collapse into a single one.
 * 
 * **_Example_**
 * 
 * ```typ
 * #for i in range(3) {
 *   [Blind text #i: ]
 *   lorem(5)
 *   parbreak()
 * }
 * ```
 * <img src="https://typst.app/assets/docs/Ugn0Cpe50EHdh4tXrmb4YAAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Syntax_**
 * 
 * Instead of calling this function, you can insert a blank line into your markup to create a paragraph break.
 */
@SerialName("parbreak")
data class TParbreak(
    override val label: TLabel? = null
) : TContent() {
    override fun elem(): TElement = ELEM

    companion object {
        val ELEM = TElement("parbreak")
    }
}
