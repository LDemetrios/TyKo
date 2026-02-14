package org.ldemetrios.tyko.model


import org.ldemetrios.tyko.model.TTransform
import org.ldemetrios.tyko.model.UnionType
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMembers

/**
 * Represents a set of [TStyle]s. Used for [TStyled] and doesn't have a stable `repr` on its own.
 * May be removed in the future.
 */
@SerialName("styles")
data class TStyles(@SerialName("value") val value: TArray<TStyle>) : TValue, TTransform {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.STYLE
    }

    override fun repr(sb: StringBuilder) {
        if (value.size == 1) {
            value[0].repr(sb)
        } else {
            val classes = value.map { it.javaClass }
            if (classes.distinct().size != 1) {
                throw AssertionError("Can't repr joined `styles` of different types")
            }
            sb.append("set ")

            val klass = classes[0]
            klass.getAnnotation(SerialName::class.java)
                .value
                .removePrefix("set-")
                .let(sb::append)
            sb.append("(")
            for (style in value) {
                klass.kotlin
                    .declaredMembers
                    .filterIsInstance<KProperty<*>>()
                    .filter { it.name != "internals" }
                    .forEach {
                        style.appendField(it, sb)
                    }
            }
            sb.append(")")
        }
    }
}


/**
 * Represents a style, either `set`-rule or `show`-rule
 */
sealed class TStyle : SequenceElement(), TValue {
    override fun type(): TType = TYPE

    companion object {
        val TYPE = TType.STYLE
    }
}

