    //package org.ldemetrios.tyko
    //
    //import org.ldemetrios.tyko.model.*
    //import org.ldemetrios.tyko.operations.*
    //import java.util.stream.LongStream
    //import kotlin.math.max
    //import kotlin.math.min
    //
    ////fun main() {
    ////    println(
    ////        TPattern(
    ////            size = TArray(30.pt, 30.pt),
    ////            body = TSequence(
    ////                TPlace(body = TLine(start = TArray(0.pc, 0.pc), end = TArray(100.pc, 100.pc))),
    ////                TPlace(body = TLine(start = TArray(0.pc, 100.pc), end = TArray(100.pc, 0.pc))),
    ////            )
    ////        ).repr()
    ////    )
    ////}
    //
    ////fun main() {
    ////    var i = 0
    ////    val lim = 100_000_000 - 172
    ////    for (n in 1 until 8589934591) {
    ////        if (n % 4194304 == 0L) println("$i/$n")
    ////        val bin = n.toString(2)
    ////        val dec = n.toString(10)
    ////        if (bin.length  == dec.length * 3) i++
    ////        if (i == lim) {
    ////            println(n)
    ////            break
    ////        }
    ////    }
    ////}
    ////
    //fun Long.pow(exponent: Int): Long {
    //    var result = 1L
    //
    //    for (i in 0 until exponent) {
    //        result *= this
    //    }
    //
    //    return result
    //}
    //
    ////fun main() {
    ////    val lim = 100_000_000 - 172
    ////    val ranges = mutableListOf<LongRange>()
    ////    for (n in 1 until 21) {
    ////        val min = max(2L.pow(3*n - 1), 10L.pow(n-1))
    ////        val max = min(2L.pow(3*n), 10L.pow(n))
    ////        ranges.add(min until max)
    ////    }
    //////    var  k = 0
    //////    for (i in ranges) {
    //////        for (j in i) {
    //////            if (k == lim - 1) {
    //////                println(j)
    //////                return
    //////            }
    //////            k += 1
    //////        }
    //////    }
    ////    println(
    ////        ranges.asSequence()
    ////            .flatten()
    ////            .drop(lim - 1)
    ////            .first()
    ////    )
    //////    println(ranges.asSequence().flatten().count())
    ////}
    //
    //fun main() {
    //    val lim = 900_000_000_000_000_000
    //    val ranges = List(18) { 10L.pow(it) until 10L.pow(it + 1)}
    //        println(
    //        ranges.stream()
    //            .flatMapToLong{ LongStream.range(it.start, it.endInclusive + 1)}
    //            .skip(lim - 1)
    //            .findFirst()
    //            .orElse(0L)
    //    )
    ////    var  k = 0L
    ////    for (i in ranges) {
    ////        for (j in i) {
    ////            if (k == lim - 1) {
    ////                println(j)
    ////                return
    ////            }
    ////            k += 1
    ////        }
    ////    }
    //}