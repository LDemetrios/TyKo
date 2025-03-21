package org.ldemetrios.tyko.manual

import org.ldemetrios.tyko.compiler.SyntaxMark
import org.ldemetrios.tyko.compiler.SyntaxMode
import org.ldemetrios.tyko.compiler.format
import org.ldemetrios.tyko.compiler.parseSource
import org.ldemetrios.tyko.original.sharedLib

fun main() {
    val source = """
            #import "@preview/fletcher:0.5.5" as fletcher: diagram as dia, node, edge
            = Total Store Order
            
            #let diagram(..args) = align(center)[
            \
            #context dia(
            edge-stroke: 1pt + text.fill,
            node-corner-radius: 5pt,
            edge-corner-radius: 8pt,
            mark-scale: 80%,
            node-fill: eastern,
            ..args,
            )
            \
            ]
            
            #diagram(
            node((0, 0), [#h(2em) CPU 1 #h(2em)], name: <cpu1>),
            node((.8, 0), [#h(2em) CPU 2 #h(2em)], name: <cpu2>),
            node((0.15, 1), [buffer], name: <buf1>),
            node((0.65, 1), [buffer], name: <buf2>),
            node((0.4, 2), [#h(6em) Memory 2 #h(6em)], name: <mem>),
            
            edge(<cpu1>, "dd", "<{-", shift: -.5, label-side: center, [read]),
            edge(<cpu2>, "dd", "<{-", shift: 0.2, label-side: center, [read]),
            
            edge(<buf1>, "u", "<{-", label-side: center, [write]),
            edge(<buf2>, "u", "<{-", label-side: center, [write]),
            edge(<buf1>, "d", "-}>"),
            edge(<buf2>, "d", "-}>", label-side: right, [write back]),
            )

        """.trimIndent()
    println( sharedLib!!.format(source, 80, 4))

}