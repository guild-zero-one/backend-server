package ShirleideProdutos.com.guilda_01.nome_do_projeto.service;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.LoteProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.LoteProdutoMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.ProdutoMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.LoteProduto;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.LoteProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoteProdutoService {

    @Autowired
    private LoteProdutoRepository loteProdutoRepository;

    @Autowired
    private ProdutoService produtoService;

    // Método para salvar a partir de um DTO
    public LoteProdutoDTO save(LoteProdutoDTO loteProdutoDTO) {
        LoteProduto loteProduto = LoteProdutoMapper.toEntity(loteProdutoDTO);
        ProdutoDTO produto = produtoService.findById(loteProdutoDTO.getProdutoId());
        loteProduto.setProduto(ProdutoMapper.toEntity(produto));
        loteProduto = save(loteProduto);
        return LoteProdutoMapper.toDTO(loteProduto);
    }

    // Método para salvar a partir de uma entidade
    public LoteProduto save(LoteProduto loteProduto) {
        return loteProdutoRepository.save(loteProduto);
    }

    public List<LoteProdutoDTO> findAll() {
        return loteProdutoRepository.findAll().stream()
                .map(LoteProdutoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public LoteProdutoDTO findById(Integer id) {
        return loteProdutoRepository.findById(id)
                .map(LoteProdutoMapper::toDTO)
                .orElse(null);
    }

    public void deleteById(Integer id) {
        loteProdutoRepository.deleteById(id);
    }
}
