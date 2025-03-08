package ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FornecedorDTO {

    private Integer id;
    private String nome;
    private String cnpj;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
