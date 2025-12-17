package br.com.laudos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "local")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_local_seq")
    @SequenceGenerator(name = "id_local_seq", allocationSize = 1)
    @Column(name = "id_local")
    private Integer id;

    @NotNull
    @NotBlank
    @Length(min = 4, max = 140)
    @Column(name = "descricao")
    private String descricao;
}
