# VigiaSaúde Recife — Guia de Design e UX

> **Versão:** 1.0 — Referência contínua de UI/UX para o aplicativo comunitário e o painel da Vigilância  
> **Projeto:** VigiaSaúde Recife · Etapa 02/06 e próximas etapas  
> **Equipe:** Kristyson Alpino · Pedro Victor Gomes · Leandro Lima  
> **Referência principal escolhida:** [Recipely — Food Recipe Mobile App UI Kit (Figma Community)](https://www.figma.com/design/sG2S33n8eMyjdPzyPSQn5d/-FREE--Recipely---Food-Recipe-Mobile-App-UI-Kit--Community-?node-id=1-2&t=XsqVqXZkmrAAevgy-1)

---

## 0. Como usar este documento

Este é o **guia oficial de direção visual e experiência** do VigiaSaúde Recife. Ao criar qualquer tela, componente, protótipo, implementação HTML/React ou apresentação de interface:

1. Considere o link do Recipely como a **referência de estilo e organização de um aplicativo mobile de exploração por categorias**.
2. Aplique as cores, tipografia, navegação, microtextos, padrões de acessibilidade e requisitos sanitários definidos neste guia.
3. Priorize o **morador que acessa pelo celular**, inclusive com pouca familiaridade digital, e não a preferência estética do desenvolvedor.
4. Preserve coerência com a arquitetura existente: **React + TypeScript + Vite; PWA; Service Worker + IndexedDB; API REST Node.js/TypeScript/Express; PostgreSQL/PostGIS; armazenamento privado de evidências; análise por IA opcional**.
5. Se uma referência visual conflitar com acessibilidade, privacidade ou simplicidade, **a regra de produto prevalece**.

**Escopo da referência:** o arquivo Figma indicado foi identificado, mas a integração disponível não permitiu ler suas camadas e valores exatos (exigiu acesso de edição). Portanto, os padrões abaixo são uma **adaptação autoral inspirada na proposta de um UI kit de receitas** — não uma transcrição de tokens, telas ou componentes extraídos do arquivo. Antes de reproduzir qualquer asset do kit, verificar licença e permissão de uso. Em especial, não presumir que ilustrações/fotos são livres para reutilização.

## 1. Conceito criativo

### Tese visual

**“O território é vivo. O cuidado começa em quem observa.”**

Queremos uma interface convidativa como um aplicativo de descoberta, porém confiável como um serviço público. O equilíbrio deve ser:

- **Acolhedora**, com fundo claro, boas fotografias e escrita cotidiana.
- **Viva e local**, com categorias reconhecíveis e referências sutis ao Recife.
- **Séria em momentos críticos**, com retorno de protocolo, status e avisos adequados.
- **Clara e rápida**, com uma ação principal por tela e sem excesso de gráficos.
- **Institucional sem parecer burocrática**; UNICAP e Prefeitura do Recife são parceiros **propostos**, não marcas a usar como endosso oficial sem autorização.

### Tradução do Recipely para o VigiaSaúde

| Padrão de exploração visual | Adaptação para o VigiaSaúde | Regra de UX |
|---|---|---|
| Vitrine de conteúdos em cartões | Cartões de orientações e ocorrências por categoria | Mostrar a ação e o estado antes de dados secundários |
| Categorias fáceis de tocar | Mosquitos e criadouros; roedores; escorpiões; serpentes; outras situações | Nomes em linguagem natural; não exigir conhecimento técnico |
| Página de detalhes | Detalhes da ocorrência, protocolo, linha do tempo e orientações seguras | Foto não deve ficar acessível publicamente por padrão |
| Conteúdo recomendado ou em destaque | Cuidados do bairro, avisos de prevenção e atividade recente agregada | Não transformar risco sanitário em competição ou ranking de bairros |
| Jornada progressiva | Registro guiado em 3 passos | Etapas curtas, revisão antes do envio |
| Conteúdo salvo | Minhas ocorrências e rascunhos offline | Explicar claramente o que já foi enviado e o que está pendente |
| Explorar e buscar | Mapa e lista de situações agregadas por área | Sem revelar endereços privados ou pontos exatos de residências |

**Não copiar do modelo de receitas:** coração/favoritos como gamificação de risco; avaliação por estrelas; linguagem de compra; menus com muitas áreas; fluxo de cadastro obrigatório antes de registrar; banners decorativos que empurrem a ação principal para baixo.

## 2. Paleta oficial

Cores escolhidas para o **VigiaSaúde**; não são apresentadas como cores originais do Recipely nem como reprodução da identidade oficial da UNICAP.

| Token | HEX | Papel |
|---|---|---|
| `brand.teal` | `#087E79` | Principal: ações, links destacados, controles ativos |
| `brand.orange` | `#F28C33` | Apoio: destaques informativos, ilustrações, acentos de categoria |
| `brand.wine` | `#A62546` | Assinatura institucional e ênfase pontual; não é cor de erro por si só |
| `brand.navy` | `#102D4D` | Títulos, navegação, leitura de alta importância |
| `surface.page` | `#F6F4EE` | Fundo creme de toda a experiência |
| `surface.card` | `#FFFFFF` | Cartões e áreas interativas |
| `surface.mint` | `#EAF4F1` | Blocos secundários, ajuda e feedback neutro |
| `text.primary` | `#172F43` | Corpo do texto |
| `text.secondary` | `#516475` | Texto secundário, metadados |
| `border.default` | `#DDE7E4` | Bordas, divisores e campos |
| `semantic.success` | `#196C4F` | Confirmação positiva de ação/processamento |
| `semantic.warning` | `#915000` | Atenção e estados que exigem cuidado (usar fundo claro) |
| `semantic.error` | `#B3261E` | Erros, falha de envio e campos inválidos |

**Aplicação recomendada:** fundo creme, cartões brancos, predominância do verde-petróleo e textos azul-marinho. Laranja aparece como elemento afetivo de apoio; vinho apenas para identidade, ênfase editorial ou separadores institucionais.

### Acessibilidade de contraste

- Usar **texto branco sobre verde-petróleo, vinho ou azul-marinho** apenas após conferência de contraste da combinação exata.
- **Não usar texto branco sobre laranja `#F28C33`** em botões ou badges: preferir texto azul-marinho `#102D4D` (contraste calculado de aproximadamente **5,7:1**).
- Verificar contraste mínimo **4,5:1 para textos normais** e **3:1 para textos grandes e componentes informativos**, segundo WCAG AA.
- Contraste calculado de `#087E79` sobre branco: **4,92:1** (adequado para texto normal); **sobre fundo creme `#F6F4EE`: cerca de 4,47:1** (ligeiramente insuficiente para corpo de texto normal). Em fundo creme, usar títulos e textos azul-marinho ou um tom de verde mais escuro validado.
- Nunca confiar somente na cor: “Pendente”, “Validada” e “Concluída” precisam ter texto e/ou ícone, não apenas bolinhas coloridas.

### Tokens CSS iniciais

```css
:root {
  --color-primary: #087E79;
  --color-accent: #F28C33;
  --color-institutional: #A62546;
  --color-heading: #102D4D;
  --color-background: #F6F4EE;
  --color-surface: #FFFFFF;
  --color-surface-subtle: #EAF4F1;
  --color-text: #172F43;
  --color-text-muted: #516475;
  --color-border: #DDE7E4;
  --color-success: #196C4F;
  --color-warning: #915000;
  --color-error: #B3261E;
  --radius-sm: 10px;
  --radius-md: 16px;
  --radius-lg: 24px;
  --radius-pill: 999px;
  --space-unit: 4px;
  --shadow-card: 0 8px 24px rgba(16,45,77,.06);
}
```

## 3. Tipografia e linguagem visual

- **Fonte preferencial:** `Nunito Sans` (identidade calorosa, sem sacrificar legibilidade). **Alternativa segura:** `Inter`, `system-ui`, `sans-serif`.
- **Títulos mobile:** 28–32 px, peso 800, entrelinha 1,12–1,2.
- **Título de seção:** 20–24 px, peso 750–800.
- **Corpo:** 16–18 px, entrelinha 1,45–1,6.
- **Rótulos e botões:** 15–16 px, peso 650–800.
- **Metadados:** preferir 14 px; abaixo de 12 px somente em casos excepcionais não essenciais.
- Evitar caixa-alta em frases; usar apenas em micro-rótulos pontuais.
- Manter **alinhamento à esquerda**, frases curtas e áreas de respiro.
- Ícones: família consistente de linhas arredondadas; sempre acompanhados de texto onde houver ação importante.
- Fotos/ilustrações: usar elementos com contexto realista de bairro, sem imagens sensacionalistas de animais ou ferimentos. Usar fotos de exemplo apenas com licença compatível e tratamento de privacidade.
- Referências sutis a Recife: água/canais, pontes, formas orgânicas, azulejos e relevo urbano, sem caricatura turística.

## 4. Grid, espaçamento e formatos

| Item | Regra |
|---|---|
| Mobile-first | Desenhar primeiro em `390 × 844 px`, testar também `320 px` e `430 px` de largura |
| Largura de conteúdo mobile | `100%` com `16–20 px` de margem lateral |
| Desktop de protótipo | até `1200–1280 px` de conteúdo útil; não ampliar formulários indefinidamente |
| Espaçamento | escala de `4, 8, 12, 16, 24, 32, 48 px` |
| Cartões | raio `16–24 px`, borda sutil e sombra discreta |
| Campos | altura mínima visual `48–52 px`, com rótulo externo claro |
| Botão principal | altura `52–56 px`, largura inteira no mobile quando possível |
| Alvos de toque | **preferir ao menos 44 × 44 px** |
| Barra inferior | máximo 4 destinos, área segura acima da borda inferior do celular |
| Formulários | uma coluna; preferir seleção visual a listas longas |

**Evitar:** excesso de chips, duas CTAs concorrentes, textos sobre fotos com contraste incerto, gradientes fortes por toda a tela, carrosséis automáticos e animações que atrapalham a tarefa.

## 5. Navegação e arquitetura da informação

### Navegação principal do morador

**Início** · **Mapa** · **Meus registros** · **Ajuda**

O botão de **Registrar ocorrência** deve aparecer com grande destaque na Início e continuar fácil de localizar nos demais contextos. Não esconder registro atrás de um botão “+” sem rótulo.

### Estrutura mínima de telas

| Tela | Objetivo | Elementos indispensáveis |
|---|---|---|
| 01. Boas-vindas | Apresentar valor em poucos segundos | Benefício, botão “Começar”, acesso a Ajuda |
| 02. Início | Fazer a pessoa agir | Saudação breve; CTA “Registrar ocorrência”; categorias; Meus registros |
| 03. Seleção de situação | Escolher categoria sem termos técnicos | Cartões com nome, ícone e breve exemplo; opção “Não sei identificar” |
| 04. Registro — Passo 1 | Dizer o que foi observado | Categoria, descrição orientada, data aproximada |
| 05. Registro — Passo 2 | Informar local de modo seguro | GPS com permissão opcional; localização manual; aviso de privacidade |
| 06. Registro — Passo 3 | Evidência e revisão | Foto opcional; revisão; indicação do que será enviado; CTA final |
| 07. Envio / offline | Tornar estado do envio inequívoco | “Salvo neste aparelho” vs. “Enviado”; retry; protocolo somente após confirmação |
| 08. Acompanhamento | Construir confiança na resposta | Protocolo, status em linguagem simples, linha do tempo, possíveis próximos passos |
| 09. Mapa / lista | Consultar riscos territoriais de modo seguro | Filtros simples, áreas agregadas, alternância mapa/lista |
| 10. Feedback após ação | Fechar ciclo com o morador | “Melhorou”, “Continua”, “Não sei informar” e observação opcional |
| 11. Ajuda e prevenção | Oferecer orientação útil | FAQ, canais públicos verificados, aviso de emergência quando pertinente |
| 12. Painel institucional | Uso do agente/gestor, separado | Autenticação, triagem, validação, status, indicadores e auditoria |

### Jornada principal

```text
Início
  → Registrar ocorrência
  → Escolher categoria
  → Descrever situação
  → Informar local (GPS ou manual)
  → Adicionar foto (opcional)
  → Revisar
  → Enviar
      ├─ online: confirmação + protocolo
      └─ offline: rascunho pendente → sincronização → confirmação + protocolo
  → Acompanhar status
  → Avaliar resultado após retorno
```

**Critério:** o usuário deve conseguir iniciar o registro em **1 toque** a partir da tela inicial; idealmente terminar em até **3 etapas curtas**, sem cadastro obrigatório para o relato comunitário inicial.

## 6. Componentes obrigatórios

### `PrimaryButton`

- Cor: `brand.teal`, texto branco com contraste verificado.
- Altura mínima 52 px, raio 16 px, rótulo com verbo: “Registrar ocorrência”, “Enviar registro”, “Tentar novamente”.
- Estados: padrão, foco visível, pressionado, carregando e desativado (com explicação).

### `CategoryCard`

- Ícone ou ilustração sóbria, título fácil de entender e exemplo em uma linha.
- Exemplo: **Escorpiões** — “Presença em casa, quintal ou calçada”.
- Seleção visível por contorno, fundo e marcador textual; nunca apenas mudança de cor.
- Categoria “Não sei identificar” sempre disponível.

### `OccurrenceCard`

- Categoria + status em texto; bairro ou região (sem endereço exato público); data relativa; protocolo quando pertencente ao usuário.
- Ação: “Ver andamento”.
- Nunca exibir nome, número do celular ou localização residencial exata em cartões públicos.

### `StepProgress`

- Indicar “Passo 1 de 3” e nome da etapa, sem depender apenas de barra colorida.
- Botão “Voltar” preserva dados já digitados.

### `StatusTimeline`

- Usar nomes simples no front-end, mapeados de forma coerente ao domínio da API:

| `status` persistido | Texto mostrado ao morador |
|---|---|
| `recebida` | Registro recebido |
| `em_triagem` | Em análise |
| `aguardando_vistoria` | Aguardando vistoria |
| `em_atendimento` | Em atendimento |
| `concluida` | Atendimento concluído |
| `descartada` | Registro não confirmado / encerrado (mostrar justificativa quando possível) |

- Não confundir **“enviado”** com **“confirmado tecnicamente”**.

### `OfflineBanner`

- Mensagem clara: “Sem internet. Seu registro ficará salvo neste aparelho e será enviado quando houver conexão e o aplicativo sincronizar.”
- Informar número de pendências e oferecer ação “Tentar sincronizar”.
- Antes da confirmação do servidor, **não exibir protocolo inventado**.

### `FeedbackState`

- **Vazio:** “Você ainda não enviou registros. Podemos começar agora?”
- **Erro:** “Não conseguimos enviar. Seu registro continua salvo neste aparelho.”
- **Sucesso:** “Registro enviado. Guarde seu protocolo para acompanhar.”
- **Atenção:** “Não tente capturar, tocar ou se aproximar do animal para fotografar.”

### `MapSummary`

- Camadas públicas somente com agregação territorial suficiente; pontos exatos restritos ao fluxo técnico autorizado.
- Permitir visualização alternativa em lista, para acessibilidade e conexão fraca.
- Sempre informar data do registro e limitações de cobertura (“Poucos relatos não significam ausência de riscos”).

## 7. Escrita da interface (microcopy)

**Tom:** direto, humano, instrutivo, sem alarmismo e sem linguagem de especialista.

| Evitar | Preferir |
|---|---|
| “Formalize uma notificação de zoonose” | “Registrar ocorrência” |
| “Efetuar georreferenciamento do ponto” | “Onde você encontrou isso?” |
| “Submeter evidência fotográfica” | “Adicionar foto (opcional)” |
| “Ocorrência deferida” | “Registro confirmado pela equipe” |
| “Erro de conectividade” | “Você está sem internet” |
| “Classificação automática: venenoso” | “Identificação sugerida; um profissional precisa confirmar” |

**Regra de segurança:** nunca incentivar aproximação, captura, manipulação ou identificação doméstica de animais perigosos. Em situações com acidentes/exposição, instruir a buscar canais oficiais de saúde e urgência adequados, sem sugerir que o app substitui atendimento.

## 8. Acessibilidade obrigatória

- **Objetivo:** WCAG 2.2 AA, compatível com leitores de tela e navegação por teclado.
- Rótulos reais em todos os campos; placeholder não substitui label.
- Mensagens de erro específicas associadas aos campos (`aria-describedby`).
- Ordem de tabulação coerente; foco sempre visível.
- Alvo de toque preferencial >= 44 × 44 px.
- Suporte a zoom do navegador e ampliação de fonte sem perda de informação ou cortes importantes.
- Ícones e fotos informativos com descrição textual; ornamentos marcados como decorativos.
- Estados informados com **texto** e não só cor.
- Evitar texto inserido diretamente em imagens.
- Não exigir gestos complexos (arrastar mapa não pode ser a única forma de indicar local).
- Respect `prefers-reduced-motion`; microanimações sutis e não essenciais.
- Formulários curtos, mensagens positivas de progresso e preservação de dados em caso de erro.
- Testar com teclado, leitor de tela, modo alto contraste, conexão instável e telas de 320 px.

## 9. Privacidade e proteção por design

- **Nenhuma fotografia privada deve aparecer no mapa público por padrão.**
- Coordenadas exatas em tabela e serviços internos protegidos por autorização; no mapa comunitário, **publicar somente áreas agregadas**.
- Minimizar dados pessoais, oferecer cadastro opcional ao morador e restringir funções técnicas a agentes/gestores autenticados.
- Registros offline terão identificador local e UUID de idempotência para prevenir duplicação no reenvio.
- Fotos em armazenamento privado; banco contém referências e metadados, não bytes de imagens.
- Preservar trilha de status, validações e intervenções para auditoria.
- A IA pode sugerir categoria ou padrão, mas **não confirma espécie, diagnóstico nem urgência clínica**.
- Validação comunitária é relato de observação, não laudo de autoridade sanitária.
- Definir base legal, retenção, governança e acesso com revisão jurídica antes de coleta real de dados.

## 10. Guia exclusivo do dashboard institucional

O dashboard compartilha marca e linguagem com o aplicativo, mas **não deve copiar o layout de catálogo mobile**.

- Menu lateral ou superior enxuto: **Visão geral · Ocorrências · Mapa · Intervenções**.
- Destaques no topo: quantidade de registros aguardando análise, tempo médio de triagem, vistorias pendentes e concluídas.
- Visão principal: tabela de ocorrências com filtros (bairro, período, categoria, status) e opção de mapa.
- Cartão de detalhe: status, descrição, evidências restritas, histórico, validação e encaminhamento.
- Prioridade operacional sugerida deve apresentar os critérios de forma compreensível; nunca fingir ser risco epidemiológico confirmado.
- Dar ao agente sempre a opção de **revisar e corrigir** sugestões automáticas.
- Tornar ações potencialmente irreversíveis dependentes de confirmação e justificativa.
- No mobile, agentes poderão consultar e validar, mas o fluxo extenso de gestão foi pensado primeiro para desktop/tablet.

## 11. Componentes e organização no Figma

```text
00 · Foundations
     Colors / Type / Grid / Elevation / Icons
01 · Components
     Buttons / Inputs / CategoryCard / OccurrenceCard
     StatusBadge / StepProgress / OfflineBanner / BottomNav
02 · Morador
     Welcome / Home / Category / Report-1 / Report-2 / Report-3
     Sending / Protocol / MyReports / Map / FollowUp / Help
03 · Agente
     Login / Overview / Inbox / ReportDetail / Validation / Intervention
04 · States
     Loading / Offline / Empty / Error / Success / AccessibleFocus
05 · Prototype
     HappyPath / OfflineRecovery / AgentWorkflow
```

**Convenção de nomes:** `Component/Variant/State`, por exemplo `Button/Primary/Loading`, `Occurrence/Status/EmTriagem` ou `Card/Category/Selected`.

**Biblioteca mínima:** um conjunto de tokens globais, componentes com variantes, estilos de texto e protótipos conectados. Mudança da paleta deve ocorrer por tokens, não alterando manualmente dezenas de telas.

## 12. Padrão de implementação

- Separar os componentes de interface dos objetos de dados e serviços da API.
- Tipar os estados e modelos com TypeScript; **não inventar estados que contradigam a modelagem da Entrega 2**.
- Conservar acesso offline mínimo à interface, rascunhos e fila de envio; sincronizar de modo explícito e confiável ao restabelecer acesso.
- Incluir skeleton/estado de carregamento onde realmente necessário.
- Prever tratamento de falhas da API e timeouts com recuperação sem perda de formulário.
- Carregar fotos de forma otimizada, respeitando os limites definidos pelo back-end.
- Não tornar a tela dependente do motor de IA: classificação assistida é opcional e revisável.
- Responsividade: 320, 390, 430, 768 e >= 1024 px.
- Usar dados fictícios claramente marcados no protótipo até existir implantação e aprovação formal.

## 13. Checklist de aprovação visual e funcional

### Antes de aprovar uma tela

- [ ] Uma tarefa dominante e CTA primária clara
- [ ] Cores respeitam os tokens e contraste verificado
- [ ] Textos compreensíveis sem conhecimentos técnicos
- [ ] Sem informação sanitária automática apresentada como certeza
- [ ] Navegação simples e botão Voltar sem perda de dados
- [ ] Estados vazio, erro, loading e offline previstos
- [ ] Alvos de toque e leitura confortável em celular
- [ ] Sem exibição pública de dados pessoais, fotos ou localização exata
- [ ] Elementos acessíveis por teclado/leitor de tela
- [ ] Componentes consistentes com a biblioteca Figma

### Antes de aprovar o protótipo completo

- [ ] Fluxo do registro guiado funciona até uma confirmação simulada
- [ ] Rascunho offline é diferenciado de ocorrência enviada
- [ ] Tela de acompanhamento usa os estados oficiais da modelagem
- [ ] Mapa/lista pública mostra dados agregados e nenhuma residência identificável
- [ ] Painel do agente requer contexto/autorização e permite validação humana
- [ ] Há feedback visível e útil após cada ação
- [ ] Responsivo em mobile e desktop, sem sobreposição
- [ ] Interfaces avaliadas com moradores ou usuários representativos antes de declarar prontas

## 14. Prompt de referência para próximas entregas

Copiar a instrução abaixo sempre que outra IA, estudante ou desenvolvedor for criar uma tela:

> **Use o arquivo `GUIA_DESIGN_VIGIASAUDE_RECIFE.md` como fonte principal de identidade visual e UX.** O projeto é o VigiaSaúde Recife, uma PWA comunitária de registro e acompanhamento de vetores, pragas urbanas e animais peçonhentos. O arquivo Figma Recipely indicado no guia serve como inspiração de experiência mobile visual, categorias e cartões, **sem copiar marcas, imagens ou funcionalidades de receitas**. Aplique a paleta verde-petróleo `#087E79`, laranja `#F28C33`, vinho `#A62546`, azul-marinho `#102D4D` e fundo creme `#F6F4EE`. Priorize acessibilidade WCAG 2.2 AA, formulários guiados, navegação simples, status claros, modo offline e proteção de localização. Não invente dados reais, diagnósticos ou integração já existente com a Prefeitura/UNICAP. Antes de finalizar, valide a lista de verificação do guia.

## 15. Referências e limites de uso

1. **Arquivo Figma selecionado pelo grupo:** [Recipely — Food Recipe Mobile App UI Kit (Community)](https://www.figma.com/design/sG2S33n8eMyjdPzyPSQn5d/-FREE--Recipely---Food-Recipe-Mobile-App-UI-Kit--Community-?node-id=1-2&t=XsqVqXZkmrAAevgy-1).
2. **Página de comunidade do kit com o mesmo nome encontrada publicamente:** [Recipely, Figma Community](https://www.figma.com/community/file/1160186880726418317/free-recipely-food-recipe-mobile-app-ui-kit). Confirme no Figma se corresponde à mesma versão/autor da referência enviada.
3. **Base técnica do projeto:** Entrega 1 (Arquitetura), Entrega 2 (Modelagem e SQL) e Entrega 3 (Contratos de API) já estabelecidas no VigiaSaúde Recife.
4. **Padrões de acessibilidade:** [WCAG 2.2](https://www.w3.org/TR/WCAG22/), W3C.

**Diretriz final:** inspirar-se na leveza e na organização de um app de descoberta, sem perder a precisão, a segurança e a responsabilidade necessárias a um serviço de saúde pública.
