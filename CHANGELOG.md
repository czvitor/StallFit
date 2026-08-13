# Changelog

Todas as mudanças relevantes deste projeto são documentadas neste arquivo.

O formato segue as convenções do [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/) e este projeto adota [Versionamento Semântico](https://semver.org/lang/pt-BR/).

> **Sobre datas:** o repositório não usa tags Git por release, então as versões abaixo não têm data associada a cada entrada. Recomenda-se criar uma tag (`git tag v1.4.0`) a cada release a partir de agora, para que datas e diffs fiquem rastreáveis automaticamente.
>
> **Sobre o agrupamento das versões:** cada versão abaixo corresponde a um ou mais commits reais do histórico do projeto (não a uma divisão narrativa arbitrária). Commits de manutenção sem efeito funcional (build de APK, ajuste de documentação, artefatos de build) foram omitidos. Como parte do histórico foi commitada em blocos grandes — em especial a versão [1.4.0], que corresponde a um único commit com dezenas de arquivos —, algumas versões abaixo agrupam mais mudanças do que uma release em cadência normal teria.
>
> **Sobre o `versionName`:** o `app/build.gradle.kts` ainda declara `versionName = "1.0.0"` / `versionCode = 1`. Este arquivo documenta a evolução *funcional* do projeto; o campo de versão do APK só deve ser incrementado quando esse fluxo de release for adotado formalmente.

---

## [1.4.0] — Tema, Backup, Progresso por Exercício e Gestão de Fichas

### 🚀 Novas Funcionalidades

- Botão alternador de tema (claro/escuro) no cabeçalho fixo do app, com a escolha persistida localmente via Jetpack DataStore — sobrevive ao fechar o app. Sem escolha prévia salva, o app assume o tema escuro por padrão (não segue a preferência do sistema operacional).
- **Modo Claro** com fundo em tom *soft off-white* (`#F8FAFC`), evitando o branco puro que "estoura" o brilho da tela em ambientes de academia bem iluminados, com paleta de contraste revisado.
- Sistema de **Backup e Restauração modular** em `.json`, por categoria (Treinos, Refeições, Perfil/Evolução Física) ou tudo de uma vez, com metadata de versão do schema no arquivo exportado. Exportação/importação via Storage Access Framework do Android, sem exigir permissões de armazenamento adicionais.
- Estratégia de importação flexível: ao importar, o app detecta o conteúdo do arquivo e permite escolher, por categoria, entre **Mesclar/Somar** ou **Substituir** os dados existentes.
- Tela de **Progresso por Exercício**: seletor de exercício, recorde pessoal (PR), histórico completo de séries e gráfico de carga ao longo do tempo (componente `LineChart` desenhado em Canvas, sem biblioteca externa de gráficos).
- Campo de **carga de referência (kg)** por exercício, editável direto na tela da ficha, com botão de edição rápida (✏️) e confirmação inline (✓ / ✕).
- Campo de **observações** por ficha, editável na mesma tela.
- **Exportação da ficha em PDF**, gerado nativamente via `android.graphics.pdf.PdfDocument` — sem biblioteca externa. Inclui cabeçalho com logo, marca d'água, metadados do aluno, tabela de exercícios com carga de referência e paginação automática para fichas longas.
- **Modal de confirmação** antes de excluir uma ficha de treino, com exclusão em cascata dos exercícios da ficha e preservação do histórico de sessões já realizadas (o vínculo da sessão com a ficha excluída fica nulo, mas a sessão em si não é apagada).

### ⚙️ Regras de Negócio & Ajustes

- **Isolamento do cálculo de Tonelagem Total**: a carga de referência da ficha é apenas informativa — a tonelagem (séries × repetições × carga) continua sendo calculada exclusivamente durante uma **Sessão de Treino Ativa**, nunca a partir dos valores estáticos salvos em "Meus Treinos".
- Entidades do banco anotadas com `@Serializable` para viabilizar a serialização do backup em JSON.

### 🐛 Resolução de Bugs & Correções (Crítico)

- **Corrigida a perda de dados a cada atualização do app.** Causa raiz: o banco usava um fallback destrutivo que apaga e recria todas as tabelas sempre que a versão do schema muda sem uma rota de migração explícita — o que já vinha acontecendo a cada bump de schema anterior.
- Substituído por migrações reais e testadas (`MIGRATION_1_2`, `MIGRATION_2_3`, `MIGRATION_3_4`, schema atualmente na versão 4), mantendo um fallback destrutivo **apenas** para o cenário de downgrade (instalar uma build mais antiga por cima de um banco mais novo, caso sem rota de migração possível).
- Adicionada cobertura de teste instrumentado (`MigrationTest`) validando que os dados existentes sobrevivem a cada migração.
- Corrigido um vazamento de recurso nativo na geração do PDF: se a renderização falhasse no meio do processo, o documento nunca era fechado.
- Nome do aluno e título da ficha nas páginas de continuação do PDF agora são truncados com reticências quando excedem a largura disponível, evitando texto sobreposto ou cortado para fora da margem em fichas com nomes longos.
- Adicionada cobertura de teste unitário (`MetabolicCalculatorTest`) para o cálculo de TMB, GET e macros.

> Nota de precisão técnica: os dados sempre foram persistidos localmente em Room (SQLite), a solução de banco local nativa do Android — não há `IndexedDB` nem `AsyncStorage` neste projeto (APIs de web/React Native que não se aplicam a um app Android nativo em Kotlin). O "controle de versão de banco" citado acima é o campo `version` da anotação `@Database` do Room.

---

## [1.3.0] — Evolução Física e Higienização de Dados

### 🚀 Novas Funcionalidades

- Substituição dos campos estáticos de peso/altura do perfil por um **histórico de Avaliações Físicas**, com botão "+ Adicionar Medidas" na tela de Perfil.
- Modal de registro cobrindo Peso, Altura, Circunferências (Tórax, Cintura, Abdômen, Quadril, Braços e Antebraços — direito/esquerdo, braço relaxado/flexionado —, Coxas e Panturrilhas — direita/esquerda), Composição Corporal (bioimpedância: % gordura, massa magra, massa gorda, % água corporal) e Dobras Cutâneas (tríceps, subescapular, suprailíaca, abdominal, coxa, peitoral, axilar média).
- Gráfico de evolução do peso ao longo do tempo, com detalhe e exclusão por registro.

### ⚙️ Regras de Negócio & Ajustes

- TMB, GET e a distribuição de macronutrientes passam a ser calculados a partir do **registro de avaliação física mais recente**, em vez de um peso/altura fixos direto no perfil.

### 🐛 Resolução de Bugs & Correções

- Corrigido um loop de abrir/fechar o teclado ao buscar exercícios e alimentos: o realce automático de rolagem do Compose ao focar um campo (para trazê-lo para cima do teclado) disparava o mesmo sinal de "rolagem em progresso" usado para fechar o teclado ao arrastar a lista, criando um ciclo de abre-fecha. A correção passou a reagir apenas a um gesto de arraste real do usuário, não a qualquer mudança no estado de rolagem.
- Deduplicação "auto-curativa" de exercícios e alimentos: além da checagem no momento da criação, as listas de exercícios e alimentos agora filtram duplicatas/quase-duplicatas (variações de acentuação ou maiúsculas/minúsculas) reativamente, comparando pelo nome normalizado — corrige entradas duplicadas que já existiam na base antes dessa guarda existir.

---

## [1.2.0] — Identidade Visual, Splash e Navegação

### 🎨 Sistema de Design & UX

- Tela de abertura (splash) com animação de zoom-in do símbolo e fade-in do nome e subtítulo ao iniciar o app.
- Cabeçalho fixo (`StallFitTopBar`) com o logotipo e o título da página, presente nas abas principais e que não rola junto com o conteúdo.
- Reestruturação visual do Dashboard e dos fluxos de criação de refeição e de ficha de treino.
- Expansão da base de exercícios e alimentos pré-carregados.

---

## [1.1.0] — Fichas de Treino, Refeições Reutilizáveis e Perfil/TMB

### 🚀 Novas Funcionalidades

- Substituição do registro de treino ad-hoc por **fichas de treino reutilizáveis**: montagem de ficha com exercícios, séries, faixa de repetições, descanso e intensidade, editável a qualquer momento e usada como ponto de partida de uma sessão.
- Substituição do diário nutricional por data por um **cardápio de refeições reutilizáveis** por categoria (café da manhã, almoço, lanche, jantar, ceia), com totais de kcal/proteína/carboidrato/gordura calculados por refeição.
- Novo módulo de **Perfil**, com dados de idade, sexo, nível de atividade e objetivo.
- Módulo de **Cálculo Metabólico**: TMB (fórmula de Mifflin-St Jeor), GET, meta calórica e meta de água, calculados a partir do perfil e do objetivo do usuário.
- Migração de schema do Room (versão 1 → 2) para suportar os novos modelos de ficha, refeição e perfil.

### 🎨 Sistema de Design & UX

- Novas fontes (Inter e Space Grotesk) e refinamento da paleta de cores e tipografia.

---

## [1.0.0] — Lançamento Inicial (MVP)

### 🚀 Novas Funcionalidades

- Registro de treino livre, série a série, com cronômetro de descanso.
- Diário nutricional por data, com base de alimentos pré-carregada e cálculo de totais.
- Banco de exercícios e de alimentos pré-carregados, com busca e cadastro de itens personalizados.
- Dashboard inicial com metas e cards de resumo.
- Tela de Metas de macronutrientes.
- Persistência 100% local via Room (SQLite) — sem backend, login ou sincronização.
