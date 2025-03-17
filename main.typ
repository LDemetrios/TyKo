#set text(fill: sys.inputs.at("kvasir-preview-foreground", default: black))
#set page(fill: sys.inputs.at("kvasir-preview-background", default: white))

#let ignore-results = sys.inputs.at("saturn-run", default: false)

#let cnt = state("saturn", 0)

#let invoke(func, ..args, handler) = context {
    [#metadata((func, args))#label("saturn-import-" + str(cnt.get()))]
    if (ignore-results) [] else {
        handler(read("saturn-output/" + str(cnt.get()) + ".typ"))
    }
    cnt.update(it => it + 1)
}

// #import "@preview/cetz:0.2.2"
// #import "@preview/tablex:0.0.8": *
// #import "@preview/zero:0.3.0": num
#import "@preview/cheq:0.2.2": checklist

#show table: block.with(breakable: false)

#let listing = if ignore-results { it => it } else {
    import "@preview/codelst:2.0.2": sourcecode
    sourcecode.with(frame: none)
}

#invoke("add", 1, 2, it => assert(eval(it) == 3))

#show raw.where(lang: "kteval"): it => {
    text(
        size: 1.25em,
        listing(raw(it.text, lang: "kt", block: it.block)),
    )
    invoke(
        "eval_kt",
        it.text,
        it => {
            let data = eval(it)
            if ("CE" in data) {
                data.CE.join[\ ]
            } else {
                let (error, output) = data
                let value = data.at("value", default:none)
                text(fill: rgb("#009900"), text(size: .9em, output))
                text(fill: red, text(size: .9em, error))
                [#value]
                if ("throwable" in data) {
                    let t =  data.throwable.split("\n")
                    text(fill: red, text(size: .9em, t.slice(0, t.len() - 20).join("\n")))
                }
            }
        },
    )
}


#show raw.where(lang: "render"): it => context {
    text(
        size: 1.25em,
        listing(raw(it.text, lang: "kt", block: it.block)),
    )
    invoke(
        "eval_kt",
        it.text,
        it => {
            let data = eval(it)
            if ("CE" in data) {
                data.CE.join[\ ]
            } else {
                let (error, output, value) = data

                text(fill: red, text(size: .9em, error))
                [#box(inset: 1em, stroke: (left: (paint: text.fill, dash: "dashed")), value)]
            }
        },
    )
}



#let infty = math.oo
#let Var = "Var"
#let Cov = "Cov"
#let dx = $upright(d)x$
#let argmax = "argmax"

#let bar = math.overline
#let prod = math.product
#show math.ast: math.dot.op

#let smallcaps(body) = {
    show regex("[a-z]"): letter => text(size: 0.65em, upper(letter))
    show regex("[а-яё]"): letter => text(size: 0.7em, upper(letter))
    body
}

#show math.equation: it => {
    show "~": math.class(
        "relation",
        "~",
    )
    it
}

#let styling = body => [
    #set text(lang: "ru")

    #let sizes = (0, 2, 1.7, 1.7, 1.6, 1.4)
    #show heading: it => {
        set align(if (it.level < 6) { center } else { left })
        set text(sizes.at(it.level, default: 1) * 1.2em)
        smallcaps(it)
    }

    #set outline(depth: 8, indent: 2em)

    #set par(justify: true)
    #show raw.where(block: true): set par(justify: false)

    #show raw.where(block: false): box

    #body
]

#show: styling

#show: checklist

#show regex("\(since \d\.\d(\.\d)?\)"): it => {
    show regex("\d\.\d(\.\d)?"): jt => raw(jt.text)
    it
}

= TyKo

TyKo /taɪko/ is project aiming to achieve interoperability between Typst and JVM languages (primary Kotlin, but theoretically all of them). There are several parts, at different stages of development:

\
- [/] Manipulating Typst values from JVM
    - [x] Basic types
    - [x] Content and other types
    - [x] Set, show rules
    - [/] Functions
    - [ ] Plugins, modules
