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
@Table(name = "droga")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Droga {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_droga_seq")
    @SequenceGenerator(name = "id_droga_seq", allocationSize = 1)
    @Column(name = "id_droga")
    private Integer id;

    @NotNull
    @NotBlank
    @Length(min = 5, max = 200)
    @Column(name = "droga")
    private String nomedroga;
}
