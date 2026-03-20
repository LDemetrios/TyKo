//#set page(fill: sys.inputs.at("kvasir-preview-background", default: black))
//#set text(fill: sys.inputs.at("kvasir-preview-foreground", default: white))

//#set page(fill: black)
//#set text(fill: white)
#let evaluate = true

#let getfield = 1
#let getstatic = 2
#let putfield = 3
#let putstatic = 4
#let invokevirtual = 5
#let invokestatic = 6
#let constructor = 8

#let upcall = upcall.with(breakglass: read("key.txt"))
#let define-class = define-class.with(breakglass: read("key.txt"))
#let lookup-in = lookup-in.with(breakglass: read("key.txt"))

#assert.eq(upcall(invokestatic, "java.lang.Integer#sum:(II)I", 1, 2), 3)

#let Example = define-class(
    "org.ldemetrios.tyko.manual.jar.Example",
    read(
        "manual-plugins/jar-loader/build/classes/java/main/org/ldemetrios/tyko/manual/jar/Example.class",
        encoding: none,
    ),
)

#let exampleLookup = lookup-in(Example)

#let adder = upcall(
    lookup: exampleLookup,
    constructor,
    "org.ldemetrios.tyko.manual.jar.Example#<init>:()V",
)

#assert.eq(
    3,
    upcall(
        lookup: exampleLookup,
        invokevirtual,
        "org.ldemetrios.tyko.manual.jar.Example#add:(SS)I",
        adder,
        1,
        2,
    ),
)

#let JarLoader = define-class(
    "org.ldemetrios.tyko.manual.jar.JarLoader",
    read(
        "manual-plugins/jar-loader/build/classes/java/main/org/ldemetrios/tyko/manual/jar/JarLoader.class",
        encoding: none,
    ),
)
#let JarLoaderLookup = lookup-in(JarLoader)

#let read-u32(data, offset) = int.from-bytes(
    data.slice(offset, count: 4),
    endian: "big",
    signed: false,
)

#let unjar(bytearr) = {
    let decoded = upcall(
        lookup: JarLoaderLookup,
        invokestatic,
        "org.ldemetrios.tyko.manual.jar.JarLoader#unjar:([B)[B",
        bytearr,
    )
    let total-entries = read-u32(decoded, 0)
    let lengths-start = 4
    let payload-start = lengths-start + total-entries * 4

    let lengths = range(total-entries).map(index => read-u32(decoded, lengths-start + index * 4))

    let pairs = range(calc.floor(total-entries / 2)).map(it => {
        let name-length = lengths.at(it * 2)
        let class-length = lengths.at(it * 2 + 1)
        let pair-offset = payload-start + lengths.slice(0, count: it * 2).sum(default: 0)
        let name = str(decoded.slice(pair-offset, count: name-length))
        let classfile = decoded.slice(pair-offset + name-length, count: class-length)
        (name, classfile)
    })

    pairs.to-dict()
}

#let define-classes(classfiles, class-names) = (
    class-names
        .map(name => (
            name,
            define-class(name, classfiles.at(name)),
        ))
        .to-dict()
)

#let launcher-classfiles = unjar(read("manual-plugins/launcher/build/libs/launcher-0.5.a.jar", encoding: none))
#let launcher-classnames = (
    "org.ldemetrios.tyko.manual.launcher.ExecutedCommand",
    "org.ldemetrios.tyko.manual.launcher.ProjectLaunchResult",
    "org.ldemetrios.tyko.manual.launcher.TemporaryProjectLauncher",
)
#let launcher-classes = define-classes(launcher-classfiles, launcher-classnames)

#let LauncherLookup = lookup-in(launcher-classes.at("org.ldemetrios.tyko.manual.launcher.TemporaryProjectLauncher"))

#let launch-temp-project(name, files, commands) = {
    let launcher = upcall(
        lookup: LauncherLookup,
        constructor,
        "org.ldemetrios.tyko.manual.launcher.TemporaryProjectLauncher#<init>:()V",
    )
    upcall(
        lookup: LauncherLookup,
        invokevirtual,
        "org.ldemetrios.tyko.manual.launcher.TemporaryProjectLauncher#launch:(Lorg/ldemetrios/tyko/model/TStr;Lorg/ldemetrios/tyko/model/TDict;Lorg/ldemetrios/tyko/model/TArray;)Lorg/ldemetrios/tyko/model/TDict;",
        launcher,
        name,
        files,
        commands,
    )
}

