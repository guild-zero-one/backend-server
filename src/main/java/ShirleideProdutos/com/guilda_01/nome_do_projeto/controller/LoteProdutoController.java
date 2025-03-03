package ShirleideProdutos.com.guilda_01.nome_do_projeto.controller;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.LoteProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.LoteProduto;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.service.LoteProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lote-produtos")
public class LoteProdutoController {
    @Autowired
    LoteProdutoService loteProdutoService;

    @PostMapping("/cadastrar/{id}")
    public ResponseEntity<LoteProdutoDTO>adicionarLote(@PathVariable Integer id, @RequestBody LoteProduto loteProduto){
        return ResponseEntity.ok(loteProdutoService.adicionarLote(id, loteProduto));
    }

    @GetMapping
    public ResponseEntity<List<LoteProdutoDTO>>listarLotes(){
        List<LoteProdutoDTO> lotes = loteProdutoService.listarLotes();
        if (lotes.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lotes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteProdutoDTO>buscarLotePorId(@PathVariable Integer id){
        return ResponseEntity.ok(loteProdutoService.buscarLotePorId(id));
    }
    @GetMapping("/buscarPorProduto/{id}")
    public ResponseEntity<List<LoteProdutoDTO>>buscarLotesPorProduto(@PathVariable Integer id){
        return ResponseEntity.ok(loteProdutoService.buscarLotesPorProduto(id));
    }
}
