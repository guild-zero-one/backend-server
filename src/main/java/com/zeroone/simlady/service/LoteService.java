package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.loteItem.LoteItemRequestDto;
import com.zeroone.simlady.entity.LoteItem;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.entity.Lote;
import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.repository.LoteItemRepository;
import com.zeroone.simlady.repository.LoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
}
