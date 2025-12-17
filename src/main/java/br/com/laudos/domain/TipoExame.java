package br.com.laudos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "tipo_exame")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoExame {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_tipo_exame_seq")
    @SequenceGenerator(name = "id_tipo_exame_seq", allocationSize = 1)
    @Column(name = "id_tipo_exame")
    private Integer id;

    @NotNull
    @NotBlank
    @Length(max = 150)
    @Column(name = "tipoexame")
    private String descricao;

    @NotNull
    @NotBlank
    @Column(name = "ordena")
    private String ordena;

    @Column(name = "esofago")
    private boolean esofago;

    @Column(name = "estomago")
    private boolean estomago;

    @Column(name = "duodeno")
    private boolean duodeno;

    @Column(name = "intestinogrosso")
    private boolean intestino;

    @Column(name = "pancreas")
    private boolean pancreas;
}
