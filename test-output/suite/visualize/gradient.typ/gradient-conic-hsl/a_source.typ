#set page(width: 120pt)
#set page(height:auto)
#set page(margin: 10pt)
#set text(size: 10pt)
// Test in HSL space.
#set page(
  width: 100pt,
  height: 100pt,
  fill: gradient.conic(red, purple, space: color.hsl)
)
