# 📊 Backend Requirements — Dashboard Endpoints — v6

## 📌 Contexto

Este documento especifica os **novos endpoints do módulo Dashboard** necessários para alimentar a tela analítica do painel administrativo do app SimLady.

O frontend já possui o endpoint `GET /dashboard/home-kpis` consumido e funcional. Os endpoints aqui descritos são os **6 restantes**, identificados a partir da análise das seções da `DashboardScreen`.

**Stack do frontend:** Expo React Native + TypeScript  
**Módulo backend esperado:** `DashboardController` / `DashboardService`  
**Autorização:** todos os endpoints abaixo são exclusivos de usuários com permissão `ADMIN`.

> **Nota de implementação:** Os endpoints de dashboard são predominantemente consultas de agregação (`SUM`, `COUNT`, `GROUP BY`, `MAX`). Prefira **JPQL com projeções** ou **native queries** a carregar entidades completas e processar em memória — o volume de dados pode ser significativo em produção.

---

## ✅ Status por Endpoint

| Endpoint | Status |
|---|---|
| `GET /dashboard/home-kpis` | ✅ Já implementado |
| `GET /dashboard/faturamento` | 🆕 Implementar |
| `GET /dashboard/status-pedidos` | 🆕 Implementar |
| `GET /dashboard/pagamentos-pendentes` | 🆕 Implementar |
| `GET /dashboard/produtos-estoque` | 🆕 Implementar |
| `GET /dashboard/ranking-compradores` | 🆕 Implementar |
| `GET /dashboard/clientes-inativos` | 🆕 Implementar |

---

## 📈 Endpoint 1 — Faturamento por período

### `GET /dashboard/faturamento?periodo={7|30|90}`

**Contexto:**
Alimenta o gráfico de barras de faturamento. O agrupamento muda conforme o período selecionado — não é possível usar o mesmo agrupamento para os três casos.

**Query param obrigatório:**

| Param | Tipo | Valores aceitos |
|---|---|---|
| `periodo` | string | `"7"`, `"30"`, `"90"` |

**Regra de agrupamento por período:**

| Período | Agrupa por | Exemplo de label |
|---|---|---|
| `7` | Dia da semana | `"Seg"`, `"Ter"`, `"Qua"`, `"Qui"`, `"Sex"`, `"Sáb"`, `"Dom"` |
| `30` | Semana do mês | `"S1"`, `"S2"`, `"S3"`, `"S4"` |
| `90` | Mês | `"Abr"`, `"Mai"`, `"Jun"` |

**Fonte de dados:** tabela `venda`, campo `valor_total`, filtrado pelos últimos N dias a partir de hoje. Considerar todas as vendas (independente de `pagamento_realizado`).

**Resposta:**
```json
[
  { "label": "S1", "valor": 420.00 },
  { "label": "S2", "valor": 680.00 },
  { "label": "S3", "valor": 530.00 },
  { "label": "S4", "valor": 790.00 }
]
```

**Implementação recomendada (JPQL — exemplo para periodo=30):**
```java
@Query("""
    SELECT WEEK(v.dataVenda) as semana, SUM(v.valorTotal) as total
    FROM Venda v
    WHERE v.dataVenda >= :dataInicio
    GROUP BY WEEK(v.dataVenda)
    ORDER BY semana ASC
""")
List<Object[]> findFaturamentoUltimos30Dias(@Param("dataInicio") LocalDate dataInicio);
```

> O service deve converter o resultado em `List<FaturamentoItemDTO>` com `label` e `valor`, aplicando a formatação do label conforme o período recebido.

---

## 🍩 Endpoint 2 — Status dos pedidos

### `GET /dashboard/status-pedidos`

**Contexto:**
Alimenta o gráfico de rosca (donut chart) com a contagem de pedidos por status. Sem parâmetro de período — reflete o estado atual da base completa.

**Fonte de dados:** tabela `pedido`, `COUNT(*) GROUP BY status`.

**Resposta:**
```json
[
  { "label": "Concluído", "value": 42 },
  { "label": "Pendente",  "value": 15 },
  { "label": "Cancelado", "value": 7  }
]
```

