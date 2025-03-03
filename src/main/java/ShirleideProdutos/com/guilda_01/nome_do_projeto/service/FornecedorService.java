package ShirleideProdutos.com.guilda_01.nome_do_projeto.service;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.FornecedorDTO;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.exception.ResourceNotFoundException;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.FornecedorMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Cliente;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Fornecedor;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FornecedorService {
    @Autowired
    FornecedorRepository fornecedorRepository;
    @Autowired
    FornecedorMapper fornecedorMapper;

    public FornecedorDTO cadastrarFornecedor(Fornecedor fornecedor){
        Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);
        return fornecedorMapper.toDTO(fornecedorSalvo);
    }

    public List<FornecedorDTO> listarFornecedores(){
        return fornecedorRepository.findAll().stream().map(fornecedorMapper::toDTO).collect(Collectors.toList());
    }

    public FornecedorDTO buscarFornecedorPorId(Integer id){
        return fornecedorMapper.toDTO(buscarFornecedor(id));
    }

    public FornecedorDTO atualizarFornecedor(Integer id, FornecedorDTO fornecedorDTO){
        Fornecedor fornecedor = buscarFornecedor(id);

        fornecedorDTO.setId(fornecedor.getId());
        fornecedor.setNome(fornecedorDTO.getNome());
        fornecedor.setCnpj(fornecedorDTO.getCnpj());

        Fornecedor fornecedorAtualizado = fornecedorRepository.save(fornecedor);
        return fornecedorMapper.toDTO(fornecedorAtualizado);
    }

    public void excluirFornecedor(Integer id){
        Fornecedor fornecedor = buscarFornecedor(id);
        fornecedorRepository.deleteById(id);
    }

    public Fornecedor buscarFornecedor(Integer id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));
    }
}
