package org.company.app.utils

import kotlinx.browser.window

internal actual fun getInfoFromUrl(): String = window.location.search