> A ordem dos itens na lista deve ser sempre `CONCLUIDO → PENDENTE → CANCELADO` para garantir consistência visual com as cores do gráfico no frontend.

**Implementação recomendada (JPQL):**
```java
@Query("""
    SELECT p.status, COUNT(p)
    FROM Pedido p
    GROUP BY p.status
""")
List<Object[]> countByStatus();
```

> O service mapeia `CONCLUIDO → "Concluído"`, `PENDENTE → "Pendente"`, `CANCELADO → "Cancelado"` e garante a ordem correta na lista de resposta.

---

## 💳 Endpoint 3 — Pagamentos pendentes

### `GET /dashboard/pagamentos-pendentes`

**Contexto:**
Alimenta a lista de vendas com pagamento em aberto, exibindo nome do cliente, data da venda e valor. Ordenado por data mais antiga primeiro — prioriza as cobranças mais urgentes.

**Fonte de dados:** tabela `venda` JOIN `usuario` (via `pedido`), filtro `pagamento_realizado = false`, ordenado por `data_venda ASC`, limitado a 10 registros.

**Resposta:**
```json
[
  {
    "cliente": "Ana Lima",
    "data": "2026-05-25",
    "valor": 157.50
  },
  {
    "cliente": "Carlos Mota",
    "data": "2026-05-22",
    "valor": 89.90
  }
]
```

> O campo `cliente` deve ser `usuario.nome + " " + usuario.sobrenome`. O campo `data` deve ser formatado como `yyyy-MM-dd`.

**Nota:** Se o relacionamento `Venda → Pedido → Usuario` já existir mapeado via JPA, usar `JOIN FETCH` para evitar N+1. Limitar a resposta a no máximo 10 itens via `LIMIT` ou `Pageable` com `size=10`.

---

## 📦 Endpoint 4 — Produtos × Estoque

### `GET /dashboard/produtos-estoque`

**Contexto:**
Alimenta a lista de análise de risco de estoque. Cruza a **demanda** (total de itens pedidos) com a **disponibilidade** (estoque atual do produto). O frontend calcula o índice de risco (`pedidos / estoque`) e aplica as cores.

**Fonte de dados:** tabela `produto` JOIN `item_pedido`, contando o total de unidades pedidas por produto. Ordenado por maior demanda relativa (`pedidos / estoque DESC`). Limitado a 10 produtos.

**Resposta:**
```json
[
  { "nome": "Sérum Antiqueda 60ml",     "pedidos": 27, "estoque": 15 },
  { "nome": "Máscara Capilar Keratina", "pedidos": 31, "estoque": 33 }
]
```

> `pedidos` = `SUM(item_pedido.quantidade)` para aquele produto, considerando apenas pedidos com status `PENDENTE` ou `CONCLUIDO` (excluir cancelados).  
> `estoque` = `produto.quantidade` (campo de estoque atual).

**Implementação recomendada (JPQL):**
```java
@Query("""
    SELECT p.nome,
           COALESCE(SUM(i.quantidade), 0) as totalPedidos,
           p.quantidade as estoque
    FROM Produto p
    LEFT JOIN ItemPedido i ON i.produto = p
    LEFT JOIN i.pedido ped ON ped.status IN ('PENDENTE', 'CONCLUIDO')
    GROUP BY p.id, p.nome, p.quantidade
    ORDER BY (CAST(COALESCE(SUM(i.quantidade), 0) AS float) / NULLIF(p.quantidade, 0)) DESC
""")
List<Object[]> findProdutosComDemandaEEstoque(Pageable pageable);
```

---

## 🏆 Endpoint 5 — Ranking de compradores

### `GET /dashboard/ranking-compradores?periodo={7|30|90}`

**Contexto:**
Alimenta o ranking dos clientes que mais compraram no período, ordenado por valor total decrescente. Limitado a 5 clientes.

**Query param obrigatório:**

| Param | Tipo | Valores aceitos |
|---|---|---|
| `periodo` | string | `"7"`, `"30"`, `"90"` |

**Fonte de dados:** tabela `venda` JOIN `pedido` JOIN `usuario`, `SUM(venda.valor_total) GROUP BY usuario`, filtrado pelos últimos N dias a partir de hoje, `ORDER BY total DESC`, `LIMIT 5`.

