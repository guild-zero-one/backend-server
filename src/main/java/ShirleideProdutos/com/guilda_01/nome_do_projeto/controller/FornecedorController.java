package ShirleideProdutos.com.guilda_01.nome_do_projeto.controller;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ClienteDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.FornecedorDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.FornecedorMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Fornecedor;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    @Autowired
    FornecedorService fornecedorService;

    @PostMapping
    public ResponseEntity<FornecedorDTO> cadastrarFornecedores(@RequestBody Fornecedor fornecedor){
        return new ResponseEntity<>(fornecedorService.cadastrarFornecedor(fornecedor), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FornecedorDTO>> listarFornecedores(){
        List<FornecedorDTO> fornecedores = fornecedorService.listarFornecedores();
        if(fornecedores.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(fornecedores, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorDTO> buscarFornecedorPorId(@PathVariable Integer id){
        return ResponseEntity.ok(fornecedorService.buscarFornecedorPorId(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FornecedorDTO> atualizarFornecedor(@PathVariable Integer id, @RequestBody FornecedorDTO fornecedorDTO) {
        return ResponseEntity.ok(fornecedorService.atualizarFornecedor(id, fornecedorDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirFornecedor(@PathVariable Integer id){
        fornecedorService.excluirFornecedor(id);
        return ResponseEntity.noContent().build();
    }
}
