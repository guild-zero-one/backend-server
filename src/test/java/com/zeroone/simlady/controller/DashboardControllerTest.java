package com.zeroone.simlady.controller;

import com.zeroone.simlady.dto.dashboard.*;
import com.zeroone.simlady.service.AutenticacaoService;
import com.zeroone.simlady.service.DashboardService;
import com.zeroone.simlady.config.security.jwt.GerenciadorTokenJwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DashboardController.class)
@TestPropertySource(properties = {
        "clerk.jwks-url=https://example.com/.well-known/jwks.json",
        "cors.allowed-origins=http://localhost:3000"
})
class DashboardControllerTest {

    @TestConfiguration
    @EnableMethodSecurity
    static class SecurityTestConfig {}

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    @MockBean
    private AutenticacaoService autenticacaoService;

    @MockBean
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    // ---- GET /dashboard/faturamento ----

    @Test
    @DisplayName("Sem periodo deve retornar 400")
    @WithMockUser(authorities = "ADMIN")
    void getFaturamento_semPeriodo_deveRetornar400() throws Exception {
        mockMvc.perform(get("/dashboard/faturamento"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("ADMIN com periodo válido deve retornar 200 com lista no body")
    @WithMockUser(authorities = "ADMIN")
    void getFaturamento_adminComPeriodoValido_deveRetornar200() throws Exception {
        when(dashboardService.getFaturamento("30"))
                .thenReturn(List.of(new FaturamentoItemDTO("S1", new BigDecimal("420.00"))));

        mockMvc.perform(get("/dashboard/faturamento").param("periodo", "30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].label").value("S1"))
                .andExpect(jsonPath("$[0].valor").value(420.00));
    }

    @Test
    @DisplayName("Usuário sem ADMIN deve retornar 403")
    @WithMockUser(authorities = "COMUM")
    void getFaturamento_semPermissao_deveRetornar403() throws Exception {
        mockMvc.perform(get("/dashboard/faturamento").param("periodo", "30"))
                .andExpect(status().isForbidden());
    }

    // ---- GET /dashboard/status-pedidos ----

    @Test
    @DisplayName("ADMIN deve retornar 200 com lista de 3 itens")
    @WithMockUser(authorities = "ADMIN")
    void getStatusPedidos_admin_deveRetornar200() throws Exception {
        when(dashboardService.getStatusPedidos()).thenReturn(List.of(
                new StatusPedidoDTO("Concluído", 42L),
                new StatusPedidoDTO("Pendente",  15L),
                new StatusPedidoDTO("Cancelado", 7L)
        ));

        mockMvc.perform(get("/dashboard/status-pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].label").value("Concluído"));
    }

    @Test
    @DisplayName("Usuário sem ADMIN deve retornar 403")
    @WithMockUser(authorities = "COMUM")
    void getStatusPedidos_semPermissao_deveRetornar403() throws Exception {
        mockMvc.perform(get("/dashboard/status-pedidos"))
                .andExpect(status().isForbidden());
    }

    // ---- GET /dashboard/pagamentos-pendentes ----

    @Test
    @DisplayName("ADMIN deve retornar 200")
    @WithMockUser(authorities = "ADMIN")
    void getPagamentosPendentes_admin_deveRetornar200() throws Exception {
        when(dashboardService.getPagamentosPendentes()).thenReturn(List.of(
                new PagamentoPendenteDTO("Ana Lima", "2026-05-25", new BigDecimal("157.50"))
        ));

        mockMvc.perform(get("/dashboard/pagamentos-pendentes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cliente").value("Ana Lima"));
    }

    @Test
    @DisplayName("Usuário sem ADMIN deve retornar 403")
    @WithMockUser(authorities = "COMUM")
    void getPagamentosPendentes_semPermissao_deveRetornar403() throws Exception {
        mockMvc.perform(get("/dashboard/pagamentos-pendentes"))
                .andExpect(status().isForbidden());
    }

    // ---- GET /dashboard/produtos-estoque ----

    @Test
    @DisplayName("ADMIN deve retornar 200")
    @WithMockUser(authorities = "ADMIN")
    void getProdutosEstoque_admin_deveRetornar200() throws Exception {
        when(dashboardService.getProdutosEstoque()).thenReturn(List.of(
                new ProdutoEstoqueDTO("Sérum Antiqueda 60ml", 27L, 15)
        ));

        mockMvc.perform(get("/dashboard/produtos-estoque"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Sérum Antiqueda 60ml"));
    }

    @Test
    @DisplayName("Usuário sem ADMIN deve retornar 403")
    @WithMockUser(authorities = "COMUM")
    void getProdutosEstoque_semPermissao_deveRetornar403() throws Exception {
        mockMvc.perform(get("/dashboard/produtos-estoque"))
                .andExpect(status().isForbidden());
    }

    // ---- GET /dashboard/ranking-compradores ----

    @Test
    @DisplayName("Sem periodo deve retornar 400")
    @WithMockUser(authorities = "ADMIN")
    void getRankingCompradores_semPeriodo_deveRetornar400() throws Exception {
        mockMvc.perform(get("/dashboard/ranking-compradores"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("ADMIN com periodo válido deve retornar 200")
    @WithMockUser(authorities = "ADMIN")
    void getRankingCompradores_adminComPeriodoValido_deveRetornar200() throws Exception {
        when(dashboardService.getRankingCompradores("7")).thenReturn(List.of(
                new RankingCompradorDTO("Ana Lima", new BigDecimal("1250.50"))
        ));

        mockMvc.perform(get("/dashboard/ranking-compradores").param("periodo", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Ana Lima"));
    }

    @Test
    @DisplayName("Usuário sem ADMIN deve retornar 403")
    @WithMockUser(authorities = "COMUM")
    void getRankingCompradores_semPermissao_deveRetornar403() throws Exception {
        mockMvc.perform(get("/dashboard/ranking-compradores").param("periodo", "7"))
                .andExpect(status().isForbidden());
    }

    // ---- GET /dashboard/clientes-inativos ----

    @Test
    @DisplayName("Sem diasSemPedido deve retornar 400")
    @WithMockUser(authorities = "ADMIN")
    void getClientesInativos_semDiasSemPedido_deveRetornar400() throws Exception {
        mockMvc.perform(get("/dashboard/clientes-inativos"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("ADMIN com diasSemPedido válido deve retornar 200")
    @WithMockUser(authorities = "ADMIN")
    void getClientesInativos_adminComDiasValido_deveRetornar200() throws Exception {
        when(dashboardService.getClientesInativos("60")).thenReturn(List.of(
                new ClienteInativoDTO("Beatriz Nunes", "2026-04-28", 33L)
        ));

        mockMvc.perform(get("/dashboard/clientes-inativos").param("diasSemPedido", "60"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Beatriz Nunes"))
                .andExpect(jsonPath("$[0].dias").value(33));
    }

    @Test
    @DisplayName("Usuário sem ADMIN deve retornar 403")
    @WithMockUser(authorities = "COMUM")
    void getClientesInativos_semPermissao_deveRetornar403() throws Exception {
        mockMvc.perform(get("/dashboard/clientes-inativos").param("diasSemPedido", "60"))
                .andExpect(status().isForbidden());
    }
}
