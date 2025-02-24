package ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ClienteDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ContatoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Cliente;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Contato;
import org.springframework.stereotype.Component;

@Component
public class ContatoMapper {

    public ContatoDTO toDto(Contato contato) {
        return new ContatoDTO(
                contato.getId(), contato.getCelular(), contato.getCriadoEm(), contato.getAtualizadoEm(), contato.getClienteId()
        );
    }

    public Contato toEntity(ContatoDTO contatoDTO) {
        return new Contato(contatoDTO.getId(),
                contatoDTO.getCelular(),
                contatoDTO.getCriadoEm(),
                contatoDTO.getAtualizadoEm(),
                contatoDTO.getClienteId());
    }

}