- [/] Calling Typst compiler from JVM
    - [/] Compilation
        - [x] PNG
        - [x] SVG
        - [ ] PDF
        - [x] HTML
    - [x] Query
    - [/] World customization
        - [/] Library customization
            - [x] Standartized (`inputs`, `features`)
            - [ ] Registering custom functions
            - [ ] Modules
        - [ ] Fonts
        - [ ] Styles by default
        - [x] File management
- [ ] Calling JVM from Typst document
    - [ ] Propagating JVM functions as Typst functions
    - [ ] Loading JARs as plugins
    - [ ] Freely throwing around JVM objects in Typst code.
- [/] Misc
    - [/] Downloading package from central repo
    - [ ] Obtaining source code info (parse tree)
    - [ ] Annotation processor for writing overloads

#pagebreak()

== Contents

#outline(title: none)

#pagebreak()

== Library structure


Library consists of four packages: \ \

- `model`
This is the package with all the classes representing Typst values. They mostly are named as `T` and then the type name in UpperCamel: `TInt`, `TFootnoteEntry` etc. \ \

- `ffi`
This is the package with C-faced API. You need to explicitly mark your code
`@OptIn(TyKoFFIEntity::class)` to use most of its content, except `TypstSharedLibrary`. They are also not at all guaranteed to remain the same way. \ \

- `compiler`
This is, obviously, for accessing Typst compiler, and all the related things (World, Diagnostics etc). \ \

- `operations`
This is yet a draft, it's a package that allows several operations on values.

#pagebreak()

=== Model

Typst has dynamic typing, therefore reflecting it in statically typed JVM is quite tricky. Because of that, there are few simplifications. The library defines several types alongside ones defined in Typst docs:

#set enum(start: 0)

#show raw.where(lang: "typdoc"): it => invoke(
    "highlightTypdoc",
    it,
    res => {
        show highlight: h => box(h.body, fill: h.fill, inset: (x: .4em), outset: (x: -.15em, y: .3em), radius: .25em)
        set text(size: 1.25em)
        eval(res)
    },
)

+ All values are immutable. It is forbidden to put mutable `List`s and `Map`s into `TArray` and `TDictionary` and change it afterwards as well, as all the invariants are computed upon creation of `TValue` (common supertype for all Typst values).

+ There are functions, mostly elements, which accept only some sets of strings (`str`s). For example, both `top-edge` and `bottom-edge` parameters of the `highlight` element can be `str`s:

    ```typdoc
    highlight(
        fill: none|color|gradient|tiling,
        stroke: none|length|color|gradient|stroke|tiling|dictionary,
        top-edge: length|str,
        bottom-edge: length|str,
        extent: length,
        radius: relative|dictionary,
        content,
    ) -> content
    ```

    ... but not any str:
    `"ascender"`,
    `"cap-height"`,
    `"x-height"`,
    `"baseline"`,
    `"bounds"` for `top-edge`, and `"baseline",`
    `"descender"`,
    `"bounds"` for `bottom-edge`. For this, we have enum-like subclasses of `TStr`, named as `T<element><param-name>` respectively, with some exceptions. Whenever you create a `TStr`, it has correct type. For example, `TStr("ascender")` would have type `TTextTopEdge`.

+ There are types, that are dictionaries, but with only specific keys. These are `location`, `point`, `sides` and `margin`. They all extend `TDictionary`, whenever you create a dictionary, the value has respective type. Also, `sides` extend `margin`. Empty dictionary has special separate type, which extends `sides`.

+ As seen above, there are a lot of union types. As there are somewhat around 30 different types in Typst, creating an interface for each possible union is impossible, so only those that are used in constructors are created. This approach somewhat limits typechecking, so, when Typst is stabilized, it'll be changed to more accurately reflect what is what (good example would be the introduction of `Numbering` type instead of `function|str`, or `Smart<T>` instead of `T|none`). For now, all unions have... not so much clever name, like `TFunctionOrStr`.

+ Some `str` values belong to more than one "enum", therefore we need "intersection types". There are much less of those, then of unions, so every inhabited intersection has its own interface. For example, `"smooth"` would be instance of `TCurveCloseModeAndImageScaling`.

