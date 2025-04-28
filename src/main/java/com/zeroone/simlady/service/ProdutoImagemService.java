package com.zeroone.simlady.service;

import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.entity.ProdutoImagem;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.ProdutoImagemRepository;
import com.zeroone.simlady.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoImagemService {
    private final ProdutoImagemRepository produtoImagemRepository;

    private final ProdutoRepository produtoRepository;

    public ProdutoImagem cadastrarImagem(ProdutoImagem produtoImagem) {
        if (produtoImagem.getProduto() != null && produtoImagem.getProduto().getId() != null) {
            Produto produtoPersistido = produtoRepository.findById(produtoImagem.getProduto().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + produtoImagem.getProduto().getId()));
            produtoImagem.setProduto(produtoPersistido);
        }
        return produtoImagemRepository.save(produtoImagem);
    }

    public List<ProdutoImagem> listarImagens() {
        return produtoImagemRepository.findAll();
    }

    public ProdutoImagem buscarImagemPorId(Integer id) {
        return produtoImagemRepository.findById(id).orElse(null);
    }

    public List<ProdutoImagem> buscarPorProduto(Integer id) { return produtoImagemRepository.findByProdutoId(id); }

    public ProdutoImagem atualizarImagem(Integer id, ProdutoImagem produtoImagemAtualizado) {
        ProdutoImagem produtoImagemExistente = buscarImagemPorId(id);
        if (produtoImagemExistente != null) {
            Produto produtoPersistido = produtoRepository.findById(produtoImagemAtualizado.getProduto().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + produtoImagemAtualizado.getProduto().getId()));

            produtoImagemAtualizado.setDataCriacao(produtoImagemExistente.getDataCriacao());
            produtoImagemAtualizado.setDataAtualizacao(produtoImagemExistente.getDataAtualizacao());

            produtoImagemAtualizado.setId(id);
            produtoImagemAtualizado.setProduto(produtoPersistido);

            return produtoImagemRepository.save(produtoImagemAtualizado);
        }
        throw new ResourceNotFoundException("Imagem não encontrada com o ID: " + id);
    }

    public void deletarImagem(Integer id) {
        ProdutoImagem produtoImagemExistente = buscarImagemPorId(id);
        if (produtoImagemExistente != null) {
            produtoImagemRepository.delete(produtoImagemExistente);
        } else {
            throw new ResourceNotFoundException("Imagem não encontrada com o ID: " + id);
        }
    }
}
