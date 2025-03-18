// #set text(fill: sys.inputs.at("kvasir-preview-foreground", default: black))
// #set page(fill: sys.inputs.at("kvasir-preview-background", default: white))
#set page(numbering: "1")

#let ignore-results = sys.inputs.at("saturn-run", default: false)

#let external-icon = "/external-link-svgrepo-com.svg"

#let external = {
    box(image(external-icon, height: .65em), baseline: -.2em, inset: (top: -2em))
}

#show link: it => {
    set text(fill: blue)
    underline(it, evade: true, offset: 1.5pt)
}

#let cnt = state("saturn", 0)

#let invoke(func, ..args, handler) = context {
    [#metadata((func, args))#label("saturn-import-" + str(cnt.get()))]
    if (ignore-results) [] else {
        handler(read("saturn-output/" + str(cnt.get()) + ".typc"))
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

#show raw.where(lang: "ktsafe"): it => {
    text(
        size: 1.25em,
        raw(it.text, lang: "kt", block: it.block),
    )
}

#let handler(mode, error, output, value: none) = if (mode == "none") {
    (..) => none
} else if (mode == "render") {
    text(fill: red, text(size: .9em, error))
    [#box(inset: 1em, stroke: (left: (paint: text.fill, dash: "dashed")), value)]
} else if (mode == "eval") {
    text(fill: rgb("#009900"), text(size: .9em, output))
    text(fill: red, text(size: .9em, error))
    [#value]
}

#show raw.where(lang: "kt"): it => context {
    let shebang = it.text.split("\n").at(0)

    let cont = if shebang.len() > 10 and shebang.slice(0, 10) == "//!saturn:" {
        let lines = it.text.split("\n")
        let code = lines.slice(1, lines.len()).join("\n").trim()
        text(
            size: 1.25em,
            box(listing(raw(code, lang: "kt"))),
        )
        let mode = shebang.slice(10, shebang.len())
        invoke(
            "eval_kt",
            mode,
            it.text,
            it => {
                let data = eval(it)
                if ("CE" in data) {
                    data.CE.join[\ ]
                } else {
                    let (error, output) = data

                    if "value" in data {
                        handler(mode, error, output, value: data.value)
                    } else {
                        handler(mode, error, output)
                    }

                    if ("throwable" in data) {
                        let t = data.throwable.split("\n")
                        text(
                            fill: red,
                            text(size: .9em, t.slice(0, t.len() - 20).join("\n    ")),
                        )
                    }
                }
            },
        )
    } else {
        // Do nothing?
        it
    }

    box(inset: (left: 1em), cont)
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
    #let sizes = (0, 2, 1.7, 1.8, 1.4, 1.4)
    #show heading: it => {
        set align(if (it.level < 5) { center } else { left })
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

#let code-header(it) = text(it.text, fill: rgb("#75F5E0").darken(50%))

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
    - [-] Downloading package from central repo
    - [x] Obtaining source code info (parse tree)
    - [ ] Annotation processor for writing overloads

#pagebreak()

== Contents

#outline(title: none)

#pagebreak()

== Library structure


Library consists of three packages: \ \

- `model`
This is the package with all the classes representing Typst values. They mostly are named as `T` and then the type name in UpperCamel: `TInt`, `TFootnoteEntry` etc. \ \

- `ffi`
This is the package with C-faced API. You need to explicitly mark your code
`@OptIn(TyKoFFIEntity::class)` to use most of its content, except `TypstSharedLibrary`. They are also not at all guaranteed to remain the same way. \ \

- `compiler`
This is, obviously, for accessing Typst compiler, and all the related things (World, Diagnostics etc). Some API in this package is expected to change, see warning in respective chapters \ \

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

==== #code-header(`TValue`)

`TValue` is the common interface for all Typst values. It has only two methods:

- ```ktsafe fun TValue.repr() : String```

Creates a code representation of the value. The resulting string is a valid code in Typst. It is also atomic from the point of the Typst compiler, so only prepending it by `#` is enough to make it valid Typst markup.

For now it is not human-readable in any way, but there are plans of improvement (that's why it's external func).
\ \
- ```ktsafe fun TValue.type() : TType```

Returns the `type` of the value.

Most of its inheritants are generated automatically, based on the information from the official documentation. There are, however, some types with specific behaviour.

===== #code-header(`TBool`), #code-header(`TInt`), #code-header(`TFloat`), #code-header(`TStr`), #code-header(`TArray`), #code-header(`TDictionary`), #code-header(`TBytes`)

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

```ktsafe
fun <E : TValue> TArray(vararg elements: E) : TArray<E>
fun <V : TValue> TDictionary(vararg pairs: Pair<String, V>) : TDictionary<V>
```

```kt
//!saturn:eval

val x = TArray(1.t, true.t)
val y = TDictionary("a" to "b".t, "c" to "d".t)

println(x.repr())
println(y.repr())
```

===== #code-header(`TAlignment`)

This is a representation of `alignment`. There are, also, specific types for horizontal and vertical alignment (`THAlignment` and `TVAlignment`), which are inheritants of the `TAlignment`. Addition is defined:

```kteval
val align = THAlignment.Center + TVAlignment.Horizon
println(align.repr())
```

===== Numeric types (#code-header(`TRatio`), #code-header(`TFraction`), #code-header(`TLength`), #code-header(`TRelative`), #code-header(`TAngle`))

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

```
//!saturn:eval
println(1.rad.repr())
println((1.em + 1.pt + 1.pc).repr())
```

More operations will be added later.


===== Colors

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

```ktsafe
//!saturn:eval
import java.awt.Color

println(Color.WHITE.t.repr())
```

===== Functions

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

```ktsafe
fun TFunction.apply(args: TArguments<*>): TValue
```

or with operators:

```ktsafe
operator fun TFunction.get(vararg named: Pair<String, TValue>) : TWith

operator fun TFunction.invoke(vararg args: TValue): TDynamic
```

The idea here is just type checking. As we can't declare parameter type `vararg Pair<...>|TValue`,
we separate them. If there are none named arguments, just use `(poistional, args)`.
If there are none positional arguments, use empty `()` (empty `[]` are not allowed):

```kt
//!saturn:eval

val lorem = TNativeFunc("lorem".t)
println(lorem(15.t).repr())
```

As you can see, no actual computation happened. `operator ()` returns an instance of
#link(<dynamic>)[`TDelayedExecution`], execution of which can be #link(<force>)[`force`]d with appropriate compiler.
The reason is, there can be different libraries, and execution of functions can require context.

===== Content

In addition, it has function ```ktsafe fun TContent.element() : TElement```.

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

```kt
//!saturn:render

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

```kt
//!saturn:render
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

```kt
//!saturn:render
TSequence {
    +"Just"
    +TSpace()
    +TEmph("formatted".text)
    +TSpace()
    +"text"
}
```

===== Styles (#code-header(`set`) and #code-header(`show`))

For each `element` class, if it has any settable parameters,
there exists corresponding `set element` class. The all extend `TSetRule`,
and inherently `TStyle`:

```kt
//!saturn:eval

println(TSetText(fill = java.awt.Color.RED.t).repr())
```

As well as `set`, there are `show` rules. As any `show` rule in Typst,
it can either contain `selector` or not, and its `transform` can be either content,
function, or an array of styles:

```kt
//!saturn:render

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

```kt
//!saturn:render

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

===== #code-header(`TDynamic`), delayed execution <dynamic>

`TDynamic` is the type that represents a value of yet unknown type. It is the subtype of _*all*_ the TyKo types, except for `TPoint` and `TLocation` (those are excluded due to generic variance problems). Therefore, typechecking can be postponed (usually, until the value is `repr`ed and sent to the Typst compiler).

As was mentioned before, no actual execution happens when you call `invoke` operator on functions. Instead, the instance of `TDelayedExecution` is returned. It is a subtype of `TDynamic`. You can access fields and functions on it, that will result in more `TDelayedExecution`s, but any attempt to convert to Kotlin will fail with an exception:

```kt
//!saturn:eval

val lorem = TNativeFunc("lorem".t)
println(lorem(15.t).type().repr())
println(lorem(15.t).strValue)
```

You can #link(<force>)[force] execution of a dynamic value, or, as other values can contain dynamic ones as their fields, you can force execution of any value, when you have #link(<compiler>)[Typst Compiler] instance.

===== TSelector <selector>

These are regular Typst `selector`s. They can be used for #link(<query>)[querying] Typst documents. For your convinience, `TRegex`, `TLabel` and `TElement` are subtypes of `TSelector`. And there are few functions, reflecting operations on selectors in Typst:

```ktsafe
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

#import "@preview/typsium-iso-7010:0.1.0": iso-7010

#let iconed(name, body) = {
    table(
        columns: 3,
        stroke: none,
        inset: 1em,
        align: top,
        box(iso-7010(name, width: 4.5em)), [], body,
    )
}

#iconed("W011")[
    #text(size: 1.3em)[_This API is subject to change._]\ #set text(size: .9em)
    This sign will appear in several parts of this chapter,
    because current scheme doesn't allow enough flexibility,
    and is not thread-safe.
]

To avoid confusion, from now on:
- just "compiler" means an instance of `TypstCompiler` from `org.ldemetrios.tyko.compiler` package, and
- "native compiler" means original Typst compiler, written in Rust.

The only implementation of compiler TyKo now has --- is `WorldBasedTypstCompiler`, which relies on entity similar
to native compiler's `trait World`. Besides that, it needs an instance of `TypstSharedLibrary`, which gives way
to call native compiler.

In a few words, `World` is what works as a bridge between native compiler and a filesystem.
Such filesystem can be:
- backed up with actual filesystem, as in Typst CLI
- consist of only one file (`SingleFileWorld`)
- contain no files at all (`DetachedWorld`)
- refer to virtual fs (`ProjectCompilerService` in #link("https://github.com/LDemetrios/Kvasir/")[Kvasir #external])
etc.

`TypstSharedLibrary` uses JNA to call functions in native compiler.
Most of them are unsafe to use and are marked `@TyKoFFIEntity`, but there are several
useful functions as well, which are described in #link(<shared>)[respective chapter].

`TypstCompiler` composes that all together and provides functions for evaluation,
compilation and querying.

#pagebreak()

==== World

#iconed("W043")[
    #text(size: 1.3em)[_This API is subject to change._]\
    This is the way it is only because it reflects native way of handling things. It will be later separated into several entities.
]

The `World` interface requires to implement several things:

```kt
interface World {
    fun library(): StdlibProvider
    fun mainFile(): FileDescriptor
    fun file(file: FileDescriptor): RResult<ByteArray, FileError>
    fun now(): WorldTime?
    val autoManageCentral: Boolean
}
```

===== #code-header(`library`)

#iconed("W065")[
    #text(size: 1.3em)[_This API is subject to change._]\
    Probably, there will be more flexibility in setting up what is stdlib
]

This method should return `StdlibProvider`, which describes what should be considered
a standard library. For now, it's only an instance of `StdlibProvider.Standard`, which
should define two values:

```ktsafe
val features: List<Feature>
val inputs: TDictionary<TValue>
```

First is the same list of features you pass via `--feature` flag in CLI.
The only possible feature available now is `Feature.Html`.

Second is what will be accessible through `sys.inputs`.
Unlike with CLI, values can be not only `str`s. For example,
Kvasir passes background and foreground colors of current theme.

The result of this method will be obtained only one time,
when first compilation is performed.

===== #code-header(`mainFile`)

#iconed("W075")[
    #text(size: 1.3em)[_This is not thread-safe._]\
    Because of this you can't compile several documents in parallel with single `World`.
]

This denotes an entry point to a compilation. This is called at least one time
per compilation, except for `evalDetached` calls (in which case --- 0 times).

The result of this function better not change during the compilation.

===== #code-header(`file`)

This function provides access to filesystem. The structure of file descriptors and errors is quite self-explanatory,
and closely follows that of native compiler. `RResult` is a analog of Rust `Result`, with two options `Ok` and `Err`.

Results of these functions are cached, you need to explicitly call #link(<reset>)[`.reset()`] on compiler.

===== #code-header(`now`)

This functions establishes "current" time for document. The result of this function is obtained once,
before the first compilation. It can be one of three:

- `WorldTime.System` . Before each compilation, compiler will ask system for current time, and sets it as document time.
- `WorldTime.Fixed(Instant)` . Sets time, provided by given Instant, as time for each document.
- `null` . Calls for time from document will result in error.

===== #code-header(`autoManageCentral`)

This value determines how native compiler should handle packages
from #link("https://typst.app/universe/")[Typst Universe #external]

- When set to `true`, the native compiler will download packages with `preview` namespace and
    cache in user's folder as it does in CLI.
- When set to `false`, it will ask `World` to provide access to files.

Files with non-null `PackageSpec`, but not `preview` namespace are unaffected by this flag,
attempts to access those will be redirected to the `World` regardless.

#pagebreak()

==== #code-header(`TypstSharedLibrary`) <shared>

This is an interface that provides access to the native compiler.
In theory you could provide your own implementation, but it's extremely unsafe.
The way you create an instance of this interface is through `TypstSharedLibrary.instance(Path)`.
The path should point to a correct shared library file for current OS and architecture.
It is forbidden to create more than one from `TypstSharedLibrary` from a single `.so`, `.dll` or `.dylib` file.
While `instance` method tries to prevent it from happening by caching instances, you will most likely get
an `IllegalStateException`. You, however, in theory are allowed to create Library from physically different files,
it was not tested.

Most of the functions require World to function, but there are several you can use without one.

===== #code-header(`format`)
```ktsafe
fun TypstSharedLibrary.format(string: String, column: Int, tab: Int): String
```

Formats the given source using #link("https://github.com/Enter-tainer/typstyle")[Typstyle #external].

===== #code-header(`parseSource`)

```ktsafe
fun TypstSharedLibrary.parseSource(string: String, mode: SyntaxMode): FlattenedSyntaxTree
```

Parses source in given `SyntaxMode` (either `Markup`, `Code`, or `Math`),
and returns flattened syntax tree. Flattened syntax tree is a list of indexed marks, each mark is either:
- Start of syntax node with given SyntaxKind
- Start of erroneous syntax node with given error message.
- End of syntax node.

===== #code-header(`anyInstance`)

Returns any of the created instances (expectedly the first created), or throws NoSuchElementException, if there are none.

===== #code-header(`evict_cache`)

As native compiler uses memoization, there could appear memory "leaks". For example, evaluating

```kt
fun main() {
    val world = WorldBasedTypstCompiler(sharedLib, DetachedWorld())
    for (i in 0 until 20000000) {
        world.evalDetached("1 + 2")
    }
}
```

causes 4.2Gb of cache to be allocated. That means, that when writing heavy applications with compilation
of differrent documents, you should call `evict_cache` from time to time. This function redirects call straight to
#link("https://docs.rs/comemo/latest/comemo/fn.evict.html")[`comemo::evict` #external].

#pagebreak()

==== Typst Compiler

There are several functions available.

===== Complile "raw"

#iconed("W039")[
    #text(size: 1.3em)[_This API is subject to change._]\
    It will later be separated into compilation to `PagedDocument`/`HtmlDocument`, and actual rendering, to allow partial (re-)compilation.
]

```ktsafe
fun TypstCompiler.compileHtmlRaw(): Warned<RResult<String, List<SourceDiagnostic>>>

fun TypstCompiler.compileSvgRaw(fromPage: Int, toPage: Int):
                Warned<RResult<List<String>, List<SourceDiagnostic>>>

fun TypstCompiler.compilePngRaw(fromPage: Int, toPage: Int, ppi: Float):
                Warned<RResult<List<ByteArray>, List<SourceDiagnostic>>>
```

`SourceDiagnostic` represents an error or a warning arose during compilation.
It as well closely follows native structs, with one difference being `Span`.
Unlike native Span, it already contains index, line and column for both text range start and end.
Indices are byte indices, not character indices! Those are the same only when you work with ASCII files.

PDF compilation is not yet available.

===== Compile

```ktsafe
fun TypstCompiler.compileHtml(): String

fun TypstCompiler.compileSvg(fromPage: Int = 0, toPage: Int = Int.MAX_VALUE):
                List<String>

fun TypstCompiler.compilePng(
    fromPage: Int = 0, toPage: Int = Int.MAX_VALUE, ppi: Float = 144.0f
) : List<ByteArray>
```
This ignores all warnings, and unwraps RResult into "natural JVM way" of handling things:
if it compiled, return result, if not, throw an exception.
Trace of the first error is then added to the stacktrace, others are added as suppressed.

```kt
//!saturn:eval
val source = """
    #let f(x) = 1 / 0
    #let g(x) = f(x) * 2
    #let h(x) = g(x)
    #show emph: it => h(it)
    This is _emphasized_
"""

val compiler = WorldBasedTypstCompiler(
    sharedLib, SingleFileWorld(source, Feature.ALL)
)

compiler.compileSvg()
```
\ 

```kt
//!saturn:render
val source = """
    #set heading(numbering: "1.")

    = Fibonacci sequence
    The Fibonacci sequence is defined through the
    recurrence relation.
    It can also be expressed in _closed form._

    #let count = 8
    #let nums = range(1, count + 1)
    #let fib(n) = (
        if n <= 2 { 1 }
        else { fib(n - 1) + fib(n - 2) }
    )

    The first #count numbers of the sequence are:
    #(nums.map(fib).map(str).join(", "))
""".trimIndent()

val compiler = WorldBasedTypstCompiler(
    sharedLib, SingleFileWorld(source, Feature.ALL)
)

TRaw(compiler.compileHtml().t, lang = "html".t)
```

===== Query <query>

```ktsafe
fun TypstCompiler.queryRaw(selector: String, format: SerialFormat):
                Warned<RResult<String, List<SourceDiagnostic>>>

fun TypstCompiler.query(selector: TSelector): TArray<TValue>
```

As clearly seen, `queryRaw` takes selector as a String, and its `format` can be any
serialization format Typst supports: PrettyJSON, JSON, YAML.
`query` returns just the queried values (it uses JSON under the hood,
but that's a story for another time). Unlike official Typst compiler,
TyKo can properly serialize and deserialize any value,
with, probably, slight hiccups on functions (those are still WIP).

````kt
//!saturn:render
val source = """
    #show raw.where(lang: "typ") : it => [
        #metadata(it) <x>
        #it
    ]

    Parentheses are smartly resolved, so you can enter your expression as
    you would into a calculator and Typst will replace parenthesized
    sub-expressions with the appropriate notation.

    ```typ
    $ 7.32 beta + sum_(i=0)^nabla (Q_i (a_i - epsilon)) / 2 $
    ```
    Preview
    Not all math constructs have special syntax.
    Instead, we use functions, just like the image function we have seen before.
    For example, to insert a column vector, we can use the vec function.
    Within math mode, function calls don't need to start with the \# character.
    ```typ
    $ v := vec(x_1, x_2, x_3) $
    ```
"""

val compiler = WorldBasedTypstCompiler(
    sharedLib, SingleFileWorld(source, Feature.ALL)
)

val code = compiler.query(TLabel("x".t))
    .map { it as TMetadata<*> }
    .map { it.value as TRaw }
    .map { compiler.evalDetached("[\n" + it.text.strValue + "\n]")  }

TSequence(TArray(
    code.flatMap { listOf(it as TContent, TParbreak(), TParbreak()) }
))
````

===== Eval detached

#iconed("W002")[
    #text(size: 1.3em)[_This API is subject to change._]\
    It will later require only stdlib, not the World, to eval.
]

```ktsafe
fun TypstCompiler.evalDetachedRaw(source: String):
                RResult<String, List<SourceDiagnostic>>

fun TypstCompiler.evalDetached(source: String): TValue
```

`evalDetached` just evaluates an expression. Note that it treats an expression as in code mode,
not markup mode. Warnings are not issued here.

===== Reset <reset>


#iconed("W035")[
    #text(size: 1.3em)[_This API is subject to change._]\
    It will later be possible to partially clear cache, and share file cache between several compilations.
]

```ktsafe
fun TypstCompiler.reset()
```

This function clears the file cache inside the compiler.


===== Force <force>

```kt
fun TypstCompiler.force(value: TValue): TValue {
    return evalDetached(value.repr())
}
```

This function forces execution of any `TDelayedExecution` in the value.
This is the same as call `repr` and then `evalDetached`... which is exactly what `force` does.

```kt
//!saturn:eval
val compiler = WorldBasedTypstCompiler(sharedLib, DetachedWorld())

val lorem = TNativeFunc("lorem".t)
println(lorem(15.t).repr())
println()
println(compiler.force(lorem(15.t)).repr())
```

It, however, *_does not_* expand (apply) `set` and `show` rules. That's what _realization_ is for,
and it is not supported here yet.

#context [#metadata(cnt.get())<saturn-import-num>]
<compiler>


#pagebreak()

== Examples & applications

=== Did you mean "recursion"?

This entire documentation is created with the help of TyKo. TODO...
