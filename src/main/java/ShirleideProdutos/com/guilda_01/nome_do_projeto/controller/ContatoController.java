package ShirleideProdutos.com.guilda_01.nome_do_projeto.controller;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ContatoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Contato;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.service.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/contatos")
public class ContatoController {


    @Autowired
    ContatoService contatoService;

    @PostMapping("/{clienteId}")
    public ResponseEntity<ContatoDTO> adicionarContato(
            @PathVariable Integer clienteId,
            @RequestBody Contato contato) {
        return ResponseEntity.status(201)
                .body(contatoService.adicionarContato(clienteId, contato));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContatoDTO> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(contatoService.buscarContatoPorId(id));
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<Set<ContatoDTO>> buscarContatosPorCliente(@PathVariable Integer id) {
        return ResponseEntity.ok(contatoService.buscarContatosPorCliente(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ContatoDTO> atualizarContato(@PathVariable Integer id, @RequestBody  Contato contato) {
        return ResponseEntity.ok(contatoService.atualizarContato(id, contato));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarContato (@PathVariable Integer id) {
        contatoService.deletarContato(id);
        return ResponseEntity.noContent().build();
    }


}
