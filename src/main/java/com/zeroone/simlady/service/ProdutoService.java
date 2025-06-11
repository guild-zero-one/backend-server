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

    public Boolean buscarPorNome(String nome) {
        return produtoRepository.existsProdutoByNomeContainingIgnoreCase(nome);
    }

    public List<Produto> buscarPorFornecedor(Integer id) {
        return produtoRepository.findByFornecedorId(id);
    }

    public void excluirPorId(Integer id) {
        produtoRepository.deleteById(id);
    }

    public Produto atualizar(Integer id, Produto produto) {
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
        if (produto.getTag() != null) {
            produtoBuscado.setTag(produto.getTag());
        }
        if (produto.getQuantidade() != null) {
            produtoBuscado.setQuantidade(produto.getQuantidade());
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

        return produtoRepository.save(produtoBuscado);
    }

    public List<ProdutoResponseDto> listarProdutosPorFornecedor(Integer fornecedorId) {
        List<Produto> produtos = produtoRepository.findByFornecedorId(fornecedorId);
        return produtos.stream()
                .map(produtoMapper::toResponseDto)
                .toList();
    }

    public List<Produto> buscarListaPorId(List<Integer> ids) {
        return produtoRepository.findAllById(ids);
    }
}
