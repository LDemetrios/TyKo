# TyKo — Kotlin bindings for Typst

# !! Warning !! This Readme is being actively updated. Please be careful following instructions, they may appear corrupted.

## Quick introduction

This library allows

- Manipulating Typst values:
    
    ```kt
    val tiling = TTiling(
        size = TArray(30.pt, 30.pt),
        body = TSequence(
            TPlace(body = TLine(start = TArray(0.pc, 0.pc), end = TArray(100.pc, 100.pc))),
            TPlace(body = TLine(start = TArray(0.pc, 100.pc), end = TArray(100.pc, 0.pc))),
        )
    )
    ```
    (example from [official documentation on tilings](https://typst.app/docs/reference/visualize/pattern/))

- Converting them to the Typst code:

    ```kt
    tiling.repr()
    ```
    which produces 
    ```typ
    tiling(size: (0.0em + 30.0pt, 0.0em + 30.0pt), { place(line(start: (0.0%, 0.0%), end: (100.0%, 100.0%))); place(line(start: (0.0%, 100.0%), end: (100.0%, 0.0%))); })
    ```
    
    (Some cosmetic improvements are planned, but not the first priority)

- Accessing the Typst compiler:

    ```kt
    val typst = Typst("/home/user/.cargo/bin/typst")
    typst.compile(Path.of("test.typ")) {
        format = OutputFormat.SVG
        ppi = 1440
    }
    ```
    
    Optional arguments are provided via lambda configuration function.

    When no path for the typst compiler is provided, the default one is used.
    
    ```kt
    val typst = Typst()
    typst.query<TMetadata<TArray<TInt>>>(Path.of("test.typ"), selector(TLabel("lbl".t)))
    ```
    
    More on queries later.
    
    Besides, default parameters can be configured while creating the `Typst` object:
    
    ```kt
    Typst("typst", "./typst-custom-serial") {
        root = Path.of("src/typ")
        ppi = 1440
        compile {
            input("mode", "heavy")
        }
        query {
            input("mode", "lite")
        }
        watch {
            ppi = 144
        }
    }
    ```
  
    `"./typst-custom-serial"` here is a separate executable for performing queries (see section on queries). Also, `watch` is not supported, plan on adding it in the next version.

## Further documentation

I plan on publishing it almost the first priority. Stay tuned.

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
```

- Put the shared library that you built earlier into `main/resources`, and name it just `typst_shared` (no prefixes, no extensions).

- (from the root of TyKo) Run:

```bash
gradle publish
```

(Or use an appropriate `gradlew` if you haven't installed gradle)

Now you can include it into your project:

### Maven

```xml
<dependency>
    <groupId>org.ldemetrios</groupId>
    <artifactId>tyko</artifactId>
    <version>0.3.0</version>
</dependency>
```

### Gradle 
Kotlin DSL:
```kt
implementation("org.ldemetrios:tyko:0.3.0")
```
Groovy DSL:
```groovy
implementation 'org.ldemetrios:tyko:0.3.0'
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
