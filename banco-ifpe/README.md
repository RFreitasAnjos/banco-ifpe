# banco-ifpe

Sistema para calculo de seguro para clientes construido com **Java EE 8**, **JSF 2.3** e **WildFly 25**.

---

## Tecnologias Java

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 8 |
| Plataforma | Java EE 8 (Jakarta EE 8) |
| Interface Web | JSF 2.3 (Facelets) |
| Injecao de dependencia | CDI 2.0 (Weld) |
| Transacoes | EJB 3.2 Stateless + JTA |
| Persistencia | JPA 2.2 / Hibernate 5 |
| Banco de dados | PostgreSQL 15 |
| Servidor de aplicacao | WildFly 25 |
| Build | Maven 3.x (packaging WAR) |

---

# Hist?ria do usu?rio

O funcion?rio dever? realizar login atraves de **SweetAlert2**, assim carregando uma tela com nome do funcion?rio, desejando bom dia, boa tarde, boa noite de acordo com o hor?rio, o hor?rio e data deve aparecer na tela. Deve mostrar centralizados, separados atraves de um justify-between os bot?es de Seguro Automotivo, Seguro Residencial/Com?rcial e seguro de vida. Haver? formul?rio distintos coletando informa??es do cliente, informa??es est?o descriminadas no final do texto. Ap?s o preenchimento, o funcion?rio ser? redirecionado para um p?gina com as informa??es coletadas do cliente, informa??o do banco-ifpe e todas as cl?usulas do seguro. Ter? um bot?o para gerar um PDF para impress?o com todas as informa??es do cliente, informa??es do banco ifpe, clausulas e as condi??es de pagamento, no rodap? deve ter um campo para assinatura do funcion?rio e outro ao lado para assinatura do cliente. Na mesma tela o funcion?rio clicar? em um bot?o confirmando ou cancelando a simula??o, caso o cliente conclua, dever? ser enviado para uma imagem para o sistema e voltar? para a tela principal. Dever? haver uma lista com nome do cliente, cpf, telefone, tipo de seguro data de in?cio e fim da virgencia do seguro.

**Cliente**
* Nome
* CPF
* Data de Nascimento
* Profiss?o
* Endere?o
* Estado civil
* Pontua??o

**Seguro Autom?vel**

* H? condutores com idade inferior a 25 anos?
* Uso apenas particular?
* Tem garagem em casa?
* Tem garagem no trabalho?
* Distancia da casa at? o trabalho

**Seguro Residencial/Comercial**

* Localidade
* Residencia, Com?rcio ou ambos?
* Casa?
 
**Seguro de Vida**

* Hist?rico de Sa?de
* Tabagismo?
* Pr?tica esportes radicais?

Observa??o 1: No caso do cliente ser do sexo feminino ao realizar contrata??o do seguro de vida deve possuir 1% de desconto do que homens.

## Como rodar com Docker via WSL

Pre-requisitos:
- Docker Desktop instalado e integrado com WSL
- WSL ativo na maquina

Passos:
1. Abra um terminal WSL.
2. Acesse a pasta do docker do projeto:
   cd /mnt/c/Users/r.freitas.dos.anjos/Documents/IFPE/banco-ifpe/docker
3. Para subir a aplicacao normalmente:
   docker compose up --timestamps
4. Para forcar rebuild completo quando alterar WildFly, datasource ou Dockerfile:
   docker compose down -v --remove-orphans
   docker compose build --no-cache
   docker compose up --timestamps

A aplicacao ficara disponivel em:
- http://localhost:8080/banco-ifpe/
- Console administrativa do WildFly: http://localhost:9990

Observacoes:
- O PostgreSQL sobe com o banco db_banco_ifpe.
- O datasource ClienteDS do WildFly deve apontar para jdbc:postgresql://postgres:5432/db_banco_ifpe.
- Se houver erro de conexao com banco antigo, refaca o build sem cache.