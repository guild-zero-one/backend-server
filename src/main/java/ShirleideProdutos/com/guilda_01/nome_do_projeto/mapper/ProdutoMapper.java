package ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Produto;

public class ProdutoMapper {

    public static ProdutoDTO toDTO(Produto produto) {
        if (produto == null) {
            return null;
        }

        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setNomeFantasia(produto.getNomeFantasia());
        dto.setFornecedorId(produto.getFornecedor().getId());
        dto.setCriadoEm(produto.getCriadoEm());
        dto.setAtualizadoEm(produto.getAtualizadoEm());

        return dto;
    }

    public static Produto toEntity(ProdutoDTO dto) {
        if (dto == null) {
            return null;
        }

        Produto produto = new Produto();
        produto.setId(dto.getId());
        produto.setNome(dto.getNome());
        produto.setNomeFantasia(dto.getNomeFantasia());
        produto.setCriadoEm(dto.getCriadoEm());
        produto.setAtualizadoEm(dto.getAtualizadoEm());

        return produto;
    }
}