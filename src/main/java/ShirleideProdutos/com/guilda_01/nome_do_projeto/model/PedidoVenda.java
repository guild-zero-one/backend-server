package ShirleideProdutos.com.guilda_01.nome_do_projeto.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.metamodel.model.domain.IdentifiableDomainType;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class PedidoVenda{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String status;

    @ManyToOne
    @JoinColumn(name = "fk_venda", nullable = false)
    private Venda venda;

    @ManyToOne
    @JoinColumn(name = "fk_cliente", nullable = false)
    private Cliente cliente;

    @CreationTimestamp
    private LocalDate criadoEm;

    @UpdateTimestamp
    private LocalDate atualizadoEm;


}
