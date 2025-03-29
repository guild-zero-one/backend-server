package ShirleideProdutos.com.guilda_01.nome_do_projeto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    private Integer id;
    private String nome;
    private LocalDate criadoEm;
    private LocalDate atualizadoEm;


}