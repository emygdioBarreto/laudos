package br.com.laudos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "duodeno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Duodeno {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_duodeno_seq")
    @SequenceGenerator(name = "id_duodeno_seq", allocationSize = 1)
    @Column(name = "id_duodeno")
    private Integer id;

    @NotNull
    @NotBlank
    @Length(min = 5, max = 140)
    @Column(name = "descricao")
    private String descricao;
}
