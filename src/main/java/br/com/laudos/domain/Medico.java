package br.com.laudos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "medico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medico {

    @Id
    @Column(name = "crm", length = 5, nullable = false)
    private String crm;

    @NotNull
    @NotBlank
    @Length(min = 5, max = 140)
    @Column(name = "medico", length = 140)
    private String medicoExecutor;
}
