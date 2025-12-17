package br.com.laudos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "solicitante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Solicitante {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_solicitante_seq")
    @SequenceGenerator(name = "id_solicitante_seq", allocationSize = 1)
    @Column(name = "id_solicitante")
    private Integer id;

    @NotNull
    @NotBlank
    @Length(min = 5, max = 140)
    @Column(name = "medico_solicitante")
    private String medicoSolicitante;
}
