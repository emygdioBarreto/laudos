package br.com.laudos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "concluir")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Concluir {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_concluir_seq")
    @SequenceGenerator(name = "id_concluir_seq", allocationSize = 1)
    @Column(name = "id_concluir")
    private Integer id;

    @NotNull
    @NotBlank
    @Length(min = 5, max = 100)
    @Column(name = "conclusao")
    private String conclusao;
}
