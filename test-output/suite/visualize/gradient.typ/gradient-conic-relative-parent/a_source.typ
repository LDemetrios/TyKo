#set page(width: 120pt)
#set page(height:auto)
#set page(margin: 10pt)
#set text(size: 10pt)
// The image should look as if there is a single gradient that is being used for
// both the page and the rectangles.
#let grad = gradient.conic(red, blue, green, purple, relative: "parent");
#let my-rect = rect(width: 50%, height: 50%, fill: grad)
#set page(
  height: 50pt,
  width: 50pt,
  margin: 2.5pt,
  fill: grad,
  background: place(top + left, my-rect),
)
#place(top + right, my-rect)
#place(bottom + center, rotate(45deg, my-rect))
