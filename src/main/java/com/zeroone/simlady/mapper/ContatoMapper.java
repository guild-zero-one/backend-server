package com.zeroone.simlady.mapper;

import com.zeroone.simlady.dto.ContatoDTO;
import com.zeroone.simlady.entity.Contato;
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
