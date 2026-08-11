# Tipografia

Duas famílias, ambas do Google Fonts:

- **Space Grotesk** — títulos e métricas (fonte display/técnica).
- **Inter** — interface e textos gerais.

## Hierarquia de estilos

| Estilo | Fonte | Peso | Tamanho / espaçamento | Uso |
|---|---|---|---|---|
| Display / Metric | Space Grotesk | Bold (700) | 32sp / -0.5sp | Grandes números de volume (`12.450 kg`, `2.800 kcal`) |
| Heading 1 | Space Grotesk | Bold (700) | 24sp | Títulos de tela |
| Heading 2 | Space Grotesk | SemiBold (600) | 18sp | Nome de exercícios e categorias de refeição |
| Body Large | Inter | Medium (500) | 16sp | Entradas de dados (valores em inputs de carga) |
| Body Medium | Inter | Regular (400) | 14sp | Textos informativos, descrições secundárias |
| Label / Caption | Inter | SemiBold (600) | 12sp / +0.5sp | Siglas e unidades (`KG`, `REPS`, `PROT`, `CARB`) |

## Arquivos de fonte

Baixados diretamente do repositório oficial open-source [google/fonts](https://github.com/google/fonts) (licença SIL Open Font License), como *variable fonts* (eixo `wght`), e empacotados localmente no app — sem dependência de rede ou do Google Play Services Font Provider, mantendo o princípio de app 100% local:

- [`app/src/main/res/font/space_grotesk_variable.ttf`](../../app/src/main/res/font/space_grotesk_variable.ttf)
- [`app/src/main/res/font/inter_variable.ttf`](../../app/src/main/res/font/inter_variable.ttf)

## Implementado em

- [`app/src/main/java/com/vitorsousa/stallfit/ui/theme/Type.kt`](../../app/src/main/java/com/vitorsousa/stallfit/ui/theme/Type.kt) — as duas `FontFamily` (`SpaceGrotesk`, `Inter`) construídas via `Font(..., variationSettings = FontVariation.Settings(FontVariation.weight(n)))`, e o objeto `StallFitTypography` (Material3 `Typography`) mapeando a hierarquia acima para os slots consumidos no app (`displaySmall`/`headlineMedium` para métricas grandes, `titleLarge`/`titleMedium` para Heading 1/2, `bodyLarge`/`bodyMedium` para corpo, `labelLarge`/`labelMedium`/`labelSmall` para labels/legendas).
- Requer `minSdk 26` (já é o mínimo do projeto) — API mínima para variable fonts (`FontVariation`) no Android.