#set page(numbering: "1")
#set page(margin: 70.87pt * 2 / 3)

#let external-icon = "/external-link-svgrepo-com.svg"

#let external = {
    box(image(external-icon, height: .65em), baseline: -.2em, inset: (top: -2em))
}

#show link: it => {
    set text(fill: blue)
    underline(it, evade: true, offset: 1.5pt)
}

// #import "@preview/cetz:0.2.2"
// #import "@preview/tablex:0.0.8": *
// #import "@preview/zero:0.3.0": num
#import "@preview/cheq:0.2.2": checklist

#show table: block.with(breakable: false)

#let listing = {
    import "@preview/codelst:2.0.2": sourcecode
    sourcecode.with(frame: none)
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

#let sizes = (0, 2, 1.7, 1.65, 1.4, 1.4)
#show heading: it => {
    set align(if (it.level < 3) { center } else { left })
    set text(sizes.at(it.level, default: 1) * 1.2em)
    smallcaps(it)
}

#set outline(depth: 8, indent: 2em)

#set par(justify: true)
#show raw.where(block: true): set par(justify: false)

#show raw.where(block: false): box
#show raw.where(block: true): box.with(inset: 1em)

#show: checklist

#show regex("\(since \d\.\d(\.\d)?\)"): it => {
    show regex("\d\.\d(\.\d)?"): jt => raw(jt.text)
    it
}

#let code-header(it) = context text(
    it.text,
    fill: color.mix((rgb("#75F5E0"), 70%), (text.fill, 20%), space: color.cmyk),
)

#let def-style = state("def-style")
#context def-style.update(text.font)

#show selector(heading).or(outline): it => {
    show raw: code => context {
        set text(font: def-style.get(), size: 1.25em)
        code-header(code)
    }
    it
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

/*
#let launched-project = launch-temp-project(
    "manual-launcher-demo",
    (
        "hello.txt": bytes("hello from launcher"),
    ),
    (
        ("sh", "-c", "cat hello.txt"),
        ("read", "hello.txt"),
    ),
)

#let test-project = arguments(
    "test-gradle",
    (
        "settings.gradle.kts": read("manual-plugins/settingsf"),
        "build.gradle.kts": read("manual-plugins/fb"),
        "src/main/kotlin/Main.kt": ```
            fun main() {}
        ```.text,
    ).map(bytes),
    (
        ("gradle", "run"),
    ),
)

#launch-temp-project(..test-project)
*/

#show "TyKo": smallcaps

#{
    set text(3.5em)
    set align(center)
    [*TyKo*]
}

TyKo /taɪko/ is project aiming to achieve interoperability between Typst and JVM languages
(primary Kotlin, but theoretically all of them).

Here's a roadmap, what it allows and what it plans to:

- [/] Manipulating Typst values from JVM
    - [x] Basic types
    - [x] Content and other types
    - [x] Set, show rules
    - [/] Functions (hiccups on closures that capture location, plugin or module)
    - [ ] Plugins, modules
- [/] Calling Typst compiler from JVM
    - [x] Compilation (PNG, SVG, PDF, HTML)
    - [x] Query
    - [ ] Watching
    - [x] Evaluation
    - [x] World customization
        - [x] Library customization
            - [x] Standartized (`inputs`, `features`)
            - [x] Registering custom functions
        - [x] Fonts
        - [x] Styles
        - [x] Virtual file system
    - [ ] Realization (set/show rule expansion)
- [x] Calling JVM from Typst document
    - [x] Propagating JVM functions as Typst functions
    - [x] Loading JARs as plugins (Possible to achieve through other features, not supported directly)
    - [x] Freely throwing around JVM objects in Typst code
- [/] Misc
    - [x] Downloading package from central repo
    - [x] Obtaining source code info (parse tree)
    - [ ] Annotation processor for writing overloads

