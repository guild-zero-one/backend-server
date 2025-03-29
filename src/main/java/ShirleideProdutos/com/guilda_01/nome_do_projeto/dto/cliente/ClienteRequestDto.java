package ShirleideProdutos.com.guilda_01.nome_do_projeto.dto.cliente;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequestDto {

    @NotBlank(message = "Nome não deve ser vazio")
    @Size(min = 3, max = 30, message = "Nome deve ter entre 3 e 30 caracteres")
    private String nome;

    @Size(min = 3, max = 30, message = "Sobrenome deve ter entre 3 e 30 caracteres")
    private String sobrenome;

    @Size(min = 2, max = 15, message = "Apelido deve ter entre 2 e 15 caracteres")
    private String apelido;

    @CPF(message = "CPF inválido")
    @Column(unique = true, nullable = false)
    @NotBlank(message = "CPF não pode ser vazio")
    private String cpf;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email não pode ser vazio")
    private String email;

    @NotBlank(message = "Senha não pode ser vazia")
    @Size(min = 8, max = 30, message = "Senha deve ter entre 8 e 30 caracteres")
    private String senha;





}