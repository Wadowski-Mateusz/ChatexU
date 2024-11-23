package com.example.chatexu.common

import android.util.Log
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

object DebugConstants {
    const val VM_ERR = "VM_ERR"
    const val TODO = "TODO"
    const val POTENTIAL_BUG = "POTENTIAL_BUG"
    const val INFO = "INFO"
    const val PEEK = "PEEK"
    const val USE_CASE = "USE_CASE"
    const val IN_PROGRESS = "IN_PROGRESS"

    val lorem: (Int) -> String = { words: Int -> LoremIpsum(words).values.reduce { acc, s -> acc + s }}

}