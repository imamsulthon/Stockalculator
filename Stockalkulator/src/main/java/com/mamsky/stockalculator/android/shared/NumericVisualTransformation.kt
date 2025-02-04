package com.mamsky.stockalculator.android.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.NumberFormat
import java.util.Locale


@Composable
fun rememberCurrencyVisualTransformation(): VisualTransformation {
    val inspectionMode = LocalInspectionMode.current
    return remember {
        if (inspectionMode) {
            VisualTransformation.None
        } else {
            NumberCommaTransformation()
        }
    }
}

@Composable
fun rememberNumberVisualTransformation(): VisualTransformation {
    val inspectionMode = LocalInspectionMode.current
    return remember {
        if (inspectionMode) {
            log("inspectionMode1")
            VisualTransformation.None
        } else {
            log("inspectionModeNot")
            NumberCommaTransformation2()
        }
    }
}

class NumberCommaTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = AnnotatedString(text.text.toLongOrNull().formatWithComma()),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    log("originalToTransformed $offset - ${text.length}")
                    return text.text.toLongOrNull().formatWithComma().length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    log("transformedToOriginal $offset - ${text.length}")
                    return text.length
                }
            }
        )
    }
}

class NumberCommaTransformation2: VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = AnnotatedString(text.text.toDoubleOrNull().formatWithComma()),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    log("originalToTransformed $text - $offset - ${text.length}")
                    return text.text.toDoubleOrNull().formatWithComma().length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    log("transformedToOriginal $text - $offset - ${text.length}")
                    return text.length
                }
            }
        )
    }
}

fun log(m: String) {
    println("NumericVisual: $m")
}

fun Long?.formatWithComma(): String =
    NumberFormat.getNumberInstance(Locale.US).format(this ?: 0)


fun Double?.formatWithComma(): String =
    NumberFormat.getNumberInstance(Locale.US).format(this ?: 0)