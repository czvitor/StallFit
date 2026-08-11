<div align="center">

# StällFit

**Treino e nutrição. Tudo em um lugar. Sem compromisso.**

*Construído para quem leva performance a sério — não para quem conta passos.*

[![License](https://img.shields.io/badge/license-Proprietary-red.svg)](LICENSE)
[![Status](https://img.shields.io/badge/status-Em%20desenvolvimento-yellow.svg)]()
[![Platform](https://img.shields.io/badge/platform-Android%20%7C%20iOS-blue.svg)]()

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

## Arquitetura

O projeto separa explicitamente a lógica de negócios dos módulos de treino e nutrição, permitindo que cada domínio evolua de forma independente. A camada de apresentação é desacoplada dos modelos de dados, facilitando testes e manutenção à medida que o app cresce.

```
StällFit
├── workout/       # Domínio de treino: sessões, exercícios, cargas, timer
├── nutrition/     # Domínio de nutrição: alimentos, refeições, macros
├── dashboard/     # Agregação e visualização de dados cruzados
└── shared/        # Componentes, utilitários e modelos compartilhados
```

---

## Sobre o projeto

StällFit é um projeto de portfólio desenvolvido por **Vitor de Sousa Nunes**, com foco em demonstrar domínio de arquitetura mobile, design de produto e experiência de usuário em contextos de alta frequência de uso.

---

## Licença

Todos os direitos reservados. Nenhuma parte deste repositório pode ser reproduzida, distribuída ou utilizada sem autorização expressa do autor.

© 2025 Vitor de Sousa Nunes
