# 📦 Backend Requirements — Mobile App (Expo) — v5

## 📌 Contexto

Este documento **consolida correções pendentes do v4** e adiciona novos requisitos identificados durante o desenvolvimento do frontend.

Novidades desta versão:
- Separação de DTOs de pedido entre listagem (resumo) e detalhe (completo)
- Remoção de itens da listagem de pedidos
- Enriquecimento do detalhe de venda com pedido completo embutido
- Novo endpoint de pedidos por usuário
- Endpoint de toggle de pagamento em vendas

> **Nota de implementação:** O Spring com JPA/Hibernate já tem os relacionamentos mapeados. Os JOINs descritos neste documento devem ser feitos via `JOIN FETCH` ou projeções para evitar N+1 queries — especialmente em `GET /vendas/{id}` que encadeia venda → pedido → itens → produto.

---

## ✅ Status por Entidade

| Entidade | Status |
|---|---|
| Produtos | ⚠️ Pendente do v4 |
| Usuários | ⚠️ Pendente do v4 |
| Pedidos | ⚠️ Pendente do v4 + novos requisitos |
| Vendas | 🆕 Novos requisitos |

---

## 📦 Entidade: Produtos

### 🔹 Campos de auditoria — `GET /produtos` e `GET /produtos/{id}`

**Pendência do v4 — não implementado.**

Incluir `criadoEm` e `atualizadoEm` no DTO de resposta em todos os endpoints de produto.

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

### 🔹 Separação de DTOs — `PedidoResumoResponse` vs `PedidoDetalheResponse`

**Contexto:**
Retornar itens na listagem gera payload desnecessário — 10 pedidos × 5 itens = 50 objetos trafegando numa tela que só exibe nome do cliente, status e valor total.

---

### 🔹 `GET /pedidos` — listagem com `PedidoResumoResponse`

**Ajustes necessários:**
- Remover `itens` da resposta
- Incluir objeto `usuario` resumido via JOIN
- Incluir `totalItens` (contagem calculada pelo backend)
- Incluir `totalValorVenda` (calculado pelo backend)
- Incluir `atualizadoEm`
- Manter `idVenda` para indicar se o pedido já foi finalizado em venda

**Exemplo de resposta:**
```json
{
  "content": [
    {
      "id": "ped-001",
      "status": "PENDENTE",
      "idVenda": null,
      "criadoEm": "2026-04-25T15:59:14",
      "atualizadoEm": "2026-04-25T15:59:14",
      "totalItens": 3,
      "totalValorVenda": 259.80,
      "usuario": {
        "id": "usr-001",
        "nome": "Maria",
        "sobrenome": "Oliveira",
        "urlImagem": "https://...",
        "celular": "(11) 98888-7777",
        "ativo": true
      }
    }
  ]
}
```

---

### 🔹 `GET /pedidos/{id}` — detalhe com `PedidoDetalheResponse`

**Ajustes necessários:**
- Manter tudo do resumo acima
- Adicionar `itens` com objeto `produto` resumido embutido
- Nos itens: incluir `precoUnitario` e `valorVenda` do momento da criação
- Nos itens: **não expor preços atuais do produto no banco**
- Adicionar `totalPrecoUnitario` (custo total)

**Exemplo de resposta:**
```json
{
  "id": "ped-001",
  "status": "CONCLUIDO",
  "idVenda": "vnd-001",
  "criadoEm": "2026-04-25T12:38:11",
  "atualizadoEm": "2026-04-25T12:38:11",
  "totalItens": 2,
  "totalPrecoUnitario": 250.00,
  "totalValorVenda": 379.80,
  "usuario": {
    "id": "usr-001",
    "nome": "Maria",
    "sobrenome": "Oliveira",
    "urlImagem": "https://...",
    "celular": "(11) 98888-7777",
    "ativo": true
  },
  "itens": [
    {
      "id": "item-001",
      "quantidade": 2,
      "precoUnitario": 100.00,
      "valorVenda": 129.90,
      "produto": {
        "id": "prod-abc",
        "nome": "Perfume Malbec",
        "urlImagem": "https://...",
        "descricao": "Fragrância amadeirada"
      }
    }
  ]
}
```

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

### 🔹 `GET /pedidos/usuario/{usuarioId}` 🆕

**Contexto:**
Necessário para a versão cliente do app — o usuário comum só deve ver seus próprios pedidos.

**Requisitos:**
- Retornar lista paginada usando o mesmo `PedidoResumoResponse`
- Suportar os mesmos query params: `page`, `size`, `sort`, `status`

```
GET /pedidos/usuario/usr-001?page=0&size=10&sort=criadoEm,desc
GET /pedidos/usuario/usr-001?status=PENDENTE
```

---

## 💰 Entidade: Vendas

