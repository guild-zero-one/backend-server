package ShirleideProdutos.com.guilda_01.nome_do_projeto.service;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.LoteProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.exception.ResourceNotFoundException;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.LoteProdutoMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.LoteProduto;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Produto;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.LoteProdutoRepository;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoteProdutoService {
    @Autowired
    LoteProdutoRepository loteProdutoRepository;
    @Autowired
    LoteProdutoMapper loteProdutoMapper;
    @Autowired
    ProdutoRepository produtoRepository;

    public LoteProdutoDTO adicionarLote(Integer produtoId, LoteProduto loteProduto){
        Produto produto = produtoRepository.findById(produtoId).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));

        loteProduto.setProduto(produto);

        return loteProdutoMapper.toDTO(loteProdutoRepository.save(loteProduto));
    }

    public List<LoteProdutoDTO> listarLotes(){
        return loteProdutoRepository.findAll().stream().map(loteProdutoMapper::toDTO).collect(Collectors.toList());
    }

    public LoteProdutoDTO buscarLotePorId(Integer id){
        LoteProduto possivelLote = loteProdutoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lote não encontrado"));
        return loteProdutoMapper.toDTO(possivelLote);
    }

    public List<LoteProdutoDTO> buscarLotesPorProduto(Integer produtoId) {
        List<LoteProduto> lotesPorProduto = loteProdutoRepository.findByProdutoId(produtoId);
        return lotesPorProduto.stream()
                .map(loteProdutoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
