#[a\ ].func()((
  [ ],
  {
    set page(width: 120.0pt)
    set page(height: auto)
    set page(margin: 10.0pt)
    set text(size: 10.0pt)
    set page(margin: ("bottom": 20.0pt))
    set page(numbering: "1")
    [a\ ].func()((
      text("A"),
      [ ],
      {
        set text(fill: rgb("#ff4136"))
        pagebreak()
      },
      [ ],
      {
        set text(fill: rgb("#0074d9"))
        text("B")
      },
      parbreak(),
    ))
  },
))
