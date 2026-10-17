-- VIGIASAUDE RECIFE | ETAPA 02/06 | ENTREGA 2
-- PostgreSQL + PostGIS | Proposta de esquema fisico do MVP
-- Autores: Kristyson Alpino, Pedro Victor Gomes, Leandro Lima
-- Todas as relacoes e valores abaixo sao PROPOSTOS, nao tabelas existentes da Prefeitura.
-- Publicacao publica de localizacao: SOMENTE agregados/anomizados via API.

CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS postgis;

-- IDENTIDADES: cadastro opcional do morador, obrigatorio para agentes e gestores.
-- Autenticacao/senhas e autorizacao ficam fora do esquema: provedor seguro + API.
CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome_exibicao VARCHAR(100) NOT NULL,
    email VARCHAR(180) UNIQUE,
    papel VARCHAR(20) NOT NULL DEFAULT 'morador'
        CHECK (papel IN ('morador', 'agente', 'gestor')),
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE categorias (
    id SMALLSERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    grupo VARCHAR(24) NOT NULL
        CHECK (grupo IN ('vetor', 'roedor', 'peconhento', 'praga_sanitaria', 'outro')),
    ativa BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE bairros (
    id SMALLSERIAL PRIMARY KEY,
    nome VARCHAR(90) NOT NULL UNIQUE
);

-- Autor ausente = registro sem cadastro; o segredo de acompanhamento nunca e salvo em texto puro.
-- idempotencia_uuid evita duplicacao em reenvios de formularios offline.
CREATE TABLE ocorrencias (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    protocolo VARCHAR(24) NOT NULL UNIQUE,
    idempotencia_uuid UUID NOT NULL UNIQUE,
    usuario_id UUID REFERENCES usuarios(id) ON DELETE SET NULL,
    categoria_id SMALLINT NOT NULL REFERENCES categorias(id) ON DELETE RESTRICT,
    bairro_id SMALLINT REFERENCES bairros(id) ON DELETE SET NULL,
    descricao TEXT NOT NULL CHECK (length(btrim(descricao)) BETWEEN 10 AND 2000),
    localizacao GEOGRAPHY(POINT, 4326) NOT NULL,
    origem_localizacao VARCHAR(10) NOT NULL
        CHECK (origem_localizacao IN ('gps', 'manual')),
    token_acompanhamento_hash CHAR(64) NOT NULL,
    observado_em TIMESTAMPTZ,
    status VARCHAR(24) NOT NULL DEFAULT 'recebida'
        CHECK (status IN ('recebida','em_triagem','aguardando_vistoria',
                         'em_atendimento','concluida','descartada')),
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- O binario da foto fica em bucket privado; apenas metadados/chave tecnica no SQL.
CREATE TABLE evidencias (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ocorrencia_id UUID NOT NULL REFERENCES ocorrencias(id) ON DELETE CASCADE,
    chave_objeto TEXT NOT NULL UNIQUE,
    mime VARCHAR(24) NOT NULL CHECK (mime IN ('image/jpeg','image/png','image/webp')),
    tamanho_bytes INTEGER NOT NULL CHECK (tamanho_bytes BETWEEN 1 AND 10485760),
    enviado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Confirmacao de observacao; nao equivale a constatacao sanitaria.
CREATE TABLE confirmacoes_comunitarias (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ocorrencia_id UUID NOT NULL REFERENCES ocorrencias(id) ON DELETE CASCADE,
    usuario_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE RESTRICT,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (ocorrencia_id, usuario_id)
);

CREATE TABLE validacoes_tecnicas (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ocorrencia_id UUID NOT NULL REFERENCES ocorrencias(id) ON DELETE RESTRICT,
    agente_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE RESTRICT,
    resultado VARCHAR(18) NOT NULL
        CHECK (resultado IN ('confirmada','descartada','inconclusiva')),
    observacao TEXT,
    validado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE intervencoes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ocorrencia_id UUID NOT NULL REFERENCES ocorrencias(id) ON DELETE RESTRICT,
    agente_id UUID NOT NULL REFERENCES usuarios(id) ON DELETE RESTRICT,
    tipo_acao VARCHAR(90) NOT NULL,
    descricao TEXT NOT NULL,
    situacao VARCHAR(16) NOT NULL DEFAULT 'planejada'
        CHECK (situacao IN ('planejada','em_execucao','concluida','cancelada')),
    iniciada_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    concluida_em TIMESTAMPTZ,
    CHECK (concluida_em IS NULL OR concluida_em >= iniciada_em)
);

-- Percepcao apos encaminhamento/intervencao: nao prova ausencia de risco.
-- Usuario pode ser nulo para resposta autenticada via protocolo + segredo.
CREATE TABLE retornos_comunitarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ocorrencia_id UUID NOT NULL REFERENCES ocorrencias(id) ON DELETE RESTRICT,
    usuario_id UUID REFERENCES usuarios(id) ON DELETE SET NULL,
    percepcao VARCHAR(14) NOT NULL
        CHECK (percepcao IN ('melhorou','persiste','nao_sabe')),
    comentario TEXT,
    informado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Historico append-only administrado pela API; alteracoes exigem auditoria.
CREATE TABLE historico_status (
    id BIGSERIAL PRIMARY KEY,
    ocorrencia_id UUID NOT NULL REFERENCES ocorrencias(id) ON DELETE RESTRICT,
    ator_id UUID REFERENCES usuarios(id) ON DELETE SET NULL,
    status_anterior VARCHAR(24),
    novo_status VARCHAR(24) NOT NULL
        CHECK (novo_status IN ('recebida','em_triagem','aguardando_vistoria',
                               'em_atendimento','concluida','descartada')),
    motivo TEXT,
    alterado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Analise assistida e opcional; categorizacao nao substitui validacao humana.
CREATE TABLE sugestoes_ia (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ocorrencia_id UUID NOT NULL REFERENCES ocorrencias(id) ON DELETE CASCADE,
    categoria_sugerida_id SMALLINT REFERENCES categorias(id) ON DELETE SET NULL,
    confianca NUMERIC(4,3) CHECK (confianca BETWEEN 0 AND 1),
    versao_modelo VARCHAR(70) NOT NULL,
    processado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Indices para listas do painel e consultas geo-espaciais.
CREATE INDEX idx_ocorrencias_geo ON ocorrencias USING GIST(localizacao);
CREATE INDEX idx_ocorrencias_status_data ON ocorrencias(status, criado_em DESC);
CREATE INDEX idx_ocorrencias_categoria_data ON ocorrencias(categoria_id, criado_em DESC);
CREATE INDEX idx_ocorrencias_bairro_data ON ocorrencias(bairro_id, criado_em DESC);
CREATE INDEX idx_evidencias_ocorrencia ON evidencias(ocorrencia_id);
CREATE INDEX idx_validacoes_ocorrencia ON validacoes_tecnicas(ocorrencia_id, validado_em DESC);
CREATE INDEX idx_intervencoes_ocorrencia ON intervencoes(ocorrencia_id, iniciada_em DESC);
CREATE INDEX idx_confirmacoes_ocorrencia ON confirmacoes_comunitarias(ocorrencia_id);
CREATE INDEX idx_retornos_ocorrencia ON retornos_comunitarios(ocorrencia_id, informado_em DESC);
CREATE INDEX idx_status_ocorrencia ON historico_status(ocorrencia_id, alterado_em DESC);
CREATE INDEX idx_sugestoes_ocorrencia ON sugestoes_ia(ocorrencia_id, processado_em DESC);

-- Exemplos de categorias do catalogo inicial (expansivel pela gestao).
INSERT INTO categorias (codigo, nome, grupo) VALUES
 ('aedes_criadouro', 'Possivel criadouro do Aedes', 'vetor'),
 ('roedores', 'Presenca recorrente de roedores', 'roedor'),
 ('escorpioes', 'Escorpioes', 'peconhento'),
 ('serpentes', 'Serpentes', 'peconhento'),
 ('infestacao_sanitaria', 'Infestacao sanitaria', 'praga_sanitaria'),
 ('nao_identificado', 'Nao identificado', 'outro')
ON CONFLICT (codigo) DO NOTHING;

-- Regras que dependem da camada de servicos:
-- 1. Ao criar ocorrencia, gerar protocolo/segredo, hash e historico em uma transacao.
-- 2. Conferir papel de agente/gestor para validacoes e intervencoes (autorizacao na API).
-- 3. Conceder retorno anonimo somente com protocolo + token seguro; limitar tentativas.
-- 4. Manter atualizado_em, verificar transicoes de status e gravar historico atomico.
-- 5. Nao disponibilizar coordenadas exatas ou chaves de imagens na API publica.
-- 6. Politica de retencao, base legal, direitos dos titulares e auditoria antes da producao.
