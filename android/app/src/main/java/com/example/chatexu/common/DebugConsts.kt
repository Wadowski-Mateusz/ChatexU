package com.example.chatexu.common

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

object DebugConsts {
    const val VM_ERR = "VM_ERR"
    const val TODO = "TODO"
    const val POTENTIAL_BUG = "POTENTIAL_BUG"
    const val INFO = "INFO"

    val lorem: (Int) -> String = { words: Int -> LoremIpsum(words).values.reduce { acc, s -> acc + s }}
}