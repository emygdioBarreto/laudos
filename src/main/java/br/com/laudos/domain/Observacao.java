package br.com.laudos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "observacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Observacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_observacao_seq")
    @SequenceGenerator(name = "id_observacao_seq", allocationSize = 1)
    @Column(name = "id_observacao")
    private Integer id;

    @NotNull
    @NotBlank
    @Length(min = 5, max = 140)
    @Column(name = "descricao")
    private String descricao;
}
