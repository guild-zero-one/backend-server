# Requisitos – Fluxo de Autenticação

## 1. Endpoint de Login (`POST /usuarios/login`)

### Requisitos

- Deve retornar:
  - `accessToken`
  - objeto `user` completo (DTO de resposta)

- Não deve retornar apenas e-mail ou identificadores parciais

### Objetivo

Permitir que o frontend inicialize o estado do usuário sem necessidade de chamadas adicionais

---

## 2. Endpoint de Autenticação via SSO (`POST /usuarios/sso`)

### Requisitos

- Deve receber:
- Authorization: Bearer <JWT do Clerk>
- Payload contendo:
  - nome
  - email
  - urlImagem (Avatar do perfil)

---

## 2.1 Comportamento esperado

Ao receber um `clerkId`, o backend deve:

### 1. Validar identidade

- Validar o JWT do Clerk
- Extrair o `sub` do token (identificador único do usuário no Clerk)
- Utilizar o `sub` como `provider_id`

### 2. Verificar existência do usuário

- Buscar usuário pelo `clerkId` na base local

### 3. Cenários

#### Usuário não existe

- Criar novo usuário com base nos dados recebidos do Clerk
- Persistir `clerkId` como identificador externo
- Retornar autenticação completa (`accessToken` + `user`)

#### Usuário já existe

- Validar integridade/autenticidade do `clerkId`
- Retornar autenticação (`accessToken` + `user`)

---

# Requisitos – Mudança na modelagem de `usuario` em DATABASE

## Contexto

O Clerk atua exclusivamente como provedor de autenticação SSO. Todos os usuários devem estar armazenados no banco de dados próprio, que é a fonte de verdade da aplicação.

A modelagem deve ser **agnóstica ao provedor**, suportando login local (email/senha) e login via SSO sem alterações estruturais futuras.

---

## Controle de perfil incompleto

- Usuários criados via SSO podem não possuir todos os dados obrigatórios
- Nesses casos, devem ser criados com `perfil_completo = false`

### Comportamento

- Quando `perfil_completo = false`:
  - O frontend deve redirecionar o usuário para o fluxo de onboarding obrigatório
  - O usuário não deve acessar funcionalidades principais do sistema

- Quando `perfil_completo = true`:
  - Acesso normal ao sistema é permitido

## Campos adicionados

| Campo               | Tipo    | Nullable        | Padrão    | Descrição                                                     |
|---                  |---      |---              |---        |---                                                            |
| `provider`          | varchar | NÃO             | `'local'` | Origem da autenticação: `'local'`, `'clerk'`, `'google'`, etc |
| `provider_id`       | varchar | SIM             | `NULL`    | ID externo do provedor. Nulo quando `provider = 'local'`      |
| `perfil_imagem_url` | varchar | SIM             | `NULL`    | URL da foto do usuário                                        |
| `perfil_completo`   | boolean | NÂO             | `TRUE`    | Flag Auxiliar para indicar se o usuário tem perfil completo   |

---

## Campos alterados

| Campo | Alteração | Motivo |
|---|---|---|
| `senha` | Passa a ser nullable | Usuários SSO não possuem senha local |

---

## Constraint adicional
```sql
-- Impede que o mesmo provedor registre o mesmo usuário duas vezes
CREATE UNIQUE INDEX uq_provider_provider_id
  ON usuario(provider, provider_id)
  WHERE provider_id IS NOT NULL;
```

---

## DDL final
```sql
CREATE TABLE "usuario" (
  "id"                integer PRIMARY KEY,

  "nome"              varchar NOT NULL,
  "sobrenome"         varchar,
  "email"             varchar NOT NULL UNIQUE,
  "senha"             varchar,                           -- NULL quando provider != 'local'
  "celular"           varchar,

  "provider"          varchar NOT NULL DEFAULT 'local',  -- 'local' | 'clerk' | 'google' | ...
  "provider_id"       varchar,                           -- ID externo do provedor
  "perfil_imagem_url" varchar,

  "ativo"             boolean NOT NULL DEFAULT true,
  "permissao"         enum('admin', 'comum'),

  "criado_em"         timestamp DEFAULT now(),
  "atualizado_em"     timestamp DEFAULT now()
);


```

---

## Regras de negócio

- `provider = 'local'` → `senha` obrigatória, `provider_id` nulo
- `provider != 'local'` → `senha` nula, `provider_id` obrigatório
- O mesmo e-mail **não pode** existir com providers diferentes — o e-mail é o identificador único do usuário na plataforma
- Novos provedores podem ser integrados sem alteração na estrutura da tabela

