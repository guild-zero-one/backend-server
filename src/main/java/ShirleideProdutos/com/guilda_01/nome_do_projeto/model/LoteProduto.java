package ShirleideProdutos.com.guilda_01.nome_do_projeto.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
public class LoteProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer qtdLote;
    private Double valorUnitCompra;
    private LocalDateTime dataValidade;

    @ManyToOne
    @JoinColumn(name = "fk_produto", nullable = false)
    private Produto produto;

    @CreationTimestamp
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    private LocalDateTime atualizadoEm;
}
