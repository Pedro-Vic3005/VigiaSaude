# VigiaSaúde Recife — Android MVP (Kotlin + Jetpack Compose)

**Projeto acadêmico — Etapa 02/06 / Protótipo funcional**  
**Integrantes:** Kristyson Alpino · Pedro Victor Gomes · Leandro Lima

> **Atenção: versão demonstrativa.** Nenhum registro vai para a Prefeitura, UNICAP, EMPREL ou qualquer serviço remoto. Não usar esta compilação para coleta de dados reais de saúde ou localização pessoal. Todos os registros e exemplos demonstrativos ficam no dispositivo.

## 1. O que foi entregue

Aplicativo nativo Android em **Kotlin + Jetpack Compose**, construído a partir do `modelobase(1).zip` enviado. O modelo original era uma tela inicial vazia; esta pasta o amplia com telas de negócio, componentes próprios e armazenamento local.

A identidade visual aplica o guia `docs/GUIA_DESIGN_VIGIASAUDE_RECIFE.md` (inspirado em categorias e cards de aplicativos de descoberta, como o Recipely, **sem reutilizar suas ilustrações ou assets**):

- Verde-petróleo `#087E79` (ações); laranja `#F28C33` (destaques);
- Vinho `#A62546`; azul-marinho `#102D4D`; creme `#F6F4EE`;
- Categorias grandes, microtextos acessíveis, formulário em 3 etapas, retorno claro de status.

### Funcionalidades utilizáveis agora

1. **Início** com categorias (criadouros, roedores, escorpiões, serpentes, infestações sanitárias e situações não identificadas) e CTA de registro.
2. **Registro guiado** em 3 passos: situação/descrição, bairro/referência, foto opcional + revisão.
3. **Seleção de imagem** pelo seletor do Android; cópia para pasta privada da aplicação (até 10 MB; JPG/PNG/WebP).
4. **Rascunhos locais**; formulário retomável depois de fechar a tela.
5. **Modo sem conexão simulado**: registros ficam pendentes; botão de sincronização cria apenas um **protocolo `DEMO-…` local**.
6. **Meus registros** com detalhe, status, histórico e resposta comunitária após conclusão simulada.
7. **Regiões** com contagens agregadas por bairro, filtros por categoria e gráfico decorativo identificado como *não geográfico*.
8. **Painel demonstrativo de agentes** com triagem, validação, descarte, atendimento e conclusão; sem autenticação real.
9. **Ajuda**, orientações de segurança, simulação de conexão e restauração dos três exemplos fictícios.
10. **Persistência em armazenamento local** (`SharedPreferences` + JSON e arquivos internos). Os dados continuam salvos ao reiniciar o aplicativo, dentro das limitações do protótipo.

### O que ainda **não** existe

- API real / servidor / WebSocket / autenticação real de agentes;
- Google Maps, Places, Geocoding, Fused Location ou captura GPS com permissões;
- PostgreSQL/PostGIS, armazenamento na nuvem, Google Cloud ou Firebase;
- IA de classificação de imagens ou classificação sanitária real;
- publicação pública de denúncias, notificação oficial ou envio para órgãos de saúde.

**O botão “Simular sincronização” não faz requisição HTTP.** A PWA offline-first prevista anteriormente foi **substituída por um app nativo Android** por decisão mais recente do grupo. A API REST, a modelagem de dados e os princípios de LGPD continuam servindo de contrato para a integração futura.

## 2. Como abrir no Android Studio

1. Extrair o arquivo ZIP do projeto em uma pasta (sem espaços complexos, se possível).
2. Abrir **Android Studio → Open** e selecionar a pasta `VigiaSaudeRecife` que contém `settings.gradle.kts`.
3. Permitir sincronização Gradle. O projeto conserva a versão Gradle **9.5.0**, AGP **9.3.1**, Compose BOM **2026.02.01** e Kotlin **2.2.10** do `modelobase` anexado. O SDK Android **API 37** precisa estar instalado para essa configuração, junto dos artefatos do Gradle. Java/JDK compatível com essa configuração é necessário.
4. Selecionar o módulo **app** e executar em emulador ou celular Android **8.0+ (API 26)**.
5. Abrir o app **VigiaSaúde Recife**.

Caso o Android Studio sugira baixar componentes, permita o download. **Não são necessárias chaves do Google para esta versão**.

> **Teste local deste pacote:** a lógica de domínio Kotlin foi compilada e exercitada; a compilação do APK não pôde ser executada no ambiente de geração, que não possui Android SDK e não consegue baixar a distribuição do Gradle. Portanto, teste `:app:assembleDebug` e execução no emulador ainda precisam ser realizados no Android Studio. Não há APK incluído.

## 3. Cenários para demonstrar em sala

### Jornada A — Registro local e acompanhamento

1. Início → **Registrar ocorrência** ou toque em uma categoria.
2. Descrever a situação (mínimo 10 caracteres).
3. Selecionar o bairro; ponto de referência é opcional (não informar endereço particular).
4. Adicionar foto se desejar; revisar.
5. **Simular recebimento do registro** → o aplicativo guarda a ocorrência e cria um protocolo **DEMO**.
6. **Registros** → abra o detalhe para ver status e linha do tempo.

### Jornada B — Persistência e rascunhos

1. Comece um registro e preencha parte da descrição.
2. Volte até a etapa 1 e feche; o app salva rascunho automaticamente.
3. Em **Registros**, abra o rascunho e continue; feche/reabra o aplicativo para confirmar persistência.

