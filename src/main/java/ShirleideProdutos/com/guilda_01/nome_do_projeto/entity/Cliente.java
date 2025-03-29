package ShirleideProdutos.com.guilda_01.nome_do_projeto.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nome não deve ser vazio")
    @Size(min = 3, max = 30, message = "Nome deve ter entre 3 e 30 caracteres")
    private String nome;

    @Size(min = 3, max = 30, message = "Sobrenome deve ter entre 3 e 30 caracteres")
    private String sobrenome;

    @Size(min = 2, max = 15, message = "Apelido deve ter entre 2 e 15 caracteres")
    private String apelido;

    @CPF(message = "CPF inválido")
    @NotBlank(message = "CPF não pode ser vazio")
    private String cpf;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email não pode ser vazio")
    private String email;

    @NotBlank(message = "Senha não pode ser vazia")
    @Size(min = 8, max = 30, message = "Senha deve ter entre 8 e 30 caracteres")
    private String senha;

    @NotNull
    private Boolean ativo = true;

    @CreationTimestamp
    private LocalDate criadoEm;

    @UpdateTimestamp
    private LocalDate atualizadoEm;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contato> contatos = new ArrayList<>();

}

