package com.takemysigns.takemysigns.ui.helpers

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText

const val mask = "(619) 798-4867"
fun mobileNumberFilter(text: AnnotatedString):
        TransformedText {
    // change the length
    val trimmed =
        if (text.text.length >= 14) text.text.substring(0..13) else text.text

    val annotatedString = AnnotatedString.Builder().run {
        for (i in trimmed.indices) {
            val trimmedPortion = trimmed[i]
            if (i == 0) {
                append("($trimmedPortion")
            } else {
                append(trimmedPortion)
            }
            if (i == 2) {
                append(") ")
            }
            if (i == 5) {
                append("-")
            }
        }
        pushStyle(
            SpanStyle(color = Color.LightGray)
        )
        try {
            append(mask.takeLast(mask.length - length))
        } catch (e: IllegalArgumentException) {
            Log.d("mobileNumberFilter","reached end of phone number")
        }

        toAnnotatedString()
    }

    val translator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 1) return offset
            if (offset <= 4) return offset + 1
            if (offset <= 9) return offset + 2
            return 14

        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <= 1) return offset
            if (offset <= 4) return offset - 1
            if (offset <= 9) return offset - 2
            return 14

        }
    }

    return TransformedText(annotatedString, translator)
}