# 📦 Backend Requirements — Mobile App (Expo) — v4

## 📌 Contexto

Este documento **consolida correções e novos requisitos** identificados durante testes reais, complementando o v3.

Os principais pontos cobertos nesta versão são:

- Campos de auditoria ausentes nas respostas de Produtos
- Comportamento incorreto do `PATCH` de Usuários (substituindo campos não enviados por `null`)
- Enriquecimento obrigatório da resposta de Pedidos com dados do usuário e do produto por item
- Campo de preço de venda ausente no item de pedido

**Princípios adotados (reforço do v3):**
> Path variable → identifica **um recurso específico**.
> Query param → **filtra ou busca** dentro de uma coleção.
> `PATCH` → afeta **apenas os campos enviados** no body.

---

## ✅ Status por Entidade

| Entidade | Status |
|---|---|
| Produtos | ⚠️ Ajustes necessários |
| Usuários | ⚠️ Ajustes necessários |
| Pedidos | ⚠️ Ajustes necessários |

---

## 📦 Entidade: Produtos

### 🔹 Campos de auditoria — `GET /produtos` e `GET /produtos/{id}`

**Problema identificado:**
- Respostas não incluem `criadoEm` e `atualizadoEm`

**Ajuste necessário:**
- Incluir ambos os campos no DTO de resposta em **todos** os endpoints de produto

**Exemplo de resposta atualizada:**
```json
{
  "id": "abc-123",
  "nome": "Perfume Malbec",
  "sku": "MLB-001",
  "descricao": "Fragrância amadeirada",
  "quantidade": 10,
  "precoUnitario": 89.90,
  "valorVenda": 129.90,
  "catalogo": true,
  "urlImagem": "https://...",
  "fornecedorId": "forn-456",
  "criadoEm": "2024-01-15T10:30:00",
  "atualizadoEm": "2024-03-20T14:22:00"
}
```

---

## 👤 Entidade: Usuários

### 🔹 Comportamento incorreto do `PATCH /usuarios/{id}`

**Problema identificado:**
- Campos não enviados no body estão sendo substituídos por `null` no banco
- Exemplo: se `email` não for enviado, o campo `email` é sobrescrito com `null`
- Campos como `provider` e `providerId` que nunca fazem parte do body editável também estão sendo afetados

**Comportamento esperado:**
- `PATCH` deve atualizar **somente os campos presentes no body**
- Campos ausentes no body devem ser **ignorados** — o valor atual no banco deve ser preservado
- Campos internos como `provider`, `providerId`, `permissao`, `ativo`, `clerkId` **nunca** devem ser alterados por este endpoint

**Campos editáveis por este endpoint:**

| Campo | Tipo |
|---|---|
| `nome` | varchar |
| `sobrenome` | varchar |
| `email` | varchar |
| `celular` | varchar |

**Exemplo de requisição:**
```
PATCH /usuarios/abc-123
```
```json
{
  "nome": "Fernando",
  "celular": "(11) 99999-0000"
}
```

**Comportamento esperado:**
- `nome` → atualizado para `"Fernando"`
- `celular` → atualizado para `"(11) 99999-0000"`
- `sobrenome`, `email` e todos os demais campos → **preservados** com os valores atuais do banco

**Implementação recomendada (Spring):**

Utilizar atualização seletiva no service, verificando campo a campo:

```java
if (dto.getNome() != null) usuario.setNome(dto.getNome());
if (dto.getSobrenome() != null) usuario.setSobrenome(dto.getSobrenome());
if (dto.getEmail() != null) usuario.setEmail(dto.getEmail());
if (dto.getCelular() != null) usuario.setCelular(dto.getCelular());
```

**Resposta:**
- Retornar o objeto `Usuario` completo atualizado (mesmo DTO já utilizado em `GET /usuarios/{id}`)

---

## 🧾 Entidade: Pedidos

### 🔹 Usuário resumido na resposta — `GET /pedidos` e `GET /pedidos/{id}`

**Problema identificado:**
- Resposta atual retorna apenas `idUsuario`
- O frontend precisa do nome e avatar do cliente para exibir na listagem e no detalhe sem requisições adicionais

**Ajuste necessário:**
- Incluir objeto `usuario` resumido **diretamente na resposta** do pedido
- O objeto deve ser montado via JOIN no backend — **não adicionar coluna nova no banco**
- O relacionamento `@ManyToOne` com `Usuario` já existente deve ser utilizado para popular o DTO

**Campos do objeto `usuario` no DTO:**

| Campo | Tipo | Nullable |
|---|---|---|
| `id` | uuid | NÃO |
| `nome` | varchar | NÃO |
| `sobrenome` | varchar | SIM |
| `urlImagem` | varchar | SIM |
| `celular` | varchar | SIM |
| `ativo` | boolean | NÃO |

**Exemplo de resposta atualizada:**
```json
{
  "id": "ped-001",
  "idVenda": "vnd-001",
  "idUsuario": "usr-001",
  "status": "PENDENTE",
  "criadoEm": "2024-03-01T10:00:00",
  "atualizadoEm": "2024-03-01T10:00:00",
  "totalPrecoUnitario": 179.80,
  "totalValorVenda": 259.80,
  "usuario": {
    "id": "usr-001",
    "nome": "Maria",
    "sobrenome": "Oliveira",
    "urlImagem": "https://...",
    "celular": "(11) 98888-7777",
    "ativo": true
  },
  "itens": []
}
```

