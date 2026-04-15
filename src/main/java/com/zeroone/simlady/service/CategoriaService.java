package com.zeroone.simlady.service;

import com.zeroone.simlady.entity.Categoria;
import com.zeroone.simlady.exception.ResourceAlreadyExistsException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public Categoria criar(Categoria categoria) {
        // Normalizar nome para evitar duplicatas (case insensitive)
        String nomeNormalizado = categoria.getNome().trim().toLowerCase();

        categoriaRepository.findByNomeIgnoreCase(nomeNormalizado)
                .ifPresent(c -> {
                    throw new ResourceAlreadyExistsException("Categoria com este nome já existe");
                });

        categoria.setNome(nomeNormalizado);
        return categoriaRepository.save(categoria);
    }

    public Page<Categoria> listar(Pageable pageable) {
        return categoriaRepository.findAll(pageable);
    }

    public Categoria buscarPorId(UUID id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
    }

    public Categoria atualizar(UUID id, Categoria categoriaAtualizada) {
        Categoria categoria = buscarPorId(id);

        if (categoriaAtualizada.getNome() != null) {
            String nomeNormalizado = categoriaAtualizada.getNome().trim().toLowerCase();

            // Verificar duplicata apenas se o nome foi alterado
            if (!nomeNormalizado.equals(categoria.getNome())) {
                categoriaRepository.findByNomeIgnoreCase(nomeNormalizado)
                        .ifPresent(c -> {
                            throw new ResourceAlreadyExistsException("Categoria com este nome já existe");
                        });
            }

            categoria.setNome(nomeNormalizado);
        }

        if (categoriaAtualizada.getDescricao() != null) {
            categoria.setDescricao(categoriaAtualizada.getDescricao());
        }

        return categoriaRepository.save(categoria);
    }

    public void deletar(UUID id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria não encontrada");
        }
        categoriaRepository.deleteById(id);
    }

    public Page<Categoria> buscarPorNome(String nome, Pageable pageable) {
        return categoriaRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }
}

