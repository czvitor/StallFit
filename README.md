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

## O problema que o StällFit resolve

A maioria dos apps de fitness escolhe um lado: ou é um contador de calorias, ou é um diário de treino. Você acaba usando dois apps, perdendo contexto e, principalmente, perdendo a correlação entre o que come e o que performa.

O StällFit não escolhe lado. Ele registra sua tonelagem levantada no mesmo lugar onde rastreia seus macros — porque esses dados não vivem separados no mundo real, e não deveriam viver separados no seu celular.

---

## Funcionalidades

### Workout Logger

| Funcionalidade | Descrição |
|---|---|
| Registro de séries | Exercício, sets, reps e carga (kg) por sessão |
| Timer de descanso | Contador regressivo entre séries com alertas configuráveis |
| Progressive overload | Cálculo automático do volume total (tonelagem) por sessão e por semana |
| Histórico de cargas | Último peso registrado no exercício aparece na tela antes de você começar |

### Macro Tracker

| Funcionalidade | Descrição |
|---|---|
| Diário alimentar | Refeições organizadas por horário ao longo do dia |
| Metas de macros | Barra de progresso em tempo real para proteína, carbo, gordura e calorias |
| Base de alimentos | Cadastro próprio com ajuste livre de gramatura e valores nutricionais |

### Dashboard

- Cruzamento direto entre treino do dia e ingestão calórica
- Visão de progresso semanal em volume e macros
- Interface **dark-first**, pensada para legibilidade sob luz intensa de academia

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
| Persistência | Room (SQLite), 100% local |
| Navegação | Navigation Compose |
| Concorrência | Coroutines + Flow (`StateFlow`, `combine`, `flatMapLatest`) |
| Injeção de dependência | Manual (`AppContainer` + `viewModelFactory`), sem Hilt/Dagger |
| Build | Gradle Kotlin DSL com catálogo de versões (`libs.versions.toml`) e KSP |

Ver [SETUP.md](SETUP.md) para instruções de build e execução.

---

## Arquitetura

O projeto separa explicitamente a camada de dados, a lógica de domínio e a apresentação, e organiza a UI por módulo de produto (treino, nutrição, dashboard, metas) em vez de por tipo de arquivo — cada tela carrega seu próprio `ViewModel` e `UiState`, mantendo os domínios de treino e nutrição independentes entre si e permitindo que cada um evolua sem afetar o outro.

```
app/src/main/java/com/vitorsousa/stallfit/
├── MainActivity.kt        # ponto de entrada — instancia o tema e o NavHost
├── StallFitApp.kt         # Application, dona do AppContainer (DI manual)
├── core/util/              # utilitários compartilhados (datas, dias-época)
├── data/
│   ├── local/               # entities, DAOs, Converters e o StallFitDatabase (Room)
│   └── repository/          # WorkoutRepository e NutritionRepository
├── di/                      # AppContainer + AppViewModelProvider
├── domain/model/            # modelos derivados, ex.: MacroTotals
├── navigation/               # rotas (Destination) e o NavHost + bottom nav
└── ui/
    ├── theme/                # paleta dark-first, tipografia
    ├── components/           # StatCard, CalorieRing, MacroProgressBar, SectionHeader, EmptyState
    ├── dashboard/             # tela inicial — resumo cruzado treino + nutrição
    ├── workout/               # lista de treinos, sessão ativa, timer de descanso
    ├── nutrition/             # diário de refeições, adicionar alimento
    └── goals/                 # metas de macros
```

---

## Sobre o projeto

StällFit é um projeto de portfólio desenvolvido por **Vitor de Sousa Nunes**, com foco em demonstrar domínio de arquitetura mobile, design de produto e experiência de usuário em contextos de alta frequência de uso.

---

## Licença

Todos os direitos reservados. Nenhuma parte deste repositório pode ser reproduzida, distribuída ou utilizada sem autorização expressa do autor.

© 2025 Vitor de Sousa Nunes
