package ShirleideProdutos.com.guilda_01.nome_do_projeto.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double desconto;
    private Boolean pagamentoRealizado;
    private Double valorTotal;
    private LocalDate dataVenda;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<PedidoVenda> pedidos;


    @CreationTimestamp
    private LocalDate criadoEm;

    @UpdateTimestamp
    private LocalDate atualizadoEm;

}
