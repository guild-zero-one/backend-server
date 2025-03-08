package ShirleideProdutos.com.guilda_01.nome_do_projeto.controller;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.LoteProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.exception.ResourceNotFoundException;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.service.LoteProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/lotes")
public class LoteProdutoController {

    @Autowired
    private LoteProdutoService loteProdutoService;

    @GetMapping
    public ResponseEntity<List<LoteProdutoDTO>> findAll() {
        List<LoteProdutoDTO> lotes = loteProdutoService.findAll();
        if (lotes.isEmpty()){
            throw new ResourceNotFoundException("Lotes não encontrados");
        }
        return ResponseEntity.ok(lotes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteProdutoDTO> findById(@PathVariable Integer id) {
        LoteProdutoDTO loteProdutoDTO = loteProdutoService.findById(id);
        if(loteProdutoDTO == null){
            throw new ResourceNotFoundException("Lote não encontrado");
        }
        return ResponseEntity.ok(loteProdutoDTO);
    }

    @PostMapping
    public ResponseEntity<LoteProdutoDTO> save(@RequestBody LoteProdutoDTO loteProdutoDTO) {
        loteProdutoDTO.setId(null);
        return ResponseEntity.ok(loteProdutoService.save(loteProdutoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        loteProdutoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
