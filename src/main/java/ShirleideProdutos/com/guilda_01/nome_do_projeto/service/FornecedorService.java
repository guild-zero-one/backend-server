package ShirleideProdutos.com.guilda_01.nome_do_projeto.service;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.FornecedorDTO;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.exception.ResourceNotFoundException;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.FornecedorMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Fornecedor;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public FornecedorDTO atualizar(Integer id, Fornecedor fornecedor){
       Fornecedor fornecedorBuscado = buscarPorId(id);
       fornecedor.setId(id);
       Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);
       return FornecedorMapper.toDTO(fornecedorSalvo);
    }

    public Fornecedor buscarPorId(Integer id){
        return fornecedorRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Fornecedor Não Encontrado"));
    }
}
