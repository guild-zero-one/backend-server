package com.zeroone.simlady.controller;

import com.zeroone.simlady.config.security.jwt.GerenciadorTokenJwt;
import com.zeroone.simlady.dto.pedido.PedidoResumoResponseDto;
import com.zeroone.simlady.entity.PedidoVenda;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.enums.Permissao;
import com.zeroone.simlady.mapper.PedidoVendaMapper;
import com.zeroone.simlady.mapper.PedidoVendaResponseMapper;
import com.zeroone.simlady.service.AutenticacaoService;
import com.zeroone.simlady.service.PedidoVendaService;
import com.zeroone.simlady.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PedidoVendaController.class)
@TestPropertySource(properties = {
        "clerk.jwks-url=https://example.com/.well-known/jwks.json",
        "cors.allowed-origins=http://localhost:3000"
})
class PedidoVendaControllerTest {

    @TestConfiguration
    @EnableMethodSecurity
    static class SecurityTestConfig {}

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoVendaService pedidoVendaService;

    @MockBean
    private PedidoVendaMapper pedidoVendaMapper;

    @MockBean
    private PedidoVendaResponseMapper pedidoVendaResponseMapper;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private AutenticacaoService autenticacaoService;

    @MockBean
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    // ---- GET /pedidos?idUsuario= ----

    @Test
    @DisplayName("Usuário comum deve ter idUsuario substituído pelo id do usuário autenticado, ignorando o valor da query string")
    @WithMockUser(authorities = "COMUM")
    void listarPedidos_usuarioComum_deveIgnorarIdUsuarioDaQueryEUsarOAutenticado() throws Exception {
        UUID idAutenticado = UUID.randomUUID();
        UUID idInformadoNaQuery = UUID.randomUUID();

        Usuario usuarioAutenticado = new Usuario();
        usuarioAutenticado.setId(idAutenticado);
        usuarioAutenticado.setPermissao(Permissao.COMUM);

        Page<PedidoVenda> page = new PageImpl<>(List.of(new PedidoVenda()));

        when(usuarioService.buscarAutenticado(any())).thenReturn(usuarioAutenticado);
        when(pedidoVendaService.listarComFiltros(isNull(), isNull(), eq(idAutenticado), any())).thenReturn(page);
        when(pedidoVendaResponseMapper.toResumo(any())).thenReturn(new PedidoResumoResponseDto());

        mockMvc.perform(get("/pedidos").param("idUsuario", idInformadoNaQuery.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Usuário ADMIN deve poder filtrar por idUsuario informado na query string")
    @WithMockUser(authorities = "ADMIN")
    void listarPedidos_usuarioAdmin_deveUsarIdUsuarioInformadoNaQuery() throws Exception {
        UUID idAutenticado = UUID.randomUUID();
        UUID idInformadoNaQuery = UUID.randomUUID();

        Usuario usuarioAutenticado = new Usuario();
        usuarioAutenticado.setId(idAutenticado);
        usuarioAutenticado.setPermissao(Permissao.ADMIN);

        Page<PedidoVenda> page = new PageImpl<>(List.of(new PedidoVenda()));

        when(usuarioService.buscarAutenticado(any())).thenReturn(usuarioAutenticado);
        when(pedidoVendaService.listarComFiltros(isNull(), isNull(), eq(idInformadoNaQuery), any())).thenReturn(page);
        when(pedidoVendaResponseMapper.toResumo(any())).thenReturn(new PedidoResumoResponseDto());

        mockMvc.perform(get("/pedidos").param("idUsuario", idInformadoNaQuery.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Usuário ADMIN sem idUsuario informado deve manter listagem geral (comportamento atual)")
    @WithMockUser(authorities = "ADMIN")
    void listarPedidos_usuarioAdmin_semIdUsuario_deveManterListagemGeral() throws Exception {
        Usuario usuarioAutenticado = new Usuario();
        usuarioAutenticado.setId(UUID.randomUUID());
        usuarioAutenticado.setPermissao(Permissao.ADMIN);

        Page<PedidoVenda> page = new PageImpl<>(List.of(new PedidoVenda()));

        when(usuarioService.buscarAutenticado(any())).thenReturn(usuarioAutenticado);
        when(pedidoVendaService.listarComFiltros(isNull(), isNull(), isNull(), any())).thenReturn(page);
        when(pedidoVendaResponseMapper.toResumo(any())).thenReturn(new PedidoResumoResponseDto());

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk());
    }
}
