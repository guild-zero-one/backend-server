package ShirleideProdutos.com.guilda_01.nome_do_projeto.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_lote_produto", nullable = false)
    private LoteProduto loteProduto;

    @ManyToOne
    @JoinColumn(name = "fk_pedido", nullable = false)
    private PedidoVenda pedidoVenda;

    private Integer quantidade;
    private Double precoUnitario;



}
