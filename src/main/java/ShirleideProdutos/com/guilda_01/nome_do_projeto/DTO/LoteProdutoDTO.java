package ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoteProdutoDTO {

    private Integer id;
    private Integer qtdLote;
    private Double valorUnitCompra;
    private LocalDateTime dataValidade;
    private Integer produtoId;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}