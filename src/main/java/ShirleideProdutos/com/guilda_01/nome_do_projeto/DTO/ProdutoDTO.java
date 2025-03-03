package ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Produto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProdutoDTO {
    private Integer id;
    private String nomeNf;
    private String nomeFantasia;
    private Integer fornecedorId;
    private LocalDate criadoEm;
    private LocalDate atualizadoEm;

    public ProdutoDTO(Produto produto) {
        this.id = produto.getId();
        this.nomeNf = produto.getNomeNf();
        this.nomeFantasia = produto.getNomeFantasia();
        this.fornecedorId = produto.getFornecedor().getId();
        this.criadoEm = produto.getCriadoEm();
        this.atualizadoEm = produto.getAtualizadoEm();
    }

}