Since version `0.4.0` the project is pure-JVM, self-contained, and doesn't require any platform-dependent binaries or
preinstalled programs. However, using native binary might be a more fast options: see details in #link(<drivers>)[Drivers] section

#pagebreak()

= Contents

#outline(title: none)

#pagebreak()

= Quick#super(typographic: false, baseline:-.5em, size:.5em)[ish] Start

For the simplest code snippet, we will need two dependencies. In `gradle.kts` syntax:

```kts
implementation("org.ldemetrios:tyko-runtime:0.5.a")
implementation("org.ldemetrios:tyko-drivers-chicory-based:0.5.a")
```

#let def-hid-imp = ```
import org.ldemetrios.tyko.compiler.*
import org.ldemetrios.tyko.conversion.*
import org.ldemetrios.tyko.driver.chicory_based.ChicoryTypstCore
import org.ldemetrios.tyko.model.*
import org.ldemetrios.tyko.runtime.*
```

#let gradle(
    name,
    hidden-imports: def-hid-imp,
    explicit-imports: ```,
    ```,
    hidden-code: ```
    ```,
    explicit-code,
    callback: _ => [],
) = {
    let code = ""
    code += hidden-imports.text

    code += "\nimport kotlin.io.path.writeText\n"
    code += "import kotlin.io.path.writeBytes\n"
    code += "\n\n"
    code += explicit-imports.text
    code += "\n\n"
    code += ```kt
    fun kotlin.reflect.KType.toShortString() = toString().replace(kotlin.text.Regex("([a-zA-Z0-9]*\\.)*([a-zA-Z0-9]+)")) {
        it.groupValues[2]
    }
    inline fun <reified T> compileTypeOf(x: T) = kotlin.reflect.typeOf<T>()

    ```.text
    code += "fun main() {\n"
    code += hidden-code.text
    code += "\n"
    code += ```
        System.setOut(java.io.PrintStream(java.io.FileOutputStream("out.txt")))
        System.setErr(java.io.PrintStream(java.io.FileOutputStream("err.txt")))

        try {
             val resultOfEvaluation = run {

    ```.text
    code += explicit-code.text.split("\n").map(it => "             " + it).join("\n")
    code += "\n        }\n"
    code += ```
            if ((resultOfEvaluation as Any?) is ByteArray) {
                java.io.File("resbytes.txt").writeBytes(resultOfEvaluation as Any? as ByteArray)
            }
            if ((resultOfEvaluation as Any?) is IntoValue) {
                java.io.File("resvalue.txt").writeText((resultOfEvaluation as Any? as IntoValue).repr())
            }
            java.io.File("res.txt").writeText(
                resultOfEvaluation
                    .toString()
                    .replace(kotlin.text.Regex("([a-zA-Z0-9]+=null)(, )?")) { "" }
                    .replace(", )", ")")
            )
            java.io.File("type.txt").writeText(compileTypeOf(resultOfEvaluation).toShortString())
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
    ```.text

    if (evaluate) {
        let result = launch-temp-project(
            name,
            (
                "settings.gradle.kts": read("manual-plugins/settingsf"),
                "build.gradle.kts": read("manual-plugins/fb"),
                "src/main/kotlin/Main.kt": code,
            ).map(bytes),
            (
                ("gradle", "run"),
                ("read", "out.txt"),
                ("read", "err.txt"),
                ("read", "resbytes.txt"),
                ("read", "resvalue.txt"),
                ("read", "res.txt"),
                ("read", "type.txt"),
            ),
        )
        if (result.executedCommands.at(0).exitCode == 0) {
            //            [#result.readFiles.map(str)]

            let code = if (explicit-imports.text.trim() == "") { explicit-code.text } else {
                explicit-imports.text + "\n\n" + explicit-code.text
            }
            raw(lang: "kt", block: true, code.trim())
            callback(result.readFiles)
        } else {
            text(fill: red, raw(block: true, result.executedCommands.at(0).stderr))
        }
    } else {
        let code = if (explicit-imports.text.trim() == "") { explicit-code.text } else {
            explicit-imports.text + "\n\n" + explicit-code.text
        }
        raw(lang: "kt", block: true, code)
    }
    //        raw(lang: "kt", block: true, code)
}

#let def-bytes = bytes("")

#let stdouterr(files) = {
    if (
        (files.at("out.txt", default: def-bytes) != def-bytes) or (files.at("err.txt", default: def-bytes) != def-bytes)
    ) {
        box(
            inset: (left: 1em, right: -100em),
            box(
                inset: (x: .5em),
                stroke: (left: (paint: text.fill)),
                [
                    #if (files.at("out.txt", default: def-bytes) != def-bytes) {
                        text(
                            fill: color.mix(space: rgb, (green, 50%), (text.fill, 50%)),
                            raw(block: true, str(files.at("out.txt")).trim()),
                        )
                    }
                    #if (files.at("err.txt", default: def-bytes) != def-bytes) {
                        text(
                            fill: color.mix(space: rgb, (red, 100%), (text.fill, 50%)),
                            raw(block: true, str(files.at("err.txt")).trim()),
                        )
                    }
                ],
            ),
        )
    }
}

#let evalkt = gradle.with(
    callback: files => context {
        stdouterr(files)
        if (str(files.at("res.txt", default: "kotlin.Unit")) != "kotlin.Unit") {
            let code = str(files.at("type.txt")) + ":\n\n" + str(files.at("res.txt"))
            block(
                breakable: true,
                inset: (x: 1em),
                block(breakable: true, inset: (x: .5em), stroke: (left: (paint: text.fill)), raw(block: true, code)),
            )
        }
    },
)

#let renderkt(on-bytes) = gradle.with(
    callback: files => context {
        stdouterr(files)
        [#box(inset: 1em, stroke: (left: (paint: text.fill, dash: "dashed")), on-bytes(files.at("resbytes.txt")))]
    },
)

#let svgkt = renderkt(b => image(b, format: "svg"))

#let contentkt = gradle.with(
    callback: files => context {
        stdouterr(files)
        eval(str(files.at("resvalue.txt")))
    },
)

Then, we need to create a TypstRuntime object. Let's for now not care what exactly it means.

```kt
val runtime = TypstRuntime { ChicoryTypstCore() }
```

Let's compile something to png. We will launch compilation in the virtual file system with a single file.
By default, the entry point of the compilation is `"/main.typ"`

#evalkt(
    "simple-compile-png",
    hidden-code: ```kt
        val runtime = TypstRuntime { ChicoryTypstCore() }
    ```,
    ```kt
    runtime.compilePng {
        files {
            singleFile("/main.typ", "Hello, world!")
        }
    }
    ```,
)

What did we get? Each compilation produces either success, or errors; and also potentially warnings.
`Warned` is the type consisting of the result and warnings.
`RResult` (Rust's `Result`, because there already is `Result` in Kotlin stdlib) is more or less "`Either`".
`SourceDiagnostic` is an error or a warning. They contain all the useful stuff Typst's compiler tells us about what went wrong:
the span in source file, the message, the trace. `List<ByteArray>` is the part that differs for compilation targets:
it's `List<ByteArray>` for png, `List<String>` for svg, `ByteArray` for pdf, `String` for html.

This closely reflect how Typst's compiler thinks about compilation. Rust typically uses ADTs like `Result`
or `Option` for represnting stuff with errors. However, Kotlin, following Java, uses exception.
So there are a few methods that convert one approach to another:

#context align(
    center,
    table(
        columns: 3,
        align: left,
        stroke: text.fill,
        [*Initial type*], [*Method*], [*Resulting type*],
        `RResult<T, List<SourceDiagnostics>>`, `orThrow`, `T`,
        `Warned<RResult<T, List<SourceDiagnostics>>>`, `outputOrThrow`, `T`,
        `Warned<RResult<T, List<SourceDiagnostics>>>`, `warnedOrThrow`, `Warned<T>`,
    ),
)

In case of the error, `TypstCompilerException` is thrown. `TyKo` tries to convert `Span`s from the original
to `StackTraceElement`s, appending them to stacktrace:

//#evalkt(
//    "error-compile-png",
//    hidden-code: ```kt
//        val runtime = TypstRuntime { ChicoryTypstCore() }
//    ```,
```kt
runtime.compilePng {
    files {
        singleFile("/main.typ", """
            #let f(x) = lorem(x)
            #let g(h) = h(-1)
            #g(f)
        """.trimIndent())
    }
}.outputOrThrow()
```,
//)

#import "stacktrace.typ": stacktrace

#stacktrace(
    arc-center: (73, 15),
    ```
    org.ldemetrios.tyko.compiler.TypstCompilerException: number must be at least zero
        at main.typ.f(main.typ:1)
        at main.typ.g(main.typ:2)
        at main.typ.<source>(main.typ:3)
        at org.ldemetrios.tyko.compiler.TypstCompilerException$Companion.construct(TypstCompilerException.kt:21)
        at org.ldemetrios.tyko.compiler.TypstCompilerException$Companion.construct$default(TypstCompilerException.kt:20)
        at org.ldemetrios.tyko.compiler.UtilsKt.orThrow(Utils.kt:12)
        at org.ldemetrios.tyko.compiler.UtilsKt.outputOrThrow(Utils.kt:15)
        at MainKt.main(Main.kt:31)
        at MainKt.main(Main.kt)
    ```,
)


What else can we declare inside `files` block? File descriptor consists of, optionally, `PackageSpec`,
and a virtual path `String`. By given descriptor, we return either the content of the file (byte array), or
a `FileError`.

#svgkt(
    "complex-compile-svg",
    hidden-code: ```kt
        val runtime = TypstRuntime { ChicoryTypstCore() }
    ```,
    ```kt
    runtime.compileSvg {
        files {
            singleFile("/main.typ", """
                #set page(height :auto, margin : 20pt)
                #import "@preview/fletcher:0.5.6" as fletcher: diagram, node, edge
                #diagram(cell-size: 15mm, $
                   G edge(f, ->) edge("d", pi, ->>) & im(f) \
                   G slash ker(f) edge("ur", tilde(f), "hook-->")
                $)
            """.trimIndent())

            inPackage(null) { fd, session -> RResult.Err(FileError.NotFound(fd)) }

            fallback { fd, session -> runtime.resolvePackage(fd) }
        }
    }.outputOrThrow().single().toByteArray()
    ```,
)

