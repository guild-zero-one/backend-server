# 📦 Backend Requirements — Mobile App (Expo) — v3

## 📌 Contexto

Este documento **consolida e corrige** os pontos identificados durante testes reais no backend, complementando o v2.

O principal problema identificado foi o **uso indevido de path variables para operações de busca/filtro**, onde query params (`?search=`) são o padrão correto. Além disso, há endpoints redundantes, ausência de paginação e comportamentos que não refletiram no banco.

**Princípio adotado em todo o documento:**
> Path variable → identifica **um recurso específico**.  
> Query param → **filtra ou busca** dentro de uma coleção.

---

## ✅ Status Geral por Entidade

| Entidade | Status |
|---|---|
| Fornecedores | ⚠️ Ajustes necessários |
| Produtos | ⚠️ Ajustes necessários |
| Categorias | ⚠️ Ajustes necessários |
| Vendas | ⚠️ Ajustes necessários |
| Pedidos | ⚠️ Ajustes necessários |

---

## 🏷️ Entidade: Fornecedores

### 🔹 Listar fornecedores — `GET /fornecedores`

**Problemas identificados:**
- Ausência de parâmetro de `search`
- Retorno sem o campo `totalProdutos` por fornecedor

**Ajustes necessários:**
- Adicionar parâmetro opcional `search` (case insensitive, busca parcial por nome)
- Incluir no DTO o campo `totalProdutos` — deve ser um **count direto**, sem carregar a lista de produtos

> **Observação:** O endpoint `/fornecedores/com-produtos` não deve ser usado para esse fim, pois gera custo de processamento e payload inadequado para mobile. O count deve ser resolvido diretamente na query de listagem.

**Comportamento esperado:**
- `search` ausente → retorna todos os fornecedores paginados normalmente
- `search` presente → filtra pelo termo

**Exemplo de requisição:**
```
GET /fornecedores?page=0&size=10&search=bot
```

**Exemplo de resposta:**
```json
{
  "content": [
    {
      "id": 1,
      "nome": "Boticário",
      "totalProdutos": 8
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1
}
```

---

## 📦 Entidade: Produtos

### 🔹 Listar produtos por fornecedor — `GET /produtos/fornecedor/{fornecedorId}`

**Problemas identificados:**
- Retorno em lista simples, sem paginação
- Criação de endpoint redundante com path variable: `/produtos/fornecedor/{fornecedorId}/search`

**Ajustes necessários:**
- Implementar **paginação (Pageable)**
- Adicionar parâmetro opcional `search` via **query param** no endpoint já existente
- **Remover** o endpoint `/produtos/fornecedor/{fornecedorId}/search` — ele é redundante e quebra o padrão

**Exemplo de requisição:**
```
GET /produtos/fornecedor/1?page=0&size=10&search=perfume
```

**Exemplo de resposta:**
```json
{
  "content": [
    {
      "id": 10,
      "nome": "Perfume Malbec",
      "preco": 129.90
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1
}
```

---

### 🔹 Listar produtos por categoria aproximada — `GET /produtos/categorias`

**Problemas identificados:**
- Endpoint implementado com path variable para search: `/produtos/categorias/search`

**Ajustes necessários:**
- Mover o parâmetro de busca para **query param** no endpoint base
- **Remover** a variação com path variable `/search`

**Comportamento esperado:**
- `search` ausente → retorna vazio (não deve retornar todos os produtos sem filtro)
- `search` presente → retorna produtos cujas categorias correspondam ao termo (case insensitive, busca parcial)

**Exemplo de requisição:**
```
GET /produtos/categorias?search=masc&page=0&size=10
```

**Exemplo de resposta:**
```json
{
  "content": [
    {
      "id": 5,
      "nome": "Perfume Malbec",
      "categorias": ["Masculino", "Popular"]
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1
}
```

---

### 🔹 Associar categoria a produto — `POST /produtos/{produtoId}/categorias/{categoriaId}`

**Problema identificado:**
- Endpoint retorna `204` mas a associação **não reflete no banco de dados**

**Ajuste necessário:**
- Verificar a transação e o commit da operação de persistência
- Validar se a relação na tabela de junção (`produto_categoria`) está sendo inserida corretamente
- Garantir que ambos os IDs (`produtoId` e `categoriaId`) existem antes de criar o vínculo

**Comportamento esperado:**
- `201 Created` ou `204 No Content` — com a associação efetivamente persistida no banco

---

### 🔹 Desassociar categoria de produto — `DELETE /produtos/{produtoId}/categorias/{categoriaId}`

**Problema identificado:**
- Endpoint retorna `204` mas a desassociação **não reflete no banco de dados**

**Ajuste necessário:**
- Verificar a execução do delete na tabela de junção (`produto_categoria`)
- Garantir que o par (`produtoId`, `categoriaId`) está sendo localizado e removido corretamente

**Comportamento esperado:**
- `204 No Content` — com o vínculo efetivamente removido do banco

---

## 🗂️ Entidade: Categorias

### 🔹 Listar categorias — `GET /categorias`

**Status:** ✅ Ok

---

### 🔹 Busca de categorias por nome aproximado

**Problema identificado:**
- Implementado como path variable: `/categorias/search`

**Ajuste necessário:**
- Remover o path `/search`
- Adicionar parâmetro opcional `search` diretamente em `GET /categorias`

