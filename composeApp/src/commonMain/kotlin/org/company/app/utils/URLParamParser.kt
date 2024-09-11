package org.company.app.utils

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
@Immutable
class URLParamParser(private val urlString: String) {
    private val params: Map<String, String> = parseParams()

    private fun parseParams(): Map<String, String> {
        val paramsMap = mutableMapOf<String, String>()

        val queryStringIndex = urlString.indexOf('?')
        if (queryStringIndex != -1) {
            val queryString = urlString.substring(queryStringIndex + 1)

            for (paramPair in queryString.split('&')) {
                val parts = paramPair.split('=')
                if (parts.size == 2) {
                    val key = parts[0].trim()
                    val value = parts[1].trim()
                    paramsMap[key] = value
                }
            }
        }
        return paramsMap
    }

    fun getParam(key: String): String? {
        return params[key]
    }

    fun getAllParams(): Map<String, String> {
        return params
    }
}