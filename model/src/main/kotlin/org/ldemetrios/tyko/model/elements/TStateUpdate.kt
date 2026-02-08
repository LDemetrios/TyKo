package org.ldemetrios.tyko.model

//!https://typst.app/docs/reference/introspection/state/#definitions-update
// AUTO-GENERATED DOCS. DO NOT EDIT.
/**
 * Updates the value of the state.
 * 
 * Returns an invisible piece of [content](https://typst.app/docs/reference/foundations/content/) that must be inserted into the document to take effect. This invisible content tells Typst that the specified update should take place wherever the content is inserted into the document.
 * 
 * State is a part of your document and runs like a thread embedded in the document content. The value of a state is the result of all state updates that happened in the document up until that point.
 * 
 * That's why `state.update` returns an invisible sliver of content that you need to return and include in the document — a state update that is not "placed" in the document does not happen, and "when" it happens is determined by where you place it. That's also why you need [context](https://typst.app/docs/reference/context/) to read state: You need to use the current document position to know where on the state's "thread" you are.
 * 
 * Storing a state update in a variable (e.g. `let my-update = state("key").update(c => c * 2)`) will have no effect by itself. Only once you insert the variable `#my-update` somewhere into the document content, the update will take effect — at the position where it was inserted. You can also use `#my-update` multiple times at different positions. Then, the update will take effect multiple times as well.
 * 
 * In contrast to [`get`](https://typst.app/docs/reference/introspection/state/#definitions-get), [`at`](https://typst.app/docs/reference/introspection/state/#definitions-at), and [`final`](https://typst.app/docs/reference/introspection/state/#definitions-final), this function does not require [context](https://typst.app/docs/reference/context/). This is because, to create the state update, we do not need to know where in the document we are. We only need this information to resolve the state's value.
 */
@SerialName("state.update")
data class TStateUpdate(
    /**
     * The key of `self`, the state to update.
     */
    @all:Positional val key: TStr,
    // AUTO-GENERATED DOCS. DO NOT EDIT.
    /**
     * A value to update to or a function to update with.
     * 
     * - If given a non-function value, sets the state to that value.
     * - If given a function, that function receives the state's previous value and has to return the state's new value.
     * 
     * When updating the state based on its previous value, you should prefer the function form instead of retrieving the previous value from the [context](https://typst.app/docs/reference/context/). This allows the compiler to resolve the final state efficiently, minimizing the number of [layout iterations](https://typst.app/docs/reference/context/#compiler-iterations) required.
     * 
     * In the following example, `fill.update(f => not f)` will paint odd [items in the bullet list](https://typst.app/docs/reference/model/list/#definitions-item) as expected. However, if it's replaced with `context fill.update(not fill.get())`, then layout will not converge within 5 attempts, as each update will take one additional iteration to propagate.
     * 
     * Required, positional; Typst type: any|function
     */
    @all:Positional val update: TFunc,
    override val label: TLabel? = null
) : TSelectableContent<TStateUpdate>() {
    override fun elem(): TLocatableElement<TStateUpdate> = ELEM

    companion object {
        val ELEM = TLocatableElement<TStateUpdate>("state.update")
    }

    override fun reprImpl(sb: StringBuilder) {
        sb.append("state(")
        key.repr(sb)
        sb.append(").update(")
        update?.repr(sb)
        sb.append(")")
    }
}
