package br.com.laudos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "pancreas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pancrea {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_pancreas_seq")
    @SequenceGenerator(name = "id_pancreas_seq", allocationSize = 1)
    @Column(name = "id_pancreas")
    private Integer id;

    @NotNull
    @NotBlank
    @Length(min = 5, max = 140)
    private String descricao;
}
