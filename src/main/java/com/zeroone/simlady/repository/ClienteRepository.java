package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Cliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

    boolean existsByCpf(@CPF(message = "CPF inválido") @NotBlank(message = "CPF não pode ser vazio") String cpf);

    boolean existsByEmail(@Email(message = "Email inválido") @NotBlank(message = "Email não pode ser vazio") String email);

    boolean existsByCpfAndIdNot(@CPF(message = "CPF inválido") @NotBlank(message = "CPF não pode ser vazio") String cpf, Integer id);

    boolean existsByEmailAndIdNot(@Email(message = "Email inválido") @NotBlank(message = "Email não pode ser vazio") String email, Integer id);
}