Here, we have one file declared explicitly, then fallback for the rest of the files in package "null"
(in the actual project, that is), and the fallback for all other files, which calls `resolvePackage`.

You can see, there still rises the problem of error handling mismatch.
And if that is more convenient for your style, there is a hierarchy of `Exception`s,
mirroring the `FileError` adt, so you can instead write:

```kt
inPackageCatching(null) { fd, session ->
    throw FileErrorException.FileNotFoundException(fd)
}
```

Besides, you could've noticed there's a `session` parameter in that lambda.
It's dedicated to handling JVM objects, but we'll get to that later (see #link(<jvm>)[JVM Object] chapter).
For now, just ignore it.

What else do we have? Typst have another powerful mechanism, `query`.
It returns not just a stuff of known format, but actual Typst values, which, in case of CLI,
are serialized into json or yaml. Therefore, we need a model to represent
Typst value. And we have that, a `TValue` hierarchy. Almost all of the types#footnote[At the time, `plugin`, `module` and `location` are not fully supported]
have their respective `T<Name>` class.

#evalkt(
    "ratio-ex",
    ```kt
    val ratio = TRatio(TFloat(.7))
    println(ratio.repr())
    ```,
)

For query to work, we need a selector.

#evalkt(
    "selector-ex",
    ```kt
    val selector = THeading.ELEM.where("level" eq TInt(1), "outlined" eq TBool(true))
    println(selector.repr())
    ```,
)

