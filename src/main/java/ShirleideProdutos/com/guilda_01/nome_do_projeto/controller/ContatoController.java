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
        return ResponseEntity.ok(contatoService.adicionarContato(clienteId, contato));
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Set<ContatoDTO>> buscarContatosPorCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(contatoService.buscarContatosPorCliente(clienteId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ContatoDTO> atualizarContato(@PathVariable Integer id, @RequestBody ContatoDTO contatoDTO) {
        return ResponseEntity.ok(contatoService.atualizarContato(id, contatoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarContato (@PathVariable Integer id) {
        contatoService.deletarContato(id);
        return ResponseEntity.noContent().build();
    }


}
