package ShirleideProdutos.com.guilda_01.nome_do_projeto.controller;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ContatoDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Cliente;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Contato;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.ClienteRepository;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/contatos")
public class ContatoController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    @PostMapping("/adicionar/{idCliente}")
    public ResponseEntity<ContatoDTO> adicionarContato(
            @PathVariable Integer idCliente,
            @RequestBody ContatoDTO contatoDTO) {

        Optional<Cliente> possivelCliente = clienteRepository.findById(idCliente);

        if (possivelCliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Cliente cliente = possivelCliente.get();

        Contato contato = new Contato();

        contato.setCelular(contatoDTO.getCelular());
        contato.setClienteId(cliente.getId());
        cliente.getContatos().add(contato);

        contatoRepository.save(contato);

        return ResponseEntity.ok(new ContatoDTO(contato));
    }


}
