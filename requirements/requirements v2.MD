# 📦 Backend Requirements – Mobile App (Expo)

## 📌 Context
Este documento descreve as necessidades do frontend para o backend (Spring Boot), com foco em **performance, paginação e flexibilidade de busca**, considerando o consumo em um app mobile (Expo).

A principal preocupação é **reduzir custo de processamento e payload**, portanto:
- Sempre que possível, utilizar **paginação (Pageable)**
- Evitar retornos massivos
- Incluir suporte a **search e sort** nos endpoints listáveis

---

## 👤 Entidade: Usuários

### 🔹 Listar usuários (geral e tipo cliente)
**Problema atual:**
- Retorno em lista simples

**Necessidades:**
- Converter para **paginação (Pageable)** seguindo padrão já existente
- Adicionar parâmetro de **search** para busca

---

### 🔹 Listar usuários admins (baixa prioridade)
- Endpoint paginado
- Pode não ser utilizado inicialmente
- Considerar como **baixa prioridade**

---

## 🏷️ Entidade: Marcas / Fornecedores

### 🔹 Buscar fornecedores
**Necessidades:**
- Adicionar parâmetro de **search** (filtro por nome da marca/fornecedor).
- Incluir no retorno o campo `totalProdutos`: contagem numérica de produtos vinculados à marca.

**Exemplo de retorno:**
- `{ "nome": "Boticário", "totalProdutos": 8 }`
---

### 🔹 Fornecedores com produtos (baixa prioridade)
**Problema atual:**
- Retornando erro **401**

**Observação:**
- Endpoint pode não ser utilizado
- Tratar como **baixa prioridade**

---

## 📦 Entidade: Produtos

### 🔹 Listar produtos por `marcaId`
**Problema atual:**
- Retorno em lista simples

**Necessidades:**
- Implementar **paginação (Pageable)**
- Adicionar **search** para filtro por nome do produto

---

### 🔹 Cadastrar múltiplos produtos
**Objetivo:**
- Suporte à integração com ETL (Python) que enviará um JSON com vários produtos

**Requisitos:**
- Criar endpoint para inserção em lote
- Produtos devem ser associados a marcas:
  - Criar marca caso não exista
  - Associar corretamente caso exista

**Observação:**
- Validar estrutura final com o time de Python
- Possível uso também via frontend

---

### 🔹 Contar pedidos por produto
**Objetivo:**
- Retornar quantidade de **pedidos distintos** associados a um produto

**Regra de negócio:**
- Se um pedido contém o mesmo produto mais de uma vez, contar como **1 pedido**

**Exemplo:**
- Produto: Malbec
- Resultado: "5 pedidos não concluídos" (a saída deve ser apenas numérica)

---

## 🗂️ Entidade: Categorias

### 🔹 Modelagem
- Um produto pode ter **0..N categorias**
- Categorias são **reutilizáveis**
- Criadas manualmente

**Exemplos:**
- Malbec → Popular, Masculino, Amadeirado
- Shampoo CR7 → sem categoria

---

### 🔹 Regras
- Categoria pode ser associada a vários produtos
- Categoria pode ser desassociada
- Relação recomendada:
  - Tabela de junção (`produto_categoria`)
  - Chave composta (`produtoId`, `categoriaId`)

**Importante:**
- Garantir integridade ao remover associações
- Avaliar uso de **cascade** ou remoção controlada

---

### 🔹 Listar categorias
**Objetivo:**
- Popular tela de seleção no frontend

**Requisitos:**
- Pode retornar lista completa (baixo volume esperado)

---

### 🔹 Listar categorias por produto
**Objetivo:**
- Retornar todas as categorias vinculadas a um produto específico para exibição e gestão.

**Requisitos:**
- O frontend enviará o `produtoId` como parâmetro.
- O retorno deve ser uma lista de objetos contendo o ID e o nome da categoria.

**Exemplo de retorno:**
- `{ "id": 1, "nome": "Feminino" }`

---

### 🔹 Criar nova categoria
**Objetivo:**
- Cadastrar uma nova categoria no sistema.

**Requisitos:**
- Campo único para `nome`.
- Implementar validação para evitar nomes duplicados (Case Insensitive).

---

### 🔹 Deletar categoria
**Objetivo:**
- Exclusão física da categoria no banco de dados.

**Regra de Negócio:**
- A exclusão deve ser permitida mesmo que a categoria esteja vinculada a produtos.
- **Comportamento esperado:** O backend deve remover automaticamente as associações existentes na tabela de junção (`produto_categoria`) antes de deletar a categoria (Cascade Delete).
- *Nota: O frontend será responsável por exibir um alerta de confirmação ao usuário antes desta operação.*

---

### 🔹 Associar categoria a produto
**Objetivo:**
- Vincular uma categoria **já existente** a um produto.

**Requisitos:**
- Receber `produtoId` e `categoriaId`.
- Validar a existência de ambos os registros antes da criação do vínculo.

---

### 🔹 Desassociar categoria de produto
**Objetivo:**
- Remover o vínculo entre um produto e uma categoria.

**Requisitos:**
- O frontend enviará os identificadores para a remoção pontual da relação.

---

### 🔹 Listar produtos por categoria aproximada
**Objetivo:**
- Busca por termo parcial

**Exemplo:**
- Entrada: "masc"
- Saída: produtos com categorias "Masculino"

**Requisitos:**
- Retorno **paginado**
- Busca **case insensitive**

---

## 💰 Entidade: Vendas

### 🔹 Listar vendas
**Problema atual:**
- Retorno em lista simples

**Necessidades:**
- Implementar **paginação (Pageable)**
- Adicionar parâmetro **sort**

---

## 🧾 Entidade: Pedidos

### 🔹 Listar pedidos
**Problema atual:**
- Retorno em lista simples

**Necessidades:**
- Implementar **paginação (Pageable)**
- Adicionar **search**:
  - Por ID do pedido
  - Por nome aproximado do usuário

**Exemplo:**
- "1020" → busca por ID
- "Maria" → busca por nome

- Adicionar parâmetro **sort**

---

## ⚙️ Padrões Recomendados

### 🔹 Paginação
- Utilizar padrão Spring (`Pageable`)
- Parâmetros esperados:
  - `page`
  - `size`
  - `sort`

---

### 🔹 Search
- Implementar busca **case insensitive**
- Preferência por `LIKE` ou equivalente
- Permitir buscas parciais

---

### 🔹 Performance
- Evitar N+1 queries
- Utilizar **fetch otimizado** quando necessário
- Retornar apenas campos necessários (DTOs)

---

## 🚧 Observações Gerais

- Priorizar endpoints utilizados pelo frontend
- Endpoints marcados como baixa prioridade podem ser postergados
- Validar contratos com integração externa (Python ETL)

---

## ✅ Objetivo Final

Garantir um backend:
- Performático
- Escalável
- Otimizado para mobile
- Flexível para busca e listagens

---


