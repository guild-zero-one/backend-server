package com.zeroone.simlady.service;

import com.zeroone.simlady.dto.usuario.UsuarioAtualizacaoDto;
import com.zeroone.simlady.entity.Usuario;
import com.zeroone.simlady.entity.enums.Permissao;
import com.zeroone.simlady.entity.enums.Provider;
import com.zeroone.simlady.exception.BadRequestException;
import com.zeroone.simlady.exception.ResourceAlreadyExistsException;
import com.zeroone.simlady.exception.ResourceNotFoundException;
import com.zeroone.simlady.repository.UsuarioRepository;
import com.zeroone.simlady.config.security.jwt.GerenciadorTokenJwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;

    public Usuario cadastrar(Usuario usuario) {

        validarEmail(usuario.getEmail());

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        usuario.setProvider(Provider.LOCAL);
        usuario.setPerfilCompleto(true);
        if (usuario.getPermissao() == null) {
            usuario.setPermissao(Permissao.COMUM);
        }

        return usuarioRepository.save(usuario);
    }

    public String autenticar(Usuario usuario, HttpServletResponse response) {

        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuario.getEmail(), usuario.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);



        usuarioRepository.findByEmail(usuario.getEmail())
                .orElseThrow(
                        () -> new ResponseStatusException(404, "Email do usuário não cadastrado", null)
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token =  gerenciadorTokenJwt.generateToken(authentication);


        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600);

        response.addCookie(cookie);

        return token;
    }

    public void atualizarPermissao( UUID id ,String permissao) {

        Usuario usuario = buscar(id);

        if(usuario.getPermissao().equals(Permissao.valueOf(permissao))) {
            throw new ResourceAlreadyExistsException("Usuário já possui essa permissão");
        }

        try{
            usuario.setPermissao(Permissao.valueOf(permissao));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Permissão inválida.");
        }

        usuarioRepository.save(usuario);

    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Page<Usuario> buscarComFiltro(String search, String type, Pageable pageable) {
        String termoBusca = search == null || search.trim().isEmpty() ? null : search.trim();
        Permissao permissao = parsePermissao(type);

        if (permissao == null && termoBusca == null) {
            return usuarioRepository.findAll(pageable);
        }

        if (permissao == null) {
            return usuarioRepository.findByNomeContainingIgnoreCaseOrSobrenomeContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    termoBusca, termoBusca, termoBusca, pageable
            );
        }

        if (termoBusca == null) {
            return usuarioRepository.findByPermissao(permissao, pageable);
        }

        return usuarioRepository.findByPermissaoAndNomeContainingIgnoreCaseOrPermissaoAndSobrenomeContainingIgnoreCaseOrPermissaoAndEmailContainingIgnoreCase(
                permissao, termoBusca,
                permissao, termoBusca,
                permissao, termoBusca,
                pageable
        );
    }

    public Usuario buscar(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public Usuario atualizar(UUID id, UsuarioAtualizacaoDto dto) {

        Usuario usuario = buscar(id);

        if (dto.getEmail() != null) {
            if (usuarioRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
                throw new ResourceAlreadyExistsException("Dados inválidos, email já cadastrado");
            }
            usuario.setEmail(dto.getEmail());
        }

        if (dto.getNome() != null) usuario.setNome(dto.getNome());
        if (dto.getSobrenome() != null) usuario.setSobrenome(dto.getSobrenome());
        if (dto.getCelular() != null) usuario.setCelular(dto.getCelular());

        return usuarioRepository.save(usuario);
    }

    public void togglePermissao(UUID id) {
        Usuario usuario = buscar(id);
        usuario.setPermissao(usuario.getPermissao() == Permissao.ADMIN ? Permissao.COMUM : Permissao.ADMIN);
        usuarioRepository.save(usuario);
    }

    public Usuario buscarAutenticado(HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        return usuarioRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public String toggleStatus(UUID id) {
        Usuario usuario = buscar(id);
        usuario.setAtivo(!usuario.getAtivo());
        usuarioRepository.save(usuario);
        return usuario.getAtivo()
                ? "Usuário ativado com sucesso."
                : "Usuário desativado com sucesso.";
    }

    @Transactional
    public void atualizarImageUrl(UUID id, String url) {
        Usuario usuario = buscar(id);
        usuario.setUrlImagem(url);
        usuarioRepository.save(usuario);
    }

    public void deletar(UUID id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        usuarioRepository.deleteById(id);
    }

    private void validarEmail(String email) {
        if(usuarioRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email já cadastrado!");
        }
    }

    @Transactional
    public Usuario sso(String clerkId, String nome, String sobrenome, String email, String urlImagem) {

        Optional<Usuario> porClerkId = usuarioRepository
                .findByProviderAndProviderId(Provider.CLERK, clerkId);

        if (porClerkId.isPresent()) {
            Usuario existing = porClerkId.get();
            existing.setUrlImagem(urlImagem);
            return usuarioRepository.save(existing);
        }

        if (usuarioRepository.existsByEmail(email)) {
            throw new BadRequestException(
                    "E-mail já cadastrado com outro método de autenticação."
            );
        }

        Usuario novo = new Usuario();
        novo.setNome(nome);
        novo.setSobrenome(sobrenome);
        novo.setEmail(email);
        novo.setUrlImagem(urlImagem);
        novo.setProvider(Provider.CLERK);
        novo.setProviderId(clerkId);
        novo.setPerfilCompleto(false);
        novo.setSenha(null);
        novo.setAtivo(true);
        novo.setPermissao(Permissao.COMUM);

        return usuarioRepository.save(novo);
    }

    public String gerarTokenDireto(String email) {
        Authentication auth = new UsernamePasswordAuthenticationToken(email, null, List.of());
        return gerenciadorTokenJwt.generateToken(auth);
    }

    private Permissao parsePermissao(String type) {
        if (type == null || type.trim().isEmpty() || "all".equalsIgnoreCase(type)) {
            return null;
        }

        if ("admin".equalsIgnoreCase(type)) {
            return Permissao.ADMIN;
        }

        if ("comum".equalsIgnoreCase(type)) {
            return Permissao.COMUM;
        }

        throw new BadRequestException("Tipo inválido. Valores aceitos: admin, comum, all.");
    }
}
