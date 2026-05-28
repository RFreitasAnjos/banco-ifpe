# Banco IFPE - Guia do Projeto

Este repositorio contem a aplicacao Banco IFPE, um sistema Java EE 8 para cadastro e gerenciamento de clientes, com foco em arquitetura legada corporativa, empacotamento WAR e deploy em WildFly.

## Visao Geral

A aplicacao segue arquitetura monolitica classica em camadas:

- Controller (JSF Managed Beans/CDI)
- Service (regras de negocio)
- Repository (acesso a dados com JPA)
- Entity (modelo de dominio)
- Views JSF/Facelets

Fluxo principal:

1. O usuario acessa a aplicacao JSF.
2. As telas invocam o `ClienteBean`.
3. O `ClienteBean` chama o `ClienteService`.
4. O `ClienteService` valida regras de negocio e usa o `ClienteRepository`.
5. O `ClienteRepository` persiste/consulta no PostgreSQL via JPA.

## Stack Tecnologica

- Java 8
- Java EE 8 (Jakarta EE 8 API)
- JSF 2.x + Facelets
- CDI
- JPA/Hibernate
- Maven
- WildFly 25
- PostgreSQL 15
- Docker + Docker Compose
- GitHub Actions

## Estrutura da Pasta Root

- `.github/workflows/ci.yml`: pipeline CI/CD
- `banco-ifpe/`: codigo fonte da aplicacao
- `banco-ifpe/docker/`: Docker Compose e imagem WildFly
- `banco-ifpe/src/main/`: codigo da aplicacao
- `banco-ifpe/src/test/`: testes unitarios e E2E

## Como Executar Localmente

### 1. Build da aplicacao

No diretorio `banco-ifpe`:

- `mvn clean package`

Gera o WAR em:

- `banco-ifpe/target/banco-ifpe.war`

### 2. Subir ambiente com Docker

No diretorio `banco-ifpe`:

- `docker compose -f docker/docker-compose.yml up -d postgres`
- `docker compose -f docker/docker-compose.yml up -d --no-deps wildfly`

Aplicacao:

- `http://localhost:8080/banco-ifpe/`

## Versionamento

- Versao da aplicacao no Maven: `1.0.0`
- Estrategia de versionamento de artefato: semver basico no `pom.xml`
- Commits com escopo e intencao clara (`fix(ci): ...`, `fix(e2e): ...`, etc.)

## Procedimento DevOps Implementado

Durante a evolucao do projeto, foi construida e estabilizada uma esteira CI com gates tecnicos em sequencia.

Pipeline em `.github/workflows/ci.yml`:

1. `validate`
   - Resolve caminho do projeto (`PROJECT_DIR`)
   - Valida estrutura Maven
   - Valida `docker-compose.yml`

2. `lint`
   - Checkstyle
   - PMD
   - SpotBugs

3. `test`
   - Testes unitarios
   - Relatorio JaCoCo

4. `build`
   - Empacotamento WAR
   - Publicacao de artefato

5. `e2e`
   - Download do WAR
   - Preparacao de deploy em WildFly
   - Subida de PostgreSQL
   - Espera de prontidao do banco (`pg_isready`)
   - Subida de WildFly
   - Espera de deploy da aplicacao por health check HTTP
   - Execucao de testes E2E
   - Coleta de logs em caso de falha

6. `pipeline-summary`
   - Resumo final dos jobs

### Melhorias DevOps Realizadas

- Correcao de resolucao de caminhos para projetos em subpasta (`banco-ifpe/`).
- Correcao de referencia de arquivo do Compose no CI.
- Ajuste de resiliencia na etapa de espera de deploy.
- Padronizacao de configuracoes de qualidade (`checkstyle.xml`, `pmd.xml`) com caminho absoluto via `${project.basedir}`.
- Inclusao de publicacao de artefatos e logs para troubleshooting rapido.

## Metodo de Trabalho: GitHub Flow

Este projeto adotou GitHub Flow para resolver problemas de:

- branches longas
- baixa frequencia de integracao
- ausencia de feedback rapido de CI

### Regras adotadas

1. `main` como branch de integracao continua.
2. Branches curtas por objetivo:
   - `feature/...`
   - `fix/...`
3. Mudancas pequenas e incrementais.
4. Push frequente para disparar CI cedo.
5. Merge somente com pipeline verde.

### Beneficios observados

- Menor risco de regressao por lote grande de mudancas.
- Falhas detectadas mais cedo (lint, build, deploy e e2e).
- Melhor rastreabilidade de causa raiz por commit.
- Maior previsibilidade para entrega continua na `main`.

## Segredos no GitHub Actions

Os segredos devem ser cadastrados em:

- Repository Settings -> Secrets and variables -> Actions

Exemplos utilizados no projeto:

- `POSTGRES_DB`
- `POSTGRES_USER`
- `PASSWORD_POSTGRES`

Importante:

- Nao usar sintaxe `${{ secrets.* }}` dentro do `docker-compose.yml`.
- No Compose, usar variaveis no formato `${VAR}`.
- A interpolacao de secrets deve ser feita no workflow (env do job/step).

## Troubleshooting Rapido

Se o job E2E falhar em deploy:

1. Verifique se o WAR foi baixado e copiado para `target/`.
2. Verifique logs de container publicados no artefato `container-logs`.
3. Valide se `PROJECT_DIR` e `COMPOSE_FILE` foram resolvidos corretamente.
4. Valide se a URL `http://localhost:8080/banco-ifpe/` responde no tempo esperado.

## Estado Atual

O projeto esta preparado para:

- desenvolvimento local com Docker
- validacao automatizada em CI
- integracao continua com branches curtas via GitHub Flow
- evolucao incremental segura com gates de qualidade
