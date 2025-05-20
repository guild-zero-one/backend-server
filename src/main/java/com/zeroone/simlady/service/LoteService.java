package com.zeroone.simlady.service;

import com.zeroone.simlady.entity.LoteItem;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.entity.Lote;
import com.zeroone.simlady.repository.LoteItemRepository;
import com.zeroone.simlady.repository.LoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoteService {

    private final LoteRepository loteRepository;
    private final LoteItemRepository loteItemRepository;
    private final ProdutoService produtoService;

    public Page<Lote> listar(Pageable pageable) {
        return loteRepository.findAll(pageable);
    }

    public Lote buscarPorId(Integer id) {
        return loteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lote não encontrado"));
    }

    public Lote cadastrarLote(Lote lote) {
        return loteRepository.save(lote);
    }

    public List<LoteItem> cadastrarLoteItem(List<LoteItem> loteItem) {
        return loteItemRepository.saveAll(loteItem);
    }

    public Lote atualizarLote(Integer id, Lote lote) {
        Lote loteExistente = buscarPorId(id);
        lote.setId(loteExistente.getId());
        return loteRepository.save(lote);
    }

    public List<LoteItem> atualizarLoteItem(Integer id, List<LoteItem> loteItemsAtualizados) {
        List<LoteItem> loteItemsExistentes = loteItemRepository.findAllByLoteId(id);
        if (loteItemsExistentes.isEmpty()) {
            throw new ResourceNotFoundException("Lote Item não encontrado");
        }

        return loteItemsExistentes.stream()
                .map(itemExistente -> {
                    // Procura o item atualizado correspondente pelo ID
                    return loteItemsAtualizados.stream()
                            .filter(itemAtualizado -> itemAtualizado.getId().equals(itemExistente.getId()))
                            .findFirst()
                            .map(itemAtualizado -> {
                                // Atualiza apenas os campos não nulos
                                if (itemAtualizado.getQtdLoteCompra() != null) {
                                    itemExistente.setQtdLoteCompra(itemAtualizado.getQtdLoteCompra());
                                }
                                if (itemAtualizado.getValorUnitarioCompra() != null) {
                                    itemExistente.setValorUnitarioCompra(itemAtualizado.getValorUnitarioCompra());
                                }
                                if (itemAtualizado.getDataValidade() != null) {
                                    itemExistente.setDataValidade(itemAtualizado.getDataValidade());
                                }
                                if (itemAtualizado.getProduto() != null) {
                                    itemExistente.setProduto(itemAtualizado.getProduto());
                                }
                                if (itemAtualizado.getLote() != null) {
                                    itemExistente.setLote(itemAtualizado.getLote());
                                }
                                return itemExistente;
                            })
                            .orElse(itemExistente); // Mantém o item original se não houver atualização
                })
                .map(loteItemRepository::save)
                .toList();
    }

    public void deletarLote(Integer id) {
        Lote lote = buscarPorId(id);
        loteRepository.delete(lote);
    }
}
