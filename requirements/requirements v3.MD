# 📦 Backend Requirements -- Mobile App (Expo) -- v3

## 📌 Context

Este documento complementa o **v2**, consolidando **pontos de atenção
identificados durante testes reais no backend**.

O objetivo é corrigir inconsistências, remover redundâncias e garantir
aderência aos padrões já definidos, com foco em:

-   **Consistência de endpoints**
-   **Padronização de paginação**
-   **Eficiência para ambiente mobile**
-   **Redução de payload e processamento desnecessário**

------------------------------------------------------------------------

## 🏷️ Entidade: Marcas / Fornecedores

### 🔹 Buscar fornecedores -- `GET /fornecedores`

**Problemas identificados:** - Ausência de parâmetro de **search** - Não
retorna a quantidade de produtos por fornecedor

**Ajustes necessários:** - Adicionar parâmetro opcional: - `search` →
filtro por nome (case insensitive) - Incluir no DTO o campo: -
`totalProdutos` (count direto, sem carregar produtos)

**Justificativa:** Evitar uso do endpoint `/fornecedores/com-produtos`,
que gera maior custo de processamento e payload, inadequado para mobile.

**Exemplo de requisição:**

    GET /fornecedores?page=0&size=10&search=bot

**Exemplo de resposta:**

``` json
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

------------------------------------------------------------------------

## 📦 Entidade: Produtos

### 🔹 Listar produtos por fornecedor -- `GET /produtos/fornecedor/{fornecedorId}`

**Problemas identificados:** - Retorno em lista simples (sem
paginação) - Existência de endpoint redundante: -
`/produtos/fornecedor/{fornecedorId}/search`

**Ajustes necessários:** - Implementar **paginação (Pageable)** -
Adicionar parâmetro opcional: - `search` (via request param, não via
path) - Remover endpoint redundante `/search`

**Padronização esperada:** Seguir o mesmo padrão dos demais endpoints
listáveis.

**Exemplo de requisição:**

    GET /produtos/fornecedor/1?page=0&size=10&search=perfume

**Exemplo de resposta:**

``` json
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

------------------------------------------------------------------------

## 💰 Entidade: Vendas

### 🔹 Listar vendas -- `GET /vendas`

**Problemas identificados:** - Retorno em lista simples

**Ajustes necessários:** - Implementar **paginação (Pageable)**

**Exemplo de requisição:**

    GET /vendas?page=0&size=10

**Exemplo de resposta:**

``` json
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 0,
  "totalPages": 0
}
```

------------------------------------------------------------------------

## 🧾 Entidade: Pedidos

### 🔹 Listar pedidos -- `GET /pedidos`

**Problemas identificados:** - Retorno em lista simples - Ausência de
suporte a **search**

**Ajustes necessários:** - Implementar **paginação (Pageable)** -
Adicionar parâmetro opcional: - `search` com suporte a: - ID do pedido -
Nome aproximado do usuário (case insensitive)

**Exemplo de requisição:**

    GET /pedidos?page=0&size=10&search=maria

**Exemplo de resposta:**

``` json
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

------------------------------------------------------------------------

## ⚙️ Padronizações Reforçadas

### 🔹 Consistência de endpoints

-   Evitar criação de endpoints redundantes
-   Priorizar uso de **request params** ao invés de variações de path
    (ex: `/search`)

------------------------------------------------------------------------

### 🔹 Search

-   Deve ser:
    -   Opcional
    -   Case insensitive
    -   Suportar busca parcial
-   Implementado via **query param (`search`)**

------------------------------------------------------------------------

