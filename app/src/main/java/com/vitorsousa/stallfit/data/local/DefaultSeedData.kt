package com.vitorsousa.stallfit.data.local

import com.vitorsousa.stallfit.data.local.entity.Equipment
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
        ExerciseEntity(name = "Supino Reto", muscleGroup = "Peito", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Supino Inclinado", muscleGroup = "Peito", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Crucifixo", muscleGroup = "Peito", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Puxada Frontal", muscleGroup = "Costas", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Remada Curvada", muscleGroup = "Costas", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Levantamento Terra", muscleGroup = "Costas", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Agachamento Livre", muscleGroup = "Pernas", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Leg Press", muscleGroup = "Pernas", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Cadeira Extensora", muscleGroup = "Pernas", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Mesa Flexora", muscleGroup = "Pernas", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Panturrilha em Pé", muscleGroup = "Pernas", equipment = Equipment.NONE),
        ExerciseEntity(name = "Desenvolvimento Militar", muscleGroup = "Ombro", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Elevação Lateral", muscleGroup = "Ombro", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Rosca Direta", muscleGroup = "Bíceps", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Rosca Alternada", muscleGroup = "Bíceps", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Tríceps Corda", muscleGroup = "Tríceps", equipment = Equipment.CABLE),
        ExerciseEntity(name = "Tríceps Testa", muscleGroup = "Tríceps", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Abdominal Supra", muscleGroup = "Abdômen", equipment = Equipment.NONE),
        ExerciseEntity(name = "Prancha", muscleGroup = "Abdômen", equipment = Equipment.NONE)
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
