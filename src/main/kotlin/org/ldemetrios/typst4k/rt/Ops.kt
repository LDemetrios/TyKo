package org.ldemetrios.typst4k.rt

import org.ldemetrios.typst4k.model.*

fun selector(it: TLabel) = TLabelSelector(it)
fun selector(it: TRegex) = TRegexSelector(it)

fun TSelector.before(selector: TSelector, inclusive: Boolean) = TBeforeSelector(this, selector, inclusive.t)
fun TSelector.after(selector: TSelector, inclusive: Boolean) = TAfterSelector(this, selector, inclusive.t)

infix fun TSelector.before(selector: TSelector) = TBeforeSelector(this, selector)
infix fun TSelector.after(selector: TSelector) = TAfterSelector(this, selector)

fun TSelector.before(it: TLabel, inclusive: Boolean) = TBeforeSelector(this, selector(it), inclusive.t)
fun TSelector.after(it: TLabel, inclusive: Boolean) = TAfterSelector(this, selector(it), inclusive.t)
infix fun TSelector.before(it: TLabel) = TBeforeSelector(this, selector(it))
infix fun TSelector.after(it: TLabel) = TAfterSelector(this, selector(it))
fun TSelector.before(it: TRegex, inclusive: Boolean) = TBeforeSelector(this, selector(it), inclusive.t)
fun TSelector.after(it: TRegex, inclusive: Boolean) = TAfterSelector(this, selector(it), inclusive.t)
infix fun TSelector.before(it: TRegex) = TBeforeSelector(this, selector(it))
infix fun TSelector.after(it: TRegex) = TAfterSelector(this, selector(it))

infix fun TSelector.and(selector: TSelector) = TAndSelector(TArray(this, selector))
infix fun TSelector.or(selector: TSelector) = TOrSelector(TArray(this, selector))
infix fun TSelector.and(it: TLabel) = TAndSelector(TArray(this, selector(it)))
infix fun TSelector.or(it: TLabel) = TOrSelector(TArray(this, selector(it)))
infix fun TSelector.and(it: TRegex) = TAndSelector(TArray(this, selector(it)))
infix fun TSelector.or(it: TRegex) = TOrSelector(TArray(this, selector(it)))

val String.text get() = TText(text = this.t)

