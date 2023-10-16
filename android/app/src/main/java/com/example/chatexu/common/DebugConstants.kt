package com.example.chatexu.common

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

object DebugConstants {
    const val VM_ERR = "VM_ERR"
    const val TODO = "TODO"
    const val POTENTIAL_BUG = "POTENTIAL_BUG"
    const val INFO = "INFO"
    const val PEEK = "PEEK"

    const val HARD_USER_ID = "652c15ac97d36836b3af44e4"
    const val HARD_CHAT_ID = "652c15ad97d36836b3af44e6"

    val lorem: (Int) -> String = { words: Int -> LoremIpsum(words).values.reduce { acc, s -> acc + s }}
}