package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Contato;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ContatoRepository extends JpaRepository<Contato, Integer> {

     boolean existsByCelular(@NotBlank(message = "Celular não deve ser vazio ou nulo.") @Pattern(regexp = "^\\d{11}$", message = "Número de celular inválido. Deve conter 11 dígitos.") String celular);

     boolean existsByCelularAndIdNot(@Size(min = 11, max = 11, message = "O número de celular deve ter exatamente 11 dígitos") @Pattern(regexp = "\\d{11}", message = "O número de celular deve conter apenas números") String celular, Integer id);
}