//!https://typst.app/docs/reference/styling/#show-rules
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/styling/#show-rules](https://typst.app/docs/reference/styling/#show-rules)
 * 
 * **_Show rules_**
 * 
 * With show rules, you can deeply customize the look of a type of element. The most basic form of show rule is a *show-set rule.* Such a rule is written as the `show` keyword followed by a [selector](https://typst.app/docs/reference/foundations/selector/), a colon and then a set rule. The most basic form of selector is an [element function](https://typst.app/docs/reference/foundations/function/#element-functions). This lets the set rule only apply to the selected element. In the example below, headings become dark blue while all other text stays black.
 * 
 * ```typ
 * #show heading: set text(navy)
 * 
 * = This is navy-blue
 * But this stays black.
 * ```
 * <img src="https://typst.app/assets/docs/DS2Pe3XVhhMMVWT9eUfjSQAAAAAAAAAA.png" alt="Preview" />
 * 
 * With show-set rules you can mix and match properties from different functions to achieve many different effects. But they still limit you to what is predefined in Typst. For maximum flexibility, you can instead write a *transformational* show rule that defines how to format an element from scratch. To write such a show rule, replace the set rule after the colon with an arbitrary [function](https://typst.app/docs/reference/foundations/function/). This function receives the element in question and can return arbitrary content. The function is often defined inline as `it => ..` using the [unnamed function syntax](https://typst.app/docs/reference/foundations/function/#unnamed). The function's parameter is typically named `it` by convention.
 * 
 * The available [fields](https://typst.app/docs/reference/scripting/#fields) on the element passed to the function match the parameters of the respective element function. Below, we define a show rule that formats headings for a fantasy encyclopedia.
 * 
 * The show rule itself adds tilde characters around the title (these must be escaped with a backslash because otherwise they would indicate a non-breaking space), emphasizes the title with italics, and then displays the heading counter after the title.
 * 
 * For this example, we also wanted center alignment and a different font. While we could've added these set rules into the existing show rule, we instead added them as separate show-set rules. This is good practice because now these rules can still be overridden by later show-set rules in the document, keeping styling composable. In contrast, set rules within a transformational show rule would not be overridable anymore.
 * 
 * ```typ
 * #set heading(numbering: "(I)")
 * #show heading: set align(center)
 * #show heading: set text(font: "Inria Serif")
 * #show heading: it => block[
 *   \~
 *   #emph(it.body)
 *   #counter(heading).display(it.numbering)
 *   \~
 * ]
 * 
 * = Dragon
 * With a base health of 15, the dragon is the most
 * powerful creature.
 * 
 * = Manticore
 * While less powerful than the dragon, the manticore
 * gets extra style points.
 * ```
 * <img src="https://typst.app/assets/docs/EDVIIxYxR-tkGleKw78EKwAAAAAAAAAA.png" alt="Preview" />
 * 
 * Like set rules, show rules are in effect until the end of the current block or file.
 * 
 * Instead of a function, the right-hand side of a show rule can also take a literal string or content block that should be directly substituted for the element. And apart from a function, the left-hand side of a show rule can also take a number of other *selectors* that define what to apply the transformation to:
 * 
 * - __Everything:__`show: rest => ..` Transform everything after the show rule. This is useful to apply a more complex layout to your whole document without wrapping everything in a giant function call.
 * - __Text:__`show "Text": ..` Style, transform or replace text.
 * - __Regex:__`show regex("\w+"): ..` Select and transform text with a regular expression for even more flexibility. See the documentation of the [`regex` type](https://typst.app/docs/reference/foundations/regex/) for details.
 * - __Function with fields:__`show heading.where(level: 1): ..` Transform only elements that have the specified fields. For example, you might want to only change the style of level-1 headings.
 * - __Label:__`show <intro>: ..` Select and transform elements that have the specified label. See the documentation of the [`label` type](https://typst.app/docs/reference/foundations/label/) for more details.
 * 
 * ```typ
 * #show "Project": smallcaps
 * #show "badly": "great"
 * 
 * We started Project in 2019
 * and are still working on it.
 * Project is progressing badly.
 * ```
 * <img src="https://typst.app/assets/docs/NBzIViTspdnPhsbo3WGDLAAAAAAAAAAA.png" alt="Preview" />
 */
@SerialName("show-rule")
data class TShowRule(
    val selector: TSelector<*>?,
    val transform: TTransform,
    val outside: Boolean? = null, // TODO wtf is this?
) : TStyle() {
    override fun repr(sb: StringBuilder) {
        sb.append("show")
        if (selector != null) {
            sb.append(" ")
            selector.repr(sb)
        }
        sb.append(":")
        transform.repr(sb)
    }
}

/**
 * Native internals of the set rule
 */
@SerialName("internals")
data class SetRuleInternals(
    val id: Int,
    val span: Long,
    val liftable: Boolean,
    val outside: Boolean
)

//!https://typst.app/docs/reference/styling/#set-rules
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/styling/#set-rules](https://typst.app/docs/reference/styling/#set-rules)
 * 
 * **_Set rules_**
 * 
 * With set rules, you can customize the appearance of elements. They are written as a [function call](https://typst.app/docs/reference/foundations/function/) to an [element function](https://typst.app/docs/reference/foundations/function/#element-functions) preceded by the `set` keyword (or `#set` in markup). Only optional parameters of that function can be provided to the set rule. Refer to each function's documentation to see which parameters are optional. In the example below, we use two set rules to change the [font family](https://typst.app/docs/reference/text/text/#parameters-font) and [heading numbering](https://typst.app/docs/reference/model/heading/#parameters-numbering).
 * 
 * ```typ
 * #set heading(numbering: "I.")
 * #set text(
 *   font: "New Computer Modern"
 * )
 * 
 * = Introduction
 * With set rules, you can style
 * your document.
 * ```
 * <img src="https://typst.app/assets/docs/nW0VZeyhJmtpweEOjJR_fgAAAAAAAAAA.png" alt="Preview" />
 * 
 * A top level set rule stays in effect until the end of the file. When nested inside of a block, it is only in effect until the end of that block. With a block, you can thus restrict the effect of a rule to a particular segment of your document. Below, we use a content block to scope the list styling to one particular list.
 * 
 * ```typ
 * This list is affected: #[
 *   #set list(marker: [--])
 *   - Dash
 * ]
 * 
 * This one is not:
 * - Bullet
 * ```
 * <img src="https://typst.app/assets/docs/6ckQbXFff1zDBcdWezXxpgAAAAAAAAAA.png" alt="Preview" />
 * 
 * Sometimes, you'll want to apply a set rule conditionally. For this, you can use a *set-if* rule.
 * 
 * ```typ
 * #let task(body, critical: false) = {
 *   set text(red) if critical
 *   [- #body]
 * }
 * 
 * #task(critical: true)[Food today?]
 * #task(critical: false)[Work deadline]
 * ```
 * <img src="https://typst.app/assets/docs/_UlmqEOdrmM6d-OQ9TsAXwAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Show rules_**
 * 
 * With show rules, you can deeply customize the look of a type of element. The most basic form of show rule is a *show-set rule.* Such a rule is written as the `show` keyword followed by a [selector](https://typst.app/docs/reference/foundations/selector/), a colon and then a set rule. The most basic form of selector is an [element function](https://typst.app/docs/reference/foundations/function/#element-functions). This lets the set rule only apply to the selected element. In the example below, headings become dark blue while all other text stays black.
 * 
 * ```typ
 * #show heading: set text(navy)
 * 
 * = This is navy-blue
 * But this stays black.
 * ```
 * <img src="https://typst.app/assets/docs/DS2Pe3XVhhMMVWT9eUfjSQAAAAAAAAAA.png" alt="Preview" />
 * 
 * With show-set rules you can mix and match properties from different functions to achieve many different effects. But they still limit you to what is predefined in Typst. For maximum flexibility, you can instead write a *transformational* show rule that defines how to format an element from scratch. To write such a show rule, replace the set rule after the colon with an arbitrary [function](https://typst.app/docs/reference/foundations/function/). This function receives the element in question and can return arbitrary content. The function is often defined inline as `it => ..` using the [unnamed function syntax](https://typst.app/docs/reference/foundations/function/#unnamed). The function's parameter is typically named `it` by convention.
 * 
 * The available [fields](https://typst.app/docs/reference/scripting/#fields) on the element passed to the function match the parameters of the respective element function. Below, we define a show rule that formats headings for a fantasy encyclopedia.
 * 
 * The show rule itself adds tilde characters around the title (these must be escaped with a backslash because otherwise they would indicate a non-breaking space), emphasizes the title with italics, and then displays the heading counter after the title.
 * 
 * For this example, we also wanted center alignment and a different font. While we could've added these set rules into the existing show rule, we instead added them as separate show-set rules. This is good practice because now these rules can still be overridden by later show-set rules in the document, keeping styling composable. In contrast, set rules within a transformational show rule would not be overridable anymore.
 * 
 * ```typ
 * #set heading(numbering: "(I)")
 * #show heading: set align(center)
 * #show heading: set text(font: "Inria Serif")
 * #show heading: it => block[
 *   \~
 *   #emph(it.body)
 *   #counter(heading).display(it.numbering)
 *   \~
 * ]
 * 
 * = Dragon
 * With a base health of 15, the dragon is the most
 * powerful creature.
 * 
 * = Manticore
 * While less powerful than the dragon, the manticore
 * gets extra style points.
 * ```
 * <img src="https://typst.app/assets/docs/EDVIIxYxR-tkGleKw78EKwAAAAAAAAAA.png" alt="Preview" />
 * 
 * Like set rules, show rules are in effect until the end of the current block or file.
 * 
 * Instead of a function, the right-hand side of a show rule can also take a literal string or content block that should be directly substituted for the element. And apart from a function, the left-hand side of a show rule can also take a number of other *selectors* that define what to apply the transformation to:
 * 
 * - __Everything:__`show: rest => ..` Transform everything after the show rule. This is useful to apply a more complex layout to your whole document without wrapping everything in a giant function call.
 * - __Text:__`show "Text": ..` Style, transform or replace text.
 * - __Regex:__`show regex("\w+"): ..` Select and transform text with a regular expression for even more flexibility. See the documentation of the [`regex` type](https://typst.app/docs/reference/foundations/regex/) for details.
 * - __Function with fields:__`show heading.where(level: 1): ..` Transform only elements that have the specified fields. For example, you might want to only change the style of level-1 headings.
 * - __Label:__`show <intro>: ..` Select and transform elements that have the specified label. See the documentation of the [`label` type](https://typst.app/docs/reference/foundations/label/) for more details.
 * 
 * ```typ
 * #show "Project": smallcaps
 * #show "badly": "great"
 * 
 * We started Project in 2019
 * and are still working on it.
 * Project is progressing badly.
 * ```
 * <img src="https://typst.app/assets/docs/NBzIViTspdnPhsbo3WGDLAAAAAAAAAAA.png" alt="Preview" />
 */
sealed class TSetRule() : TStyle() {
    abstract val internals: SetRuleInternals?
    override fun type(): TType = TYPE

    override fun repr(sb: StringBuilder) {
        sb.append("set ")
        this.javaClass
            .getAnnotation(SerialName::class.java)
            .value
            .removePrefix("set-")
            .let(sb::append)
        sb.append("(")
        this.javaClass.kotlin
            .declaredMembers
            .filterIsInstance<KProperty<*>>()
            .filter { it.name != "internals" }
            .forEach {
                appendField(it, sb)
            }
        sb.append(")")
    }
}
