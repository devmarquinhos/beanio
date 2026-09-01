# Beanio

API REST para gerenciamento de **cafeterias, avaliações, perfis de usuários e favoritos**.

O projeto foi desenvolvido em **Java 26** com **Spring Boot 4.1** e utiliza **Spring Security** e **JWT (JSON Web Token)** para autenticação e proteção das rotas internas.

## Visão geral

O Beanio disponibiliza funcionalidades para:

* Cadastro e autenticação de usuários;
* Listagem e criação de cafeterias;
* Consulta de cafeterias;
* Avaliações de cafeterias;
* Respostas de proprietários às avaliações;
* Gerenciamento de favoritos;
* Estatísticas dos estabelecimentos;
* Consulta e atualização do perfil do usuário autenticado.

## Stack

| Tecnologia          | Utilização                              |
| ------------------- | --------------------------------------- |
| **Java 26**         | Linguagem de programação                |
| **Spring Boot 4.1** | Framework da aplicação                  |
| **Spring Security** | Segurança e autorização                 |
| **PostgreSQL**      | Banco de dados                          |
| **JWT**             | Autenticação baseada em token           |
| **Maven**           | Gerenciamento do projeto e dependências |

## Requisitos

Para executar o projeto localmente, é necessário ter:

* **Java 26 ou superior**;
* **PostgreSQL** em execução;
* Variáveis de ambiente configuradas.

### Variáveis de ambiente

| Variável       | Descrição                          |
| -------------- | ---------------------------------- |
| `DB_NAME`      | Nome do banco de dados             |
| `DB_USERNAME`  | Usuário do PostgreSQL              |
| `DB_PASSWORD`  | Senha do PostgreSQL                |
| `TOKEN_SECRET` | Chave utilizada para os tokens JWT |

## Execução local

Com o PostgreSQL em execução e as variáveis de ambiente configuradas, execute:

```bash
./mvnw spring-boot:run
```

A API será iniciada em:

```text
http://localhost:8080
```

## Autenticação

As rotas protegidas exigem um token JWT enviado no header da requisição:

```http
Authorization: Bearer <token>
```

O login retorna o token que deve ser utilizado nas requisições autenticadas.

As rotas públicas incluem os endpoints de autenticação e consulta de cafeterias. As demais operações exigem autenticação.

## Endpoints

| Método   | URL                                                      | Autenticação | Descrição                                         |
| -------- | -------------------------------------------------------- | :----------: | ------------------------------------------------- |
| `POST`   | `/auth/register`                                         |      Não     | Registra um novo usuário                          |
| `POST`   | `/auth/login`                                            |      Não     | Realiza login e retorna um token JWT              |
| `GET`    | `/coffee-shops?context={context}`                        |      Não     | Lista cafeterias com filtro opcional por contexto |
| `POST`   | `/coffee-shops`                                          |      Sim     | Cria uma nova cafeteria                           |
| `GET`    | `/coffee-shops/{id}`                                     |      Não     | Busca uma cafeteria pelo ID                       |
| `POST`   | `/coffee-shops/{id}/highlights`                          |      Sim     | Adiciona um destaque especial a uma cafeteria     |
| `GET`    | `/coffee-shops/my-shop`                                  |      Sim     | Retorna a cafeteria do usuário autenticado        |
| `GET`    | `/coffee-shops/{coffeeShopId}/statistics`                |      Sim     | Retorna estatísticas da cafeteria                 |
| `POST`   | `/coffee-shops/{coffeeShopId}/favorites`                 |      Sim     | Adiciona uma cafeteria aos favoritos              |
| `DELETE` | `/coffee-shops/{coffeeShopId}/favorites`                 |      Sim     | Remove uma cafeteria dos favoritos                |
| `GET`    | `/users/me/favorites`                                    |      Sim     | Lista as cafeterias favoritas do usuário          |
| `GET`    | `/users/me`                                              |      Sim     | Retorna o perfil do usuário autenticado           |
| `PUT`    | `/users/me`                                              |      Sim     | Atualiza o perfil do usuário autenticado          |
| `POST`   | `/coffee-shops/{coffeeShopId}/reviews`                   |      Sim     | Cria uma avaliação para uma cafeteria             |
| `POST`   | `/coffee-shops/{coffeeShopId}/reviews/{reviewId}/reply`  |      Sim     | Responde a uma avaliação                          |
| `GET`    | `/coffee-shops/{coffeeShopId}/reviews?context={context}` |      Não     | Lista avaliações com filtro opcional por contexto |

## Banco de dados

O projeto utiliza **PostgreSQL** para persistência dos dados.

Atualmente, o Hibernate está configurado com:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Dessa forma, o schema do banco é atualizado automaticamente de acordo com o mapeamento das entidades durante a execução da aplicação.

## Segurança

O sistema utiliza **Spring Security** para controle de acesso e **JWT** para autenticação.

De forma geral:

* `POST /auth/**` possui acesso público;
* A leitura de cafeterias possui acesso público;
* Operações de escrita exigem autenticação;
* Dados relacionados ao usuário exigem autenticação;
* Rotas protegidas utilizam o token JWT enviado no header `Authorization`.

## Estrutura principal

O código-fonte está organizado nas seguintes áreas principais:

```text
src/
└── main/
    ├── java/com/devmarquinhos/beanio/
    │   ├── config/
    │   ├── controller/
    │   ├── domain/
    │   ├── dto/
    │   ├── repository/
    │   ├── security/
    │   ├── service/
    │   └── BeanioApplication.java
    │
    └── resources/
        └── application.properties

└── test/
```

### Principais responsabilidades

* **`controller/`** — exposição dos endpoints REST;
* **`service/`** — regras e operações de negócio;
* **`repository/`** — acesso e persistência dos dados;
* **`domain/`** — entidades e objetos relacionados ao domínio;
* **`dto/`** — objetos utilizados na transferência de dados;
* **`security/`** — componentes relacionados à autenticação e segurança;
* **`config/`** — configurações da aplicação.