package ShirleideProdutos.com.guilda_01.nome_do_projeto.controller;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.FornecedorDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.exception.ResourceNotFoundException;
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
    public ResponseEntity<List<FornecedorDTO>> findAll() {
        List<FornecedorDTO>fornecedores = fornecedorService.findAll();
        if (fornecedores.isEmpty()){
            throw new ResourceNotFoundException("Fornecedores não encontrados");
        }
        return ResponseEntity.ok(fornecedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorDTO> findById(@PathVariable Integer id) {
        FornecedorDTO fornecedorDTO = fornecedorService.findById(id);
        if(fornecedorDTO == null){
            throw new ResourceNotFoundException("Fornecedor não encontrado");
        }
        return ResponseEntity.ok(fornecedorDTO);
    }

    @PostMapping
    public ResponseEntity<FornecedorDTO> save(@RequestBody FornecedorDTO fornecedorDTO) {
        fornecedorDTO.setId(null);
        return ResponseEntity.ok(fornecedorService.save(fornecedorDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        fornecedorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
