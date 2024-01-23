package com.mamsky.stockalculator.android.screen.araarb

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
        this < 50 -> 1
        this in 200..< 501 -> 2
        this in 500..< 2001 -> 5
        this in 2000..< 5001 -> 10
        this >= 5001 -> 25
        else -> 1
    }
}

fun Int.sheet() = this * 100