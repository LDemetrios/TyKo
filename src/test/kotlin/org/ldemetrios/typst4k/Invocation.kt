//package org.ldemetrios.tyko
//
//import io.kotest.core.spec.style.FreeSpec
//import io.kotest.matchers.shouldBe
//import org.ldemetrios.tyko.compiler.Typst
//import org.ldemetrios.tyko.compiler.TypstDefaultCompiler
//import org.ldemetrios.tyko.model.*
//import org.ldemetrios.tyko.model.repr
//import org.ldemetrios.tyko.operations.*
//
//class Invocation : FreeSpec({
//    beforeAny {
//        TypstDefaultCompiler.compiler =            Typst(executable)
//    }
//    "Basic test" {
//        val function = TClosure("double(x) = x * 2".t)
//        function(1.t) shouldBe 2.t
//        function(2.t) shouldBe 4.t
//
//        val lambda = TClosure("(x: (x: (x) => x) => x) => x".t)
//        lambda["x" to 5.t]() shouldBe 5.t
//        (lambda() is TClosure) shouldBe true
//        lambda().repr() shouldBe "(x: (x) => x) => x"
//        println(((lambda() as TFunction)() as TFunction)(4.t))
//    }
//
////    "Eval with import" {
////        val evalF = TNativeFunc("eval".t)
////        fun eval(str: String) = evalF["mode" to "markup".t](str.t) as TContent
////    println("DAMN YOU COMPILER")
////        println(
////        eval(
////            """
////                 #import "@preview/zero:0.3.0": num
////
////                 Найдём соответствующие выборочные значения.
////                 Выборочное среднее равно $ num(1.0785358732578, digits: #3)$, несмещённая выборочная дисперсия --- $ num(1.0785358732578, digits: #3)$.
////                 Откуда получаем $ theta = "Var"/EE = num(1.0785358732578, digits: #3)$, $ k = EE/theta = num(1.0785358732578, digits: #3)$.
////            """.trimIndent()
////        ).repr())
////    }
//})