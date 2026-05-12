package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.produto.ProdutoResponseDto;
import com.zeroone.simlady.entity.Categoria;
import com.zeroone.simlady.entity.Fornecedor;
import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.exception.ResourceAlreadyExistsException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.ProdutoMapper;
import com.zeroone.simlady.repository.CategoriaRepository;
import com.zeroone.simlady.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoMapper produtoMapper;
    private final FornecedorService fornecedorService;

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

    public Page<Produto> listarPorFornecedor(UUID fornecedorId, Pageable pageable) {
        return produtoRepository.findByFornecedorId(fornecedorId, pageable);
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

    public Page<Produto> listarPorFornecedorComFiltro(UUID fornecedorId, String nome, Pageable pageable) {
        String nomeNormalizado = nome != null ? nome.trim() : "";
        return produtoRepository.findByFornecedorIdAndNomeContainingIgnoreCase(fornecedorId, nomeNormalizado, pageable);
    }

    public Page<Produto> listarPorCategoria(String categoriaNome, Pageable pageable) {
        String categoriaNormalizado = categoriaNome.trim();
        return produtoRepository.findByCategorias_NomeContainingIgnoreCase(categoriaNormalizado, pageable);
    }

    public Long contarPedidosDistintosPorProduto(UUID produtoId) {
        buscarPorId(produtoId); // Validar que produto existe
        Long count = produtoRepository.countDistinctPedidosByProdutoId(produtoId);
        return count != null ? count : 0L;
    }

    @Transactional
    public void atualizarImageUrl(UUID id, String url) {
        Produto produto = buscarPorId(id);
        produto.setUrlImagem(url);
        produtoRepository.save(produto);
    }

    @Transactional
    public void associarCategoria(UUID produtoId, UUID categoriaId) {
        Produto produto = buscarPorId(produtoId);
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        if (produto.getCategorias().contains(categoria)) {
            throw new ResourceAlreadyExistsException("Categoria já associada a este produto");
        }

        produto.getCategorias().add(categoria);
        produtoRepository.save(produto);
    }

    @Transactional
    public void desassociarCategoria(UUID produtoId, UUID categoriaId) {
        Produto produto = buscarPorId(produtoId);
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        if (!produto.getCategorias().contains(categoria)) {
            throw new ResourceNotFoundException("Categoria não está associada a este produto");
        }

        produto.getCategorias().remove(categoria);
        produtoRepository.save(produto);
    }

    @Transactional
    public com.zeroone.simlady.dto.produto.ProdutoLoteResponseDto cadastrarEmLote(
            List<com.zeroone.simlady.dto.produto.ProdutoLoteRequestDto> produtosDto) {

        List<com.zeroone.simlady.dto.produto.ProdutoResponseDto> produtosCriados = new ArrayList<>();
        List<com.zeroone.simlady.dto.produto.ProdutoLoteResponseDto.ProdutoErroResponseDto> produtosErro = new ArrayList<>();

        for (com.zeroone.simlady.dto.produto.ProdutoLoteRequestDto produtoDto : produtosDto) {
            try {
                // Buscar ou criar o fornecedor
                String fornecedorNome = produtoDto.getFornecedorNome().trim().toLowerCase();
                Fornecedor fornecedor = fornecedorService.buscarPorNomeExato(produtoDto.getFornecedorNome());

                if (fornecedor == null) {
                    // Criar novo fornecedor
                    fornecedor = new Fornecedor();
                    fornecedor.setNome(fornecedorNome);
                    fornecedor.setCnpj(produtoDto.getFornecedorCnpj());
                    fornecedor.setDescricao(produtoDto.getFornecedorDescricao());
                    fornecedor.setImagemUrl(produtoDto.getFornecedorImagemUrl());
                    fornecedor = fornecedorService.cadastrarFornecedor(fornecedor);
                }

                // Criar produto
                Produto produto = new Produto();
                produto.setNome(produtoDto.getNome());
                produto.setSku(produtoDto.getSku());
                produto.setDescricao(produtoDto.getDescricao());
                produto.setQuantidade(produtoDto.getQuantidade());
                produto.setUrlImagem(produtoDto.getUrlImagem());
                produto.setPrecoUnitario(produtoDto.getPrecoUnitario());
                produto.setCatalogo(produtoDto.getCatalogo());
                produto.setValorVenda(produtoDto.getValorVenda());
                produto.setFornecedor(fornecedor);

                Produto produtoSalvo = produtoRepository.save(produto);
                produtosCriados.add(produtoMapper.toResponseDto(produtoSalvo));

            } catch (Exception e) {
                produtosErro.add(
                    new com.zeroone.simlady.dto.produto.ProdutoLoteResponseDto.ProdutoErroResponseDto(
                        produtoDto.getNome(),
                        "Erro ao processar produto: " + e.getMessage()
                    )
                );
            }
        }

        return new com.zeroone.simlady.dto.produto.ProdutoLoteResponseDto(
            produtosDto.size(),
            produtosCriados.size(),
            produtosErro.size(),
            produtosCriados,
            produtosErro
        );
    }
}
