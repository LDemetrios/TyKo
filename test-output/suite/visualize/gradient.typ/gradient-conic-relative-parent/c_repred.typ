#[a\ ].func()((
  [ ],
  {
    set page(width: 120.0pt)
    set page(height: auto)
    set page(margin: 10.0pt)
    set text(size: 10.0pt)
    [a\ ].func()((
      [ ],
      [ ],
      [a\ ].func()(()),
      [ ],
      [a\ ].func()(()),
      {
        set page(width: 50.0pt)
        set page(height: 50.0pt)
        set page(margin: 2.5pt)
        set page(
          fill: gradient.conic(
            ..(
              (rgb("#ff4136"), 0.0%),
              (
                rgb("#0074d9"),
                33.33333333333333%,
              ),
              (
                rgb("#2ecc40"),
                66.66666666666666%,
              ),
              (rgb("#b10dc9"), 100.0%),
            ),
            angle: 0.0deg,
            space: oklab,
            relative: "parent",
            center: (50.0%, 50.0%),
          ),
        )
        set page(
          background: place(
            top + left,
            rect(
              width: 50.0%,
              height: 50.0%,
              fill: gradient.conic(
                ..(
                  (rgb("#ff4136"), 0.0%),
                  (
                    rgb("#0074d9"),
                    33.33333333333333%,
                  ),
                  (
                    rgb("#2ecc40"),
                    66.66666666666666%,
                  ),
                  (rgb("#b10dc9"), 100.0%),
                ),
                angle: 0.0deg,
                space: oklab,
                relative: "parent",
                center: (50.0%, 50.0%),
              ),
            ),
          ),
        )
        [a\ ].func()((
          place(
            top + right,
            rect(
              width: 50.0%,
              height: 50.0%,
              fill: gradient.conic(
                ..(
                  (rgb("#ff4136"), 0.0%),
                  (
                    rgb("#0074d9"),
                    33.33333333333333%,
                  ),
                  (
                    rgb("#2ecc40"),
                    66.66666666666666%,
                  ),
                  (rgb("#b10dc9"), 100.0%),
                ),
                angle: 0.0deg,
                space: oklab,
                relative: "parent",
                center: (50.0%, 50.0%),
              ),
            ),
          ),
          [ ],
          place(
            bottom + center,
            rotate(
              45.0deg,
              rect(
                width: 50.0%,
                height: 50.0%,
                fill: gradient.conic(
                  ..(
                    (rgb("#ff4136"), 0.0%),
                    (
                      rgb("#0074d9"),
                      33.33333333333333%,
                    ),
                    (
                      rgb("#2ecc40"),
                      66.66666666666666%,
                    ),
                    (rgb("#b10dc9"), 100.0%),
                  ),
                  angle: 0.0deg,
                  space: oklab,
                  relative: "parent",
                  center: (50.0%, 50.0%),
                ),
              ),
            ),
          ),
          parbreak(),
        ))
      },
    ))
  },
))
