# Paleta de cores

StällFit é dark-first: o tema escuro abaixo é o padrão do app, mas existe também uma variante
clara (Light Mode) que o usuário liga manualmente pelo botão Sol/Lua no cabeçalho. Sem escolha
salva, o app sempre abre no tema escuro — a preferência do sistema operacional não é consultada.

## Dark Mode (padrão)

### Cores de marca & ação

| Nome | Hex | Aplicação principal |
|---|---|---|
| Volt Neon | `#CCFF00` | Botões primários, CTA de salvar série, métricas ativas |
| Volt Variant | `#A6D400` | Hover, press e foco secundário |

### Superfícies & hierarquia

| Nome | Hex | Aplicação principal |
|---|---|---|
| Obsidian Black | `#0D0F12` | Fundo principal da aplicação |
| Dark Slate | `#16191E` | Cards de exercícios, refeições e containers |
| Charcoal Input | `#22262F` | Campos de digitação, modais, chips |
| Steel Border | `#2E333D` | Divisores, bordas de cards, linhas de tabela |

### Tipografia & leitura (cores de texto)

| Nome | Hex | Aplicação principal |
|---|---|---|
| Text Primary | `#F0F3F8` | Títulos, cargas, valores numéricos principais |
| Text Secondary | `#9EA7B6` | Labels, unidades (kg, reps, kcal), descrições |
| Text Disabled | `#535C6A` | Placeholders, estados inativos |

### Cores semânticas

| Nome | Hex | Aplicação principal |
|---|---|---|
| Cyber Lime | `#00E676` | Série/exercício concluído, meta de proteína atingida |
| Hydro Blue | `#00B0FF` | Timer de descanso, progresso de água |
| Crimson Red | `#FF3B30` | Ações destrutivas, estouro de calorias |

## Light Mode

Paleta clara opcional: fundo off-white (nunca branco puro, para não estourar o brilho da tela),
cards em branco puro para criar profundidade, e acentos aprofundados (tons 800/900) no lugar dos
tons vivos do Dark Mode — os hexes vivos (ex.: Volt Neon) rendem ~1.2:1 de contraste como
texto/ícone sobre branco, então cada acento foi reescolhido para ficar **≥7:1 (WCAG AAA)** contra
as superfícies claras abaixo, mantendo a mesma família de cor (lima, esmeralda, azul, vermelho).

### Cores de marca & ação

| Nome | Hex | Aplicação principal |
|---|---|---|
| Light Primary | `#365314` | Botões primários, CTA, métricas ativas (também usado como texto sobre `primaryContainer`) |
| Light Primary Container | `#E4F0C8` | Fundo suave atrás de ícone/texto primário (ex.: indicador da nav bar) |

### Superfícies & hierarquia

| Nome | Hex | Aplicação principal |
|---|---|---|
| Light Background | `#F8FAFC` | Fundo principal da aplicação (Slate 50) |
| Light Surface Dim | `#F1F5F9` | Barra de navegação, inputs, fundo de diálogos (Slate 100) |
| Light Surface Card | `#FFFFFF` | Cards e modais — branco puro para destacar do fundo off-white |
| Light Outline | `#CBD5E1` | Divisores, bordas de cards, linhas de tabela |
| Light Outline Variant | `#E2E8F0` | Divisores mais discretos |

### Tipografia & leitura (cores de texto)

| Nome | Hex | Aplicação principal |
|---|---|---|
| Light Text Primary | `#0F172A` | Títulos, cargas, valores numéricos principais (Slate 900) |
| Light Text Secondary | `#334155` | Labels, unidades, descrições (Slate 700 — folga extra sobre o mínimo AAA) |
| Light Text Disabled | `#64748B` | Placeholders, estados inativos |

### Cores semânticas

| Nome | Hex | Aplicação principal |
|---|---|---|
| Light Status Success | `#065F46` | Série/exercício concluído, meta de proteína atingida |
| Light Status Water | `#0C4A6E` | Timer de descanso, progresso de água |
| Light Status Warning | `#991B1B` | Ações destrutivas, estouro de calorias |

## Alternância de tema

O botão Sol ☀️ / Lua 🌙 fica no cabeçalho de todas as telas principais (ao lado do ícone de metas
na aba Nutrição). A troca de cores usa `Crossfade` com 300ms para evitar um flash abrupto de
tela, e a escolha é persistida via DataStore Preferences (`ThemePreferences` → `ThemeRepository`
→ `ThemeViewModel`), lida de volta em toda abertura do app.

## Implementado em

- [`app/src/main/java/com/vitorsousa/stallfit/ui/theme/Color.kt`](../../app/src/main/java/com/vitorsousa/stallfit/ui/theme/Color.kt) — todos os tokens acima. Dark: `VoltNeon`, `VoltNeonVariant`, `ObsidianBackground`, `DarkSlateCard`, `CharcoalInput`, `SteelBorder`, `TextPrimary`, `TextSecondary`, `TextDisabled`, `StatusSuccess`, `StatusWater`, `StatusWarning`. Light: `LightPrimary`, `LightPrimaryContainer`, `LightBackground`, `LightSurfaceDim`, `LightSurfaceCard`, `LightOutline`, `LightOutlineVariant`, `LightTextPrimary`, `LightTextSecondary`, `LightTextDisabled`, `LightStatusSuccess`, `LightStatusWater`, `LightStatusWarning`.
- [`app/src/main/java/com/vitorsousa/stallfit/ui/theme/Theme.kt`](../../app/src/main/java/com/vitorsousa/stallfit/ui/theme/Theme.kt) — mapeamento desses tokens para os papéis semânticos do Material3 (`primary`, `background`, `surface`, `error`, etc.) via `darkColorScheme`/`lightColorScheme`, consumidos em todas as telas via `MaterialTheme.colorScheme.*`. `StallFitTheme(isDarkTheme: Boolean)` escolhe entre os dois e faz o cross-fade.
- [`app/src/main/java/com/vitorsousa/stallfit/ui/theme/ThemeViewModel.kt`](../../app/src/main/java/com/vitorsousa/stallfit/ui/theme/ThemeViewModel.kt), [`data/local/ThemePreferences.kt`](../../app/src/main/java/com/vitorsousa/stallfit/data/local/ThemePreferences.kt) e [`data/repository/ThemeRepository.kt`](../../app/src/main/java/com/vitorsousa/stallfit/data/repository/ThemeRepository.kt) — persistência da escolha do usuário.
- [`app/src/main/res/drawable/stallfit_symbol_light.xml`](../../app/src/main/res/drawable/stallfit_symbol_light.xml) e [`stallfit_wordmark_light.xml`](../../app/src/main/res/drawable/stallfit_wordmark_light.xml) — variantes do logo com a tinta branca trocada por `Light Text Primary`, usadas no cabeçalho quando o Light Mode está ativo.

`Text Disabled`/`Light Text Disabled` ficam disponíveis como tokens diretos (não existe um papel
"disabled" nativo no `ColorScheme` do Material3 — estados desabilitados usam alpha sobre a cor de
conteúdo atual).
