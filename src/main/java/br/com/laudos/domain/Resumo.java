package br.com.laudos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "resumo_clinico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Resumo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_resumo_clinico_seq")
    @SequenceGenerator(name = "id_resumo_clinico_seq", allocationSize = 1)
    @Column(name = "id_resumo_clinico")
    private Integer id;

    @NotNull
    @NotBlank
    @Length(min = 5, max = 200)
    @Column(name = "descricao")
    private String descricao;
}