> **Observação:** O campo `usuario` é **exclusivo do DTO** e não deve refletir em nenhuma coluna no banco. O banco já possui `id_usuario` como FK — o objeto é montado no service a partir do relacionamento existente.

---

### 🔹 Campo `atualizadoEm` ausente na resposta

**Problema identificado:**
- Campo `atualizadoEm` não está sendo retornado

**Ajuste necessário:**
- Incluir `atualizadoEm` no DTO de resposta de pedido em **ambos** os endpoints

---

### 🔹 Detalhes do produto nos itens do pedido

**Problema identificado:**
- Item de pedido retorna apenas `idProduto`, sem informações do produto
- O frontend precisa do nome e imagem do produto para exibir no detalhe do pedido

**Ajuste necessário:**
- Incluir objeto `produto` resumido dentro de cada item do pedido
- Dados devem ser buscados via JOIN — **não expor os valores de preço armazenados no produto** (preços do item já estão registrados no próprio item no momento da criação)

**Campos do objeto `produto` no DTO do item:**

| Campo | Tipo | Nullable |
|---|---|---|
| `id` | uuid | NÃO |
| `nome` | varchar | NÃO |
| `urlImagem` | varchar | SIM |
| `descricao` | varchar | SIM |

> **Regra de negócio:** Os campos `precoUnitario` e `valorVenda` do produto **não devem ser expostos** no contexto do item de pedido. O item já possui seus próprios valores registrados no momento da criação, que são os valores que regem o pedido.

**Exemplo de resposta atualizada (itens):**
```json
"itens": [
  {
    "id": "item-001",
    "idProduto": "prod-abc",
    "quantidade": 2,
    "precoUnitario": 89.90,
    "valorVenda": 129.90,
    "produto": {
      "id": "prod-abc",
      "nome": "Perfume Malbec",
      "urlImagem": "https://...",
      "descricao": "Fragrância amadeirada"
    }
  }
]
```

---

### 🔹 Campo `valorVenda` ausente no item de pedido

**Problema identificado:**
- Item de pedido não recebe nem armazena `valorVenda` na criação
- Item de pedido não retorna `valorVenda` na listagem
- Sem esse campo o frontend não consegue calcular o lucro nem exibir o preço de venda correto

**Ajuste necessário — criação:**
- Endpoint de criação de pedido deve aceitar `valorVenda` por item no body

**Exemplo de body de criação:**
```json
{
  "idUsuario": "usr-001",
  "itens": [
    {
      "idProduto": "prod-abc",
      "quantidade": 2,
      "precoUnitario": 89.90,
      "valorVenda": 129.90
    }
  ]
}
```

**Ajuste necessário — resposta:**
- `valorVenda` deve ser retornado em cada item nas respostas de `GET /pedidos` e `GET /pedidos/{id}`

---

### 🔹 Filtro por status — `GET /pedidos`

**Problema identificado:**
- Não há como filtrar pedidos por status via query param

**Ajuste necessário:**
- Adicionar parâmetro opcional `status`
- Valores aceitos: `PENDENTE`, `CONCLUIDO`, `CANCELADO`
- Ausente → retorna todos os status

**Exemplos de requisição:**
GET /pedidos?status=PENDENTE
GET /pedidos?status=CONCLUIDO&page=0&size=10

---

### 🔹 Search sem efeito — `GET /pedidos`

**Problema identificado:**
- Parâmetro `search` não está filtrando por nome do usuário nem por UUID do pedido

**Ajuste necessário:**
- Termo numérico/UUID → busca por `id` exato do pedido
- Termo textual → busca por nome aproximado do usuário (case insensitive, `LIKE %termo%`)

**Exemplos de requisição:**
GET /pedidos?search=maria
GET /pedidos?search=ped-001

## 📋 Resumo de Pendências v4

| Endpoint | Problema | Ação |
|---|---|---|
| `GET /produtos` | Sem `criadoEm` e `atualizadoEm` | Adicionar campos no DTO |
| `GET /produtos/{id}` | Sem `criadoEm` e `atualizadoEm` | Adicionar campos no DTO |
| `PATCH /usuarios/{id}` | Campos não enviados viram `null` | Atualização seletiva por campo |
| `GET /pedidos` | Sem objeto `usuario`, sem `atualizadoEm` | Adicionar ao DTO via JOIN |
| `GET /pedidos/{id}` | Sem objeto `usuario`, sem `atualizadoEm` | Adicionar ao DTO via JOIN |
| `GET /pedidos` (itens) | Sem objeto `produto` por item, sem `valorVenda` | Adicionar ao DTO via JOIN |
| `GET /pedidos/{id}` (itens) | Sem objeto `produto` por item, sem `valorVenda` | Adicionar ao DTO via JOIN |
| Criação de pedido (itens) | `valorVenda` não aceito no body | Adicionar ao DTO de entrada |
| `GET /pedidos` | Sem filtro por status | Adicionar query param `?status=` aceitando `PENDENTE`, `CONCLUIDO` ou `CANCELADO` — ausente retorna todos |
| `GET /pedidos` | Search não funciona por nome do usuário nem por UUID do pedido | Corrigir implementação — busca deve funcionar por nome aproximado do usuário (case insensitive) e por UUID exato do pedido |