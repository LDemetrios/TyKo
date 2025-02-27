#set page(width: 120pt)
#set page(height:auto)
#set page(margin: 10pt)
#set text(size: 10pt)
// Test forcing math size
$a/b, display(a/b), display(a)/display(b), inline(a/b), script(a/b), sscript(a/b) \
 mono(script(a/b)), script(mono(a/b))\
 script(a^b, cramped: #true), script(a^b, cramped: #false)$
