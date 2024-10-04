## Change log

### 0.3.0

- Add automatic tests (And almost everything else is thanks to that)
- **_(Breaking)_** Change Typst compiler calls DSL
- Introduce a Typst stacktrace parser and let add its entries to JVM exceptions
- **_(Breaking)_** Change how `Relative` is represented in Kotlin 
- Fix bugs with varargs in `table`, `list`, `math.cases`, `math.mat`, `math.vec`, `grid`,
    `grid.header`, `grid.footer`, `stack`, `path`, `polygon`
- Fix repr of `math.root`
- Add missing `math.op`, `math.scripts`, `math.align-point`,
  `math.limits`, `figure.caption`
- Fix falsely required `body` of `block`, `box`
- Support Typst 0.12:
  - Add `figure.scope`, `heading.hanging-indent`,
  `text.costs`, `math.mat.align`, `math.vec.align`, `block.sticky`,
  `place.scope`, `repeat.gap`, `repeat.justify`, `path.fill-rule`, `polygon.fill-rule` parameters
  
  - Change types of `raw.theme`, `text.stylistic-set`, `math.vec.delim`
  `math.cases.delim`, `math.mat.delim`, `block.width`, `block.height`, `box.height`,
  `page.header`, `page.footer`, `scale.x`, `scale.y`, `rect.width`, `rect.height` parameters
  
  - Add `math.binom`, `smallcaps`, `math.overparen`, `math.underparen`,
    `math.overshell`, `math.undershell`, `place.flush`, `skew`, `math.stretch` element types
  - Add `decimal` type
  - **_But not new optional CLI arguments_**
- Fix bugs duplicated names (
`grid`/`cell`: `footer`, `header`, `rowspan`, `colspan`, `cell`, `hline`, `vline`;
`list`/`enum`/`term` : `item`
  )
- Introduce draft `styled` class to later use for set rules


### 0.2.0

- Sync serialization with PR version
- Add a few more types

### 0.1.1

- TArray and TDictionary no longer implement List and Map respectively.

- Appropriate extension functions, duplicating functionality of stdlib ones, will be added. 

### 0.1.0 

- Publish beta version of the library



