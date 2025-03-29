package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.Contato;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ContatoRepository extends JpaRepository<Contato, Integer> {
     Optional<Contato> findByCelular(String celular);
}
