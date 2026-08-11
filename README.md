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
| Fichas de treino | Monte uma ficha reutilizável (exercícios + séries, faixa de reps, descanso e intensidade) e treine a partir dela |
| Registro por ficha | Tabela com carga (kg) por exercício e botão "Salvar Registro" por linha |
| Registro livre | Fluxo ad-hoc de sessão sem ficha, para quem prefere registrar série a série na hora |
| Progressive overload | Cálculo automático do volume total (tonelagem) por sessão e por semana |
| Histórico de cargas | Último peso registrado no exercício aparece na tela antes de você começar |

### Perfil & TMB

| Funcionalidade | Descrição |
|---|---|
| Dados do perfil | Idade, peso, altura, sexo, nível de atividade, objetivo e medidas corporais opcionais |
| Cálculo metabólico | TMB (Mifflin-St Jeor), GET, meta calórica e meta de água calculados automaticamente a partir do objetivo |
| Distribuição de macros | Proteína, carboidrato e gordura calculados a partir do objetivo e aplicados direto às Metas |

### Cardápio

| Funcionalidade | Descrição |
|---|---|
| Refeições reutilizáveis | Cadastre refeições nomeadas por categoria (café da manhã, almoço, lanche, jantar, ceia) |
| Totais por refeição | Cada card de refeição mostra kcal, proteína, carboidrato e gordura somados dos ingredientes |
| Base de alimentos | Cadastro próprio com ajuste livre de gramatura e valores nutricionais |

### Dashboard

- Metas diárias (calculadas no Perfil ou definidas manualmente) exibidas como referência fixa
- Acesso rápido ao treino ativo e ao cardápio de refeições
- Visão de progresso semanal em volume
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
│   └── repository/          # WorkoutRepository, WorkoutTemplateRepository, NutritionRepository, ProfileRepository
├── di/                      # AppContainer + AppViewModelProvider
├── domain/model/            # modelos derivados, ex.: MacroTotals, MetabolicCalculator
├── navigation/               # rotas (Destination) e o NavHost + bottom nav
└── ui/
    ├── theme/                # paleta dark-first, tipografia
    ├── components/           # StatCard, SectionHeader, EmptyState
    ├── dashboard/             # tela inicial — metas do dia, treino ativo, atalho para o cardápio
    ├── workout/               # fichas de treino, registro por ficha, sessão livre, histórico
    ├── nutrition/             # cardápio de refeições reutilizáveis, criação de refeição, detalhe de ingredientes
    ├── profile/               # perfil do usuário e cálculo de TMB/GET/macros
    └── goals/                 # metas de macros (manuais ou aplicadas pelo Perfil)
```

---

## Sobre o projeto

StällFit é um projeto de portfólio desenvolvido por **Vitor de Sousa Nunes**, com foco em demonstrar domínio de arquitetura mobile, design de produto e experiência de usuário em contextos de alta frequência de uso.

---

## Licença

Todos os direitos reservados. Nenhuma parte deste repositório pode ser reproduzida, distribuída ou utilizada sem autorização expressa do autor.

© 2025 Vitor de Sousa Nunes
