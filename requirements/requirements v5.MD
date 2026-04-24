# 📦 Backend Requirements — Mobile App (Expo) — v5

## 📌 Contexto

Este documento **consolida correções pendentes do v4** e adiciona novos requisitos identificados durante o desenvolvimento do frontend.

Novidades desta versão:
- Separação de DTOs de pedido entre listagem (resumo) e detalhe (completo)
- Novo endpoint de pedidos por usuário
- Endpoint de toggle de pagamento em vendas

---

## ✅ Status por Entidade

| Entidade | Status |
|---|---|
| Produtos | ⚠️ Pendente do v4 |
| Usuários | ⚠️ Pendente do v4 |
| Pedidos | ⚠️ Pendente do v4 + novos requisitos |
| Vendas | 🆕 Novo requisito |

---

## 📦 Entidade: Produtos

### 🔹 Campos de auditoria — `GET /produtos` e `GET /produtos/{id}`

**Pendência do v4 — não implementado.**

- Incluir `criadoEm` e `atualizadoEm` no DTO de resposta em todos os endpoints de produto

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

**Pendência do v4 — não implementado.**

- `PATCH` deve atualizar **somente os campos presentes no body**
- Campos ausentes devem ser **preservados** — nunca substituídos por `null`
- Campos internos (`provider`, `providerId`, `permissao`, `ativo`, `clerkId`) nunca devem ser alterados por este endpoint

**Campos editáveis:**

| Campo | Tipo |
|---|---|
| `nome` | varchar |
| `sobrenome` | varchar |
| `email` | varchar |
| `celular` | varchar |

**Implementação recomendada:**
```java
if (dto.getNome() != null) usuario.setNome(dto.getNome());
if (dto.getSobrenome() != null) usuario.setSobrenome(dto.getSobrenome());
if (dto.getEmail() != null) usuario.setEmail(dto.getEmail());
if (dto.getCelular() != null) usuario.setCelular(dto.getCelular());
```

---

## 🧾 Entidade: Pedidos

### 🔹 Separação de DTOs — listagem vs detalhe

**Contexto:**
Retornar os itens do pedido na listagem gera payload desnecessário — 10 pedidos × 5 itens = 50 objetos trafegando numa tela que só exibe nome do cliente, status e valor total.

**Ajuste necessário:**
Criar dois DTOs distintos no backend:

**`PedidoResumoResponse`** — usado em `GET /pedidos` e `GET /pedidos/usuario/{usuarioId}`:

| Campo | Descrição |
|---|---|
| `id` | UUID do pedido |
| `status` | `PENDENTE`, `CONCLUIDO` ou `CANCELADO` |
| `criadoEm` | Data de criação |
| `atualizadoEm` | Data de atualização |
| `totalValorVenda` | Valor total de venda |
| `usuario` | Objeto resumido do cliente (ver campos abaixo) |

**`PedidoDetalheResponse`** — usado em `GET /pedidos/{id}`, inclui tudo acima mais:

| Campo | Descrição |
|---|---|
| `idVenda` | UUID da venda associada |
| `idUsuario` | UUID do usuário |
| `totalPrecoUnitario` | Total de custo |
| `itens` | Lista completa de itens com produto resumido |

---

### 🔹 Objeto `usuario` resumido — ambos os DTOs

**Pendência do v4 — não implementado.**

Montar via JOIN a partir do relacionamento `@ManyToOne` existente. Não adicionar coluna no banco.

| Campo | Tipo | Nullable |
|---|---|---|
| `id` | uuid | NÃO |
| `nome` | varchar | NÃO |
| `sobrenome` | varchar | SIM |
| `urlImagem` | varchar | SIM |
| `celular` | varchar | SIM |
| `ativo` | boolean | NÃO |

---

### 🔹 Objeto `produto` resumido nos itens — `PedidoDetalheResponse`

**Pendência do v4 — não implementado.**

Incluir dentro de cada item do pedido. Não expor preços do produto — o item já possui os valores registrados no momento da criação.

| Campo | Tipo | Nullable |
|---|---|---|
| `id` | uuid | NÃO |
| `nome` | varchar | NÃO |
| `urlImagem` | varchar | SIM |
| `descricao` | varchar | SIM |

---

### 🔹 Campo `valorVenda` no item de pedido

**Pendência do v4 — não implementado.**

- Aceitar `valorVenda` no body de criação por item
- Retornar `valorVenda` na resposta de cada item

**Body de criação:**
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

---

### 🔹 Campo `atualizadoEm` ausente