+ Most of the interfaces have their `Companion` extend `TType` or `TElement`. Therefore, we can simply write `TInt`, and it is what `type(1)` would be. The exceptions are `TNone` and `TAuto`, which are values. `TNone.type()` and `TAuto.type()` are their respective types.


#pagebreak()

#let external = [#"   " _(external)_]
#let inherited(what) = [#"    " _(inherited from #what)_]

==== `TValue`

`TValue` is the common interface for all Typst values. It has only two methods:

#let marked(it) = context box(
    inset: (left: 2em),
    box([\ #it\ #" "], outset: (left: 1em), stroke: (left: text.fill + .5pt)),
)

#marked[
    - ```kt fun TValue.repr() : String``` #external

    Creates a code representation of the value. The resulting string is a valid code in Typst. It is also atomic from the point of the Typst compiler, so only prepending it by `#` is enough to make it valid Typst markup.

    For now it is not human-readable in any way, but there are plans of improvement (that's why it's external func).
    \ \
    - ```kt fun TValue.type() : TType```

    Returns the `type` of the value.
]

Most of its inheritants are generated automatically, based on the information from the official documentation. There are, however, some types with specific behaviour.

==== `TBool`, `TInt`, `TFloat`, `TStr`, `TArray`, `TDictionary`, `TBytes`

These are the types, analogs of which exist in Kotlin. They can be instantiated with `<value>.t`, where `<value>` is respectively:

#context {
    set align(center)
    table(
        align: left,
        columns: 3,
        stroke: text.fill,
        [*Typst type*], [*TyKo type*], [*Can be created by *`.t`* from*],
        `bool`, `TBool`, `Boolean`,
        `int`, `TInt`, [`Byte`, `Short`, `Int`, `Long`],
        `float`, `TFloat`, [`Float`, `Double`],
        `str`, `TStr`, [`String`],
        `array`, `TArray<out E>`, [`List<E>`, where `E : TValue`],
        `dictionary`, `TDictionary<out V>`, [`Map<String, V>`, where `V : TValue`],
        `bytes`, `TBytes`, [`ByteArray`],
    )
}

... and converted back to Kotlin types with `.value`.

In addition to that, `TArray<E>` implements `List<E>` and `TDictionary<V>` implements `Map<String, V>`; and there are helper functions for construction:

#marked[
    ```kt fun <E : TValue> TArray(vararg elements: E) : TArray<E> ```
    ```kt fun <V : TValue> TDictionary(vararg pairs: Pair<String, V>) : TDictionary<V> ```
]

```kteval
val x = TArray(1.t, true.t)
val y = TDictionary("a" to "b".t, "c" to "d".t)

println(x.repr())
println(y.repr())
```

==== `TAlignment`

This is a representation of `alignment`. There are, also, specific types for horizontal and vertical alignment (`THAlignment` and `TVAlignment`), which are inheritants of the `TAlignment`. Addition is defined:

```kteval
val align = THAlignment.Center + TVAlignment.Horizon
println(align.repr())
```

==== Numeric types (`TRatio`, `TFraction`, `TLength`, `TRelative`, `TAngle`)

They can be created with the same postfixes, as in Typst, but simulated via extension values.
All of them contain ```typc float``` inside, therefore, can be created from any Kotlin or Typst
primitive number:

#context {
    set align(center)
    table(
        align: left,
        columns: 3,
        stroke: text.fill,
        [*Typst type*], [*TyKo type*], [*Can be created by with*],
        `ratio`, `TRatio`, `.pc`,
        `fraction`, `TFraction`, [`.fr`],
        `length`, `TLength`, [`.em`, `.pt`],
        `relative`, `TRelative`, [],
        `angle`, `TAngle`, [`.deg`, `.rad`],
    )
}

And for your convinience, there exists addition, but only for `length` and between `length` and `ratio`:

```kteval
println(1.rad.repr())
println((1.em + 1.pt + 1.pc).repr())
```

More operations will be added later.


==== Colors

