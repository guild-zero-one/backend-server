package com.zeroone.simlady.service;

import com.zeroone.simlady.entity.ProdutoImagem;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.ProdutoImagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoImagemService {
    private final ProdutoImagemRepository produtoImagemRepository;

    public ProdutoImagem cadastrarImagem(ProdutoImagem produtoImagem) {
        return produtoImagemRepository.save(produtoImagem);
    }

    public List<ProdutoImagem> listarImagens() {
        return produtoImagemRepository.findAll();
    }

    public ProdutoImagem buscarImagemPorId(Integer id) {
        return produtoImagemRepository.findById(id).orElse(null);
    }

    public ProdutoImagem atualizarImagem(Integer id, ProdutoImagem produtoImagemAtualizado) {
        ProdutoImagem produtoImagemExistente = buscarImagemPorId(id);
        if (produtoImagemExistente != null) {
            produtoImagemAtualizado.setId(id);
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
