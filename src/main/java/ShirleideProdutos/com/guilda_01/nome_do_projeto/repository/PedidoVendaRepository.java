package ShirleideProdutos.com.guilda_01.nome_do_projeto.repository;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.entity.Cliente;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.entity.PedidoVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoVendaRepository extends JpaRepository<PedidoVenda, Integer> {
    List<PedidoVenda> findAllByStatus(String status);

    List<PedidoVenda> findAllByCliente(Cliente cliente);
}
