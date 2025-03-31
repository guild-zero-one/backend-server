package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.LoteProdutoDTO;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.LoteProdutoMapper;
import com.zeroone.simlady.entity.LoteProduto;
import com.zeroone.simlady.entity.Produto;
import com.zeroone.simlady.repository.LoteProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoteProdutoService {

    @Autowired
    private LoteProdutoRepository loteProdutoRepository;

    @Autowired
    private ProdutoService produtoService;

    public LoteProdutoDTO cadastrarLote(LoteProdutoDTO loteProdutoDTO) {
        LoteProduto loteProduto = LoteProdutoMapper.toEntity(loteProdutoDTO);
        Produto produto = produtoService.buscarPorId(loteProdutoDTO.getProdutoId());
        loteProduto.setProduto(produto);
        loteProduto = cadastrarLote(loteProduto);
        return LoteProdutoMapper.toDTO(loteProduto);
    }

    public LoteProduto cadastrarLote(LoteProduto loteProduto) {
        return loteProdutoRepository.save(loteProduto);
    }

    public List<LoteProdutoDTO> listar() {
        return loteProdutoRepository.findAll().stream()
                .map(LoteProdutoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public LoteProduto buscarPorId(Integer id) {
        return loteProdutoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Fornecedor Não Encontrado"));
    }

    public void excluirPorId(Integer id) {
        loteProdutoRepository.deleteById(id);
    }

    public LoteProdutoDTO atualizar(Integer id, LoteProdutoDTO loteProdutoDTO){
        LoteProduto loteBuscado = buscarPorId(id);
        loteBuscado.setQtdLote(loteProdutoDTO.getQtdLote());
        loteBuscado.setValorUnitCompra(loteProdutoDTO.getValorUnitCompra());
        LoteProduto loteSalvo = loteProdutoRepository.save(loteBuscado);
        return LoteProdutoMapper.toDTO(loteSalvo);
    }
}
