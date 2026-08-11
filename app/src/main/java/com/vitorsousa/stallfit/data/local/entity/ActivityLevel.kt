package com.vitorsousa.stallfit.data.local.entity

/** [multiplier] scales BMR into TDEE (Harris-Benedict/Mifflin-St Jeor activity factor convention). */
enum class ActivityLevel(val label: String, val multiplier: Double) {
    SEDENTARY("Sedentário", 1.2),
    LIGHT("Leve", 1.375),
    MODERATE("Moderado", 1.55),
    INTENSE("Intenso", 1.725),
    VERY_INTENSE("Muito intenso", 1.9)
}
