package ShirleideProdutos.com.guilda_01.nome_do_projeto.service;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.FornecedorDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.FornecedorMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.ProdutoMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Produto;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FornecedorService fornecedorService;

    // Método para salvar a partir de um DTO
    public ProdutoDTO save(ProdutoDTO produtoDTO) {
        Produto produto = ProdutoMapper.toEntity(produtoDTO);
        FornecedorDTO fornecedor = fornecedorService.findById(produtoDTO.getFornecedorId());
        produto.setFornecedor(FornecedorMapper.toEntity(fornecedor));
        produto = save(produto);
        return ProdutoMapper.toDTO(produto);
    }

    // Método para salvar a partir de uma entidade
    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }

    public List<ProdutoDTO> findAll() {
        return produtoRepository.findAll().stream()
                .map(ProdutoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProdutoDTO findById(Integer id) {
        return produtoRepository.findById(id)
                .map(ProdutoMapper::toDTO)
                .orElse(null);
    }

    public void deleteById(Integer id) {
        produtoRepository.deleteById(id);
    }
}
