package ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProdutoDTO {

    private Integer id;
    private String nome;
    private String nomeFantasia;
    private Integer fornecedorId;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}