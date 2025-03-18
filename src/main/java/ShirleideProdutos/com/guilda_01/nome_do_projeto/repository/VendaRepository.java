package ShirleideProdutos.com.guilda_01.nome_do_projeto.repository;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Integer> {
}
