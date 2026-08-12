# Ícone do app

Ícone do app em alta resolução, derivado do símbolo "S" em [`design/logo/stallfit-simbolo.svg`](../logo/stallfit-simbolo.svg), antes da conversão para os formatos consumidos pelo Android.

| Arquivo | Uso |
|---|---|
| `stallfit-icon-1024.png` | Fonte em alta resolução (1024×1024) do ícone, fundo Obsidian Black |
| `stallfit-app-icon.ico` | Empacotamento `.ico` do mesmo ícone, para uso fora do Android (ex.: atalhos/documentação em Windows) |

## Implementado em

O ícone real do app (adaptive icon) não usa estes arquivos diretamente — ele é o mesmo path do símbolo, reaplicado como vetor Android em:

- [`app/src/main/res/drawable/ic_launcher_foreground.xml`](../../app/src/main/res/drawable/ic_launcher_foreground.xml) / [`ic_launcher_background.xml`](../../app/src/main/res/drawable/ic_launcher_background.xml)
- [`app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`](../../app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml) (e variante `_round`)
