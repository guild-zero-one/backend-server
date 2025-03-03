package ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Fornecedor;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {
    public ProdutoDTO toDTO(Produto produto){
        return new ProdutoDTO(produto);
    }

    public Produto toEntity(ProdutoDTO produtoDTO, Fornecedor fornecedor){
        Produto produto = new Produto();
        produto.setId(produtoDTO.getId());
        produto.setNomeNf(produtoDTO.getNomeNf());
        produto.setNomeFantasia(produtoDTO.getNomeFantasia());
        produto.setFornecedor(fornecedor);
        produto.setCriadoEm(produtoDTO.getCriadoEm());
        produto.setAtualizadoEm(produtoDTO.getAtualizadoEm());
        return produto;
    }
}
