package ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoItemDTO {

    private Integer id;
    private Integer fkLote;
    private Integer fkPedido;
    private Integer quantidade;
    private Double precoUnitario;
}
