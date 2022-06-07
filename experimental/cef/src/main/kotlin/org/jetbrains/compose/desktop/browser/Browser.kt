@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package org.jetbrains.compose.desktop.browser

import androidx.compose.ui.awt.ComposePanel

internal interface Browser {
    fun load(panel: ComposePanel, url: String)
}