**Pendência do v4 — não implementado.**

- Incluir `atualizadoEm` nos DTOs de listagem e detalhe

---

### 🔹 Filtro por status — `GET /pedidos`

**Pendência do v4 — não implementado.**

- Query param opcional `?status=` aceitando `PENDENTE`, `CONCLUIDO` ou `CANCELADO`
- Ausente → retorna todos

```
GET /pedidos?status=PENDENTE&page=0&size=10
```

---

### 🔹 Search sem efeito — `GET /pedidos`

**Pendência do v4 — não implementado.**

- Termo textual → busca por nome aproximado do usuário (case insensitive, `LIKE %termo%`)
- Termo UUID → busca por `id` exato do pedido

```
GET /pedidos?search=maria
GET /pedidos?search=ped-001
```

---

### 🔹 Pedidos por usuário — `GET /pedidos/usuario/{usuarioId}` 🆕

**Contexto:**
Necessário para a versão cliente do app — o usuário comum só deve ver seus próprios pedidos sem depender de filtro no frontend.

**Requisitos:**
- Retornar lista paginada de pedidos do usuário informado
- Usar o mesmo `PedidoResumoResponse` da listagem geral
- Suportar os mesmos query params: `page`, `size`, `sort`, `status`

```
GET /pedidos/usuario/usr-001?page=0&size=10&sort=criadoEm,desc
GET /pedidos/usuario/usr-001?status=PENDENTE
```

**Exemplo de resposta:**
```json
{
  "content": [
    {
      "id": "ped-001",
      "status": "PENDENTE",
      "criadoEm": "2024-03-01T10:00:00",
      "atualizadoEm": "2024-03-01T10:00:00",
      "totalValorVenda": 259.80,
      "usuario": {
        "id": "usr-001",
        "nome": "Maria",
        "sobrenome": "Oliveira",
        "urlImagem": "https://...",
        "ativo": true
      }
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "number": 0,
  "last": true
}
```

---

### 🔹 Toggle de pagamento — `PATCH /vendas/{id}/pagamento` 🆕

**Contexto:**
O admin alterna o status de pagamento de uma venda diretamente pela tela de detalhe usando um switch.

**Requisitos:**
- Receber o novo valor de `pagamentoRealizado` no body
- Persistir o valor recebido diretamente — sem lógica de inversão no backend
- Retornar a venda atualizada

**Exemplo de requisição:**
```
PATCH /vendas/vnd-001/pagamento
```
```json
{ "pagamentoRealizado": true }
```

**Exemplo de resposta:**
```json
{
  "id": "vnd-001",
  "pagamentoRealizado": true,
  "dataVenda": "2024-03-01",
  "desconto": 0
}
```

> **Motivo da escolha:** O frontend envia o estado exato que o switch assumiu, eliminando a necessidade de um `SELECT` antes do `UPDATE` no backend e evitando condições de corrida em caso de cliques rápidos.

## 📋 Resumo de Pendências v5

| Endpoint | Problema | Ação |
|---|---|---|
| `GET /produtos` | Sem `criadoEm` e `atualizadoEm` | Adicionar campos no DTO |
| `GET /produtos/{id}` | Sem `criadoEm` e `atualizadoEm` | Adicionar campos no DTO |
| `PATCH /usuarios/{id}` | Campos não enviados viram `null` | Atualização seletiva por campo |
| `GET /pedidos` | DTO retorna itens desnecessários na listagem | Separar em `PedidoResumoResponse` |
| `GET /pedidos` | Sem objeto `usuario` | Adicionar ao DTO via JOIN |
| `GET /pedidos` | Sem `atualizadoEm` | Adicionar ao DTO |
| `GET /pedidos` | Sem filtro por `status` | Adicionar query param `?status=` |
| `GET /pedidos` | Search sem efeito | Corrigir busca por nome e UUID |
| `GET /pedidos/{id}` | Sem objeto `usuario` e `produto` nos itens | Adicionar ao DTO via JOIN |
| `GET /pedidos/{id}` | Sem `atualizadoEm` e `valorVenda` nos itens | Adicionar ao DTO |
| `POST /pedidos` (itens) | `valorVenda` não aceito no body | Adicionar ao DTO de entrada |
| `GET /pedidos/usuario/{id}` | Endpoint inexistente | **Implementar** |
| `PATCH /vendas/{id}/pagamento` | Endpoint inexistente | **Implementar** — recebe `{ pagamentoRealizado: boolean }` no body |
