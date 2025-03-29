package ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.dto.ContatoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.entity.Contato;
import org.springframework.stereotype.Component;

@Component
public class ContatoMapper {

    public ContatoDTO toDto(Contato contato) {
        return new ContatoDTO(
                contato.getId(), contato.getCelular(), contato.getCriadoEm(), contato.getAtualizadoEm()
        );
    }

    public Contato toEntity(ContatoDTO contatoDTO) {
        Contato contato = new Contato();

        contato.setId(contatoDTO.getId());
        contato.setCelular(contatoDTO.getCelular());
        contato.setCriadoEm(contatoDTO.getCriadoEm());
        contato.setAtualizadoEm(contatoDTO.getAtualizadoEm());

        return contato;
    }

}