### 🔹 `GET /vendas` — listagem com objeto `usuario`

**Ajuste necessário:**
- Incluir objeto `usuario` resumido via JOIN para nomear a lista pelo cliente

**Campos do objeto `usuario`:** mesmo padrão dos pedidos (id, nome, sobrenome, urlImagem, ativo)

**Exemplo de resposta:**
```json
{
  "content": [
    {
      "id": "vnd-001",
      "valorTotal": 120.00,
      "desconto": 0.00,
      "pagamentoRealizado": true,
      "dataVenda": "2026-04-25",
      "usuario": {
        "id": "usr-001",
        "nome": "Maria",
        "sobrenome": "Oliveira",
        "urlImagem": "https://...",
        "ativo": true
      }
    }
  ]
}
```

---

### 🔹 `GET /vendas/{id}` — detalhe com pedido completo embutido

**Ajuste necessário:**
- Incluir objeto `pedido` completo embutido — mesmo `PedidoDetalheResponse` com `usuario` e `produto` nos itens
- Uma única requisição retorna tudo que o detalhe de venda precisa exibir

> **Atenção de implementação:** Este endpoint encadeia `venda → pedido → itens → produto` e `pedido → usuario`. Usar `JOIN FETCH` ou projeções para evitar N+1 queries.

**Exemplo de resposta:**
```json
{
  "id": "vnd-001",
  "valorTotal": 259.80,
  "desconto": 0.00,
  "pagamentoRealizado": false,
  "dataVenda": "2026-04-25",
  "pedido": {
    "id": "ped-001",
    "status": "CONCLUIDO",
    "criadoEm": "2026-04-25T12:38:11",
    "totalValorVenda": 259.80,
    "usuario": {
      "id": "usr-001",
      "nome": "Maria",
      "sobrenome": "Oliveira",
      "urlImagem": "https://...",
      "ativo": true
    },
    "itens": [
      {
        "id": "item-001",
        "quantidade": 2,
        "precoUnitario": 100.00,
        "valorVenda": 129.90,
        "produto": {
          "id": "prod-abc",
          "nome": "Perfume Malbec",
          "urlImagem": "https://...",
          "descricao": "Fragrância amadeirada"
        }
      }
    ]
  }
}
```

---

### 🔹 `PATCH /vendas/{id}/pagamento` 🆕

**Contexto:**
O admin alterna o status de pagamento diretamente pela tela de detalhe usando um switch.

**Requisitos:**
- Receber o novo valor de `pagamentoRealizado` no body
- Persistir o valor recebido — sem lógica de inversão no backend
- Retornar a venda atualizada

```
PATCH /vendas/vnd-001/pagamento
```
```json
{ "pagamentoRealizado": true }
```

> **Motivo da escolha:** O frontend envia o estado exato que o switch assumiu, eliminando `SELECT` antes do `UPDATE` e evitando condições de corrida.

---

## 📋 Resumo de Pendências v5

| Endpoint | Problema | Ação |
|---|---|---|
| `GET /produtos` | Sem `criadoEm` e `atualizadoEm` | Adicionar campos no DTO |
| `GET /produtos/{id}` | Sem `criadoEm` e `atualizadoEm` | Adicionar campos no DTO |
| `PATCH /usuarios/{id}` | Campos não enviados viram `null` | Atualização seletiva por campo |
| `GET /pedidos` | Retorna itens desnecessários | Remover itens — usar `PedidoResumoResponse` |
| `GET /pedidos` | Sem `usuario`, `totalItens`, `totalValorVenda`, `atualizadoEm` | Adicionar ao DTO via JOIN |
| `GET /pedidos` | Sem filtro por `status` | Adicionar query param `?status=` |
| `GET /pedidos` | Search sem efeito | Corrigir busca por nome e UUID |
| `GET /pedidos/{id}` | Sem `usuario` e `produto` nos itens | Adicionar ao DTO via JOIN — usar `PedidoDetalheResponse` |
| `GET /pedidos/{id}` | Sem `atualizadoEm`, `valorVenda`, `totalPrecoUnitario` e `totalItens` | Adicionar ao DTO |
| `POST /pedidos` (itens) | `valorVenda` não aceito no body | Adicionar ao DTO de entrada |
| `GET /pedidos/usuario/{id}` | Endpoint inexistente | **Implementar** com `PedidoResumoResponse` |
| `GET /vendas` | Sem objeto `usuario` | Adicionar ao DTO via JOIN |
| `GET /vendas/{id}` | Sem pedido completo embutido | **Implementar** com `JOIN FETCH` — venda → pedido → itens → produto |
| `PATCH /vendas/{id}/pagamento` | Endpoint inexistente | **Implementar** — recebe `{ pagamentoRealizado: boolean }` |