Worth notice that `repr` of a `TValue` is not the same as #repr inside Typst.
For `TValue`s, it only produces correct code, that evaluates to the value.
For comparison, real #repr for that selector would be #box[#(heading.where(level:1, outlined:true))].


#evalkt(
    "query-ex",
    hidden-code: ```kt
        val runtime = TypstRuntime { ChicoryTypstCore() }
        val selector = THeading.ELEM.where("level" eq TInt(1), "outlined" eq TBool(true))
    ```,
    ```kt
    runtime.query(selector) {
        files {
            singleFile("/main.typ", """
                = Introduction
                #lorem(10)
                #pagebreak()

                == Sub-Heading
                #lorem(8)

                = Discussion
                #lorem(18)
            """.trimIndent())
        }
    }.outputOrThrow()
    ```,
)

The good thing is, the type `TArray<THeading>` is auto-inferred from the selector:
it's clear that, `heading.where(..)` select, well, headings. These types are propagated
through selector construction (however, they might be lost during `selector.and` construction).

The bad thing... Kotlin still doesn't support proper union types, and neither does Java.
I racked my brains trying to fit all the required combinations into the hierarchy.
To make matters worse, there are synthetic types, types with additional constraints.
Like point is an array with exactly two elements of the same type. Et cetera, et cetera.
Visit #link(<model>)[Model] chapter if you want to know more.

