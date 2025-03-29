package ShirleideProdutos.com.guilda_01.nome_do_projeto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@Setter

public class Contato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String celular;

    @CreationTimestamp
    private LocalDate criadoEm;
    @UpdateTimestamp
    private LocalDate atualizadoEm;

    @ManyToOne
    private Cliente cliente;

}
