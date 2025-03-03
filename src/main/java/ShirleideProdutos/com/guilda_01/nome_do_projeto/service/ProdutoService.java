package ShirleideProdutos.com.guilda_01.nome_do_projeto.service;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.exception.ResourceNotFoundException;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.ProdutoMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Fornecedor;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Produto;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.FornecedorRepository;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    FornecedorService fornecedorService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    public ProdutoDTO cadastrarProduto(ProdutoDTO produtoDTO){
        produtoDTO.setId(null);
        Fornecedor fornecedor = fornecedorService.buscarFornecedor(produtoDTO.getFornecedorId());
        Produto produtoCriado = produtoMapper.toEntity(produtoDTO, fornecedor);
        Produto produtoSalvo = produtoRepository.save(produtoCriado);
        return produtoMapper.toDTO(produtoSalvo);
    }

    public List<ProdutoDTO> listarProdutos(){
        return produtoRepository.findAll().stream().map(produtoMapper::toDTO).collect(Collectors.toList());
    }

    public ProdutoDTO buscarProdutoPorId(Integer id){
        return produtoMapper.toDTO(buscarProduto(id));
    }

    public ProdutoDTO atualizarProduto(Integer id,ProdutoDTO produtoDTO, Fornecedor fornecedor){
        Produto produto = buscarProduto(id);

        produto.setNomeNf(produtoDTO.getNomeNf());
        produto.setNomeFantasia(produtoDTO.getNomeFantasia());
        produto.setFornecedor(fornecedor);

        Produto produtoSalvo = produtoRepository.save(produto);
        return produtoMapper.toDTO(produtoSalvo);
    }

    public void deletarProduto(Integer id){
        if(!produtoRepository.existsById(id)){
            throw new ResourceNotFoundException("Produto não encontrado");
        }
        produtoRepository.deleteById(id);
    }

    private Produto buscarProduto(Integer id) {
        return produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
    }

}
