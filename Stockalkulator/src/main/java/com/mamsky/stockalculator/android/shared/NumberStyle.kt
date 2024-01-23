package com.mamsky.stockalculator.android.shared

import androidx.compose.ui.graphics.Color

fun Float?.color(): Color = if (this == null) Color.Gray else if (this < .00f) Color.Red else Color.Blue

fun Int?.color(): Color = if (this == null) Color.Gray else if (this < 0) Color.Red else Color.Blue
