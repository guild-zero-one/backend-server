package com.zeroone.simlady.entity;

import com.zeroone.simlady.entity.enums.Permissao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private String sobrenome;

    private String cpf;

    private String email;

    private String senha;

    private String urlImagem;

    @Enumerated(EnumType.STRING)
    private Permissao permissao;

    private Boolean ativo = true;

    @CreationTimestamp
    private LocalDate criadoEm;

    @UpdateTimestamp
    private LocalDate atualizadoEm;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Contato> contatos = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PedidoVenda> pedidos = new HashSet<>();

    public void adicionarContato(Contato contato) {
        contatos.add(contato);
        contato.setUsuario(this);
    }
}
