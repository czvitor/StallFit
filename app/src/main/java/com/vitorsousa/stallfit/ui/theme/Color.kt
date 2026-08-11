package com.vitorsousa.stallfit.ui.theme

import androidx.compose.ui.graphics.Color

// Marca & ação
val VoltNeon = Color(0xFFCCFF00)        // botões primários, CTA de salvar série, métricas ativas
val VoltNeonVariant = Color(0xFFA6D400) // hover, press e foco secundário

// Superfícies & hierarquia (dark theme)
val ObsidianBackground = Color(0xFF0D0F12) // fundo principal da aplicação
val DarkSlateCard = Color(0xFF16191E)      // cards de exercícios, refeições e containers
val CharcoalInput = Color(0xFF22262F)      // campos de digitação, modais, chips
val SteelBorder = Color(0xFF2E333D)        // divisores, bordas de cards, linhas de tabela

// Tipografia & leitura
val TextPrimary = Color(0xFFF0F3F8)   // títulos, cargas, valores numéricos principais
val TextSecondary = Color(0xFF9EA7B6) // labels, unidades (kg, reps, kcal), descrições
val TextDisabled = Color(0xFF535C6A)  // placeholders, estados inativos

// Semânticas — feedback rápido
val StatusSuccess = Color(0xFF00E676) // série/exercício concluído, meta de proteína atingida
val StatusWater = Color(0xFF00B0FF)   // timer de descanso, progresso de água
val StatusWarning = Color(0xFFFF3B30) // ações destrutivas (excluir), estouro de calorias
