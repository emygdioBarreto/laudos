package br.com.laudos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "esofago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Esofago {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_esofago_seq")
    @SequenceGenerator(name = "id_esofago_seq", allocationSize = 1)
    @Column(name = "id_esofago")
    private Integer id;

    @NotNull
    @NotBlank
    @Length(min = 5, max = 140)
    @Column(name = "descricao")
    private String descricao;
}
