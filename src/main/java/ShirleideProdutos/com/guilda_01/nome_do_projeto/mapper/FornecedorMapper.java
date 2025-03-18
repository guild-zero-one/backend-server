package ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.FornecedorDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Fornecedor;

public class FornecedorMapper {

    public static FornecedorDTO toDTO(Fornecedor fornecedor) {
        if (fornecedor == null) {
            return null;
        }

        FornecedorDTO dto = new FornecedorDTO();
        dto.setId(fornecedor.getId());
        dto.setNome(fornecedor.getNome());
        dto.setCnpj(fornecedor.getCnpj());
        dto.setCriadoEm(fornecedor.getCriadoEm());
        dto.setAtualizadoEm(fornecedor.getAtualizadoEm());

        return dto;
    }

    public static Fornecedor toEntity(FornecedorDTO dto) {
        if (dto == null) {
            return null;
        }

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(dto.getId());
        fornecedor.setNome(dto.getNome());
        fornecedor.setCnpj(dto.getCnpj());
        fornecedor.setCriadoEm(dto.getCriadoEm());
        fornecedor.setAtualizadoEm(dto.getAtualizadoEm());

        return fornecedor;
    }
}