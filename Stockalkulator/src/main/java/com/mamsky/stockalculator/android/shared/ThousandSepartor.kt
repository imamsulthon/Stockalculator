package com.mamsky.stockalculator.android.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat


internal val VisualTransformation.Companion.ThousandSeparator
    @Composable
    get() = remember(::ThousandSeparatorTransform)

internal class ThousandSeparatorTransform : VisualTransformation {
    private val symbols = DecimalFormat().decimalFormatSymbols
    override fun filter(text: AnnotatedString): TransformedText {
        val string = text.text
        val integer = string.substringBefore(symbols.decimalSeparator)
        val output = string.withSeparator(
            separator = symbols.groupingSeparator,
            length = integer.length,
        )
        return TransformedText(
            text = AnnotatedString(output),
            offsetMapping = SeparatorMapping(integer),
        )
    }
}

/**
 * 12345678.00 -> 12,345,678.00
 * @param segmentSize length between each separator (345)
 * @param length total length of the segments that need to separate (12345678)
 * @param startOffset size of first segment (12)
 * @param separatorCount count of separator 2 for (12,345,678.00)
 * @property padding _12,345,678.00 padding is length of _
 */
private class SeparatorMapping(
    private val text: String,
    private val segmentSize: Int = 3,
    startOffset: Int = text.lastIndex % segmentSize + 1,
    separatorCount: Int = text.lastIndex / segmentSize,
) : OffsetMapping {
    private val padding = segmentSize - startOffset
    private val transformedTextLength = text.length + separatorCount

    override fun originalToTransformed(offset: Int): Int {
        val intOffset = offset.coerceAtMost(text.length)
        val offsetSeparatorCount = (intOffset + padding - 1) / segmentSize
        val decimalOffset = (offset - text.length).coerceAtLeast(0)
        return intOffset + offsetSeparatorCount + decimalOffset
    }

    override fun transformedToOriginal(offset: Int): Int {
        val intOffset = offset.coerceAtMost(transformedTextLength)
        val offsetSeparatorCount = (intOffset + padding) / (segmentSize + 1)
        val decimalOffset = (offset - transformedTextLength).coerceAtLeast(0)
        return intOffset - offsetSeparatorCount + decimalOffset
    }
}

/**
 * 12345678.00 -> 12,345,678.00
 * @param separator ","
 * @param length total length of the segments that need to separate (12345678)
 * @param segmentSize length between each separator (345)
 * @param startOffset size of first segment (12)
 * @param separatorCount count of separator 2 for (12,345,678.00)
 */
internal fun String.withSeparator(
    separator: Char,
    length: Int = this.length,
    segmentSize: Int = 3,
    startOffset: Int = (length - 1) % segmentSize + 1,
    separatorCount: Int = (length - 1) / segmentSize,
) = buildString {
    append(this@withSeparator)
    repeat(separatorCount) { index ->
        val offset = startOffset + index * (segmentSize + 1)
        insert(offset, separator)
    }
}