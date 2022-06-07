package org.jetbrains.compose.desktop

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.desktop.browser.Browser
import org.jetbrains.compose.desktop.browser.BrowserSlicer
import org.jetbrains.compose.desktop.browser.BrowserView
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

@Composable
@Preview
fun App(window: ComposeWindow, browser: Browser, url: MutableState<String>) {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.DarkGray
        ) {
            Column {
                AddressBar(window, browser, url)
                Spacer(Modifier.height(10.dp))
                WebView(browser)
            }
        }
    }
}


fun main(args: Array<String>) = application {
    val browser = when {
        args.isEmpty() -> BrowserView()
        args[0] == "slices" -> BrowserSlicer(IntSize(800, 700))
        else -> {
            BrowserView()
        }
    }
    val url = mutableStateOf("https://www.google.com")

    Window(onCloseRequest = ::exitApplication) {
        window.title = "CEF-compose"
        window.addWindowFocusListener(object : WindowAdapter() {
            override fun windowGainedFocus(e: WindowEvent?) {
                browser.load(this@Window.window, url.value)
            }
        })
        App(window, browser, url)
    }
}

@Composable
private fun AddressBar(composeWindow: ComposeWindow, browser: Browser, url: MutableState<String>) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .height(58.dp)
            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 0.dp)
    ) {
        Row {
            TextField(
                value = url.value,
                onValueChange = {
                    url.value = "it"
                },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.textFieldColors(),
                shape = CircleShape,
                label = { }
            )
            Spacer(Modifier.width(10.dp))
            Button(
                modifier = Modifier.height(48.dp),
                shape = CircleShape,
                onClick = { browser.load(composeWindow, url.value) }
            ) {
                Text(text = "Go!")
            }
        }
    }
}

@Composable
private fun WebView(browser: Browser) {
    Surface(
        color = Color.Gray,
        modifier = Modifier.fillMaxSize().padding(10.dp)
    ) {
        when (browser) {
            is BrowserView -> {
                browser.view()
            }
            is BrowserSlicer -> {
                Column {
                    browser.slice(0, 200)
                    Spacer(Modifier.height(30.dp))
                    browser.slice(200, 200)
                    Spacer(Modifier.height(30.dp))
                    browser.tail()
                }
            }
        }
    }
}
