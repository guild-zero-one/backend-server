package ShirleideProdutos.com.guilda_01.nome_do_projeto.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;


import java.time.LocalDateTime;

@Data
@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String nomeFantasia;

    @ManyToOne
    @JoinColumn(name = "fk_fornecedor", nullable = false)
    private Fornecedor fornecedor;

    @CreationTimestamp
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    private LocalDateTime atualizadoEm;
}