There are two potential events, that might screw it completely and make me redesign model from the ground up.
First, that might be Typst introducing custom types. In this case, TyKo would need to support
those as well. Second, Kotlin team already has discussion about union-like types
for errors. In current state it only could replace `RResult`, and couldn't be used
for hierarchies containing generics (like `TArray` or `TDict` do), and I don't particularly like
the idea of abandoning generics for those. Threads about Ceylon-like unions have been going
around since at least 2012, but apparently in vain. But if at any future time they finally support union types,
TyKo will migrate to use them.

Anyway. For now, TyKo doesn't support `watch`ing, although, it has long been requested,
and it's one of the next big priorities. But TyKo has
a function to `eval` content to a TValue. It's not like `eval` function in Typst:
it tracks spans, it has access to VFS, in a word, it behaves like `compile`, but doesn't
perform realization and layout:

#evalkt(
    "query-ex",
    hidden-code: ```kt
        val runtime = TypstRuntime { ChicoryTypstCore() }
    ```,
    ```kt
    runtime.eval {
        files {
            singleFile("/main.typ", """
                #metadata(1 + 2)
            """.trimIndent())
        }
    }.outputOrThrow()
    ```,
)

That's all with the operations, but not with customization. So far we've been only changing files.
But there are few things we could set up.

Most notably, there is Library, what the Typst compiler considers to be its stdlib.
This includes features (html and a11y), `inputs`, custom functions and definitions, and styles by default.
For the library used by default, it's neither of the two features supported, empty inputs, and empty styles.
You can also specify the specific FileDescriptor as entry point.

All right then.

#evalkt(
    "lib-ex",
    hidden-code: ```kt
        val runtime = TypstRuntime { ChicoryTypstCore() }
    ```,
    ```kt
    runtime.compileHtml(FileDescriptor(null, "/start.typ")) {
        library {
            fresh(setOf(Feature.Html))

            inputs {
                "key"(1.t)
            }

            styles {
                add(TSetText(size = 20.0.t.pt))
            }

            values {
                "conifer"(TRgbColor(0x9f.t, 0xEB.t, 0x52.t, 0xFF.t))
            }
        }
        files {
            singleFile("/start.typ", """
                The input is: #sys.inputs.key

                #context text.size

                #conifer

                #text(fill: conifer)[This is not colored, because Typst doesn't support that]
            """.trimIndent())
        }
    }.outputOrThrow()
    ```,
)

Functions are a little bit more complicated. In short, they accept `arguments` (represented by `TArgs<**>`),
and optionally a context (represented by `TStyles`); and return either a result (`TValue`), or an error (String).

#evalkt(
    "funcs-ex",
    hidden-code: ```kt
        val runtime = TypstRuntime { ChicoryTypstCore() }
    ```,
    ```kt
    runtime.compileHtml {
        library {
            fresh(setOf(Feature.Html))

            func("println") {
                param("value") {
                    input(TStr.TYPE)
                    positional = true
                    named = false
                    required = true
                }

                returns(TNone.TYPE)

                pure {
                    val value = it.positional[0] as TStr
                    println(value.value)
                    RResult.Ok(TNone)
                }
            }

            func("infer-size") {
                returns(TFloat.TYPE)

                contextual { _, ctx ->
                    ctx.value.filterIsInstance<TSetText>()
                        .firstNotNullOfOrNull { it.size?.abs }
                        ?.let { RResult.Ok(it) }
                        ?: RResult.Err("No size is set")
                }
            }
        }
        files {
            singleFile("/main.typ", """
                #set text(size: 20pt)

                #println("Hello, world")

                #context infer-size()
                """.trimIndent())
        }
    }.outputOrThrow()
    ```,
)

