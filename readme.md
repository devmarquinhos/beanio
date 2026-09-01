# Beanio

API REST para gerenciamento de cafeterias, avaliações, perfis de usuários e favoritos. O projeto foi desenvolvido em Java com Spring Boot e utiliza autenticação JWT para proteger rotas internas.

## Visão geral

- Cadastro e autenticação de usuários
- Listagem e criação de cafeterias
- Avaliações e respostas de proprietários
- Favoritos por usuário
- Estatísticas do estabelecimento
- Perfil do usuário autenticado

## Stack

- Java 26
- Spring Boot 4.1
- Spring Security
- PostgreSQL
- JWT (JSON Web Token)
- Maven

## Requisitos

- Java 26+
- PostgreSQL em execução
- Variáveis de ambiente configuradas:
    - `DB_NAME`
    - `DB_USERNAME`
    - `DB_PASSWORD`
    - `TOKEN_SECRET`

## Execução local

```text
./mvnw spring-boot:run
```

A API será iniciada em:

```text
http://localhost:8080
```

## Autenticação

Algumas rotas exigem um token JWT no header:

```http
Authorization: Bearer <token>
```

Os endpoints públicos incluem autenticação e listagem de cafeterias. As demais rotas protegidas exigem login.

## Endpoints

| Método | URL | Autenticação | Descrição |
| --- | --- | --- | --- |
| POST | `http://localhost:8080/auth/register` | Não | Registra um novo usuário. |
| POST | `http://localhost:8080/auth/login` | Não | Realiza login e retorna um token JWT. |
| GET | `http://localhost:8080/coffee-shops?context={context}` | Não | Lista cafeterias com filtro opcional por contexto. |
| POST | `http://localhost:8080/coffee-shops` | Sim | Cria uma nova cafeteria. |
| GET | `http://localhost:8080/coffee-shops/{id}` | Não | Busca uma cafeteria pelo ID. |
| POST | `http://localhost:8080/coffee-shops/{id}/highlights` | Sim | Adiciona um destaque especial a uma cafeteria. |
| GET | `http://localhost:8080/coffee-shops/my-shop` | Sim | Retorna a cafeteria do usuário autenticado. |
| GET | `http://localhost:8080/coffee-shops/{coffeeShopId}/statistics` | Sim | Retorna estatísticas da cafeteria. |
| POST | `http://localhost:8080/coffee-shops/{coffeeShopId}/favorites` | Sim | Adiciona a cafeteria aos favoritos do usuário. |
| DELETE | `http://localhost:8080/coffee-shops/{coffeeShopId}/favorites` | Sim | Remove a cafeteria dos favoritos do usuário. |
| GET | `http://localhost:8080/users/me/favorites` | Sim | Lista as cafeterias favoritas do usuário logado. |
| GET | `http://localhost:8080/users/me` | Sim | Retorna o perfil atual do usuário. |
| PUT | `http://localhost:8080/users/me` | Sim | Atualiza o perfil do usuário autenticado. |
| POST | `http://localhost:8080/coffee-shops/{coffeeShopId}/reviews` | Sim | Cria uma avaliação para uma cafeteria. |
| POST | `http://localhost:8080/coffee-shops/{coffeeShopId}/reviews/{reviewId}/reply` | Sim | Responde a uma avaliação. |
| GET | `http://localhost:8080/coffee-shops/{coffeeShopId}/reviews?context={context}` | Não | Lista avaliações de uma cafeteria, com filtro opcional por contexto. |

## Observações

- O projeto usa `spring.jpa.hibernate.ddl-auto=update`, então o schema do banco é gerado automaticamente.
- As rotas de leitura de cafeterias estão abertas para acesso público, enquanto ações de escrita e dados do usuário exigem autenticação.
- O sistema de segurança foi configurado para permitir somente `POST /auth/**` e leitura de cafeterias sem token.

## Estrutura principal

```text
src/
├── main/
│   ├── java/com/devmarquinhos/beanio/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── domain/
│   │   ├── dto/
│   │   ├── repository/
│   │   ├── security/
│   │   ├── service/
│   │   └── BeanioApplication.java
│   └── resources/
│       └── application.properties
└── test/
```
