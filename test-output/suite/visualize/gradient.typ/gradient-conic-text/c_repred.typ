#[a\ ].func()((
  [ ],
  {
    set page(width: 120.0pt)
    set page(height: auto)
    set page(margin: 10.0pt)
    set text(size: 10.0pt)
    set page(width: 200.0pt)
    set page(height: auto)
    set page(margin: 10.0pt)
    set par(justify: true)
    set text(
      fill: gradient.conic(
        ..(
          (rgb("#ff4136"), 0.0%),
          (rgb("#0074d9"), 100.0%),
        ),
        angle: 45.0deg,
        space: oklab,
        relative: auto,
        center: (50.0%, 50.0%),
      ),
    )
    [a\ ].func()((
      text("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim aeque doleamus animo, cum corpore dolemus, fieri."),
      parbreak(),
    ))
  },
))