Well, yeah, RResult again. Even worse, it assumes the error is just a string, no stacktraces.
But like that's gonna stop us! Of course, we have a catching option:


//#evalkt(
//    "funcs-throw-ex",
//    hidden-code: ```kt
//        val runtime = TypstRuntime { ChicoryTypstCore() }
//    ```,
```kt
fun f(): Nothing = throw AssertionError("Error")
fun g(): Nothing = f()

runtime.compileHtml {
    library {
        fresh(setOf(Feature.Html))

        func("throw") {
            returns(TNone.TYPE)

            pureCatching {
                g()
            }
        }
    }
    files {
        singleFile("/main.typ", """
            #throw()
            """.trimIndent())
    }
}.outputOrThrow()
```
//)

#stacktrace(
    arc-center: (70, 17),
    ```
    org.ldemetrios.tyko.compiler.TypstCompilerException: java.lang.AssertionError: Error
        at MainKt.main$lambda$1$f(Main.kt:23)
        at MainKt.main$lambda$1$g(Main.kt:24)
        at MainKt.main$lambda$1$0$0$0$0(Main.kt:34)
        at org.ldemetrios.tyko.runtime.NewFunctionSpecBuilder.pureCatching$lambda$0(LibrarySpecBuilder.kt:189)
        at main.typ.<source>(main.typ:1)
        at org.ldemetrios.tyko.compiler.TypstCompilerException$Companion.construct(TypstCompilerException.kt:21)
        at org.ldemetrios.tyko.compiler.TypstCompilerException$Companion.construct$default(TypstCompilerException.kt:20)
        at org.ldemetrios.tyko.compiler.UtilsKt.orThrow(Utils.kt:12)
        at org.ldemetrios.tyko.compiler.UtilsKt.outputOrThrow(Utils.kt:15)
        at MainKt.main(Main.kt:43)
        at MainKt.main(Main.kt)
    ```,
)


Funny enough, the part before "main.typ" is actually a whole message, that just happen to contain
formatted `StackTraceElement`s.



/*

There are several layers of abstraction:

#import "@preview/fletcher:0.5.8" as fletcher: diagram, node, edge

#context align(center)[#diagram(
	edge-stroke: text.fill,
	spacing: (.5em, 2em),
$
	& #`runtime` \
	edge("ur", "<|-") #`compiler` & & edge("ul", "<|-") #`model` & \
	edge("u", "<|-") #`core` \
	edge("u", "<|-") #`driver`
$)]

You need a drive
*/

#pagebreak()

= Model <model>

Model is a module that mostly consists of Kotlin classes representing Typst types.
The supertype for them is `TValue`. Well, actually, there's a wider hierarchy, `IntoValue`.
Well, actually, there's even wider type, `Argument`. Whyyyy?!

You see, Kotlin doesn't support union types. Typst, however, has union types
all over. For example, `gradient.linear.stops` is an array. Array of what?
Of either `color`s or `array`s. But the latter aren't just arrays, they are arrays of size specifically 2,
where the first element is `color`, and the second is `ratio|angle`. That sounds like a separate type to me,
and it is: `TGradientStop`. Actually, it doesn't support angles. But that doesn't really matter. Types of such nature implement `IntoValue`, but not `TValue`. They have method `intoValue()` that returns
the `TValue` that has the same Typst semantics.

But we were talking about union types. Sometimes, you can find a reasonable common name for stuff, like
`TPaint` to describe either `TGradient`, `TColor` or `TTiling` (this name was adapted from original Typst compiler).
But there are quite a lot of these unions. For that reason, an early version of TyKo had names like `TAutoOrColorOrFunctionOrGradientOrWhateverElseNeededToBeThere`.
Which, with generics, made code really unreadable. And, even though there is now a much cleaner hierarchy, I would still advise
you to not have those "unions" explicitly declared anywhere in your code.

