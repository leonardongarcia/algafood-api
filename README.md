# Algafood Api
API de delivery de comida

## Segurança da aplicação

Todos os endepoinst estão protegidos pelo [Spring Security OAuth2](https://spring.io/projects/spring-security).

Os Tokens JWT são emitidos e gerenciados pelo novo projeto [Spring Authorization server](https://spring.io/projects/spring-authorization-server).

Cada usuário tem as permissões de acordo com o grupo em que está inserido, quais sejam:

* Gerente
* Vendedor
* Secretária
* Cadastrador

E temos as seguintes permissões:

1	Permite editar cozinhas
2	Permite criar ou editar formas de pagamento
3	Permite criar ou editar cidades
4	Permite criar ou editar estados
5	Permite consultar usuários, grupos e permissões
6	Permite criar ou editar usuários, grupos e permissões
7	Permite criar, editar ou gerenciar restaurantes
8	Permite consultar pedidos
9	Permite gerenciar pedidos
10	Permite gerar relatórios

O usuário que está no grupo de Gerente possui todas as permissões.

Já o usuário Vendedor ou a Secretária possui apenas as permissões 5 e 8.

Por fim o cadastrador tem apenas a permissão 7.

## Como acessar e testar

A API está disponível na nuvem da [AWS Amazon](https://aws.amazon.com/pt/?nc2=h_lg) pelo link abaixo:

* Link Documentação: http://algafood-lb-1853601454.us-east-1.elb.amazonaws.com/swagger-ui/4.11.1/index.html#/

E abaixo o email e senha de 4 usuários para testar os endpoints:

A senha é 123 para todos os emails.

* Gerente joao.ger@algafood.com.br

* Vendedor maria.vnd@algafood.com.br


* Secretária jose.aux@algafood.com.br


* Cadastrador sebastiao.cad@algafood.com.br


## Descrição do projeto

Esta API é uma aplicação backend desenvolvida durante o curso "Especialista Spring rest" desenvolvido pela AlgaWorks ministrado pelo professor Thiago Faria de Andrade.

## Modelo de domínio do projeto

![Diagrama das classes de domínio](diagrama-de-classes-de-dominio.jpg)

## Aprendizados e tecnologias 

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


# Créditos

Especial agradecimento ao grande professor Thiago pela grande competência e didática e também a toda AlgaWorks juntamente com os tutores pela grande presteza na resolução de dúvidas e escalrecimentos adicionais.

* AlgaWorks - https://www.algaworks.com/
* Thiago Faria - https://www.linkedin.com/in/thiagofa

# Autor

Leonardo do Nascimento Garcia


https://www.linkedin.com/in/leonardo-n-garcia
