package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.FornecedorDTO;

import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.mapper.FornecedorMapper;
import com.zeroone.simlady.entity.Fornecedor;
import com.zeroone.simlady.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;


    public FornecedorDTO cadastrarFornecedor(FornecedorDTO fornecedorDTO) {
        Fornecedor fornecedor = FornecedorMapper.toEntity(fornecedorDTO);
        fornecedor = cadastrarFornecedor(fornecedor);
        return FornecedorMapper.toDTO(fornecedor);
    }


    public Fornecedor cadastrarFornecedor(Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
    }

    public List<FornecedorDTO> listar() {
        return fornecedorRepository.findAll().stream()
                .map(FornecedorMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void excluirPorId(Integer id) {
        fornecedorRepository.deleteById(id);
    }

    public FornecedorDTO atualizar(Integer id, FornecedorDTO fornecedorDTO){
       Fornecedor fornecedorBuscado = buscarPorId(id);
       fornecedorBuscado.setNome(fornecedorDTO.getNome());
       Fornecedor fornecedorAtualizado = fornecedorRepository.save(fornecedorBuscado);
       return FornecedorMapper.toDTO(fornecedorAtualizado);
    }

    public Fornecedor buscarPorId(Integer id){
        return fornecedorRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Fornecedor Não Encontrado"));
    }
}
