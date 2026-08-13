<div align="center">

# StällFit

**Treino e nutrição. Tudo em um lugar. Sem compromisso.**

*Construído para quem leva performance a sério — não para quem conta passos.*

[![License](https://img.shields.io/badge/license-Proprietary-red.svg)](LICENSE)
[![Status](https://img.shields.io/badge/status-MVP%20funcional-brightgreen.svg)]()
[![Platform](https://img.shields.io/badge/platform-Android%20nativo-blue.svg)]()
[![Stack](https://img.shields.io/badge/stack-Kotlin%20%2B%20Jetpack%20Compose-7F52FF.svg)]()

</div>

---

## 📲 Baixar e instalar

**[⬇️ Baixar StällFit.apk](app/build/outputs/apk/debug/StällFit_v1.0.0.apk)** — build de debug mais recente, pronta para instalar em qualquer Android 8.0+ (API 26).

1. Baixe o arquivo `.apk` acima no seu celular.
2. Abra o arquivo baixado. Na primeira instalação, o Android pede permissão para instalar apps de fontes desconhecidas pelo app usado para abrir o arquivo (Chrome, Arquivos, etc.) — confirme.
3. Abra o StällFit normalmente.

> É uma build de desenvolvimento (debug), não assinada para a Play Store — gerada para fins de portfólio/demonstração. Para compilar a partir do código-fonte, veja [SETUP.md](SETUP.md).

---

## O problema que o StällFit resolve

A maioria dos apps de fitness escolhe um lado: ou é um contador de calorias, ou é um diário de treino. Você acaba usando dois apps, perdendo contexto e, principalmente, perdendo a correlação entre o que come e o que performa.

O StällFit não escolhe lado. Ele registra sua tonelagem levantada no mesmo lugar onde rastreia seus macros — porque esses dados não vivem separados no mundo real, e não deveriam viver separados no seu celular.

---

## Funcionalidades

### Workout Logger

| Funcionalidade | Descrição |
|---|---|
| Fichas de treino | Monte uma ficha reutilizável (exercícios + séries, faixa de reps, descanso e intensidade), edite ou exclua (com confirmação) a qualquer momento e inicie uma sessão de treino a partir dela |
| Banco de exercícios | Catálogo pré-carregado cobrindo máquina, barra, halter, cabo e peso corporal, com busca e criação de exercícios personalizados |
| Carga de referência | Peso de consulta editável por exercício direto na ficha, só para referência — a tonelagem (séries × repetições × carga) é sempre calculada durante uma sessão de treino ativa, nunca a partir da ficha estática |
| Exportação em PDF | Gera uma ficha em PDF com a identidade visual do app — cabeçalho com logo, marca d'água, metadados do aluno e tabela de exercícios — pronta para impressão ou envio |
| Registro livre | Fluxo ad-hoc de sessão sem ficha, para quem prefere registrar série a série na hora |
| Progressive overload | Cálculo automático do volume total (tonelagem) por sessão e por semana |
| Histórico de cargas | Último peso registrado no exercício aparece na tela antes de você começar |
| Recorde pessoal (PR) | Maior carga já registrada em cada exercício, exibida durante a sessão ativa e na tela de Progresso |
| Progresso por exercício | Tela dedicada com seletor de exercício, recorde pessoal, histórico completo de séries e gráfico de carga ao longo do tempo |

### Perfil & TMB

| Funcionalidade | Descrição |
|---|---|
| Dados do perfil | Idade, sexo, nível de atividade e objetivo |
| Cálculo metabólico | TMB (Mifflin-St Jeor), GET, meta calórica e meta de água calculados automaticamente a partir do objetivo e do registro de evolução física mais recente |
| Distribuição de macros | Proteína, carboidrato e gordura calculados a partir do objetivo e aplicados direto às Metas |
| Evolução física | Histórico de registros de peso, altura e medidas corporais ao longo do tempo, com gráfico de evolução do peso, detalhe e exclusão por registro |
| Backup e restauração modular | Exporta e importa por categoria (Treinos, Refeições, Perfil) ou tudo de uma vez, num `.json` com metadata de versão; importação detecta o conteúdo do arquivo e deixa escolher entre Mesclar/Somar ou Substituir por categoria — proteção extra além das migrations automáticas do banco |

### Cardápio

| Funcionalidade | Descrição |
|---|---|
| Refeições reutilizáveis | Cadastre refeições nomeadas por categoria (café da manhã, almoço, lanche, jantar, ceia) |
| Totais por refeição | Cada card de refeição mostra kcal, proteína, carboidrato e gordura somados dos ingredientes |
| Base de alimentos | Catálogo pré-carregado com dezenas de alimentos e valores nutricionais prontos, com busca e cadastro de alimentos personalizados |

### Dashboard

- Metas diárias (calculadas no Perfil ou definidas manualmente) exibidas como referência fixa
- Cards de TMB, Água, Volume hoje e GET com altura padronizada entre si
- Acesso rápido ao treino ativo e ao cardápio de refeições
- Visão de progresso semanal em volume
- Interface **dark-first**, pensada para legibilidade sob luz intensa de academia

### Navegação e identidade visual

- Tela de abertura com animação de zoom-in do símbolo e fade-in do nome e subtítulo ao iniciar o app
- Cabeçalho fixo com o logotipo (nome) e o título da página, presente nas quatro abas principais e que não rola junto com o conteúdo
- Ícone do app derivado do símbolo da marca

---

## Design

O StällFit foi desenhado com um princípio central: **velocidade de registro**. Entre uma série e outra, você tem segundos — não minutos. Cada tela foi projetada para exigir o menor número possível de toques, sem abrir mão de precisão nos dados.

- Hierarquia visual clara com alto contraste
- Componentes reutilizáveis e consistentes em toda a navegação
- Feedback imediato em cada interação

---

## Stack técnica

O StällFit é um app **Android nativo**, sem framework híbrido e sem backend — todos os dados vivem no dispositivo do usuário.

| Camada | Tecnologia |
|---|---|
| Linguagem | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Persistência | Room (SQLite), 100% local, com migrations versionadas (dados sobrevivem a atualizações do app) |
| Navegação | Navigation Compose |
| Concorrência | Coroutines + Flow (`StateFlow`, `combine`, `flatMapLatest`) |
| Injeção de dependência | Manual (`AppContainer` + `viewModelFactory`), sem Hilt/Dagger |
| Serialização | kotlinx.serialization (backup/restauração em JSON) |
| Build | Gradle Kotlin DSL com catálogo de versões (`libs.versions.toml`) e KSP |

Ver [SETUP.md](SETUP.md) para instruções de build e execução.

---

## Testes automatizados

| Tipo | Cobertura |
|---|---|
| Unitário (JVM) | `MetabolicCalculatorTest` — cálculo de TMB, GET e macros para as combinações de sexo/nível de atividade/objetivo |
| Instrumentado (Room) | `MigrationTest` — cada `Migration` registrada em `Migrations.kt`, validando que o schema resultante bate com o schema exportado pelo Room e que os dados existentes sobrevivem à migração |

```bash
./gradlew testDebugUnitTest        # unitários
./gradlew connectedDebugAndroidTest # instrumentados (emulador/dispositivo conectado)
```

---

## Arquitetura

O projeto separa explicitamente a camada de dados, a lógica de domínio e a apresentação, e organiza a UI por módulo de produto (treino, nutrição, dashboard, metas) em vez de por tipo de arquivo — cada tela carrega seu próprio `ViewModel` e `UiState`, mantendo os domínios de treino e nutrição independentes entre si e permitindo que cada um evolua sem afetar o outro.

```
app/src/main/java/com/vitorsousa/stallfit/
├── MainActivity.kt        # ponto de entrada — exibe a splash e, em seguida, o tema/NavHost
├── StallFitApp.kt         # Application, dona do AppContainer (DI manual)
├── core/util/              # utilitários compartilhados (datas, dias-época, normalização de texto)
├── data/
│   ├── local/               # entities, DAOs, relations, Converters, Migrations.kt e o StallFitDatabase (Room)
│   ├── backup/              # BackupModule/BackupEnvelope — schema modular de backup por categoria (treinos/refeições/perfil)
│   └── repository/          # WorkoutRepository, WorkoutTemplateRepository, NutritionRepository, ProfileRepository, BackupRepository
├── di/                      # AppContainer + AppViewModelProvider
├── domain/model/            # modelos derivados, ex.: MacroTotals, MetabolicCalculator
├── navigation/               # rotas (Destination), o NavHost, bottom nav e o cabeçalho fixo (StallFitTopBar)
└── ui/
    ├── theme/                # paleta dark-first, tipografia
    ├── components/           # StatCard, SectionHeader, EmptyState, LineChart (gráfico em Canvas)
    ├── splash/                # tela de abertura (zoom-in do símbolo + fade-in do nome/subtítulo)
    ├── dashboard/             # tela inicial — metas do dia, treino ativo, atalho para o cardápio
    ├── workout/               # fichas de treino, registro por ficha, sessão livre, histórico, progresso por exercício (PR + gráfico)
    ├── nutrition/             # cardápio de refeições reutilizáveis, criação de refeição, detalhe de ingredientes
    ├── profile/               # perfil do usuário, evolução física (histórico de medidas) e cálculo de TMB/GET/macros
    └── goals/                 # metas de macros (manuais ou aplicadas pelo Perfil)
```

---

## Sobre o projeto

StällFit é um projeto de portfólio desenvolvido por **Vitor de Sousa Nunes**, com foco em demonstrar domínio de arquitetura mobile, design de produto e experiência de usuário em contextos de alta frequência de uso.

---

## Licença

Todos os direitos reservados. Nenhuma parte deste repositório pode ser reproduzida, distribuída ou utilizada sem autorização expressa do autor.

© 2025 Vitor de Sousa Nunes
