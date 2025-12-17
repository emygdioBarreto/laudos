package br.com.laudos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "equipamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_equipamento_seq")
    @SequenceGenerator(name = "id_equipamento_seq", allocationSize = 1)
    @Column(name = "id_equipamento")
    private Integer id;

    @NotNull
    @NotBlank
    @Length(min = 5, max = 140)
    @Column(name = "descricao")
    private String descricao;

    @Column(name = "superior", length = 2)
    private String superior;

    @Column(name = "inferior", length = 2)
    private String inferior;

    @Column(name = "direita", length = 2)
    private String direita;

    @Column(name = "esquerda", length = 2)
    private String esquerda;

    @Column(name = "modlaudo", length = 1)
    private String modLaudo;

    @Column(name = "cidade", length = 25)
    private String cidade;

    @Column(name = "ordena", length = 1)
    private String ordena;
}
