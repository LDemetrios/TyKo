#[a\ ].func()((
  [ ],
  {
    set page(width: 120.0pt)
    set page(height: auto)
    set page(margin: 10.0pt)
    set text(size: 10.0pt)
    [a\ ].func()((
      align(
        bottom + center,
        square(
          width: 50.0pt,
          height: 50.0pt,
          fill: luma(0.0%),
          stroke: stroke(
            paint: gradient.conic(
              ..(
                (rgb("#ff4136"), 0.0%),
                (rgb("#0074d9"), 100.0%),
              ),
              angle: 0.0deg,
              space: oklab,
              relative: auto,
              center: (50.0%, 50.0%),
            ),
            thickness: 10.0pt,
          ),
        ),
      ),
      parbreak(),
    ))
  },
))
