package br.com.laudos.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "laudo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Laudo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "id_laudo_seq")
    @SequenceGenerator(name = "id_laudo_seq", allocationSize = 1)
    @Column(name = "id_laudo")
    @OrderBy("id ASC")
    private Long id;

    @Column(name = "data", nullable = false)
	private Date data;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_equipamento", nullable = false)
    private Equipamento equipamento;

    @Column(name = "paciente", length = 70, nullable = false)
    private String paciente;

    @Column(name = "idade", length = 15)
    private String idade;

    @Column(name = "nascimento")
    private Date nascimento;

    @Column(name = "sexo", length = 1, nullable = false)
    private String sexo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_solicitante", columnDefinition = "id")
    private Solicitante solicitante;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_procedencia", columnDefinition = "id")
    private Procedencia procedencia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_premedicacao", columnDefinition = "id")
    private Premedicacao premedicacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_local", columnDefinition = "id")
    private Local localExame;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "crm", columnDefinition = "crm")
    private Medico medicoExecutor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_resumo_clinico", columnDefinition = "id")
    private Resumo resumo;

    @Column(name = "observacao_clinica", length = 100)
    private String observacaoClinica;

    @Column(name = "esofago", columnDefinition = "TEXT")
    private String esofago;

    @Column(name = "estomago", columnDefinition = "TEXT")
    private String estomago;

    @Column(name = "duodeno", columnDefinition = "TEXT")
    private String duodeno;

    @Column(name = "intestino_grosso", columnDefinition = "TEXT")
    private String intestino;

    @Column(name = "pancreas", columnDefinition = "TEXT")
    private String pancreas;

    @Column(name = "solucao")
    private String solucao;

    @Column(name = "conclusao", columnDefinition = "TEXT")
    private String conclusao;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_exame", columnDefinition = "id")
    private TipoExame tipoExame;
}
