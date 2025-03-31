package com.zeroone.simlady.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "contato")
@Getter
@Setter
public class Contato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 11, max = 11, message = "O número de celular deve ter exatamente 11 dígitos")
    @Pattern(regexp = "\\d{11}", message = "O número de celular deve conter apenas números")
    private String celular;

    @CreationTimestamp
    private LocalDate criadoEm;

    @UpdateTimestamp
    private LocalDate atualizadoEm;

    @ManyToOne
    @JoinColumn(name = "fk_cliente")
    private Cliente cliente;


}
