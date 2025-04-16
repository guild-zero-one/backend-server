package com.zeroone.simlady.service;

import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.entity.Fornecedor;
import com.zeroone.simlady.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FornecedorService {
    private final FornecedorRepository fornecedorRepository;

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
}
