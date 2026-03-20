#import "@preview/curvly:0.1.0"

#let get-percents-char-width(chars, radius, degrees) = {
    if chars.len() == 0 {
        return ()
    }
    if chars.len() == 1 {
        return (.5,)
    }

    let widths = chars.map(c => measure(raw(c)).width)
    let half-widths = widths.map(w => w / 2)
    let half-angles = half-widths.map(hw => calc.atan2(radius.pt(), hw.pt()))

    let remaining-angle = degrees
    for i in range(half-angles.len()) {
        if i == 0 or i == half-angles.len() - 1 {
            remaining-angle -= half-angles.at(i)
        } else {
            remaining-angle -= half-angles.at(i) * 2
        }
    }

    let inter-char-angle = remaining-angle / (chars.len() - 1)
    let current = 0deg
    let offsets = (current,)
    for i in range(1, chars.len()) {
        current += half-angles.at(i - 1) + inter-char-angle + half-angles.at(i)
        offsets.push(current)
    }

    offsets.map(angle => angle / degrees)
}

#let fit-count(chars, width) = {
    let count = 0
    while count < chars.len() and measure(raw(chars.slice(0, count + 1).join(""))).width <= width {
        count += 1
    }
    count
}

#let bend-degrees(chars, radius, reaches-vertical) = {
    if reaches-vertical or chars.len() <= 1 {
        return 90deg
    }

    let text-width = measure(raw(chars.join(""))).width
    let ratio = calc.min(1, text-width.pt() / (2 * radius.pt()))
    calc.min(90deg, 2 * calc.asin(ratio))
}

#let quarter-arc(chars, center, radius, color, reaches-vertical) = context {
    let degrees = bend-degrees(chars, radius, reaches-vertical)
    let percents = get-percents-char-width(chars, radius, degrees)
    let y-char-offset = measure(raw("M")).width / 2

    for i in range(chars.len()) {
        let percent = percents.at(i)
        let theta = -90deg + percent * degrees
        let x = center.at("x") + radius * calc.cos(theta)
        let y = center.at("y") + radius * calc.sin(theta)
        let ch = chars.at(i)
        let cur = measure(raw(ch))

        place(
            top + left,
            dx: x - cur.width / 2,
            dy: y - y-char-offset,
            rotate(theta + 90deg, text(fill: color, raw(ch))),
        )
    }
}

#let stacktrace(code, arc-center: (x: 52, y: 18)) = context {
    let fill = color.mix(space: rgb, (red, 100%), (text.fill, 50%))
    let inset-x = .5em
    let lines = code.text.split("\n")
    let row-step = measure(raw(block: true, "M\nM")).height - measure(raw(block: true, "M")).height
    let sample = measure(raw("M"))
    let char-width = sample.width
    let cap-height = sample.height

    layout(size => {
        let center-shift = measure(box(inset: .5em, [])).width
        let split-cx = arc-center.at(0) * char-width
        let split-cy = arc-center.at(1) * row-step
        let cx = split-cx + center-shift
        let cy = split-cy + center-shift
        let base-height = lines.len() * row-step + cap-height
        let left-width = 0pt

        for index in range(lines.len()) {
            let line = lines.at(index)
            let chars = line.clusters()
            let row-y = index * row-step
            let visible-left = if row-y < split-cy {
                chars.slice(0, fit-count(chars, split-cx)).join("")
            } else {
                line
            }
            left-width = calc.max(left-width, measure(raw(visible-left)).width)
        }

        box(
            inset: (left: 1em),
            block(
                width: left-width,
                height: base-height,
                {
                    box(
                        width: left-width + inset-x * 2,
                        height: base-height,
                        inset: (x: inset-x),
                        stroke: (left: (paint: text.fill)),
                    )

                    place(
                        top + left,
                        dx: inset-x + .25em,
                        dy: .5em,
                        block(
                            width: size.width,
                            height: base-height,
                            {
                                for index in range(lines.len()) {
                                    let line = lines.at(index)
                                    let chars = line.clusters()
                                    let row-y = index * row-step

                                    if row-y >= split-cy {
                                        place(top + left, dx: 0pt, dy: row-y, text(fill: fill, raw(line)))
                                        continue
                                    }

                                    let radius = split-cy - row-y
                                    let entry-width = split-cx
                                    let straight-count = fit-count(chars, entry-width)
                                    let straight = chars.slice(0, straight-count).join("")

                                    place(top + left, dx: 0pt, dy: row-y, text(fill: fill, raw(straight)))

                                    if straight-count < chars.len() {
                                        let tail = chars.slice(straight-count)
                                        let bend-capacity = calc.max(1, calc.floor(calc.pi * radius / (2 * char-width)))
                                        let bend-count = calc.min(bend-capacity, tail.len())
                                        let bend = tail.slice(0, bend-count)
                                        let vertical = if bend-count < tail.len() {
                                            tail.slice(bend-count).join("")
                                        } else {
                                            ""
                                        }

                                        quarter-arc(
                                            bend,
                                            (x: cx - .8em, y: cy - .78em),
                                            radius,
                                            fill,
                                            vertical != "",
                                        )

                                        if vertical != "" {
                                            place(
                                                top + left,
                                                dx: cx + radius - .5em,
                                                dy: cy - .5em,
                                                rotate(90deg, origin: top + left, text(fill: fill, raw(vertical))),
                                            )
                                        }
                                    }
                                }
                            },
                        ),
                    )
                },
            ),
        )
    })
}

#stacktrace(
    arc-center: (52, 18),
    ```
    org.ldemetrios.tyko.compiler.TypstCompilerException: number must be at least zero
       at main.typ.f(main.typ:1)
       at main.typ.g(main.typ:2)
       at main.typ.<source>(main.typ:3)
       at org.ldemetrios.tyko.compiler.TypstCompilerException$Companion.construct(TypstCompilerException.kt:21)
       at org.ldemetrios.tyko.compiler.TypstCompilerException$Companion.construct$default(TypstCompilerException.kt:20)
       at org.ldemetrios.tyko.compiler.UtilsKt.orThrow(Utils.kt:12)
       at org.ldemetrios.tyko.compiler.UtilsKt.outputOrThrow(Utils.kt:15)
       at MainKt.main(Main.kt:31)
       at MainKt.main(Main.kt)
    ```,
)
