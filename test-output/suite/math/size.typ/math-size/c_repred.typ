#[a\ ].func()((
  [ ],
  {
    set page(width: 120.0pt)
    set page(height: auto)
    set page(margin: 10.0pt)
    set text(size: 10.0pt)
    [a\ ].func()((
      [ ],
      math.equation(
        [a\ ].func()((
          math.frac(symbol("a"), symbol("b")),
          symbol(","),
          [ ],
          {
            set math.equation()
            show: math.display
            math.frac(symbol("a"), symbol("b"))
          },
          symbol(","),
          [ ],
          math.frac(
            {
              set math.equation()
              show: math.display
              symbol("a")
            },
            {
              set math.equation()
              show: math.display
              symbol("b")
            },
          ),
          symbol(","),
          [ ],
          {
            set math.equation()
            show: math.inline
            math.frac(symbol("a"), symbol("b"))
          },
          symbol(","),
          [ ],
          {
            set math.equation()
            show: math.script
            math.frac(symbol("a"), symbol("b"))
          },
          symbol(","),
          [ ],
          {
            set math.equation()
            show: math.sscript
            math.frac(symbol("a"), symbol("b"))
          },
          [ ],
          linebreak(),
          [ ],
          {
            show: math.mono
            set math.equation()
            show: math.script
            math.frac(symbol("a"), symbol("b"))
          },
          symbol(","),
          [ ],
          {
            set math.equation()
            show: math.script
            show: math.mono
            math.frac(symbol("a"), symbol("b"))
          },
          linebreak(),
          [ ],
          {
            set math.equation()
            show: math.script
            math.attach(
              symbol("a"),
              t: symbol("b"),
            )
          },
          symbol(","),
          [ ],
          {
            set math.equation()
            show: math.script
            math.attach(
              symbol("a"),
              t: symbol("b"),
            )
          },
        )),
        block: false,
      ),
      parbreak(),
    ))
  },
))
