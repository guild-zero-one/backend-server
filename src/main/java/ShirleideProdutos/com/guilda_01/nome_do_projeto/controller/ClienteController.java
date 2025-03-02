package ShirleideProdutos.com.guilda_01.nome_do_projeto.controller;

import ShirleideProdutos.com.guilda_01.nome_do_projeto.DTO.ClienteDTO;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.model.Cliente;
import ShirleideProdutos.com.guilda_01.nome_do_projeto.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping()
    public ResponseEntity<ClienteDTO> cadastrarCliente(@RequestBody Cliente cliente) {
        return new ResponseEntity<>(clienteService.cadastrarCliente(cliente), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity <List<ClienteDTO>> listarClientes() {
        List<ClienteDTO> clientes = clienteService.listarClientes();
        if(clientes.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarClientePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.buscarClientePorId(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClienteDTO> atualizarCliente(@PathVariable Integer id, @RequestBody ClienteDTO clienteDTO) {
        return ResponseEntity.ok(clienteService.atualizarCliente(id, clienteDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Integer id) {
        clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
