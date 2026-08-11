# Paleta de cores

StällFit é dark-first: a interface roda sempre no tema escuro abaixo, sem variante clara.

## Cores de marca & ação

| Nome | Hex | Aplicação principal |
|---|---|---|
| Volt Neon | `#CCFF00` | Botões primários, CTA de salvar série, métricas ativas |
| Volt Variant | `#A6D400` | Hover, press e foco secundário |

## Superfícies & hierarquia

| Nome | Hex | Aplicação principal |
|---|---|---|
| Obsidian Black | `#0D0F12` | Fundo principal da aplicação |
| Dark Slate | `#16191E` | Cards de exercícios, refeições e containers |
| Charcoal Input | `#22262F` | Campos de digitação, modais, chips |
| Steel Border | `#2E333D` | Divisores, bordas de cards, linhas de tabela |

## Tipografia & leitura (cores de texto)

| Nome | Hex | Aplicação principal |
|---|---|---|
| Text Primary | `#F0F3F8` | Títulos, cargas, valores numéricos principais |
| Text Secondary | `#9EA7B6` | Labels, unidades (kg, reps, kcal), descrições |
| Text Disabled | `#535C6A` | Placeholders, estados inativos |

## Cores semânticas

| Nome | Hex | Aplicação principal |
|---|---|---|
| Cyber Lime | `#00E676` | Série/exercício concluído, meta de proteína atingida |
| Hydro Blue | `#00B0FF` | Timer de descanso, progresso de água |
| Crimson Red | `#FF3B30` | Ações destrutivas, estouro de calorias |

## Implementado em

- [`app/src/main/java/com/vitorsousa/stallfit/ui/theme/Color.kt`](../../app/src/main/java/com/vitorsousa/stallfit/ui/theme/Color.kt) — todos os tokens acima, com os mesmos nomes (`VoltNeon`, `VoltNeonVariant`, `ObsidianBackground`, `DarkSlateCard`, `CharcoalInput`, `SteelBorder`, `TextPrimary`, `TextSecondary`, `TextDisabled`, `StatusSuccess`, `StatusWater`, `StatusWarning`).
- [`app/src/main/java/com/vitorsousa/stallfit/ui/theme/Theme.kt`](../../app/src/main/java/com/vitorsousa/stallfit/ui/theme/Theme.kt) — mapeamento desses tokens para os papéis semânticos do `darkColorScheme` do Material3 (`primary`, `background`, `surface`, `error`, etc.), consumidos em todas as telas via `MaterialTheme.colorScheme.*`.

`Text Disabled` fica disponível como token direto (não existe um papel "disabled" nativo no `ColorScheme` do Material3 — estados desabilitados usam alpha sobre a cor de conteúdo atual).
