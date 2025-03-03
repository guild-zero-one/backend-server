package ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.LoteProduto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoteProdutoDTO {
    private Integer id;
    private Integer qtdLote;
    private Double valorUnitario;
    private LocalDate dataValidade;
    private Integer produtoId;
    private LocalDate criadoEm;
    private LocalDate atualizadoEm;
}
