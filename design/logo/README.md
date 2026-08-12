# Logo

Logo do StällFit em vetor (SVG/EPS), com o símbolo "S" e o nome em duas camadas de cor (`silver` = `#F0F3F8`, `volt` = `#CCFF00`), seguindo `fill-rule="evenodd"`.

## Variações

| Arquivo | Conteúdo |
|---|---|
| `stallfit-simbolo.svg` | Símbolo "S" isolado, sem texto |
| `stallfit-wordmark.svg` | Nome "STÄLLFIT" isolado, sem símbolo |
| `stallfit-nome-subtitulo.svg` | Nome + subtítulo ("TREINO • NUTRIÇÃO") |
| `stallfit-vertical.svg` | Lockup vertical — símbolo empilhado sobre o nome |
| `stallfit-logo.svg` | Lockup quadrado (1254×1254), fundo transparente — origem do ícone do app |
| `stallfit-logo-fundo.svg` | Igual ao anterior, com fundo Obsidian Black (`#0D0F12`) sólido |
| `stallfit-preview.png` / `stallfit-variantes-preview.png` | Imagens de referência visual (não usadas no app) |

Cada variação também tem um `.eps` equivalente para uso em materiais impressos/vetoriais fora do Android.

## Implementado em

- `stallfit-simbolo.svg` → path reaproveitado em [`ic_launcher_foreground.xml`](../../app/src/main/res/drawable/ic_launcher_foreground.xml) (ícone do app) e em [`stallfit_symbol.xml`](../../app/src/main/res/drawable/stallfit_symbol.xml) (animação de zoom-in na [tela de abertura](../../app/src/main/java/com/vitorsousa/stallfit/ui/splash/SplashScreen.kt)).
- `stallfit-wordmark.svg` → path reaproveitado em [`stallfit_wordmark.xml`](../../app/src/main/res/drawable/stallfit_wordmark.xml), usado no [cabeçalho fixo](../../app/src/main/java/com/vitorsousa/stallfit/ui/components/StallFitTopBar.kt) das quatro abas principais e também na tela de abertura.
- `stallfit-logo.svg` / `stallfit-logo-fundo.svg` → origem do ícone exportado em [`design/icons/`](../icons/).
- `stallfit-nome-subtitulo.svg` e `stallfit-vertical.svg` ainda não têm um uso direto no app (o subtítulo da splash é renderizado como texto nativo, não a partir deste SVG) — mantidos como variações de marca disponíveis para uso futuro (loja de apps, materiais externos).
