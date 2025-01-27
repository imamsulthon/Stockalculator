package com.mamsky.stockalculator.domain

fun Int.fractionAra(): Float {
    return when {
        this in 0..200 -> 0.35f
        this in 201..< 5000 -> 0.25f
        this >= 5000 -> 0.2f
        else -> 0f
    }
}

fun Int.fractionArb(): Float {
    return when {
        this in 50..200 -> 0.35f
        this in 201..< 5000 -> 0.25f
        this >= 5000 -> 0.2f
        else -> 0f
    }
}

fun Int.fraction(): Int {
    return when {
        this < 200 -> 1
        this in 200..< 501 -> 2
        this in 500..< 2001 -> 5
        this in 2000..< 5001 -> 10
        this >= 5001 -> 25
        else -> 1
    }
}

fun Int.modFraction(up: Boolean = false): Int {
    val fr = this.fraction()
    val mod = this % fr
    if (mod <= 0) return this
    return if (up) this + fr - mod else this - mod
}

private fun Int.upFold0(): Int {
    val fr = this.fraction()
    val temp = (this % fr)
    return if (temp > 0) this + fr - temp else this
}

private fun Int.downFold0(): Int {
    val fr = this.fraction()
    val temp = (this % fr)
    return if (temp > 0) this - temp else this
}

fun Int.sheet() = this * 100
