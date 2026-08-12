package com.vitorsousa.stallfit.core.util

import java.text.Normalizer

/**
 * Case- and accent-insensitive comparison key for user-entered names, so e.g. "Supino Reto" and
 * "supino  reto" — or "Café" and "Cafe" — are recognized as the same entry when de-duplicating
 * exercises/foods (seeded or custom).
 */
fun String.normalizedKey(): String {
    val withoutAccents = Normalizer.normalize(trim(), Normalizer.Form.NFD)
        .replace("\\p{Mn}+".toRegex(), "")
    return withoutAccents.lowercase().replace("\\s+".toRegex(), " ")
}
