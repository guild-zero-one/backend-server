package ShirleideProdutos.com.guilda_01.nome_do_projeto.controller;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ClienteDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Cliente;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping("/cadastrar")
    public ResponseEntity<ClienteDTO> cadastrarCliente(@RequestBody ClienteDTO clienteDTO) {
        if (clienteDTO.getNome() == null || clienteDTO.getNome().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.getNome());

        Cliente clienteSalvo = clienteRepository.save(cliente);

        return ResponseEntity.ok(new ClienteDTO(clienteSalvo));
    }


    @GetMapping
    public List<ClienteDTO> listar() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarClientePorId(@PathVariable Integer id) {
        return clienteRepository.findById(id)
                .map(cliente -> ResponseEntity.ok(new ClienteDTO(cliente)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ClienteDTO> atualizarCliente(@PathVariable Integer id, @RequestBody ClienteDTO cliente) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);

        if (clienteExistente.isPresent()) {
            Cliente clienteAtualizado = clienteExistente.get();
            clienteAtualizado.setNome(cliente.getNome());
            clienteAtualizado.setAtualizadoEm(LocalDate.now());

            clienteRepository.save(clienteAtualizado);

            return ResponseEntity.ok(new ClienteDTO(clienteAtualizado));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deletarCliente(@PathVariable Integer id) {

        if(clienteRepository.existsById(id)){
            clienteRepository.deleteById(id);
            return "Cliente removido com sucesso! ";
        }

        return "Cliente não encontrado!";

    }

}
