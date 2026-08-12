package com.vitorsousa.stallfit.data.local.entity

/** [multiplier] scales BMR into TDEE (Harris-Benedict/Mifflin-St Jeor activity factor convention). */
enum class ActivityLevel(val label: String, val multiplier: Double) {
    SEDENTARY("Sedentário (não treina)", 1.2),
    LIGHT("Leve (treina 1 a 3x/semana)", 1.375),
    MODERATE("Moderado (treina 3 a 5x/semana)", 1.55),
    INTENSE("Intenso (treina 6 a 7x/semana)", 1.725),
    VERY_INTENSE("Muito intenso (treina 2x ao dia ou trabalho físico)", 1.9)
}
