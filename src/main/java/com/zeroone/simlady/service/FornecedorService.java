package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.fornecedor.FornecedorComProdutosResponseDto;
import com.zeroone.simlady.dto.produto.ProdutoResponseDto;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.entity.Fornecedor;
import com.zeroone.simlady.mapper.FornecedorMapper;
import com.zeroone.simlady.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
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

    public List<Fornecedor> listar() {
        return fornecedorRepository.findAll();
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

    public List<FornecedorComProdutosResponseDto> listarFornecedoresComProdutos() {
        List<Fornecedor> fornecedores = fornecedorRepository.findAll();

        return fornecedores.stream()
                .map(fornecedor -> {
                    List<ProdutoResponseDto> produtos = produtoService.listarProdutosPorFornecedor(fornecedor.getId());
                    return fornecedorMapper.toFornecedorComProdutosResponseDto(fornecedor, produtos);
                })
                .toList();
    }
}
