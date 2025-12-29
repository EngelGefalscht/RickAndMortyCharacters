package com.cda.data.mapper

internal fun extractIdFromUrl(url: String): Int? =
    url.trimEnd('/').substringAfterLast('/', missingDelimiterValue = "").toIntOrNull()
