package br.com.laudos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "intestino_grosso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Intestino {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_intestino_grosso_seq")
    @SequenceGenerator(name = "id_intestino_grosso_seq", allocationSize = 1)
    @Column(name = "id_intestino_grosso")
    private Integer id;

    @NotNull
    @NotBlank
    @Length(min = 5, max = 140)
    @Column(name = "descricao")
    private String descricao;
}
