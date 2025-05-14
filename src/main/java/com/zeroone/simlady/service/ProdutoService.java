package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.produto.ProdutoResponseDto;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.mapper.ProdutoMapper;
import com.zeroone.simlady.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    private final ProdutoMapper produtoMapper;

    public Produto cadastrarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Page<Produto> listar(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }

    public Produto buscarPorId(Integer id) {
        return produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não Encontrado"));
    }

    public List<Produto> buscarPorFornecedor(Integer id) {
        return produtoRepository.findByFornecedorId(id);
    }

    public void excluirPorId(Integer id) {
        produtoRepository.deleteById(id);
    }

    public Produto atualizar(Integer id, Produto produto){
        Produto produtoBuscado = buscarPorId(id);
        produtoBuscado.setNome(produto.getNome());
        produtoBuscado.setSku(produto.getSku());
        produtoBuscado.setDescricao(produto.getDescricao());
        produtoBuscado.setTag(produto.getTag());
        produtoBuscado.setQuantidade(produto.getQuantidade());
        produtoBuscado.setPrecoUnitario(produto.getPrecoUnitario());
        produtoBuscado.setCatalogo(produto.getCatalogo());
        produtoBuscado.setValorVenda(produto.getValorVenda());
        return produtoRepository.save(produtoBuscado);
    }

    public List<ProdutoResponseDto> listarProdutosPorFornecedor(Integer fornecedorId) {
        List<Produto> produtos = produtoRepository.findByFornecedorId(fornecedorId);
        return produtos.stream()
                .map(produtoMapper::toResponseDto)
                .toList();
    }
}
