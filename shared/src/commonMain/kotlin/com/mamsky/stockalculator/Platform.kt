package com.mamsky.stockalculator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform