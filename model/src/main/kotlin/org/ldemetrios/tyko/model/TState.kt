package org.ldemetrios.tyko.model

//!https://typst.app/docs/reference/introspection/state/
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Generated based on: [https://typst.app/docs/reference/introspection/state/](https://typst.app/docs/reference/introspection/state/)
 * 
 * Manages stateful parts of your document.
 * 
 * Let's say you have some computations in your document and want to remember the result of your last computation to use it in the next one. You might try something similar to the code below and expect it to output 10, 13, 26, and 21. However this __does not work__ in Typst. If you test this code, you will see that Typst complains with the following error message: *Variables from outside the function are read-only and cannot be modified.*
 * 
 * ```typ
 * // This doesn't work!
 * #let star = 0
 * #let compute(expr) = {
 *   star = eval(
 *     expr.replace("⭐", str(star))
 *   )
 *   [New value is #star.]
 * }
 * 
 * #compute("10") \
 * #compute("⭐ + 3") \
 * #compute("⭐ * 2") \
 * #compute("⭐ - 5")
 * ```
 * 
 * **_State and document markup_**
 * 
 * Why does it do that? Because, in general, this kind of computation with side effects is problematic in document markup and Typst is upfront about that. For the results to make sense, the computation must proceed in the same order in which the results will be laid out in the document. In our simple example, that's the case, but in general it might not be.
 * 
 * Let's look at a slightly different, but similar kind of state: The heading numbering. We want to increase the heading counter at each heading. Easy enough, right? Just add one. Well, it's not that simple. Consider the following example:
 * 
 * ```typ
 * #set heading(numbering: "1.")
 * #let template(body) = [
 *   = Outline
 *   ...
 *   #body
 * ]
 * 
 * #show: template
 * 
 * = Introduction
 * ...
 * ```
 * <img src="https://typst.app/assets/docs/OC8Yphz4-mFQhH6Mm9lwwAAAAAAAAAAA.png" alt="Preview" />
 * 
 * Here, Typst first processes the body of the document after the show rule, sees the `Introduction` heading, then passes the resulting content to the `template` function and only then sees the `Outline`. Just counting up would number the `Introduction` with `1` and the `Outline` with `2`.
 * 
 * **_Managing state in Typst_**
 * 
 * So what do we do instead? We use Typst's state management system. Calling the `state` function with an identifying string key and an optional initial value gives you a state value which exposes a few functions. The two most important ones are `get` and `update`:
 * 
 * - The [`get`](https://typst.app/docs/reference/introspection/state/#definitions-get) function retrieves the current value of the state. Because the value can vary over the course of the document, it is a *contextual* function that can only be used when [context](https://typst.app/docs/reference/context/) is available.
 * - The [`update`](https://typst.app/docs/reference/introspection/state/#definitions-update) function modifies the state. You can give it any value. If given a non-function value, it sets the state to that value. If given a function, that function receives the previous state and has to return the new state.
 * 
 * Our initial example would now look like this:
 * 
 * ```typ
 * #let star = state("star", 0)
 * #let compute(expr) = {
 *   star.update(old =>
 *     eval(expr.replace("⭐", str(old)))
 *   )
 *   [New value is #context star.get().]
 * }
 * 
 * #compute("10") \
 * #compute("⭐ + 3") \
 * #compute("⭐ * 2") \
 * #compute("⭐ - 5")
 * ```
 * <img src="https://typst.app/assets/docs/kFWNvudx6AjsofaM3zqRgAAAAAAAAAAA.png" alt="Preview" />
 * 
 * State managed by Typst is always updated in layout order, not in evaluation order. The `update` method returns content and its effect occurs at the position where the returned content is inserted into the document.
 * 
 * As a result, we can now also store some of the computations in variables, but they still show the correct results:
 * 
 * ```typ
 * ...
 * 
 * #let more = [
 *   #compute("⭐ * 2") \
 *   #compute("⭐ - 5")
 * ]
 * 
 * #compute("10") \
 * #compute("⭐ + 3") \
 * #more
 * ```
 * <img src="https://typst.app/assets/docs/YNg0KJ7ZWmzQ6tVelwuOlAAAAAAAAAAA.png" alt="Preview" />
 * 
 * This example is of course a bit silly, but in practice this is often exactly what you want! A good example are heading counters, which is why Typst's [counting system](https://typst.app/docs/reference/introspection/counter/) is very similar to its state system.
 * 
 * **_Time Travel_**
 * 
 * By using Typst's state management system you also get time travel capabilities! We can find out what the value of the state will be at any position in the document from anywhere else. In particular, the `at` method gives us the value of the state at any particular location and the `final` methods gives us the value of the state at the end of the document.
 * 
 * ```typ
 * ...
 * 
 * Value at `<here>` is
 * #context star.at(<here>)
 * 
 * #compute("10") \
 * #compute("⭐ + 3") \
 * *Here.* <here> \
 * #compute("⭐ * 2") \
 * #compute("⭐ - 5")
 * ```
 * <img src="https://typst.app/assets/docs/UbqeqJWdWK6IZdSEGeKpbgAAAAAAAAAA.png" alt="Preview" />
 * 
 * **_A word of caution_**
 * 
 * To resolve the values of all states, Typst evaluates parts of your code multiple times. However, there is no guarantee that your state manipulation can actually be completely resolved.
 * 
 * For instance, if you generate state updates depending on the final value of a state, the results might never converge. The example below illustrates this. We initialize our state with `1` and then update it to its own final value plus 1. So it should be `2`, but then its final value is `2`, so it should be `3`, and so on. This example displays a finite value because Typst simply gives up after a few attempts.
 * 
 * ```typ
 * // This is bad!
 * #let x = state("key", 1)
 * #context x.update(x.final() + 1)
 * #context x.get()
 * ```
 * <img src="https://typst.app/assets/docs/NTGR2IA5Wqc2aX0exn3mBgAAAAAAAAAA.png" alt="Preview" />
 * 
 * In general, you should try not to generate state updates from within context expressions. If possible, try to express your updates as non-contextual values or functions that compute the new value from the previous value. Sometimes, it cannot be helped, but in those cases it is up to you to ensure that the result converges.
 */
@SerialName("state")
data class TState(
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/introspection/state/](https://typst.app/docs/reference/introspection/state/)
     * 
     * The key that identifies this state.
     * 
     * Any [updates](https://typst.app/docs/reference/introspection/state/#definitions-update) to the state will be identified with the string key. If you construct multiple states with the same `key`, then updating any one will affect all of them.
     * 
     * Required, positional; Typst type: str
     */
    @all:Positional val key: TStr,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * Generated based on: [https://typst.app/docs/reference/introspection/state/](https://typst.app/docs/reference/introspection/state/)
     * 
     * The initial value of the state.
     * 
     * If you construct multiple states with the same `key` but different `init` values, they will each use their own initial value but share updates. Specifically, the value of a state at some location in the document will be computed from that state's initial value and all preceding updates for the state's key.
     * 
     * Positional; Typst type: any
     */
    @all:Positional val init: TValue? = null
) : TValue {
    override fun type(): TType = TType.STATE
}
