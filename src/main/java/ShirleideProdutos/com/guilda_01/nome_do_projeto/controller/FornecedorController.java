package ShirleideProdutos.com.guilda_01.nome_do_projeto.controller;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.FornecedorDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.exception.ResourceNotFoundException;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.FornecedorMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Fornecedor;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    @GetMapping
    public ResponseEntity<List<FornecedorDTO>> listar() {
        List<FornecedorDTO>fornecedores = fornecedorService.listar();
        if (fornecedores.isEmpty()){
            throw new ResourceNotFoundException("Fornecedores não encontrados");
        }
        return ResponseEntity.ok(fornecedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorDTO> buscarPorId(@PathVariable Integer id) {
        Fornecedor fornecedor = fornecedorService.buscarPorId(id);
        if(fornecedor == null){
            throw new ResourceNotFoundException("Fornecedor não encontrado");
        }
        return ResponseEntity.ok(FornecedorMapper.toDTO(fornecedor));
    }

    @PostMapping
    public ResponseEntity<FornecedorDTO> cadastrarFornecedor(@RequestBody FornecedorDTO fornecedorDTO) {
        fornecedorDTO.setId(null);
        return ResponseEntity.ok(fornecedorService.cadastrarFornecedor(fornecedorDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPorId(@PathVariable Integer id) {
        fornecedorService.excluirPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedorDTO> atualizar(@PathVariable Integer id, @RequestBody Fornecedor fornecedor){
        return ResponseEntity.ok(fornecedorService.atualizar(id,fornecedor));
    }
}
