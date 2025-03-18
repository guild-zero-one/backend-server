package ShirleideProdutos.com.guilda_01.nome_do_projeto.repository;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Contato;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ContatoRepository extends JpaRepository<Contato, Integer> {
     Optional<Contato> findByCelular(String celular);
}
