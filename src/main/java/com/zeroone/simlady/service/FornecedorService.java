package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.fornecedor.FornecedorComProdutosResponseDto;
import com.zeroone.simlady.dto.fornecedor.FornecedorResponseDto;
import com.zeroone.simlady.dto.produto.ProdutoResponseDto;
import com.zeroone.simlady.entity.Fornecedor;
import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.exception.ResourceAlreadyExistsException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.FornecedorMapper;
import com.zeroone.simlady.mapper.ProdutoMapper;
import com.zeroone.simlady.repository.FornecedorRepository;
import com.zeroone.simlady.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;
    private final FornecedorMapper fornecedorMapper;
    private final ProdutoMapper produtoMapper;

    public Fornecedor cadastrarFornecedor(Fornecedor fornecedor) {
        // Normalizar nome para evitar duplicatas (case insensitive)
        String nomeNormalizado = fornecedor.getNome().trim().toLowerCase();

        fornecedorRepository.findByNomeIgnoreCase(nomeNormalizado)
                .ifPresent(f -> {
                    throw new ResourceAlreadyExistsException("Fornecedor com este nome já existe");
                });

        fornecedor.setNome(nomeNormalizado);
        return fornecedorRepository.save(fornecedor);
    }

    public Page<Fornecedor> listar(Pageable pageable) {
        return fornecedorRepository.findAll(pageable);
    }

    public void excluirPorId(UUID id) {
        fornecedorRepository.deleteById(id);
    }

    public Fornecedor atualizar(UUID id, Fornecedor fornecedor) {
        Fornecedor fornecedorBuscado = buscarPorId(id);

        if (fornecedor.getNome() != null) {
            String nomeNormalizado = fornecedor.getNome().trim().toLowerCase();

            // Verificar duplicata apenas se o nome foi alterado
            if (!nomeNormalizado.equals(fornecedorBuscado.getNome())) {
                fornecedorRepository.findByNomeIgnoreCase(nomeNormalizado)
                        .ifPresent(f -> {
                            throw new ResourceAlreadyExistsException("Fornecedor com este nome já existe");
                        });
            }

            fornecedorBuscado.setNome(nomeNormalizado);
        }
        if (fornecedor.getCnpj() != null) {
            fornecedorBuscado.setCnpj(fornecedor.getCnpj());
        }
        if (fornecedor.getDescricao() != null) {
            fornecedorBuscado.setDescricao(fornecedor.getDescricao());
        }
        if (fornecedor.getImagemUrl() != null) {
            fornecedorBuscado.setImagemUrl(fornecedor.getImagemUrl());
        }

        return fornecedorRepository.save(fornecedorBuscado);
    }

    public Fornecedor buscarPorId(UUID id) {
        return fornecedorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Fornecedor Não Encontrado"));
    }

    public Fornecedor buscarPorNomeExato(String nome) {
        String nomeNormalizado = nome.trim().toLowerCase();
        return fornecedorRepository.findByNomeIgnoreCase(nomeNormalizado)
                .orElse(null);
    }

    public Page<Fornecedor> buscarPorNome(String nome, Pageable pageable) {
        String nomeNormalizado = nome.trim();
        return fornecedorRepository.findByNomeContainingIgnoreCase(nomeNormalizado, pageable);
    }

    public Page<FornecedorComProdutosResponseDto> listarFornecedoresComProdutos(Pageable pageable) {
        Page<Fornecedor> fornecedores = fornecedorRepository.findAll(pageable);

        return fornecedores.map(fornecedor -> {
            List<Produto> produtos = produtoRepository.findByFornecedorId(fornecedor.getId());
            List<ProdutoResponseDto> produtosDto = produtos.stream()
                    .map(produtoMapper::toResponseDto)
                    .toList();
            return fornecedorMapper.toFornecedorComProdutosResponseDto(fornecedor, produtosDto);
        });
    }

    public FornecedorResponseDto toResponseDtoComTotalProdutos(Fornecedor fornecedor) {
        FornecedorResponseDto dto = fornecedorMapper.toResponseDto(fornecedor);
        Long totalProdutos = produtoRepository.countByFornecedorId(fornecedor.getId());
        dto.setTotalProdutos(totalProdutos);
        return dto;
    }
}
