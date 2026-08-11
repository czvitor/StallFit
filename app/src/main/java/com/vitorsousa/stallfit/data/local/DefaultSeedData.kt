package com.vitorsousa.stallfit.data.local

import com.vitorsousa.stallfit.data.local.entity.ExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.FoodEntity
import com.vitorsousa.stallfit.data.local.entity.MacroGoalEntity

/**
 * Starter content so a fresh install of StällFit is immediately usable — a real gym-goer's
 * exercise list and a handful of common foods — instead of opening to empty lists. Everything
 * here is editable/deletable like any user-created entry.
 */
object DefaultSeedData {

    val exercises = listOf(
        ExerciseEntity(name = "Supino Reto", muscleGroup = "Peito"),
        ExerciseEntity(name = "Supino Inclinado", muscleGroup = "Peito"),
        ExerciseEntity(name = "Crucifixo", muscleGroup = "Peito"),
        ExerciseEntity(name = "Puxada Frontal", muscleGroup = "Costas"),
        ExerciseEntity(name = "Remada Curvada", muscleGroup = "Costas"),
        ExerciseEntity(name = "Levantamento Terra", muscleGroup = "Costas"),
        ExerciseEntity(name = "Agachamento Livre", muscleGroup = "Pernas"),
        ExerciseEntity(name = "Leg Press", muscleGroup = "Pernas"),
        ExerciseEntity(name = "Cadeira Extensora", muscleGroup = "Pernas"),
        ExerciseEntity(name = "Mesa Flexora", muscleGroup = "Pernas"),
        ExerciseEntity(name = "Panturrilha em Pé", muscleGroup = "Pernas"),
        ExerciseEntity(name = "Desenvolvimento Militar", muscleGroup = "Ombro"),
        ExerciseEntity(name = "Elevação Lateral", muscleGroup = "Ombro"),
        ExerciseEntity(name = "Rosca Direta", muscleGroup = "Bíceps"),
        ExerciseEntity(name = "Rosca Alternada", muscleGroup = "Bíceps"),
        ExerciseEntity(name = "Tríceps Corda", muscleGroup = "Tríceps"),
        ExerciseEntity(name = "Tríceps Testa", muscleGroup = "Tríceps"),
        ExerciseEntity(name = "Abdominal Supra", muscleGroup = "Abdômen"),
        ExerciseEntity(name = "Prancha", muscleGroup = "Abdômen")
    )

    val foods = listOf(
        FoodEntity(name = "Arroz Branco Cozido", caloriesPer100g = 130.0, proteinPer100g = 2.7, carbsPer100g = 28.0, fatPer100g = 0.3),
        FoodEntity(name = "Feijão Carioca Cozido", caloriesPer100g = 76.0, proteinPer100g = 4.8, carbsPer100g = 13.6, fatPer100g = 0.5),
        FoodEntity(name = "Peito de Frango Grelhado", caloriesPer100g = 165.0, proteinPer100g = 31.0, carbsPer100g = 0.0, fatPer100g = 3.6),
        FoodEntity(name = "Ovo Cozido", caloriesPer100g = 155.0, proteinPer100g = 13.0, carbsPer100g = 1.1, fatPer100g = 11.0),
        FoodEntity(name = "Batata Doce Cozida", caloriesPer100g = 86.0, proteinPer100g = 1.6, carbsPer100g = 20.0, fatPer100g = 0.1),
        FoodEntity(name = "Whey Protein (pó)", caloriesPer100g = 400.0, proteinPer100g = 80.0, carbsPer100g = 8.0, fatPer100g = 5.0),
        FoodEntity(name = "Aveia em Flocos", caloriesPer100g = 389.0, proteinPer100g = 17.0, carbsPer100g = 66.0, fatPer100g = 7.0),
        FoodEntity(name = "Banana", caloriesPer100g = 89.0, proteinPer100g = 1.1, carbsPer100g = 23.0, fatPer100g = 0.3),
        FoodEntity(name = "Pão Francês", caloriesPer100g = 300.0, proteinPer100g = 8.0, carbsPer100g = 58.0, fatPer100g = 3.0),
        FoodEntity(name = "Azeite de Oliva", caloriesPer100g = 884.0, proteinPer100g = 0.0, carbsPer100g = 0.0, fatPer100g = 100.0),
        FoodEntity(name = "Carne Moída (patinho)", caloriesPer100g = 172.0, proteinPer100g = 26.0, carbsPer100g = 0.0, fatPer100g = 7.0),
        FoodEntity(name = "Tilápia Grelhada", caloriesPer100g = 128.0, proteinPer100g = 26.0, carbsPer100g = 0.0, fatPer100g = 2.7),
        FoodEntity(name = "Iogurte Natural Integral", caloriesPer100g = 61.0, proteinPer100g = 3.5, carbsPer100g = 4.7, fatPer100g = 3.3),
        FoodEntity(name = "Amendoim", caloriesPer100g = 567.0, proteinPer100g = 25.8, carbsPer100g = 16.1, fatPer100g = 49.2),
        FoodEntity(name = "Brócolis Cozido", caloriesPer100g = 35.0, proteinPer100g = 2.4, carbsPer100g = 7.2, fatPer100g = 0.4)
    )

    val defaultMacroGoal = MacroGoalEntity(
        calorieGoal = 2200,
        proteinGoal = 160,
        carbGoal = 220,
        fatGoal = 70
    )
}
