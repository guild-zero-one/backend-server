package com.zeroone.simlady.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = TesteExceptionController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        }
)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    // ---- handlers existentes ----

    @Test
    @DisplayName("Deve retornar 404 quando ResourceNotFoundException for lançada")
    void deveRetornar404QuandoResourceNotFoundExceptionLancada() throws Exception {
        mockMvc.perform(get("/teste-exception/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Recurso não encontrado."));
    }

    @Test
    @DisplayName("Deve retornar 409 quando ResourceAlreadyExistsException for lançada")
    void deveRetornar409QuandoResourceAlreadyExistsExceptionLancada() throws Exception {
        mockMvc.perform(get("/teste-exception/already-exists"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Recurso já existe."));
    }

    @Test
    @DisplayName("Deve retornar 400 quando BadRequestException for lançada")
    void deveRetornar400QuandoBadRequestExceptionLancada() throws Exception {
        mockMvc.perform(get("/teste-exception/bad-request"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Requisição inválida."));
    }

    @Test
    @DisplayName("Deve retornar 401 quando UnauthorizedException for lançada")
    void deveRetornar401QuandoUnauthorizedExceptionLancada() throws Exception {
        mockMvc.perform(get("/teste-exception/unauthorized"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Não autorizado."));
    }

    @Test
    @DisplayName("Deve retornar 405 quando MethodNotAllowedException for lançada")
    void deveRetornar405QuandoMethodNotAllowedExceptionLancada() throws Exception {
        mockMvc.perform(get("/teste-exception/method-not-allowed"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405))
                .andExpect(jsonPath("$.message").value("Método não permitido."));
    }

    // ---- novos handlers ----

    @Test
    @DisplayName("Deve retornar 400 com mensagem de validação quando @Valid falhar")
    void deveRetornar400ComMensagemValidacaoQuandoValidFalhar() throws Exception {
        mockMvc.perform(post("/teste-exception/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("O nome não pode ser vazio."));
    }

    @Test
    @DisplayName("Deve retornar 409 quando DataIntegrityViolationException for lançada")
    void deveRetornar409QuandoDataIntegrityViolationExceptionLancada() throws Exception {
        mockMvc.perform(get("/teste-exception/data-integrity"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Registro já existe ou viola uma restrição de dados."));
    }

    @Test
    @DisplayName("Deve retornar 403 quando AccessDeniedException for lançada")
    void deveRetornar403QuandoAccessDeniedExceptionLancada() throws Exception {
        mockMvc.perform(get("/teste-exception/access-denied"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value("Você não tem permissão para esta ação."));
    }

    @Test
    @DisplayName("Deve retornar 500 quando Exception genérica for lançada")
    void deveRetornar500QuandoExceptionGenericaLancada() throws Exception {
        mockMvc.perform(get("/teste-exception/generic-error"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Erro interno. Tente novamente mais tarde."));
    }

    // ---- estrutura do body ----

    @Test
    @DisplayName("Deve incluir campos timestamp, status, error e message no body de erro")
    void deveIncluirCamposEstruturaisNoBodyDeErro() throws Exception {
        mockMvc.perform(get("/teste-exception/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.message").exists());
    }
}
