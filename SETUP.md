# Rodando o StällFit

Este documento explica como abrir, compilar e rodar o app Android a partir deste repositório.

## Requisitos

- **Android Studio** Koala (2024.1) ou mais recente — mais simples, resolve o wrapper e o SDK automaticamente.
- Ou, para build manual via linha de comando: **JDK 17** e o **Android SDK** (`compileSdk 34`, `minSdk 26`) instalados, com a variável `ANDROID_HOME`/`ANDROID_SDK_ROOT` configurada.

## ⚠️ Sobre o Gradle Wrapper

O repositório inclui `gradlew`, `gradlew.bat` e `gradle/wrapper/gradle-wrapper.properties` (apontando para o **Gradle 8.7**), mas **não** inclui o binário `gradle/wrapper/gradle-wrapper.jar` — ele é um arquivo binário e foi deixado de fora intencionalmente (veja o `.gitignore`). Isso é comum e não é sinal de projeto quebrado; existem duas formas simples de resolver:

### Opção A — Abrir no Android Studio (recomendado)

Abra a pasta raiz do projeto no Android Studio. Ao detectar o wrapper incompleto, o Android Studio baixa o `gradle-wrapper.jar` e sincroniza o projeto automaticamente. Depois disso, `./gradlew` passa a funcionar normalmente também pelo terminal.

### Opção B — Gerar o wrapper manualmente

Se você já tem uma instalação do Gradle no sistema (`gradle` no PATH), rode na raiz do projeto:

```bash
gradle wrapper --gradle-version 8.7
```

Isso recria `gradle/wrapper/gradle-wrapper.jar` localmente (o arquivo é ignorado pelo Git de propósito, então não gera diffs no repositório).

## Compilando e rodando

Com o wrapper resolvido (Opção A ou B):

```bash
# Debug build
./gradlew assembleDebug

# Instalar num emulador/dispositivo conectado
./gradlew installDebug
```

Ou, no Android Studio: selecione um emulador/dispositivo e clique em **Run ▶**.

A `applicationId` é `com.vitorsousa.stallfit`, `minSdk = 26` (Android 8.0+), `targetSdk = 34`.

## Primeira execução

Na primeira abertura, o Room cria o banco local (SQLite) e popula automaticamente:
- ~19 exercícios padrão (peito, costas, pernas, ombros, braços, core);
- ~15 alimentos comuns com valores nutricionais por 100 g;
- uma meta de macros inicial (2200 kcal / 160 g proteína / 220 g carboidratos / 70 g gordura), editável a qualquer momento na tela **Metas**.

Todos os dados ficam **apenas no dispositivo** — não há backend, login ou sincronização nesta versão.

## Estrutura do código

```
app/src/main/java/com/vitorsousa/stallfit/
├── MainActivity.kt          # ponto de entrada (setContent { StallFitTheme { StallFitApp() } })
├── StallFitApp.kt           # Application, dono do AppContainer (DI manual)
├── core/util/               # utilitários compartilhados (datas/dias-época)
├── data/
│   ├── local/                # entities, DAOs, Converters, StallFitDatabase (Room), seed inicial
│   └── repository/           # WorkoutRepository, NutritionRepository
├── di/                       # AppContainer + AppViewModelProvider (DI manual, sem Hilt)
├── domain/model/             # modelos derivados (ex.: MacroTotals)
├── navigation/                # rotas (Destination) e o NavHost + bottom nav
└── ui/
    ├── theme/                 # paleta dark-first, tipografia, Theme.kt
    ├── components/            # StatCard, CalorieRing, MacroProgressBar, SectionHeader, EmptyState
    ├── dashboard/              # tela inicial (resumo cruzado treino + nutrição)
    ├── workout/                # lista de treinos, sessão ativa, timer de descanso
    ├── nutrition/              # diário de refeições, adicionar alimento
    └── goals/                  # metas de macros
```

## Notas sobre este build

Este código foi escrito e revisado manualmente, sem acesso a um toolchain Android local (sem JDK/Android SDK instalados no ambiente de desenvolvimento). As versões de bibliotecas foram fixadas deliberadamente (`gradle/libs.versions.toml`) em combinações conhecidas e estáveis entre si (Kotlin 1.9.24, Compose BOM 2024.06.00, compose compiler 1.5.14, AGP 8.4.2, Room 2.6.1) para reduzir o risco de incompatibilidade. Ainda assim, é recomendável que a primeira sincronização/build seja feita no Android Studio, que vai sinalizar rapidamente qualquer ajuste de versão necessário no seu ambiente local.
