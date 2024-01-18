package com.mamsky.stockalculator.android.screen.araarb

class AutoRejectionRules {

    
}

fun Int.fractionAra(): Float {
    return when {
        this in 0..200 -> {
            0.35f
        }
        this in 201..< 5000 -> {
            0.25f
        }
        this >= 5000 -> {
            0.2f
        }
        else -> {
            0f
        }
    }
}

fun Int.fractionArb(): Float {
    return when {
        this in 50..200 -> {
            -0.35f
        }
        this in 201..< 5000 -> {
            -0.025f
        }
        this >= 5000 -> {
            -0.07f
        }
        else -> {
            0f
        }
    }
}

fun Int.fraction(): Int {
    return when {
        this < 200 -> {
            1
        }
        this in 201..< 500 -> {
            2
        }
        this in 501..< 2000 -> {
            5
        }
        this in 2000..< 5000 -> {
            10
        }
        this >= 5000 -> {
            25
        }
        else -> {
            0
        }
    }
}