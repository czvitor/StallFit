package com.vitorsousa.stallfit.ui.theme

import androidx.compose.ui.graphics.Color

// Marca & ação
val VoltNeon = Color(0xFFCCFF00)        // botões primários, CTA de salvar série, métricas ativas
val VoltNeonVariant = Color(0xFFA6D400) // hover, press e foco secundário

// Superfícies & hierarquia (dark theme) — escada tonal do mais escuro (fundo) ao mais claro
// (elementos "flutuantes" como diálogos), usada para dar profundidade sem depender de sombra.
val ObsidianBackground = Color(0xFF0D0F12)     // fundo principal da aplicação (nível mais baixo)
val SurfaceContainerLow = Color(0xFF12151A)    // leve elevação sobre o fundo (ex.: barra de navegação)
val DarkSlateCard = Color(0xFF16191E)          // cards de exercícios, refeições e containers
val CharcoalInput = Color(0xFF22262F)          // campos de digitação, chips, fundo de diálogos
val SurfaceContainerHighest = Color(0xFF2A2F3A) // nível mais claro — menus e elementos elevados
val SteelBorder = Color(0xFF2E333D)            // divisores, bordas de cards, linhas de tabela
val OutlineVariant = Color(0xFF23262E)         // divisores mais discretos que SteelBorder

// Tons de contêiner — versões escurecidas das cores semânticas, usadas atrás de ícones/texto
// da mesma cor (ex.: indicador do item selecionado na barra de navegação).
val PrimaryContainerTint = Color(0xFF333F14)   // Volt Neon escurecido
val SecondaryContainerTint = Color(0xFF163828) // StatusSuccess escurecido
val TertiaryContainerTint = Color(0xFF0F2E3D)  // StatusWater escurecido
val ErrorContainerTint = Color(0xFF3A1613)     // StatusWarning escurecido

// Tipografia & leitura
val TextPrimary = Color(0xFFF0F3F8)   // títulos, cargas, valores numéricos principais
val TextSecondary = Color(0xFF9EA7B6) // labels, unidades (kg, reps, kcal), descrições
val TextDisabled = Color(0xFF535C6A)  // placeholders, estados inativos

// Semânticas — feedback rápido
val StatusSuccess = Color(0xFF00E676) // série/exercício concluído, meta de proteína atingida
val StatusWater = Color(0xFF00B0FF)   // timer de descanso, progresso de água
val StatusWarning = Color(0xFFFF3B30) // ações destrutivas (excluir), estouro de calorias
