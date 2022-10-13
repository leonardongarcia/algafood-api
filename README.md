# AlgaFood Api

API de delivery de comida

* Documentação: http://algafood-lb-1853601454.us-east-1.elb.amazonaws.com/swagger-ui/4.11.1/index.html#/


## 1 - Descrição

Esta API é uma aplicação backend desenvolvida durante o curso "Especialista Spring Rest" desenvolvido pela [AlgaWorks](https://www.algaworks.com/) ministrado pelo professor [Thiago Faria](https://www.linkedin.com/in/thiagofa).

Este é um MVP (Produto Mínimo Viável) de um sistema de delivery de comida inspirado no IFood. Com ele é possível fazer todas as operações básicas de tal sistema, como cadastrar um cliente, um produto, um restaurante, e registrar um pedido com os seus itens para determinado cliente.


## 2 - Tecnologias

* Java 17
* IntelliJ
* Postman
* MySql 8.0
* JUnit
* Spring Boot
* Spring Data JPA
* Hibernate
* Flyway
* Spring Security OAuth2
* Spring Authorization Server
* ModelMapper
* MapStruct
* JasperSoft Studio
* Apache FreeMarcker
* SendGrid
* Loggly
* Docker
* Docker-compose
* AWS Elastic Container Service
* AWS Elastic Container Registry
* AWS Load Balancer
* AWS S3
* AWS RDS
* AWS Fargate
* Redis
* Swagger OpenApi
* SpringDoc


## 3 - Modelo de domínio

![Diagrama das classes de domínio](diagrama-de-classes-de-dominio.jpg)


## 4 - Como executar a aplicação


### 4.1 -  Descritivo da segurança

Todos os endepoinst estão protegidos pelo [Spring Security OAuth2](https://spring.io/projects/spring-security).

Os Tokens JWT são emitidos e gerenciados pelo novo projeto [Spring Authorization server](https://spring.io/projects/spring-authorization-server).

Cada usuário tem as permissões de acordo com o grupo em que está inserido, quais sejam:

* Gerente
* Vendedor
* Secretária
* Cadastrador

E temos as seguintes permissões:

* 1	Permite editar cozinhas
* 2	Permite criar ou editar formas de pagamento
* 3	Permite criar ou editar cidades
* 4	Permite criar ou editar estados
* 5	Permite consultar usuários, grupos e permissões
* 6	Permite criar ou editar usuários, grupos e permissões
* 7	Permite criar, editar ou gerenciar restaurantes
* 8	Permite consultar pedidos
* 9	Permite gerenciar pedidos
* 10	Permite gerar relatórios

O usuário que está no grupo de Gerente possui todas as permissões.

Já o usuário Vendedor ou a Secretária possui apenas as permissões 5 e 8.

Por fim o cadastrador tem apenas a permissão 7.


### 4.2 - Usuários para teste

A API está disponível na nuvem da [AWS Amazon](https://aws.amazon.com/pt/?nc2=h_lg) pelo link abaixo:

* Documentação: http://algafood-lb-1853601454.us-east-1.elb.amazonaws.com/swagger-ui/4.11.1/index.html#/

O email e senha de 4 usuários para testar os endpoints são os seguintes:

* A senha é 123 para todos os emails.

* Gerente joao.ger@algafood.com.br

* Vendedor maria.vnd@algafood.com.br

* Secretária jose.aux@algafood.com.br

* Cadastrador sebastiao.cad@algafood.com.br


## 5 - Conhecimentos e habilidades adquiridos com o projeto

* Spring e Injeção de Dependências
* JPA, Hibernate e Flyway
* Spring Data JPA
* Domain-Driven Design (DDD)
* Fundamentos avançados de REST com Spring
* Validações com Bean Validation
* Tratamento e modelagem de erros da API
* Testes de integração
* Boas práticas e modelagem avançada de APIs
* Modelagem de projeções, pesquisas e relatórios
* Upload e download de arquivos
* Envio de e-mails transacionais
* Cache de HTTP
* Documentação com OpenAPI (Swagger)
* HATEOAS e Discoverability
* CORS e consumo de APIs com Java e JavaScript
* Segurança com Spring Security, OAuth2 e JWT
* Cloud-native APIs
* Configuração e gerenciamento de logs
* Versionamento de APIs
* Dockerização da aplicação
* Deploy em produção na nuvem da Amazon


## 6 - Créditos

Especial agradecimento ao grande professor Thiago pela grande competência e didática e também a toda AlgaWorks juntamente com os tutores pela grande presteza na resolução de dúvidas e escalrecimentos adicionais.

* AlgaWorks - https://www.algaworks.com/
* Thiago Faria - https://www.linkedin.com/in/thiagofa


## 7 - Autor

Leonardo do Nascimento Garcia

https://www.linkedin.com/in/leonardo-n-garcia
