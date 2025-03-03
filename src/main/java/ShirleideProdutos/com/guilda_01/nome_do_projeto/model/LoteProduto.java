package ShirleideProdutos.com.guilda_01.nome_do_projeto.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;


@Getter
@Setter
@Entity
public class LoteProduto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer qtdLote;
    private Double valorUnitario;
    private LocalDate dataValidade;

    @ManyToOne
    @JoinColumn(name = "fk_produto", nullable = false)
    private Produto produto;

    @CreationTimestamp
    private LocalDate criadoEm = LocalDate.now();

    @UpdateTimestamp
    private LocalDate atualizadoEm;


}
