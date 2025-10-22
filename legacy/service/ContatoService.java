package com.zeroone.simlady.service;

import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.exception.ResourceAlreadyExistsException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.entity.Contato;
import com.zeroone.simlady.repository.UsuarioRepository;
import com.zeroone.simlady.repository.ContatoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public Contato adicionar(Integer clienteId, Contato contato) {

        Usuario usuario = usuarioService.buscar(clienteId);

        validarCelular(contato.getCelular());

        contato.setUsuario(usuario);
        usuario.adicionarContato(contato);

        contatoRepository.save(contato);

        return contato;

    }

    public Contato buscar(Integer id) {
        return contatoRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Contato não encontrado."));
    }

    public Set<Contato> buscarPorCliente (Integer clienteId) {

        Usuario usuario = usuarioService.buscar(clienteId);

        return usuario
                .getContatos();
    }

    public Contato atualizar(Integer id, Contato contato) {

        Usuario usuario = usuarioService.buscar(id);


        if(contatoRepository.existsByCelularAndIdNot(contato.getCelular(), id)) {
            throw new ResourceAlreadyExistsException("Celular já cadastrado.");
        }

        contato.setId(id);
        contato.setUsuario(usuario);

        contatoRepository.save(contato);
        return contato;
    }

    public void deletar(Integer id) {
        Contato contato = buscar(id);

        Usuario usuario = usuarioService.buscar(contato.getUsuario().getId());
        usuario.getContatos().remove(contato);

        usuarioRepository.save(usuario);
        contatoRepository.delete(contato);

    }

    private void validarCelular(String celular) {
        if(contatoRepository.existsByCelular(celular)) {
            throw new ResourceAlreadyExistsException("Celular já cadastrado.");
        }
    }

}
