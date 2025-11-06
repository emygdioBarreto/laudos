package br.com.laudos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "premedicacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Premedicacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_premedicacao_seq")
    @SequenceGenerator(name = "id_premedicacao_seq", allocationSize = 1)
    @Column(name = "id_premedicacao")
    private Integer id;

    @NotNull
    @NotBlank
    @Length(min = 5, max = 150)
    @Column(name = "premedicacao")
    private String premedicacao;
}
