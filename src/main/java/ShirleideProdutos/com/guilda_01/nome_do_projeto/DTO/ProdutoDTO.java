package ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Produto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Getter
@Setter

public class ProdutoDTO {
    private Integer id;
    private String nomeNf;
    private String nomeFantasia;
    private Integer qtdTotal;
    private LocalDate criadoEm;
    private LocalDate atualizadoEm;

    public ProdutoDTO(Produto produto) {
        this.id = produto.getId();
        this.nomeNf = produto.getNomeNf();
        this.nomeFantasia = produto.getNomeFantasia();
        this.qtdTotal = produto.getQtdTotal();
        this.criadoEm = produto.getCriadoEm();
        this.atualizadoEm = produto.getAtualizadoEm();
    }

}
