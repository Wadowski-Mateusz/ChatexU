package com.example.chatexu.common

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

object DebugConstants {
    const val VM_ERR = "VIEWMODEL_ERROR"
    const val UC_ERR = "USE_CASE_ERROR"
    const val TODO = "TODO"
    const val POTENTIAL_BUG = "POTENTIAL_BUG"
    const val INFO = "INFO"
    const val PEEK = "PEEK"
    const val USE_CASE = "USE_CASE"
    const val IN_PROGRESS = "IN_PROGRESS"
    const val RESOURCE_LOADING = "RESOURCE_LOADING"

    val lorem: (Int) -> String = { words: Int -> LoremIpsum(words).values.reduce { acc, s -> acc + s }}
    val loremChars: (Int) -> String = { chars: Int -> LoremIpsum(chars).values.reduce { acc, s -> acc + s }.take(chars)}

}