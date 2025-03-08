package ShirleideProdutos.com.guilda_01.nome_do_projeto.service;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.FornecedorDTO;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.FornecedorMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Fornecedor;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    // Método para salvar a partir de um DTO
    public FornecedorDTO save(FornecedorDTO fornecedorDTO) {
        Fornecedor fornecedor = FornecedorMapper.toEntity(fornecedorDTO);
        fornecedor = save(fornecedor); // Chama o método que salva a entidade
        return FornecedorMapper.toDTO(fornecedor);
    }

    // Método para salvar a partir de uma entidade
    public Fornecedor save(Fornecedor fornecedor) {
        return fornecedorRepository.save(fornecedor);
    }

    public List<FornecedorDTO> findAll() {
        return fornecedorRepository.findAll().stream()
                .map(FornecedorMapper::toDTO)
                .collect(Collectors.toList());
    }

    public FornecedorDTO findById(Integer id) {
        return fornecedorRepository.findById(id)
                .map(FornecedorMapper::toDTO)
                .orElse(null);
    }

    public void deleteById(Integer id) {
        fornecedorRepository.deleteById(id);
    }
}
