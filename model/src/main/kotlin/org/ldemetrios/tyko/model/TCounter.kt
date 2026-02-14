package org.ldemetrios.tyko.model

import org.ldemetrios.tyko.model.UnionType

/**
 * Either a Counter, or something that can be a Counter key
 */
@UnionType(["str", "label", "selector", "location", "function", "counter"])
sealed interface IntoCounter: TValue {
    fun counter() : TCounter
}

/**
 * The value that can be used as counter's key
 */
@UnionType(["str", "label", "selector", "location", "function"])
sealed interface TCounterKey : TValue, IntoCounter {
    override fun counter(): TCounter = TCounter(this)
}

//!https://typst.app/docs/reference/introspection/counter/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/introspection/counter/](https://typst.app/docs/reference/introspection/counter/)
 * 
 * Counts through pages, elements, and more.
 * 
 * With the counter function, you can access and modify counters for pages, headings, figures, and more. Moreover, you can define custom counters for other things you want to count.
 * 
 * Since counters change throughout the course of the document, their current value is *contextual.* It is recommended to read the chapter on [context](https://typst.app/docs/reference/context/) before continuing here.
 * 
 * **_Accessing a counter_**
 * 
 * To access the raw value of a counter, we can use the [`get`](https://typst.app/docs/reference/introspection/counter/#definitions-get) function. This function returns an [array](https://typst.app/docs/reference/foundations/array/): Counters can have multiple levels (in the case of headings for sections, subsections, and so on), and each item in the array corresponds to one level.
 * 
 * ```typ
 * #set heading(numbering: "1.")
 * 
 * = Introduction
 * Raw value of heading counter is
 * #context counter(heading).get()
 * ```
 * <img src="https://typst.app/assets/docs/jqVSznl_yGBcNN9ecF8OVAAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Displaying a counter_**
 * 
 * Often, we want to display the value of a counter in a more human-readable way. To do that, we can call the [`display`](https://typst.app/docs/reference/introspection/counter/#definitions-display) function on the counter. This function retrieves the current counter value and formats it either with a provided or with an automatically inferred [numbering](https://typst.app/docs/reference/model/numbering/).
 * 
 * ```typ
 * #set heading(numbering: "1.")
 * 
 * = Introduction
 * Some text here.
 * 
 * = Background
 * The current value is: #context {
 *   counter(heading).display()
 * }
 * 
 * Or in roman numerals: #context {
 *   counter(heading).display("I")
 * }
 * ```
 * <img src="https://typst.app/assets/docs/7EUi61p1PXmzQyka_2NqiAAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Modifying a counter_**
 * 
 * To modify a counter, you can use the `step` and `update` methods:
 * 
 * - The `step` method increases the value of the counter by one. Because counters can have multiple levels , it optionally takes a `level` argument. If given, the counter steps at the given depth.
 * - The `update` method allows you to arbitrarily modify the counter. In its basic form, you give it an integer (or an array for multiple levels). For more flexibility, you can instead also give it a function that receives the current value and returns a new value.
 * 
 * The heading counter is stepped before the heading is displayed, so `Analysis` gets the number seven even though the counter is at six after the second update.
 * 
 * ```typ
 * #set heading(numbering: "1.")
 * 
 * = Introduction
 * #counter(heading).step()
 * 
 * = Background
 * #counter(heading).update(3)
 * #counter(heading).update(n => n * 2)
 * 
 * = Analysis
 * Let's skip 7.1.
 * #counter(heading).step(level: 2)
 * 
 * == Analysis
 * Still at #context {
 *   counter(heading).display()
 * }
 * ```
 * <img src="https://typst.app/assets/docs/EOYqv5YWVpmiQyBJoYpqQAAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Page counter_**
 * 
 * The page counter is special. It is automatically stepped at each pagebreak. But like other counters, you can also step it manually. For example, you could have Roman page numbers for your preface, then switch to Arabic page numbers for your main content and reset the page counter to one.
 * 
 * ```typ
 * #set page(numbering: "(i)")
 * 
 * = Preface
 * The preface is numbered with
 * roman numerals.
 * 
 * #set page(numbering: "1 / 1")
 * #counter(page).update(1)
 * 
 * = Main text
 * Here, the counter is reset to one.
 * We also display both the current
 * page and total number of pages in
 * Arabic numbers.
 * ```
 * <img src="https://typst.app/assets/docs/PDCorO6nPZEoa3HjHUVgRwAAAAAAAAAA.png" alt="Preview" /><img src="https://typst.app/assets/docs/PDCorO6nPZEoa3HjHUVgRwAAAAAAAAAB.png" alt="Preview" />
 * 
 * **_Custom counters_**
 * 
 * To define your own counter, call the `counter` function with a string as a key. This key identifies the counter globally.
 * 
 * ```typ
 * #let mine = counter("mycounter")
 * #context mine.display() \
 * #mine.step()
 * #context mine.display() \
 * #mine.update(c => c * 3)
 * #context mine.display()
 * ```
 * <img src="https://typst.app/assets/docs/CxXLMyCvJp2FnmacPN3WUgAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_How to step_**
 * 
 * When you define and use a custom counter, in general, you should first step the counter and then display it. This way, the stepping behaviour of a counter can depend on the element it is stepped for. If you were writing a counter for, let's say, theorems, your theorem's definition would thus first include the counter step and only then display the counter and the theorem's contents.
 * 
 * ```typ
 * #let c = counter("theorem")
 * #let theorem(it) = block[
 *   #c.step()
 *   *Theorem #context c.display():*
 *   #it
 * ]
 * 
 * #theorem[$1 = 1$]
 * #theorem[$2 < 3$]
 * ```
 * <img src="https://typst.app/assets/docs/af6Y7nOR_IldvYHIWDmkIQAAAAAAAAAA.png" alt="Preview" />
 * 
 * The rationale behind this is best explained on the example of the heading counter: An update to the heading counter depends on the heading's level. By stepping directly before the heading, we can correctly step from `1` to `1.1` when encountering a level 2 heading. If we were to step after the heading, we wouldn't know what to step to.
 * 
 * Because counters should always be stepped before the elements they count, they always start at zero. This way, they are at one for the first display (which happens after the first step).
 * 
 * **_Time travel_**
 * 
 * Counters can travel through time! You can find out the final value of the counter before it is reached and even determine what the value was at any particular location in the document.
 * 
 * ```typ
 * #let mine = counter("mycounter")
 * 
 * = Values
 * #context [
 *   Value here: #mine.get() \
 *   At intro: #mine.at(<intro>) \
 *   Final value: #mine.final()
 * ]
 * 
 * #mine.update(n => n + 3)
 * 
 * = Introduction <intro>
 * #lorem(10)
 * 
 * #mine.step()
 * #mine.step()
 * ```
 * <img src="https://typst.app/assets/docs/wodRGpSsJgDZtfsMk_GNgwAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_Other kinds of state_**
 * 
 * The `counter` type is closely related to [state](https://typst.app/docs/reference/introspection/state/) type. Read its documentation for more details on state management in Typst and why it doesn't just use normal variables for counters.
 */
@SerialName("counter")
data class TCounter(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/introspection/counter/](https://typst.app/docs/reference/introspection/counter/)
     * 
     * The key that identifies this counter globally.
     * 
     * - If it is a string, creates a custom counter that is only affected by manual updates,
     * - If it is the [`page`](https://typst.app/docs/reference/layout/page/) function, counts through pages,
     * - If it is a [selector](https://typst.app/docs/reference/foundations/selector/), counts through elements that match the selector. For example,
     *   - provide an element function: counts elements of that type,
     *   - provide a [`where`](https://typst.app/docs/reference/foundations/function/#definitions-where) selector: counts a type of element with specific fields,
     *   - provide a [`<label>`](https://typst.app/docs/reference/foundations/label/): counts elements with that label.
     * 
     * Required, positional; Typst type: str|label|selector|location|function
     */
    @all:Positional val key: TCounterKey
) : TValue,
    TCounterKey { // TCounter: TCounterKey, this is ridiculous...
    override fun type(): TType = TType.COUNTER
    override fun counter(): TCounter = this
}