**Resposta:**
```json
[
  { "nome": "Ana Lima",    "total": 1250.50 },
  { "nome": "Maria Souza", "total": 980.00  }
]
```

> `nome` = `usuario.nome + " " + usuario.sobrenome`.  
> Considerar apenas vendas cujo `pedido.status = CONCLUIDO`.  
> `periodo` define quantos dias retroativos calcular a partir de hoje.

**Implementação recomendada (JPQL):**
```java
@Query("""
    SELECT u.nome, u.sobrenome, SUM(v.valorTotal) as total
    FROM Venda v
    JOIN v.pedido p
    JOIN p.usuario u
    WHERE p.status = 'CONCLUIDO'
      AND v.dataVenda >= :dataInicio
    GROUP BY u.id, u.nome, u.sobrenome
    ORDER BY total DESC
""")
List<Object[]> findRankingCompradores(
    @Param("dataInicio") LocalDate dataInicio,
    Pageable pageable
);
```

---

## 😴 Endpoint 6 — Clientes inativos

### `GET /dashboard/clientes-inativos?diasSemPedido={30|60}`

**Contexto:**
Alimenta a lista de clientes que não realizaram pedidos há pelo menos N dias, ajudando a identificar quem precisa de reengajamento.

**Query param obrigatório:**

| Param | Tipo | Valores aceitos |
|---|---|---|
| `diasSemPedido` | string | `"30"`, `"60"` |

**Fonte de dados:** tabela `pedido` JOIN `usuario`, agrupado por usuário. Retorna usuários cujo `MAX(pedido.criado_em)` é anterior a `hoje - N dias`. Ordenado por `dias DESC` (mais inativos primeiro). Limitado a 10 clientes.

**Resposta:**
```json
[
  {
    "nome": "Beatriz Nunes",
    "ultimoPedido": "2026-04-28",
    "dias": 33
  },
  {
    "nome": "Ricardo Alves",
    "ultimoPedido": "2026-04-20",
    "dias": 41
  }
]
```

> `ultimoPedido` = `MAX(pedido.criado_em)` formatado como `yyyy-MM-dd`.  
> `dias` = diferença em dias entre hoje e `ultimoPedido` — pode ser calculado pelo backend com `DATEDIFF` ou pelo service em Java.  
> Considerar apenas pedidos com status `PENDENTE` ou `CONCLUIDO` (excluir cancelados).  
> Excluir usuários com permissão `ADMIN` — apenas clientes comuns.

**Implementação recomendada (JPQL):**
```java
@Query("""
    SELECT u.nome, u.sobrenome, MAX(p.criadoEm) as ultimoPedido
    FROM Pedido p
    JOIN p.usuario u
    WHERE p.status IN ('PENDENTE', 'CONCLUIDO')
      AND u.permissao = 'COMUM'
    GROUP BY u.id, u.nome, u.sobrenome
    HAVING MAX(p.criadoEm) < :corte
    ORDER BY ultimoPedido ASC
""")
List<Object[]> findClientesInativos(
    @Param("corte") LocalDateTime corte,
    Pageable pageable
);
```

> O service calcula `corte = LocalDateTime.now().minusDays(diasSemPedido)` e `dias = ChronoUnit.DAYS.between(ultimoPedido.toLocalDate(), LocalDate.now())`.

---

## 🗂️ DTOs esperados

### `FaturamentoItemDTO`
```java
public record FaturamentoItemDTO(String label, BigDecimal valor) {}
```

### `StatusPedidoDTO`
```java
public record StatusPedidoDTO(String label, Long value) {}
```

### `PagamentoPendenteDTO`
```java
public record PagamentoPendenteDTO(String cliente, String data, BigDecimal valor) {}
```

### `ProdutoEstoqueDTO`
```java
public record ProdutoEstoqueDTO(String nome, Long pedidos, Integer estoque) {}
```

### `RankingCompradorDTO`
```java
public record RankingCompradorDTO(String nome, BigDecimal total) {}
```

### `ClienteInativoDTO`
```java
public record ClienteInativoDTO(String nome, String ultimoPedido, Long dias) {}
```

