package com.zeroone.simlady.config;

import com.zeroone.simlady.exception.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teste-exception")
public class TesteExceptionController {

    record PedidoValidacaoDto(@NotBlank(message = "O nome não pode ser vazio.") String nome) {}

    @GetMapping("/not-found")
    public void notFound() { throw new ResourceNotFoundException("Recurso não encontrado."); }

    @GetMapping("/already-exists")
    public void alreadyExists() { throw new ResourceAlreadyExistsException("Recurso já existe."); }

    @GetMapping("/bad-request")
    public void badRequest() { throw new BadRequestException("Requisição inválida."); }

    @GetMapping("/unauthorized")
    public void unauthorized() { throw new UnauthorizedException("Não autorizado."); }

    @GetMapping("/method-not-allowed")
    public void methodNotAllowed() { throw new MethodNotAllowedException("Método não permitido."); }

    @PostMapping("/validation")
    public void validation(@Valid @RequestBody PedidoValidacaoDto dto) {}

    @GetMapping("/data-integrity")
    public void dataIntegrity() { throw new DataIntegrityViolationException("Violação de constraint."); }

    @GetMapping("/access-denied")
    public void accessDenied() { throw new AccessDeniedException("Acesso negado."); }

    @GetMapping("/generic-error")
    public void genericError() { throw new RuntimeException("Erro inesperado."); }
}
