package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.fornecedor.FornecedorComProdutosResponseDto;
import com.zeroone.simlady.dto.produto.ProdutoResponseDto;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.entity.Fornecedor;
import com.zeroone.simlady.mapper.FornecedorMapper;
import com.zeroone.simlady.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FornecedorService {
    private final FornecedorRepository fornecedorRepository;

    private final ProdutoService produtoService;

    private final FornecedorMapper fornecedorMapper;

    public Fornecedor cadastrarFornecedor(Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
    }

    public Page<Fornecedor> listar(Pageable pageable) {
        return fornecedorRepository.findAll(pageable);
    }

    public void excluirPorId(Integer id) {
        fornecedorRepository.deleteById(id);
    }

    public Fornecedor atualizar(Integer id, Fornecedor fornecedor){
       Fornecedor fornecedorBuscado = buscarPorId(id);
       fornecedorBuscado.setNome(fornecedor.getNome());
       return fornecedorRepository.save(fornecedorBuscado);
    }

    public Fornecedor buscarPorId(Integer id){
        return fornecedorRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Fornecedor Não Encontrado"));
    }

    public Page<FornecedorComProdutosResponseDto> listarFornecedoresComProdutos(Pageable pageable) {
        Page<Fornecedor> fornecedores = fornecedorRepository.findAll(pageable);

        return fornecedores.map(fornecedor -> {
            List<ProdutoResponseDto> produtos = produtoService.listarProdutosPorFornecedor(fornecedor.getId());
            return fornecedorMapper.toFornecedorComProdutosResponseDto(fornecedor, produtos);
        });
    }
}
