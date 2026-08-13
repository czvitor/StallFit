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

// ============================================================================
// Light theme — superfícies off-white/brancas com cores de acento aprofundadas.
// Os tons de marca acima (ex.: Volt Neon) falham como cor de texto/ícone sobre
// fundo claro (~1.2:1 de contraste) — cada acento aqui usa um tom "800/900"
// equivalente, verificado ≥7:1 (WCAG AAA) contra os dois tons de superfície
// abaixo, mantendo a mesma família de cor (lima, esmeralda, azul, vermelho).
// ============================================================================

// Marca & ação
val LightPrimary = Color(0xFF365314)          // botões primários, CTA, métricas ativas — também usado como onPrimaryContainer
val LightPrimaryContainer = Color(0xFFE4F0C8) // fundo suave atrás de ícone/texto primário (ex.: indicador da nav bar)

// Superfícies & hierarquia (light theme) — do off-white (fundo) ao branco puro (cards/modais)
val LightBackground = Color(0xFFF8FAFC)     // fundo principal — nunca branco puro, evita estourar o brilho da tela
val LightSurfaceDim = Color(0xFFF1F5F9)     // barra de navegação, inputs, fundo de diálogos
val LightSurfaceCard = Color(0xFFFFFFFF)    // cards e modais — branco puro reservado para destacar do fundo off-white
val LightOutline = Color(0xFFCBD5E1)        // divisores, bordas de cards, linhas de tabela
val LightOutlineVariant = Color(0xFFE2E8F0) // divisores mais discretos

// Tons de contêiner — versões suaves das cores semânticas, atrás de ícone/texto da mesma cor
val LightSecondaryContainer = Color(0xFFD4F3E4)
val LightTertiaryContainer = Color(0xFFD6ECF8)
val LightErrorContainer = Color(0xFFFBDADA)

// Tipografia & leitura
val LightTextPrimary = Color(0xFF0F172A)   // títulos, cargas, valores numéricos principais
val LightTextSecondary = Color(0xFF334155) // labels, unidades, descrições (Slate 700 — folga extra sobre AAA)
val LightTextDisabled = Color(0xFF64748B)  // placeholders, estados inativos

// Semânticas — feedback rápido (também usados como onSecondaryContainer/onTertiaryContainer/onErrorContainer)
val LightStatusSuccess = Color(0xFF065F46) // série/exercício concluído, meta de proteína atingida
val LightStatusWater = Color(0xFF0C4A6E)   // timer de descanso, progresso de água
val LightStatusWarning = Color(0xFF991B1B) // ações destrutivas (excluir), estouro de calorias
