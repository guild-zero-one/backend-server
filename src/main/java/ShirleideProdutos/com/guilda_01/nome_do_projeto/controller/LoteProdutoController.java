package ShirleideProdutos.com.guilda_01.nome_do_projeto.controller;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.LoteProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.exception.ResourceNotFoundException;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.LoteProdutoMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.LoteProduto;
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
    public ResponseEntity<List<LoteProdutoDTO>> listar() {
        List<LoteProdutoDTO> lotes = loteProdutoService.listar();
        if (lotes.isEmpty()){
            throw new ResourceNotFoundException("Lotes não encontrados");
        }
        return ResponseEntity.ok(lotes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteProdutoDTO> buscarPorID(@PathVariable Integer id) {
        LoteProduto loteProduto = loteProdutoService.buscarPorId(id);
        return ResponseEntity.ok(LoteProdutoMapper.toDTO(loteProduto));
    }

    @PostMapping
    public ResponseEntity<LoteProdutoDTO> cadastrarLote(@RequestBody LoteProdutoDTO loteProdutoDTO) {
        loteProdutoDTO.setId(null);
        return ResponseEntity.ok(loteProdutoService.cadastrarLote(loteProdutoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPorId(@PathVariable Integer id) {
        loteProdutoService.excluirPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LoteProdutoDTO> atualizarLote(@PathVariable Integer id, @RequestBody LoteProdutoDTO loteProdutoDTO) {
        return ResponseEntity.ok(loteProdutoService.atualizar(id,loteProdutoDTO));
    }
}
