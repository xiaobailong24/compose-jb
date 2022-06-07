package org.jetbrains.compose.desktop.browser

import androidx.compose.ui.awt.ComposeWindow

interface Browser {
    fun load(window: ComposeWindow, url: String)
}
