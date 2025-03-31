
<div align="right"><p style='align: right;'>
Languages: <a href="Changelog-RU.md"><img src="/flags/RU.png" alt="Russian" width="6%"  align="right"></a> <a href="Changelog.md"><img src="/flags/UK.png" alt="English" width="6%" align="right"></a> 
</p></div>

## Changelog

### 0.4.0

- Ускорена компиляция (Взаимодействие между TyKo и Typst теперь использует JNA,
    а не процессы ОС).
- **_(Breaking)_** Переименованы пакеты.
- **_(Breaking)_** Создание значений теперь происходит через функции, а не конструкторы (влияет на Java проекты).
- **_(Breaking)_** API доступа к компилятору Typst изменён на основанный на `World`.
- Исправлены пробелы в математике.
- Поддержана десериализация `tiling.body`.
- Поддержаны `show` и `set` правила.
- Поддержан Typst 0.13.
- Исправлен баг с контекстуальными замыканиями, состоящими из единственного идентификатора.

### 0.3.0

- Добавлено автоматическое тестирование (и всё остальное в основном благодаря этому)
- **_(Breaking)_** Изменён DSL вызовов компилятора Typst.
- Введён парсер стектрейсов Typst, появилась возможность дописывать его в Exception.
- **_(Breaking)_** Изменено представление `Relative` в Kotlin.
- Исправлены баги с vararg в  `table`, `list`, `math.cases`, `math.mat`, `math.vec`, `grid`,
  `grid.header`, `grid.footer`, `stack`, `path`, `polygon`
- Исправлена repr `math.root`
- Добавлены пропущенные`math.op`, `math.scripts`, `math.align-point`,
  `math.limits`, `figure.caption`
- Исправлено ошибочное требование наличия `body` у `block`, `box`
- Поддержан Typst 0.12:
    - Добавлены параметры `figure.scope`, `heading.hanging-indent`,
      `text.costs`, `math.mat.align`, `math.vec.align`, `block.sticky`,
      `place.scope`, `repeat.gap`, `repeat.justify`, `path.fill-rule`, `polygon.fill-rule` 

    - Изменены типы параметров `raw.theme`, `text.stylistic-set`, `math.vec.delim`
      `math.cases.delim`, `math.mat.delim`, `block.width`, `block.height`, `box.height`,
      `page.header`, `page.footer`, `scale.x`, `scale.y`, `rect.width`, `rect.height` 

    - Добавлены элементы `math.binom`, `smallcaps`, `math.overparen`, `math.underparen`,
      `math.overshell`, `math.undershell`, `place.flush`, `skew`, `math.stretch`
    - Добавлен тип `decimal`
    - **_Но не новые аргументы CLI_**
- Исправлены баги с дублирующимися именами (
  `grid`/`cell`: `footer`, `header`, `rowspan`, `colspan`, `cell`, `hline`, `vline`;
  `list`/`enum`/`term` : `item`
  )
- Введён черновой вариант `styled` для последующего использования в `set` правилах.

... и появился этот Changelog

### 0.2.0

- Синхронизирована сериализация с версией из PR
- Добавлены новые типы

### 0.1.0

- Опубликован первый вариант библиотеки


