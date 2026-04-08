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
import java.util.UUID;

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

    public Produto buscarPorId(UUID id) {
        return produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não Encontrado"));
    }

    public Produto buscarProdutoPorSku(String sku) {
        return produtoRepository.findProdutoBySkuIgnoreCase(sku);
    }

    public List<Produto> buscarPorFornecedor(UUID id) {
        return produtoRepository.findByFornecedorId(id);
    }

    public void excluirPorId(UUID id) {
        produtoRepository.deleteById(id);
    }

    public Produto atualizar(UUID id, Produto produto) {
        Produto produtoBuscado = buscarPorId(id);

        if (produto.getNome() != null) {
            produtoBuscado.setNome(produto.getNome());
        }
        if (produto.getSku() != null) {
            produtoBuscado.setSku(produto.getSku());
        }
        if (produto.getDescricao() != null) {
            produtoBuscado.setDescricao(produto.getDescricao());
        }
        if (produto.getQuantidade() != null) {
            produtoBuscado.setQuantidade(produto.getQuantidade());
        }
        if (produto.getUrlImagem() != null) {
            produtoBuscado.setUrlImagem(produto.getUrlImagem());
        }
        if (produto.getPrecoUnitario() != null) {
            produtoBuscado.setPrecoUnitario(produto.getPrecoUnitario());
        }
        if (produto.getCatalogo() != null) {
            produtoBuscado.setCatalogo(produto.getCatalogo());
        }
        if (produto.getValorVenda() != null) {
            produtoBuscado.setValorVenda(produto.getValorVenda());
        }
        if (produto.getFornecedor() != null) {
            produtoBuscado.setFornecedor(produto.getFornecedor());
        }
        return produtoRepository.save(produtoBuscado);
    }

    public List<ProdutoResponseDto> listarProdutosPorFornecedor(UUID fornecedorId) {
        List<Produto> produtos = produtoRepository.findByFornecedorId(fornecedorId);
        return produtos.stream()
                .map(produtoMapper::toResponseDto)
                .toList();
    }

    public List<Produto> buscarListaPorId(List<UUID> ids) {
        return produtoRepository.findAllById(ids);
    }
}