What's an `Argument`, then? It's another union. There are places where you expect either `IntoValue`, or `Pair<String, IntoValue>`.
Argument is exactly that --- either "positional" `IntoValue`, or "named" `NamedArgument`, constructed with infix functions `eq` instead of `to`.
It has already appeared in the manual:

```kt
THeading.ELEM.where("level" eq TInt(1), "outlined" eq TBool(true))
```

Also, it surely is cumbersome to create `TInt`, `TBool` etc by hand.
At the time, there are extension `val t` properties:

#evalkt("t-ex", `1.t`)

They work for primitives, `String`s and `ByteArray`s. In the future, it is planned to have annotation processor,
that would generate necessary overloads, and extend TyKo with such overloads as well. Hypothetic example:

```kt
@TyKoOverloads(allKotlin = false)
operator fun TInt.plus(x: TInt) = TInt(this.value + x.value)
```

->

```kt
operator fun TInt.plus(x: TInt) = TInt(this.value + x.value)
operator fun TInt.plus(x: Long) = this.plus(TInt(x))
operator fun Long.plus(x: TInt) = TInt(this).plus(x)
```

With that out of the way, let's go through some types, which caveats do they have, etc.

== Basic types

==== Primitives

For primitives, everything is simple. You have `TBool` for booleans, `TInt` for integrals (`i64`, `Long`),
`TFloat` for floating point (`f64`, `Double`), `TStr` for strings.

==== Float-like values

These include `TAngle`, `TFraction`, `TRatio`.

#evalkt(
    "float-like",
    ```kt
    val x = 1.0.t
    println(x.deg)
    println(x.rad)
    println(x.pc)
    println(x.fr)
    ```,
)

The common thing is, there are extension vals to mimic numeric literals. Kinda.

==== Relative Length

Typst has three different types for that, `ratio`, `length`, and `relative`, with the latter representing the sum of the former two.
TyKo has... well... There are `TRatio`, `TLength`, and `TRelative`. The latter is supertype, except that for
generic-compatibility reasons it's actually a typealias `TRelativeBase<*>`. The rest works the same, though:

#evalkt(
    "relative",
    ```kt
    println(1.0.t.pc + 2.0.t.em)
    println(2.0.t.em)
    ```,
)

==== Collections

There are `TArray` and `TDict`. They implement `List` and `Map`, respectively.

#evalkt(
    "collection-examples",
    ```kt
    TArray(listOf(1.t, "2".t))
    ```,
)

And there is `TArgs`, which represents `arguments` type. It consists of positional and named arguments,
and there's a helper function for mixing them:

#evalkt(
    "args-ex",
    ```kt
    TArgs.of(1.t, "key" eq 2.t)
    ```,
)

They are generic and covariant, e.g. $ #`TColor` <: #`TPaint` =>  #`TArray<TColor>` <∶ #`TArray<TPaint>`$.

==== Stroke

A lot things in Typst can be a stroke. Any paint is a stroke, length is a stroke. And there's a specific
kind of dictionaries, which can be strokes as well. TyKo has a `TStroke` interface, which is implemented by
all the aforementioned types, and for dicts there is synthetic type called, unoriginally, TStrokeImpl:

#evalkt(
    "stroke-type",
    ```kt
    listOf(
        1.0.t.pt,
        java.awt.Color.RED.t,
        TTiling(TText(".".t))
    )
    ```,
)

-- common type here is actually not denotable (contains intersections), but there's `TStroke` somewhere in there, trust me.

#evalkt(
    "stroke-impl",
    ```kt
    println((java.awt.Color.BLUE.t + 1.0.t.pt).repr())
    ```,
)

-- `+` is defined for stroke creation as well.

==== Заебало

= JVM Objects <jvm>
= Drivers <drivers>

#upcall(invokestatic, "java.lang.System#getProperty:(Ljava/lang/String;)Ljava/lang/String;", "java.version")
