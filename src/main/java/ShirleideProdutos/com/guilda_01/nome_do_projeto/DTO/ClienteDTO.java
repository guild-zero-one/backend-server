package ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Cliente;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Contato;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    private Integer id;
    private String nome;
    private LocalDate criadoEm;
    private LocalDate atualizadoEm;
    private List<Contato> contatos;

    public ClienteDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.criadoEm = cliente.getCriadoEm();
        this.atualizadoEm = cliente.getAtualizadoEm();
        this.contatos = new ArrayList<>();
    }

}