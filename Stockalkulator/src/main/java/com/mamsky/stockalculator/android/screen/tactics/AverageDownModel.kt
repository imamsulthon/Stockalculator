package com.mamsky.stockalculator.android.screen.tactics

data class AvgDownModel(
    var topPrice: Int,
    var bottomPrice: Int,
    var factorPrice: Int,
    var topLot: Int,
    var bottomLot: Int,
    var factorLot: Int,
    var fee: Float = 0f,
)

object LotSequence {

    inline fun fibonacci(num: Int, crossinline  callback: (Int) -> Boolean): List<Int> {
        val list = mutableListOf<Int>()
        var temp1 = 1
        var temp2 = 1
        for (i in 1..num) {
            list.add(temp1)
            val sum = temp1 + temp2
            temp1 = temp2
            temp2 = sum
            val point = callback.invoke(temp1)
            if (point) break
        }
        return list
    }

    inline fun List<Int>.reverted(crossinline callback: (Int) -> Boolean): List<Int> {
        val newList = this.asReversed()
        newList.forEach {
            val point = callback.invoke(it)
            if (point) return newList
        }
        return newList
    }

    fun martingale(start: Int, count: Int): List<Int> {
        val list = mutableListOf(start)
        var v = start
        for (i in 1 .. count) {
            v += v
            list.add(v)
        }
        return list
    }

    inline fun martingale(start: Int, count: Int, crossinline callback: (Int) -> Boolean): List<Int> {
        val list = mutableListOf<Int>()
        var v = start
        list.add(v)
        for (i in 1 ..count) {
            v += v
            list.add(v)
            val point = callback.invoke(v)
            if (point) break
        }
        return list
    }

    inline fun general(start: Int, end: Int, f: Int, crossinline callback: (Int) -> Boolean): List<Int> {
        val list = mutableListOf<Int>()
        for (i in start ..end step f) {
            val point = callback.invoke(i)
            list.add(i)
            if (point) break
        }
        return list
    }

    inline fun antiGeneral(start: Int, end: Int, f: Int, crossinline callback: (Int) -> Boolean): List<Int> {
        val list = mutableListOf<Int>()
        for (i in start downTo end step f) {
            list.add(i)
            val point = callback.invoke(i)
            if (point) break
        }
        return list
    }

    inline fun general2(start: Int, end: Int, f: Int, crossinline callback: (Int) -> Boolean): List<Int> {
        if (start == end || (f > start && f > end)) return emptyList()
        val list = mutableListOf<Int>()
        if (start > end) {
            var v = start
            while (v >= end) {
                list.add(v)
                v -= f
                callback.invoke(v)
            }
        } else {
            var v = start
            while (v <= end) {
                list.add(v)
                v += f
                callback.invoke(v)
            }
        }
        return list
    }

}
