package ShirleideProdutos.com.guilda_01.nome_do_projeto.controller;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ProdutoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.exception.ResourceNotFoundException;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.mapper.ProdutoMapper;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Produto;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listar() {
        List<ProdutoDTO> produtos = produtoService.listar();
        if (produtos.isEmpty()){
            throw new ResourceNotFoundException("Nenhum produto encontrado");
        }
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Integer id) {
        Produto produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(ProdutoMapper.toDTO(produto));
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> cadastrarProduto(@RequestBody ProdutoDTO produtoDTO) {
        produtoDTO.setId(null);
        return ResponseEntity.ok(produtoService.cadastrarProduto(produtoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProduto(@PathVariable Integer id) {
        produtoService.excluirPorId(id);
        return ResponseEntity.noContent().build();
    }
}
