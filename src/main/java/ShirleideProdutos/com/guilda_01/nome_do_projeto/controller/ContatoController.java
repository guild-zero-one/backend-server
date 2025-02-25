package ShirleideProdutos.com.guilda_01.nome_do_projeto.controller;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ContatoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Cliente;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Contato;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.ClienteRepository;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.ContatoRepository;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.service.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contatos")
public class ContatoController {


    @Autowired
    ContatoService contatoService;

    @PostMapping("/adicionar/{clienteId}")
    public ResponseEntity<ContatoDTO> adicionarContato(
            @PathVariable Integer clienteId,
            @RequestBody ContatoDTO contatoDTO) {
        return ResponseEntity.ok(contatoService.adicionarContato(clienteId, contatoDTO));
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<List<ContatoDTO>> buscarContatosPorCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(contatoService.buscarContatosPorCliente(clienteId));
    }


}