---

## 🔐 Segurança

Todos os endpoints devem exigir autenticação e permissão `ADMIN`. Configurar no `SecurityFilterChain` ou via anotação no controller:

```java
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/dashboard/faturamento")
public ResponseEntity<List<FaturamentoItemDTO>> getFaturamento(...) { ... }
```

---

## 🧪 Testes Unitários

Todos os endpoints devem ter cobertura de testes **antes do deploy no Railway**. Usar **JUnit 5 + Mockito** para a camada de serviço e **MockMvc** para a camada de controller.

---

### Estrutura esperada

```
src/test/java/.../dashboard/
  DashboardServiceTest.java
  DashboardControllerTest.java
```

---

### `DashboardServiceTest` — camada de serviço

Testar o `DashboardService` isolado, mockando o repositório com `@ExtendWith(MockitoExtension.class)`.

#### Casos obrigatórios por método:

**`getFaturamento(String periodo)`**
- `periodo = "7"` → deve chamar o repositório com `dataInicio = hoje - 7 dias` e retornar lista com labels de dia da semana
- `periodo = "30"` → deve chamar com `dataInicio = hoje - 30 dias` e retornar labels `S1..S4`
- `periodo = "90"` → deve chamar com `dataInicio = hoje - 90 dias` e retornar labels de mês abreviado
- Repositório retorna lista vazia → deve retornar `[]` sem lançar exceção
- `periodo` inválido → deve lançar `IllegalArgumentException` ou `ResponseStatusException(400)`

**`getStatusPedidos()`**
- Repositório retorna contagens para os 3 status → resposta deve conter exatamente 3 itens na ordem `Concluído → Pendente → Cancelado`
- Um status sem pedidos (ex: `CANCELADO = 0`) → deve aparecer na resposta com `value = 0` (não omitir)

**`getPagamentosPendentes()`**
- Repositório retorna lista com vendas pendentes → deve mapear `nome + sobrenome` no campo `cliente` e formatar `data` como `yyyy-MM-dd`
- Repositório retorna mais de 10 itens → resposta deve conter no máximo 10
- Sem vendas pendentes → deve retornar `[]`

**`getProdutosEstoque()`**
- Produtos retornados pelo repositório devem aparecer ordenados por maior risco (`pedidos / estoque DESC`)
- Produto com `estoque = 0` → não deve causar divisão por zero (repositório usa `NULLIF` — verificar que o service trata `null` se vier)
- Sem produtos → deve retornar `[]`

**`getRankingCompradores(String periodo)`**
- `periodo = "7"` → `dataInicio = hoje - 7 dias`, retorna top 5 ordenado por `total DESC`
- `periodo = "30"` → `dataInicio = hoje - 30 dias`
- `periodo = "90"` → `dataInicio = hoje - 90 dias`
- Repositório retorna menos de 5 clientes → deve retornar todos sem erro
- Sem vendas no período → deve retornar `[]`

**`getClientesInativos(String diasSemPedido)`**
- `diasSemPedido = "30"` → `corte = hoje - 30 dias`, retorna clientes com `MAX(criadoEm) < corte`
- `diasSemPedido = "60"` → `corte = hoje - 60 dias`
- Campo `dias` calculado corretamente (diferença entre hoje e `ultimoPedido`)
- Sem clientes inativos → deve retornar `[]`

---

### `DashboardControllerTest` — camada HTTP

Testar o `DashboardController` com `@WebMvcTest` + `@MockBean` no service.

#### Casos obrigatórios por endpoint:

**`GET /dashboard/faturamento`**
- Sem `periodo` → deve retornar `400 Bad Request`
- `periodo = "30"` com usuário ADMIN → deve retornar `200 OK` com lista no body
- Usuário sem role ADMIN → deve retornar `403 Forbidden`

**`GET /dashboard/status-pedidos`**
- Usuário ADMIN → deve retornar `200 OK` com lista de 3 itens
- Usuário sem role ADMIN → deve retornar `403 Forbidden`

**`GET /dashboard/pagamentos-pendentes`**
- Usuário ADMIN → deve retornar `200 OK`
- Usuário sem role ADMIN → deve retornar `403 Forbidden`