**Exemplo de requisição:**
```
GET /categorias?search=fem
```

**Exemplo de resposta:**
```json
[
  { "id": 2, "nome": "Feminino" }
]
```

> Categorias têm volume baixo, retorno em lista simples é aceitável. Paginação opcional.

---

### 🔹 Listar categorias de um produto — `GET /categorias/produtos/{produtoId}`

**Problema identificado:**
- Endpoint não encontrado / não implementado

**Ajuste necessário:**
- Implementar o endpoint
- Retornar lista de categorias vinculadas ao `produtoId` informado

**Exemplo de requisição:**
```
GET /categorias/produtos/5
```

**Exemplo de resposta:**
```json
[
  { "id": 1, "nome": "Masculino" },
  { "id": 3, "nome": "Popular" }
]
```

---

### 🔹 Criar nova categoria — `POST /categorias`

**Status:** ✅ Ok

> **Observação:** O campo `descrição` da entidade Categoria não será utilizado pelo frontend — pode ser ignorado ou removido do DTO de resposta.

---

### 🔹 Deletar categoria — `DELETE /categorias/{id}`

**Status:** ✅ Ok

> Cascade delete nas associações da tabela `produto_categoria` já esperado conforme v2.

---

## 💰 Entidade: Vendas

### 🔹 Listar vendas — `GET /vendas`

**Problema identificado:**
- Retorno em lista simples, sem paginação

**Ajuste necessário:**
- Implementar **paginação (Pageable)**

**Exemplo de requisição:**
```
GET /vendas?page=0&size=10
```

**Exemplo de resposta:**
```json
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 0,
  "totalPages": 0
}
```

---

## 🧾 Entidade: Pedidos

### 🔹 Listar pedidos — `GET /pedidos`

**Problemas identificados:**
- Retorno em lista simples, sem paginação
- Busca implementada em endpoint separado com path variable: `/pedidos/search`

**Ajustes necessários:**
- Implementar **paginação (Pageable)**
- Adicionar parâmetro opcional `search` via **query param** no endpoint base
- **Remover** o endpoint `/pedidos/search` — redundante e fora do padrão

**Comportamento do search:**
- Termo numérico → busca por ID do pedido
- Termo textual → busca por nome aproximado do usuário (case insensitive)

**Exemplo de requisição:**
```
GET /pedidos?page=0&size=10&search=maria
GET /pedidos?page=0&size=10&search=1020
```

**Exemplo de resposta:**
```json
{
  "content": [
    {
      "id": 1020,
      "usuarioNome": "Maria Silva",
      "status": "PENDENTE"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1
}
```

---

## ⚙️ Padrões Reforçados

### 🔹 Query Param vs Path Variable

| Situação | Abordagem correta |
|---|---|
| Identificar um recurso específico | Path variable: `/produtos/{id}` |
| Buscar ou filtrar dentro de uma coleção | Query param: `/produtos?search=termo` |
| Paginação | Query param: `?page=0&size=10` |
| Ordenação | Query param: `?sort=nome,asc` |

**Endpoints com path variable `/search` a remover:**

| Remover | Substituir por |
|---|---|
| `GET /categorias/search` | `GET /categorias?search=` |
| `GET /produtos/categorias/search` | `GET /produtos/categorias?search=` |
| `GET /produtos/fornecedor/{id}/search` | `GET /produtos/fornecedor/{id}?search=` |
| `GET /pedidos/search` | `GET /pedidos?search=` |

---

### 🔹 Padrão de Search

- Parâmetro: `search` (query param)
- Opcional — ausência não deve gerar erro
- Case insensitive
- Busca parcial (`LIKE %termo%` ou equivalente)

---

### 🔹 Padrão de Paginação

Todos os endpoints listáveis devem seguir o padrão Spring Pageable:

```
?page=0&size=10&sort=nome,asc
```

Resposta padrão:

```json
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 0,
  "totalPages": 0
}
```

---

### 🔹 Performance

- Evitar N+1 queries
- Usar fetch otimizado (JOIN FETCH ou projeções) quando necessário
- Retornar apenas os campos necessários via DTOs — sem carregar relacionamentos completos desnecessariamente

---

## 📋 Resumo de Pendências

| Endpoint | Problema | Ação |
|---|---|---|
| `GET /fornecedores` | Sem `search`, sem `totalProdutos` | Adicionar ambos |
| `GET /produtos/fornecedor/{id}` | Lista simples, sem paginação | Paginar + adicionar `search` |
| `GET /produtos/fornecedor/{id}/search` | Endpoint redundante | **Remover** |
| `GET /produtos/categorias/search` | Path variable para search | Migrar para query param, **remover** path |
| `POST /produtos/{id}/categorias/{id}` | 204 mas não persiste | Corrigir persistência |
| `DELETE /produtos/{id}/categorias/{id}` | 204 mas não persiste | Corrigir persistência |
| `GET /categorias/search` | Path variable para search | Migrar para query param, **remover** path |
| `GET /categorias/produtos/{produtoId}` | Não encontrado | **Implementar** |
| `GET /vendas` | Lista simples, sem paginação | Paginar |
| `GET /pedidos` | Lista simples, sem paginação, search em path | Paginar + query param |
| `GET /pedidos/search` | Endpoint redundante | **Remover** |
