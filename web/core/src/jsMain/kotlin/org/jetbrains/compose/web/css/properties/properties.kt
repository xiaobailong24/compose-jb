/*
 * Copyright 2020-2021 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 */

package org.jetbrains.compose.web.css

fun StylePropertyBuilder.opacity(value: Number) {
    property("opacity", value)
}

fun StylePropertyBuilder.opacity(value: CSSSizeValue<CSSUnit.percent>) {
    property("opacity", (value.value / 100))
}

fun StylePropertyBuilder.display(displayStyle: DisplayStyle) {
    property("display", displayStyle.value)
}