**`GET /dashboard/produtos-estoque`**
- Usuário ADMIN → deve retornar `200 OK`
- Usuário sem role ADMIN → deve retornar `403 Forbidden`

**`GET /dashboard/ranking-compradores`**
- Sem `periodo` → deve retornar `400 Bad Request`
- `periodo = "7"` com usuário ADMIN → deve retornar `200 OK`
- Usuário sem role ADMIN → deve retornar `403 Forbidden`

**`GET /dashboard/clientes-inativos`**
- Sem `diasSemPedido` → deve retornar `400 Bad Request`
- `diasSemPedido = "60"` com usuário ADMIN → deve retornar `200 OK`
- Usuário sem role ADMIN → deve retornar `403 Forbidden`

---

### Exemplo de estrutura — `DashboardServiceTest`

```java
@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private DashboardRepository dashboardRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void getFaturamento_periodo30_deveRetornarQuatroSemanas() {
        // given
        List<Object[]> mockRows = List.of(
            new Object[]{ 1, new BigDecimal("420.00") },
            new Object[]{ 2, new BigDecimal("680.00") }
        );
        when(dashboardRepository.findFaturamento(any(LocalDate.class)))
            .thenReturn(mockRows);

        // when
        List<FaturamentoItemDTO> result = dashboardService.getFaturamento("30");

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).label()).isEqualTo("S1");
        verify(dashboardRepository, times(1)).findFaturamento(any(LocalDate.class));
    }

    @Test
    void getFaturamento_periodoInvalido_deveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
            () -> dashboardService.getFaturamento("365"));
    }

    @Test
    void getStatusPedidos_semPedidos_deveRetornarTresItensComZero() {
        when(dashboardRepository.countByStatus()).thenReturn(List.of());

        List<StatusPedidoDTO> result = dashboardService.getStatusPedidos();

        assertThat(result).hasSize(3);
        assertThat(result.get(0).label()).isEqualTo("Concluído");
        assertThat(result.get(0).value()).isEqualTo(0L);
    }
}
```

### Exemplo de estrutura — `DashboardControllerTest`

```java
@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getFaturamento_periodoValido_deveRetornar200() throws Exception {
        when(dashboardService.getFaturamento("30"))
            .thenReturn(List.of(new FaturamentoItemDTO("S1", new BigDecimal("420.00"))));

        mockMvc.perform(get("/dashboard/faturamento").param("periodo", "30"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].label").value("S1"))
            .andExpect(jsonPath("$[0].valor").value(420.00));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getFaturamento_semPeriodo_deveRetornar400() throws Exception {
        mockMvc.perform(get("/dashboard/faturamento"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "COMUM")
    void getFaturamento_semPermissao_deveRetornar403() throws Exception {
        mockMvc.perform(get("/dashboard/faturamento").param("periodo", "30"))
            .andExpect(status().isForbidden());
    }
}
```

---

### Dependências necessárias (se ainda não estiverem no `pom.xml`)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

> **Critério de aceite para deploy:** todos os testes do módulo `dashboard` devem passar (`mvn test -Dtest=Dashboard*`) sem erros antes de abrir o PR ou fazer push para a branch de produção.

---

## 📋 Resumo de Implementação

| Endpoint | Query principal | Limite | Param |
|---|---|---|---|
| `GET /dashboard/faturamento` | `SUM(valor_total) GROUP BY período` | — | `periodo=7\|30\|90` |
| `GET /dashboard/status-pedidos` | `COUNT(*) GROUP BY status` | — | — |
| `GET /dashboard/pagamentos-pendentes` | `venda WHERE pagamento_realizado=false ORDER BY data ASC` | 10 | — |
| `GET /dashboard/produtos-estoque` | `SUM(item.quantidade) JOIN produto ORDER BY risco DESC` | 10 | — |
| `GET /dashboard/ranking-compradores` | `SUM(valor_total) GROUP BY usuario ORDER BY total DESC` | 5 | `periodo=7\|30\|90` |
| `GET /dashboard/clientes-inativos` | `MAX(criado_em) GROUP BY usuario HAVING MAX < corte` | 10 | `diasSemPedido=30\|60` |
