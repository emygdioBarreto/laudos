package br.com.laudos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "concluir")
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

    public Concluir() {
    }

    public Concluir(Integer id, String conclusao) {
        this.id = id;
        this.conclusao = conclusao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotNull @NotBlank @Length(min = 5, max = 100) String getConclusao() {
        return conclusao;
    }

    public void setConclusao(@NotNull @NotBlank @Length(min = 5, max = 100) String conclusao) {
        this.conclusao = conclusao;
    }
}
