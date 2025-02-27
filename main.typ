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

#let listing = if ignore-results { it => it } else {
  import "@preview/codelst:2.0.1": sourcecode
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
        let (error, output, value) = data
        text(fill: rgb("#009900"), text(size: .9em, output))
        text(fill: red, text(size: .9em, error))
        [#value]
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

  #set outline(depth: 5, indent: 2em)

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

#let marked(it) = box(inset: (left: 2em), box([\ #it\ #" "], outset: (left: 1em), stroke: (left: black + .5pt)))

#marked[
  - ```kt fun TValue.repr() : String``` #external

  Creates a code representation of the value. The resulting string is a valid code in Typst. It is also atomic from the point of the Typst compiler, so only prepending it by `#` is enough to make it valid Typst markup.

  For now it is not human-readable in any way, but there are plans of improvement (that's why it's external func).
  \ \
  - ```kt fun TValue.type() : TType```

  Returns the `type` of the value.
]

Most of its inheritants are generated automatically, based on the information from the official documentation. There are, however, some types with specific behaviour.

====== `TBool`, `TInt`, `TFloat`, `TStr`, `TArray`, `TDictionary`, `TBytes`

These are the types, analogs of which exist in Kotlin. They can be instantiated with `<value>.t`, where `<value>` is respectively:

#{
  set align(center)
  table(
    align: left,
    columns: 3,
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

====== `TAlignment`

This is a representation of `alignment`. There are, also, specific types for horizontal and vertical alignment (`THAlignment` and `TVAlignment`), which are inheritants of the `TAlignment`. Addition is defined:

```kteval
val align = THAlignment.Center + TVAlignment.Horizon
println(align.repr())
```

====== Numeric types (`TRatio`, `TFraction`, `TLength`, `TRelative`, `TAngle`)


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


