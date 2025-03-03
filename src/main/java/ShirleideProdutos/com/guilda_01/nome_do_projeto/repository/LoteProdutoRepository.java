package ShirleideProdutos.com.guilda_01.nome_do_projeto.repository;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.LoteProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoteProdutoRepository extends JpaRepository<LoteProduto, Integer> {
    List<LoteProduto> findByProdutoId(Integer produtoId);
}