#context {
    set align(center)
    table(
        align: left,
        columns: 2,
        stroke: text.fill,
        [*Typst type*], [*TyKo type*],
        `cmyk`, `TCmyk`,
        `color.hsl`, `THsl`,
        `color.hsv`, `THsv`,
        `color.linear-rgb`, `TLinearRgb`,
        `luma`, `TLuma`,
        `oklab`, `TOklab`,
        `oklch`, `TOklch`,
        `rgb`, `TRgb`,
    )
}

All of them extend `TColor`. Also, `TRgb` can be created with `.t` from `java.awt.Color`:

```kteval
import java.awt.Color

println(Color.WHITE.t.repr())
```

==== Functions

For specific tools developers' convinience, hierarchy of functions follows Typst's internals:

#context {
    set align(center)
    table(
        align: left,
        columns: 3,
        stroke: text.fill,
        [*TyKo type*], [*What is it*], [*Example*],
        `TNativeFunc`, [Native function from stdlib], ```typc calc.cos```,
        `TClosure`,
        [User-defined function with Typst code, possibly with captured values],
        [```typc it => it * 2```\ ```typc let f(x) = x + 1; f```],

        `TWith`, [Another function, with some of its arguments preset], `box.with(inset: 1em)`,
        `TElement`,
        [Specific kind of native function, representing an element function. It also is a #link(<selector>)[`selector`]],
        `box`,

        [`TNativeFunc` #super[(2)]], [Can be used to create any function from Typst code], [],
    )
}

However, when you write functions that only operate Typst values, not analyze them,
you can simply ignore all that and only invoke them. That can be done with eiither `apply` method:

```kt
fun TFunction.apply(args: TArguments<*>): TValue
```

or with operators:

```kt
operator fun TFunction.get(vararg named: Pair<String, TValue>) : TWith

operator fun TFunction.invoke(vararg args: TValue): TDynamic
```

The idea here is just type checking. As we can't declare parameter type `vararg Pair<...>|TValue`,
we separate them. If there are none named arguments, just use `(poistional, args)`.
If there are none positional arguments, use empty `()` (empty `[]` are not allowed):

```kteval
val lorem = TNativeFunc("lorem".t)
println(lorem(15.t).repr())
```

As you can see, no actual computation happened. `operator ()` returns an instance of
#link(<dynamic>)[`TDelayedExecution`], execution of which can be #link(<force>)[`force`]d with appropriate compiler.
The reason is, there can be different libraries, and execution of functions can require context.

==== Content

In addition, it has function ```kt fun TContent.element() : TElement```.

Elements, that usually are available only inside `equation`s or with `math` module,
have `Math` prefix in them:

#context {
    set align(center)
    table(
        align: left,
        columns: 2,
        stroke: text.fill,
        [*Typst type*], [*TyKo type*],
        `heading`, `THeading`,
        `emph`, `TEmph`,
        `cancel`, `TMathCancel`,
        `equation`, `TMathEquation`,
    )
}

Also, several type that don't have explicit constructor in Typst, have in TyKo:


#context {
    set align(center)
    table(
        align: left,
        columns: 3,
        stroke: text.fill,
        [*TyKo type*], [*What is it*], [*Example*],
        `TMathAlignPoint`, [Align point in equations], `$ 1 & 2 \ 3 & 4 $`,
        `TContext`, [Contextual expression], `#context text.fill`,
        `TSequence`, [Markup block, more or less], `Just _formatted_ text`,
        [`TStyled` (deprecated)],
        [Represents `set` and `show` rules application],
        ```typ
        #show emph: set text(fill:red)
        Just _formatted_ text
        ```,
    )
}

```render
TSequence(
    TArray(
        TText(text = "Just".t),
        TSpace(),
        TEmph(TText(text = "formatted".t)),
        TSpace(),
        TText(text = "text".t)
    )
)
```

To avoid boilerplate, there are some improvements.
First, `TText` can be created with `.text` from both `String` and `TStr`.
Second, `TSequence` has vararg constructor.

```render
TSequence(
    "Just".text,
    TSpace(),
    TEmph("formatted".text),
    TSpace(),
    "text".text
)
```

Third, specifically `TSequence` and `TMathEquation` have DSL constructor,
in context of which the text is added via unary `+`:

