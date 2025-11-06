package br.com.laudos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "procedencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Procedencia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_procedencia_seq")
    @SequenceGenerator(name = "id_procedencia_seq", allocationSize = 1)
    @Column(name = "id_procedencia")
    private Integer id;

    @NotNull
    @NotBlank
    @Length(min = 5, max = 200)
    @Column(name = "procedencia")
    private String descricao;
}
