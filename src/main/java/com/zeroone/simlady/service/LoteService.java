package com.zeroone.simlady.service;

import com.zeroone.simlady.entity.LoteItem;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.entity.Lote;
import com.zeroone.simlady.repository.LoteItemRepository;
import com.zeroone.simlady.repository.LoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoteService {
    private final LoteRepository loteRepository;

    private final LoteItemRepository loteItemRepository;

    private final ProdutoService produtoService;

    public List<Lote> listar() {
        return loteRepository.findAll();
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

    public List<LoteItem>atualizarLoteItem(Integer id, List<LoteItem> loteItem) {
        List<LoteItem> loteItemExistente = loteItemRepository.findAllByLoteId(id);
        if (loteItemExistente.isEmpty()) {
            throw new ResourceNotFoundException("Lote Item não encontrado");
        }
        return loteItemRepository.saveAll(loteItem);
    }

    public void deletarLote(Integer id) {
        Lote lote = buscarPorId(id);
        loteRepository.delete(lote);
    }
}