```render
TSequence {
    +"Just"
    +TSpace()
    +TEmph("formatted".text)
    +TSpace()
    +"text"
}
```

==== Styles (`set` and `show`)

For each `element` class, if it has any settable parameters,
there exists corresponding `set element` class. The all extend `TSetRule`,
and inherently `TStyle`:

```kteval
println(TSetText(fill = java.awt.Color.RED.t).repr())
```

As well as `set`, there are `show` rules. As any `show` rule in Typst,
it can either contain `selector` or not, and its `transform` can be either content,
function, or an array of styles:

```render
TStyled(
    TArray(
        TShowRule(TEmph.Elem, TSetText(fill = java.awt.Color.RED.t))
    ),
    TSequence {
        +"Just"
        +TSpace()
        +TEmph("formatted".text)
        +TSpace()
        +"text"
    },
)
```

Besides, in TyKo every `style` is just a content, and may be inserted into the sequence.
It then spans until the end of `TSequence`:

```render
TSequence {
    +TSequence {
        +TShowRule(TEmph.Elem, TSetText(fill = java.awt.Color.RED.t))
        +"Just"
        +TSpace()
        +TEmph("formatted".text)
        +TSpace()
        +"text."
    }
    +TLinebreak()
    +TEmph("This is outside the scope.".text)
}
```

==== `TDynamic`, delayed execution <dynamic>

`TDynamic` is the type that represents a value of yet unknown type.  It is the subtype of _*all*_ the TyKo types, except for `TPoint` and `TLocation` (those are excluded due to generic variance problems). Therefore, typechecking can be postponed (usually, until the value is `repr`ed and sent to the Typst compiler).

As was mentioned before, no actual execution happens when you call `invoke` operator on functions. Instead, the instance of `TDelayedExecution` is returned. It is a subtype of `TDynamic`. You can access fields and functions on it, that will result in more `TDelayedExecution`s, but any attempt to convert to Kotlin will fail with an exception:

```kteval
val lorem = TNativeFunc("lorem".t)
println(lorem(15.t).type().repr())
println(lorem(15.t).strValue)
```

You can #link(<force>)[force] execution of a dynamic value, or, as other values can contain dynamic ones as their fields, you can force execution of any value, when you have #link(<compiler>)[Typst Compiler] instance.

==== TSelector <selector>

These are regular Typst `selector`s. They can be used for #link(<query>)[querying] Typst documents. For your convinience, `TRegex`, `TLabel` and `TElement` are subtypes of `TSelector`. And there are few functions, reflecting operations on selectors in Typst:

```kt
fun TSelector.before(selector: TSelector, inclusive: Boolean) : TSelector
fun TSelector.after(selector: TSelector, inclusive: Boolean) : TSelector

infix fun TSelector.before(selector: TSelector) : TSelector
infix fun TSelector.after(selector: TSelector) : TSelector

fun TSelector.and(vararg others: TSelector) : TSelector
fun TSelector.or(vararg others: TSelector) : TSelector
```

More on selectors can be found in the chapter about queries.

#pagebreak()

=== Compiler

To avoid confusion, from now on:
 - just "compiler" means an instance of `TypstCompiler` from `org.ldemetrios.tyko.compiler` package, and
 - "native compiler" means original Typst compiler, written in Rust.

The only implementation of compiler TyKo now has --- is `WorldBasedTypstCompiler`, which relies on entity similar, to native compiler's `trait World`. Besides that, it needs an instance of `TypstSharedLibrary`, which gives way to call native compiler.

TODO...

==== Force<force>
==== Query<query>
/*

```kteval
val tiling = TTiling(
    sz = TArray(30.pt, 30.pt),
    body = TSequence(
        TPlace(body = TLine(start = TArray(0.pc, 0.pc), end = TArray(100.pc, 100.pc))),
        TPlace(body = TLine(start = TArray(0.pc, 100.pc), end = TArray(100.pc, 0.pc))),
    )
)
println(tiling.repr())
```
*/

#context [#metadata(cnt.get())<saturn-import-num>]
<compiler>

