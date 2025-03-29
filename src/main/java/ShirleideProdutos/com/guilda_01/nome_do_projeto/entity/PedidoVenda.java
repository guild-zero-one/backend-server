package ShirleideProdutos.com.guilda_01.nome_do_projeto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class PedidoVenda{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String status;

        @ManyToOne
        @JoinColumn(name = "fk_venda")
        private Venda venda;

    @ManyToOne
    @JoinColumn(name = "fk_cliente", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "pedidoVenda", cascade = CascadeType.ALL)
    private List<PedidoItem> itens;

    @CreationTimestamp
    private LocalDate criadoEm;

    @UpdateTimestamp
    private LocalDate atualizadoEm;


}
