# TyKo — Kotlin bindings for Typst

## Quick introduction

This library allows

- Manipulating Typst values:
    
    ```kt
    val tiling = TTiling(
        sz = TArray(30.pt, 30.pt),
        body = TSequence(
            TPlace(body = TLine(start = TArray(0.pc, 0.pc), end = TArray(100.pc, 100.pc))),
            TPlace(body = TLine(start = TArray(0.pc, 100.pc), end = TArray(100.pc, 0.pc))),
        )
    )
    ```
    (example from [official documentation on tilings](https://typst.app/docs/reference/visualize/tiling/))

    All the parameters are named the same way as in Typst, except for `size`, 
    which is `sz` for the sake of compatibility with Kotlin `List`s

- Converting them to the Typst code:

    ```kt
    tiling.repr()
    ```

    (The resulting `repr` is not human-readable, however, some cosmetic improvements are planned)

- Accessing the Typst Engine.

    The compiler requires two things: [TypstSharedLibrary](https://github.com/LDemetrios/typst-shared-library),
    compiled for your system (see [Installation](https://github.com/LDemetrios/TyKo#Installation)),
    and a `World` (See Docs \[Not available]). There are two built-in implementations of World at the moment:

    ```kt
    val lib = TypstSharedLibrary.instance(Path("../libtypst_shared.so"))
  
    val compiler = WorldBasedTypstCompiler(lib, DetachedWorld())

    val anotherTiling = compiler.evalDetached(
        """
            tiling(
                size: (30pt, 30pt),
                spacing: (10pt, 10pt),
                relative: "parent",
                square(
                    size: 30pt,
                    fill: gradient.conic(..color.map.rainbow),
                ),
            )
        """.trimIndent()
    )
  
    compiler.close()
    ```
  
    Compilers are Closeable. Not closing the Compiler properly may cause native AND java memory leaks.
    
- Compiling to html, png and svg (pdf is coming):

    ```kt
    val page = TSequence {
        +TSetPage(height = TAuto, margin = 20.pt)
        +TRect(
            fill = tiling,
            width = 100.pc,
            height = 60.pt,
            stroke = 1.pt,
        )
    }
    val fileCompiler = WorldBasedTypstCompiler(lib, SingleFileWorld("#" + page.repr()))

    val compiled = fileCompiler.compilePng(ppi = 288.0f)[0] // Oth page
    File("output.png").writeBytes(compiled)
    ```
    
    ![Result](output.png
- )
## Further documentation

Read the [manual](manual.pdf)

## Installation 

This library is yet in the beta testing stage.

- Build helper library:
```bash
git clone https://github.com/LDemetrios/LDemetriosCommons.git
cd LDemetriosCommons 
gradle publish
cd ..
```

- Build shared library:
```bash
git clone https://github.com/LDemetrios/typst-custom-serialize
cd typst-custom-serialize
cargo build --release
cd ..
```

save the built shared library (e.g. `target/release/libtypst_shared.so`), you won't need the rest. 

- Download this repo:
```bash
git clone https://github.com/LDemetrios/TyKo
cd TyKo
gradle publish
cd ..
```

(Or use an appropriate `gradlew` if you haven't installed gradle)

Now you can include it into your project:

### Maven

```xml
<dependency>
    <groupId>org.ldemetrios</groupId>
    <artifactId>tyko</artifactId>
    <version>0.4.0</version>
</dependency>
```

### Gradle 
Kotlin DSL:
```kt
implementation("org.ldemetrios:tyko:0.4.0")
```
Groovy DSL:
```groovy
implementation 'org.ldemetrios:tyko:0.4.0'
```

## Changelog

See [file](Changelog.md)

## Plans

- [x] Split arguments for calls into separate chunks (avoiding multiple overloads)
- [x] Add tests
- [X] Support `set` and `show` rules
- [ ] Improve type checking during deserialization
- [ ] Allow functions, which take primitive arguments (`int`, `str` etc) also accept corresponding Kotlin values (`Int`, `String`). 
- [ ] Beautify `repr` (make more human-readable)
- [ ] Add support for typed queries (query(heading) can only return THeading)
- [X] Add support for labeled content
- [ ] Improve function ser/deser according to new Typst structure (functions belong to modules)
- [ ] Make World implementations more flexible (custom inputs, fonts)
- [ ] Allow passing fully custom Java (Kotlin) functions into the compiler

## Contacts

If you experience bugs or have a proposal for improvements, feel free to open issues. 
PRs are also welcome, feel free to ask questions about the internal structure of the project.

tg: @LDemetrios

mail: ldemetrios@yandex.ru
