package ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Cliente;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Contato;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContatoDTO {
    private Integer id;
    private String celular;
    private LocalDate criadoEm;
    private LocalDate atualizadoEm;
    private Cliente cliente;
    

    public ContatoDTO(Contato contato) {
        this.id = contato.getId();
        this.celular = contato.getCelular();
        this.criadoEm = contato.getCriadoEm();
        this.atualizadoEm = contato.getAtualizadoEm();
        this.cliente = contato.getCliente();

    }
}