### Jornada C — Simulação offline

1. **Ajuda** → ativar **Simular sem conexão**.
2. Registrar normalmente; o item fica marcado **Pendente no aparelho**.
3. **Ajuda** → desativar simulação → **Simular sincronização**.
4. Verificar se o status vira **Registro recebido** e aparece um **DEMO** local.

### Jornada D — Validação institucional simulada

1. Início → **Explorar painel de agentes**, ou Ajuda → **Abrir painel demonstrativo**.
2. Abrir uma ocorrência fictícia da lista.
3. Registrar observação e avançar: `recebida → em_triagem → aguardando_vistoria → em_atendimento → concluida`.
4. Voltar ao detalhe da ocorrência (quando criada pelo morador) e registrar percepção **Melhorou / Ainda acontece / Não sei informar**.

### Jornada E — Visualização agregada

1. **Regiões** → filtrar por categoria → observar as quantidades por bairro.
2. Verificar que nenhuma coordenada residencial, foto ou nome de morador aparece nessa tela.

## 4. Estrutura de código

```text
app/src/main/java/com/example/modelobase/
  MainActivity.kt                Navegação Compose e estado de app
  domain/Occurrence.kt          Categorias, estados, eventos e transições
  data/OccurrenceRepository.kt  Interface de repositório + portas futuras
  data/LocalOccurrenceRepository.kt
                                 Persistência e demonstração local
  data/PhotoStore.kt            Importação de fotos para pasta privada
  ui/theme/                     Tokens e tema Material 3
  ui/components/                Botões, cards, status e avisos
  ui/screens/                   Início, registro, regiões, registros,
                                 detalhe, ajuda e painel de agentes
app/src/test/                   Testes de regras de status

docs/
  GUIA_DESIGN_VIGIASAUDE_RECIFE.md
  Arquitetura_Original.html
  Modelagem_Original.html
  Modelo_Dados_PostGIS.sql
  Contrato_OpenAPI_3.0.yaml
  Contrato_OpenAPI_3.0.json
```

No MVP, `LocalOccurrenceRepository` é a única implementação persistente. Os contratos de domínio preservam **códigos de categoria**, **estados de ocorrência** e **UUID de idempotência**, permitindo migrar para outro repositório sem redesenhar as telas do morador.

## 5. Como será a integração real no futuro

1. **Serviços remotos:** implementar `FutureApiGateway` (ou substituí-la por interface de repositório mais completa) com Retrofit/OkHttp, seguindo `docs/Contrato_OpenAPI_3.0.yaml`, `Authorization: Bearer`, TLS e UUID de idempotência.
2. **Localização e Google:** adicionar **Maps SDK for Android / Maps Compose** para seleção de ponto e agregados, e opcionalmente **Fused Location Provider** (Google Play Services) após consentimento; a coleta precisa de localização confirmada para a API atual. A versão local captura apenas bairro e referência — **não atende ainda o campo geográfico obrigatório da API real**.
3. **Backend e banco:** servidor Node.js/TypeScript/Express, PostgreSQL + PostGIS, bucket de objetos privado para evidências e autorização por papéis. Nunca ligar o app diretamente ao banco SQL.
4. **Autenticação:** integrar provedor seguro de identidade para agente/gestor antes de liberar funções institucionais. O painel demonstrativo **não é um painel seguro**.
5. **Offline confiável:** migrar `SharedPreferences` para Room e `WorkManager` para fila resiliente, tentativas com backoff e sincronização comprovada pelo servidor. `DEMO-...` não será usado em produção.
6. **IA opcional:** um serviço separado que só sugere categoria ou concentração; humanos validam. Nunca usar resultado como diagnóstico ou ordem de intervenção.
7. **Privacidade e operação:** base legal/LGPD, análise de risco, retenção, logs auditáveis, limites de fotos, transparência ao usuário, autorização pública e ensaio piloto com moradores.

**Nota importante:** este projeto não usa API do Google nem cobra uso de nuvem. A escolha entre serviços Google/Firebase/Google Cloud deve ser feita depois com critérios de privacidade, preço e necessidade real.

## 6. Princípios e limites éticos

- Exemplos localizados por bairros são **fictícios**, não dados epidemiológicos medidos.
- O modo agente é exclusivamente demonstrativo, sem credenciais reais.
- Não coletar dados reais identificáveis de terceiros nesta versão.
- Não promover captura ou aproximação de animais perigosos.
- Telas com ações primárias grandes, rótulos textuais, ícones apoiados por texto e mensagens de estado explícitas.
- Sem alegar endosso formal de UNICAP, Prefeitura ou EMPREL.

## 7. Referências

- [Recipely — Figma Community](https://www.figma.com/design/sG2S33n8eMyjdPzyPSQn5d/-FREE--Recipely---Food-Recipe-Mobile-App-UI-Kit--Community-?node-id=1-2&t=XsqVqXZkmrAAevgy-1)
- Documentos anteriores no diretório `docs/`
- Entrega 1: arquitetura monólito modular + API REST
- Entrega 2: modelo relacional e georreferenciado
- Entrega 3: contratos de API

---

**Autores:** Kristyson Alpino, Pedro Victor Gomes e Leandro Lima.  
**Estado:** protótipo acadêmico com persistência local — não implantado e não homologado.
