package ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.LoteProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.LoteProduto;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class LoteProdutoMapper {
    public LoteProdutoDTO toDTO(LoteProduto loteProduto){
        LoteProdutoDTO loteProdutoDTO = new LoteProdutoDTO();
        loteProdutoDTO.setId(loteProduto.getId());
        loteProdutoDTO.setQtdLote(loteProduto.getQtdLote());
        loteProdutoDTO.setValorUnitario(loteProduto.getValorUnitario());
        loteProdutoDTO.setDataValidade(loteProduto.getDataValidade());
        loteProdutoDTO.setCriadoEm(loteProduto.getCriadoEm());
        loteProdutoDTO.setAtualizadoEm(loteProduto.getAtualizadoEm());
        loteProdutoDTO.setProdutoId(loteProduto.getProduto().getId());
        return loteProdutoDTO;
    }

    public LoteProduto toEntity(LoteProdutoDTO loteProdutoDTO, Produto produto) {
        LoteProduto loteProdutos = new LoteProduto();
        loteProdutos.setId(loteProdutoDTO.getId());
        loteProdutos.setQtdLote(loteProdutoDTO.getQtdLote());
        loteProdutos.setValorUnitario(loteProdutoDTO.getValorUnitario());
        loteProdutos.setDataValidade(loteProdutoDTO.getDataValidade());
        loteProdutos.setCriadoEm(loteProdutoDTO.getCriadoEm());
        loteProdutos.setAtualizadoEm(loteProdutoDTO.getAtualizadoEm());
        loteProdutos.setProduto(produto);
        return loteProdutos;
    }

}
