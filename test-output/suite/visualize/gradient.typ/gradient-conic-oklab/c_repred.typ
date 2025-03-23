#[a\ ].func()((
  [ ],
  {
    set page(width: 120.0pt)
    set page(height: auto)
    set page(margin: 10.0pt)
    set text(size: 10.0pt)
    [a\ ].func()((
      [ ],
      {
        set page(width: 100.0pt)
        set page(height: 100.0pt)
        set page(
          fill: gradient.conic(
            ..(
              (rgb("#ff4136"), 0.0%),
              (rgb("#b10dc9"), 100.0%),
            ),
            angle: 0.0deg,
            space: oklab,
            relative: auto,
            center: (50.0%, 50.0%),
          ),
        )
        parbreak()
      },
    ))
  },
))
