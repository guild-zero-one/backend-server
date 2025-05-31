# SimLady API

SimLady é uma API REST desenvolvida em Spring Boot para gestão de vendas, pedidos, usuários e integração com RabbitMQ e Azure Blob Storage.

## Funcionalidades

- Autenticação e gerenciamento de usuários
- Cadastro e consulta de vendas e pedidos
- Envio de mensagens de pedidos via RabbitMQ
- Upload de imagens para Azure Blob Storage

## Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Security
- Maven
- RabbitMQ
- Azure Blob Storage
- JUnit 5 & Mockito (testes)

## Estrutura do Projeto

- `src/main/java/com/zeroone/simlady/`
  - `entity/` — Entidades JPA (ex: `Usuario`, `PedidoVenda`, `Venda`)
  - `repository/` — Repositórios Spring Data
  - `service/` — Lógica de negócio e integrações (ex: `VendaService`, `RabbitMqService`, `AzureBlobStorageService`)
  - `controller/` — Controladores REST
  - `dto/` — Objetos de transferência de dados
  - `mapper/` — Conversores entre entidades e DTOs
  - `config/` — Classes de configuração (ex: RabbitMQ)
- `src/test/java/com/zeroone/simlady/` — Testes unitários e de integração

## Como Executar

### Pré-requisitos

- Java 17 ou superior
- Maven
- RabbitMQ em execução (para funcionalidades de mensageria)
- Conta no Azure Blob Storage (para upload de imagens)

### Passos

1. Clone o repositório:
   ```sh
   git clone https://github.com/sua-org/simlady.git
   cd simlady
2. Configure as variáveis de ambiente ou o application.properties para:

Conexão com o banco de dados
Conexão com o RabbitMQ
Credenciais do Azure Blob Storage
Compile e execute:
mvn clean install
mvn spring-boot:run

## Documentação da API

A documentação completa dos endpoints está disponível via Swagger após iniciar a aplicação:

- [http://{IP:PORTA}/swagger-ui.html](http://{IP:PORTA}/swagger-ui.html)

