package br.com.laudos.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "laudo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Laudo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_laudo_seq")
    @SequenceGenerator(name = "id_laudo_seq", sequenceName = "id_laudo_seq", allocationSize = 1)
    @Column(name = "id_laudo")
    private Long idLaudo;

    @Column(name = "data_criacao", nullable = false)
	private LocalDate dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipamento", nullable = false)
    private Equipamento equipamento;

    @Column(name = "paciente", length = 70, nullable = false)
    private String paciente;

    @Column(name = "idade", length = 15)
    private String idade;

    @Column(name = "nascimento", nullable = false)
    private LocalDate nascimento;

    @Column(name = "sexo", length = 1, nullable = false)
    private String sexo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitante", nullable = false)
    private Solicitante solicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_procedencia", nullable = false)
    private Procedencia procedencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_premedicacao", nullable = false)
    private Premedicacao premedicacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_exame_id", nullable = false)
    private TipoExame tipoExame;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_localexame", nullable = false)
    private Local localExame;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crm", nullable = false)
    private Medico medicoExecutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_resumo", nullable = false)
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
}
