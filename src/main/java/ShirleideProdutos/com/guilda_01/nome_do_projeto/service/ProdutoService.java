package ShirleideProdutos.com.guilda_01.nome_do_projeto.service;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.FornecedorDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.exception.ResourceNotFoundException;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.FornecedorMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.ProdutoMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Fornecedor;
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

    public ProdutoDTO cadastrarProduto(ProdutoDTO produtoDTO) {
        Produto produto = ProdutoMapper.toEntity(produtoDTO);
        Fornecedor fornecedor = fornecedorService.buscarPorId(produtoDTO.getFornecedorId());
        produto.setFornecedor(fornecedor);
        produto = cadastrarProduto(produto);
        return ProdutoMapper.toDTO(produto);
    }

    public Produto cadastrarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public List<ProdutoDTO> listar() {
        return produtoRepository.findAll().stream()
                .map(ProdutoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Produto buscarPorId(Integer id) {
        return produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não Encontrado"));
    }

    public void excluirPorId(Integer id) {
        produtoRepository.deleteById(id);
    }

    public ProdutoDTO atualizar(Integer id, Produto produto){
        Produto produtoBuscado = buscarPorId(id);
        produto.setId(id);
        Produto produtoSalvo = produtoRepository.save(produto);
        return ProdutoMapper.toDTO(produtoSalvo);
    }
}
