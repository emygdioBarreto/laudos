package br.com.laudos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "estomago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Estomago {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_estomago_seq")
    @SequenceGenerator(name = "id_estomago_seq", allocationSize = 1)
    @Column(name = "id_estomago")
    private Integer id;

    @NotNull
    @NotBlank
    @Length(min = 5, max = 140)
    @Column(name = "descricao")
    private String descricao;
}
