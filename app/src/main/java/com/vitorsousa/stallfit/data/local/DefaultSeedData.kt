package com.vitorsousa.stallfit.data.local

import com.vitorsousa.stallfit.data.local.entity.Equipment
import com.vitorsousa.stallfit.data.local.entity.ExerciseEntity
import com.vitorsousa.stallfit.data.local.entity.FoodEntity
import com.vitorsousa.stallfit.data.local.entity.MacroGoalEntity

/**
 * Starter content so a fresh install of StällFit is immediately usable — a full gym exercise
 * catalog across every muscle group/equipment type, and a broad everyday food list with macros —
 * instead of opening to empty lists. Everything here is editable/deletable like any user-created
 * entry. Food values are per 100 g (edible/cooked portion, matching [FoodEntity]'s convention).
 */
object DefaultSeedData {

    val exercises = listOf(
        // Peito
        ExerciseEntity(name = "Supino Reto", muscleGroup = "Peito", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Supino Reto com Halteres", muscleGroup = "Peito", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Supino Inclinado", muscleGroup = "Peito", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Supino Inclinado com Halteres", muscleGroup = "Peito", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Supino Declinado", muscleGroup = "Peito", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Supino Máquina", muscleGroup = "Peito", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Crucifixo", muscleGroup = "Peito", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Crucifixo Inclinado", muscleGroup = "Peito", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Crossover", muscleGroup = "Peito", equipment = Equipment.CABLE),
        ExerciseEntity(name = "Peck Deck (Voador)", muscleGroup = "Peito", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Pullover", muscleGroup = "Peito", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Flexão de Braço", muscleGroup = "Peito", equipment = Equipment.NONE),

        // Costas
        ExerciseEntity(name = "Puxada Frontal", muscleGroup = "Costas", equipment = Equipment.CABLE),
        ExerciseEntity(name = "Puxada Frontal Pegada Aberta", muscleGroup = "Costas", equipment = Equipment.CABLE),
        ExerciseEntity(name = "Puxada Supinada", muscleGroup = "Costas", equipment = Equipment.CABLE),
        ExerciseEntity(name = "Remada Curvada", muscleGroup = "Costas", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Remada Cavalinho (T-bar)", muscleGroup = "Costas", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Remada Unilateral com Halter", muscleGroup = "Costas", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Remada Baixa", muscleGroup = "Costas", equipment = Equipment.CABLE),
        ExerciseEntity(name = "Remada Máquina", muscleGroup = "Costas", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Levantamento Terra", muscleGroup = "Costas", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Levantamento Terra Romeno", muscleGroup = "Costas", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Barra Fixa", muscleGroup = "Costas", equipment = Equipment.NONE),
        ExerciseEntity(name = "Hiperextensão", muscleGroup = "Costas", equipment = Equipment.NONE),

        // Pernas — Quadríceps
        ExerciseEntity(name = "Agachamento Livre", muscleGroup = "Pernas", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Agachamento Frontal", muscleGroup = "Pernas", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Agachamento Smith", muscleGroup = "Pernas", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Agachamento Búlgaro", muscleGroup = "Pernas", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Leg Press", muscleGroup = "Pernas", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Hack Machine", muscleGroup = "Pernas", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Cadeira Extensora", muscleGroup = "Pernas", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Avanço (Afundo)", muscleGroup = "Pernas", equipment = Equipment.DUMBBELL),

        // Pernas — Posterior de coxa e glúteos
        ExerciseEntity(name = "Mesa Flexora", muscleGroup = "Pernas", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Cadeira Flexora", muscleGroup = "Pernas", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Stiff", muscleGroup = "Pernas", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Elevação Pélvica (Hip Thrust)", muscleGroup = "Pernas", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Cadeira Abdutora", muscleGroup = "Pernas", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Cadeira Adutora", muscleGroup = "Pernas", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Glúteo no Cabo (Coice)", muscleGroup = "Pernas", equipment = Equipment.CABLE),

        // Pernas — Panturrilha
        ExerciseEntity(name = "Panturrilha em Pé", muscleGroup = "Pernas", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Panturrilha Sentado", muscleGroup = "Pernas", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Panturrilha no Leg Press", muscleGroup = "Pernas", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Panturrilha em Pé com Halteres", muscleGroup = "Pernas", equipment = Equipment.DUMBBELL),

        // Ombro
        ExerciseEntity(name = "Desenvolvimento Militar", muscleGroup = "Ombro", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Desenvolvimento com Halteres", muscleGroup = "Ombro", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Desenvolvimento Máquina", muscleGroup = "Ombro", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Elevação Lateral", muscleGroup = "Ombro", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Elevação Lateral no Cabo", muscleGroup = "Ombro", equipment = Equipment.CABLE),
        ExerciseEntity(name = "Elevação Frontal", muscleGroup = "Ombro", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Remada Alta", muscleGroup = "Ombro", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Crucifixo Invertido", muscleGroup = "Ombro", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Face Pull", muscleGroup = "Ombro", equipment = Equipment.CABLE),
        ExerciseEntity(name = "Encolhimento de Ombros (Shrug)", muscleGroup = "Ombro", equipment = Equipment.BARBELL),

        // Bíceps
        ExerciseEntity(name = "Rosca Direta", muscleGroup = "Bíceps", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Rosca Alternada", muscleGroup = "Bíceps", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Rosca Martelo", muscleGroup = "Bíceps", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Rosca Scott", muscleGroup = "Bíceps", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Rosca Concentrada", muscleGroup = "Bíceps", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Rosca no Cabo", muscleGroup = "Bíceps", equipment = Equipment.CABLE),

        // Tríceps
        ExerciseEntity(name = "Tríceps Corda", muscleGroup = "Tríceps", equipment = Equipment.CABLE),
        ExerciseEntity(name = "Tríceps Barra V", muscleGroup = "Tríceps", equipment = Equipment.CABLE),
        ExerciseEntity(name = "Tríceps Testa", muscleGroup = "Tríceps", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Tríceps Francês", muscleGroup = "Tríceps", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Tríceps Coice (Kickback)", muscleGroup = "Tríceps", equipment = Equipment.DUMBBELL),
        ExerciseEntity(name = "Supino Fechado", muscleGroup = "Tríceps", equipment = Equipment.BARBELL),
        ExerciseEntity(name = "Mergulho no Banco (Dips)", muscleGroup = "Tríceps", equipment = Equipment.NONE),

        // Abdômen
        ExerciseEntity(name = "Abdominal Supra", muscleGroup = "Abdômen", equipment = Equipment.NONE),
        ExerciseEntity(name = "Abdominal Infra", muscleGroup = "Abdômen", equipment = Equipment.NONE),
        ExerciseEntity(name = "Prancha", muscleGroup = "Abdômen", equipment = Equipment.NONE),
        ExerciseEntity(name = "Prancha Lateral", muscleGroup = "Abdômen", equipment = Equipment.NONE),
        ExerciseEntity(name = "Elevação de Pernas", muscleGroup = "Abdômen", equipment = Equipment.NONE),
        ExerciseEntity(name = "Abdominal na Máquina", muscleGroup = "Abdômen", equipment = Equipment.MACHINE),
        ExerciseEntity(name = "Abdominal no Cabo", muscleGroup = "Abdômen", equipment = Equipment.CABLE),
        ExerciseEntity(name = "Rotação de Tronco no Cabo", muscleGroup = "Abdômen", equipment = Equipment.CABLE),

        // Cardio
        ExerciseEntity(name = "Esteira", muscleGroup = "Cardio", equipment = Equipment.OTHER),
        ExerciseEntity(name = "Bicicleta Ergométrica", muscleGroup = "Cardio", equipment = Equipment.OTHER),
        ExerciseEntity(name = "Elíptico", muscleGroup = "Cardio", equipment = Equipment.OTHER),
        ExerciseEntity(name = "Pular Corda", muscleGroup = "Cardio", equipment = Equipment.NONE)
    )

    val foods = listOf(
        // Carboidratos
        FoodEntity(name = "Arroz Branco Cozido", caloriesPer100g = 130.0, proteinPer100g = 2.7, carbsPer100g = 28.0, fatPer100g = 0.3),
        FoodEntity(name = "Arroz Integral Cozido", caloriesPer100g = 124.0, proteinPer100g = 2.6, carbsPer100g = 25.8, fatPer100g = 1.0),
        FoodEntity(name = "Batata Doce Cozida", caloriesPer100g = 86.0, proteinPer100g = 1.6, carbsPer100g = 20.0, fatPer100g = 0.1),
        FoodEntity(name = "Batata Inglesa Cozida", caloriesPer100g = 87.0, proteinPer100g = 1.9, carbsPer100g = 20.1, fatPer100g = 0.1),
        FoodEntity(name = "Mandioca Cozida", caloriesPer100g = 125.0, proteinPer100g = 0.6, carbsPer100g = 30.0, fatPer100g = 0.3),
        FoodEntity(name = "Macarrão Cozido", caloriesPer100g = 158.0, proteinPer100g = 5.8, carbsPer100g = 31.0, fatPer100g = 0.9),
        FoodEntity(name = "Pão Francês", caloriesPer100g = 300.0, proteinPer100g = 8.0, carbsPer100g = 58.0, fatPer100g = 3.0),
        FoodEntity(name = "Pão Integral", caloriesPer100g = 247.0, proteinPer100g = 13.0, carbsPer100g = 41.0, fatPer100g = 3.4),
        FoodEntity(name = "Tapioca (goma hidratada)", caloriesPer100g = 240.0, proteinPer100g = 0.0, carbsPer100g = 59.0, fatPer100g = 0.0),
        FoodEntity(name = "Aveia em Flocos", caloriesPer100g = 389.0, proteinPer100g = 17.0, carbsPer100g = 66.0, fatPer100g = 7.0),
        FoodEntity(name = "Quinoa Cozida", caloriesPer100g = 120.0, proteinPer100g = 4.4, carbsPer100g = 21.3, fatPer100g = 1.9),
        FoodEntity(name = "Cuscuz Cozido", caloriesPer100g = 112.0, proteinPer100g = 3.8, carbsPer100g = 23.2, fatPer100g = 0.2),

        // Proteínas
        FoodEntity(name = "Peito de Frango Grelhado", caloriesPer100g = 165.0, proteinPer100g = 31.0, carbsPer100g = 0.0, fatPer100g = 3.6),
        FoodEntity(name = "Coxa de Frango Assada", caloriesPer100g = 209.0, proteinPer100g = 26.0, carbsPer100g = 0.0, fatPer100g = 10.9),
        FoodEntity(name = "Carne Moída (patinho)", caloriesPer100g = 172.0, proteinPer100g = 26.0, carbsPer100g = 0.0, fatPer100g = 7.0),
        FoodEntity(name = "Alcatra Grelhada", caloriesPer100g = 163.0, proteinPer100g = 30.0, carbsPer100g = 0.0, fatPer100g = 4.5),
        FoodEntity(name = "Picanha Grelhada", caloriesPer100g = 289.0, proteinPer100g = 25.0, carbsPer100g = 0.0, fatPer100g = 21.0),
        FoodEntity(name = "Lombo Suíno Grelhado", caloriesPer100g = 143.0, proteinPer100g = 26.0, carbsPer100g = 0.0, fatPer100g = 4.0),
        FoodEntity(name = "Ovo Cozido", caloriesPer100g = 155.0, proteinPer100g = 13.0, carbsPer100g = 1.1, fatPer100g = 11.0),
        FoodEntity(name = "Clara de Ovo", caloriesPer100g = 52.0, proteinPer100g = 11.0, carbsPer100g = 0.7, fatPer100g = 0.2),
        FoodEntity(name = "Tilápia Grelhada", caloriesPer100g = 128.0, proteinPer100g = 26.0, carbsPer100g = 0.0, fatPer100g = 2.7),
        FoodEntity(name = "Salmão Grelhado", caloriesPer100g = 208.0, proteinPer100g = 20.0, carbsPer100g = 0.0, fatPer100g = 13.0),
        FoodEntity(name = "Atum em Água (lata)", caloriesPer100g = 116.0, proteinPer100g = 26.0, carbsPer100g = 0.0, fatPer100g = 0.8),
        FoodEntity(name = "Whey Protein (pó)", caloriesPer100g = 400.0, proteinPer100g = 80.0, carbsPer100g = 8.0, fatPer100g = 5.0),
        FoodEntity(name = "Tofu", caloriesPer100g = 76.0, proteinPer100g = 8.0, carbsPer100g = 1.9, fatPer100g = 4.8),

        // Leguminosas
        FoodEntity(name = "Feijão Carioca Cozido", caloriesPer100g = 76.0, proteinPer100g = 4.8, carbsPer100g = 13.6, fatPer100g = 0.5),
        FoodEntity(name = "Feijão Preto Cozido", caloriesPer100g = 77.0, proteinPer100g = 4.5, carbsPer100g = 14.0, fatPer100g = 0.5),
        FoodEntity(name = "Lentilha Cozida", caloriesPer100g = 116.0, proteinPer100g = 9.0, carbsPer100g = 20.0, fatPer100g = 0.4),
        FoodEntity(name = "Grão de Bico Cozido", caloriesPer100g = 164.0, proteinPer100g = 8.9, carbsPer100g = 27.4, fatPer100g = 2.6),

        // Laticínios
        FoodEntity(name = "Iogurte Natural Integral", caloriesPer100g = 61.0, proteinPer100g = 3.5, carbsPer100g = 4.7, fatPer100g = 3.3),
        FoodEntity(name = "Iogurte Grego Natural", caloriesPer100g = 97.0, proteinPer100g = 9.0, carbsPer100g = 3.6, fatPer100g = 5.0),
        FoodEntity(name = "Leite Integral", caloriesPer100g = 61.0, proteinPer100g = 3.2, carbsPer100g = 4.7, fatPer100g = 3.3),
        FoodEntity(name = "Leite Desnatado", caloriesPer100g = 35.0, proteinPer100g = 3.4, carbsPer100g = 5.0, fatPer100g = 0.1),
        FoodEntity(name = "Queijo Minas Frescal", caloriesPer100g = 264.0, proteinPer100g = 17.4, carbsPer100g = 3.2, fatPer100g = 20.2),
        FoodEntity(name = "Requeijão", caloriesPer100g = 257.0, proteinPer100g = 9.6, carbsPer100g = 3.0, fatPer100g = 23.0),
        FoodEntity(name = "Queijo Cottage", caloriesPer100g = 98.0, proteinPer100g = 11.0, carbsPer100g = 3.4, fatPer100g = 4.3),

        // Frutas
        FoodEntity(name = "Banana", caloriesPer100g = 89.0, proteinPer100g = 1.1, carbsPer100g = 23.0, fatPer100g = 0.3),
        FoodEntity(name = "Maçã", caloriesPer100g = 52.0, proteinPer100g = 0.3, carbsPer100g = 14.0, fatPer100g = 0.2),
        FoodEntity(name = "Laranja", caloriesPer100g = 47.0, proteinPer100g = 0.9, carbsPer100g = 12.0, fatPer100g = 0.1),
        FoodEntity(name = "Mamão", caloriesPer100g = 43.0, proteinPer100g = 0.5, carbsPer100g = 11.0, fatPer100g = 0.1),
        FoodEntity(name = "Manga", caloriesPer100g = 60.0, proteinPer100g = 0.8, carbsPer100g = 15.0, fatPer100g = 0.4),
        FoodEntity(name = "Abacate", caloriesPer100g = 160.0, proteinPer100g = 2.0, carbsPer100g = 8.5, fatPer100g = 14.7),
        FoodEntity(name = "Uva", caloriesPer100g = 69.0, proteinPer100g = 0.7, carbsPer100g = 18.0, fatPer100g = 0.2),
        FoodEntity(name = "Morango", caloriesPer100g = 32.0, proteinPer100g = 0.7, carbsPer100g = 7.7, fatPer100g = 0.3),
        FoodEntity(name = "Abacaxi", caloriesPer100g = 50.0, proteinPer100g = 0.5, carbsPer100g = 13.0, fatPer100g = 0.1),

        // Vegetais
        FoodEntity(name = "Brócolis Cozido", caloriesPer100g = 35.0, proteinPer100g = 2.4, carbsPer100g = 7.2, fatPer100g = 0.4),
        FoodEntity(name = "Espinafre Cozido", caloriesPer100g = 23.0, proteinPer100g = 3.0, carbsPer100g = 3.6, fatPer100g = 0.4),
        FoodEntity(name = "Cenoura Cozida", caloriesPer100g = 35.0, proteinPer100g = 0.8, carbsPer100g = 8.0, fatPer100g = 0.2),
        FoodEntity(name = "Tomate", caloriesPer100g = 18.0, proteinPer100g = 0.9, carbsPer100g = 3.9, fatPer100g = 0.2),
        FoodEntity(name = "Alface", caloriesPer100g = 15.0, proteinPer100g = 1.4, carbsPer100g = 2.9, fatPer100g = 0.2),
        FoodEntity(name = "Abobrinha Cozida", caloriesPer100g = 17.0, proteinPer100g = 1.2, carbsPer100g = 3.1, fatPer100g = 0.3),

        // Gorduras e oleaginosas
        FoodEntity(name = "Azeite de Oliva", caloriesPer100g = 884.0, proteinPer100g = 0.0, carbsPer100g = 0.0, fatPer100g = 100.0),
        FoodEntity(name = "Amendoim", caloriesPer100g = 567.0, proteinPer100g = 25.8, carbsPer100g = 16.1, fatPer100g = 49.2),
        FoodEntity(name = "Pasta de Amendoim", caloriesPer100g = 588.0, proteinPer100g = 25.0, carbsPer100g = 20.0, fatPer100g = 50.0),
        FoodEntity(name = "Castanha do Pará", caloriesPer100g = 656.0, proteinPer100g = 14.3, carbsPer100g = 12.3, fatPer100g = 66.4),
        FoodEntity(name = "Castanha de Caju", caloriesPer100g = 553.0, proteinPer100g = 18.2, carbsPer100g = 30.2, fatPer100g = 43.9),
        FoodEntity(name = "Amêndoas", caloriesPer100g = 579.0, proteinPer100g = 21.2, carbsPer100g = 21.6, fatPer100g = 49.9),
        FoodEntity(name = "Óleo de Coco", caloriesPer100g = 862.0, proteinPer100g = 0.0, carbsPer100g = 0.0, fatPer100g = 99.1),

        // Outros
        FoodEntity(name = "Mel", caloriesPer100g = 304.0, proteinPer100g = 0.3, carbsPer100g = 82.4, fatPer100g = 0.0),
        FoodEntity(name = "Açúcar Refinado", caloriesPer100g = 387.0, proteinPer100g = 0.0, carbsPer100g = 100.0, fatPer100g = 0.0),
        FoodEntity(name = "Granola", caloriesPer100g = 471.0, proteinPer100g = 10.0, carbsPer100g = 64.0, fatPer100g = 20.0)
    )

    val defaultMacroGoal = MacroGoalEntity(
        calorieGoal = 2200,
        proteinGoal = 160,
        carbGoal = 220,
        fatGoal = 70
    )
}
