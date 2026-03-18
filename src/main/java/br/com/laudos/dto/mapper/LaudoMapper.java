package br.com.laudos.dto.mapper;

import br.com.laudos.domain.*;
import br.com.laudos.dto.LaudoCreateDTO;
import br.com.laudos.dto.LaudoDTO;
import br.com.laudos.dto.LaudoUpdateDTO;
import org.springframework.stereotype.Component;

@Component
public class LaudoMapper {

    public LaudoDTO toDTO(Laudo laudo) {
        if (laudo == null) {
            return null;
        }
        return new LaudoDTO(
                laudo.getIdLaudo(),
                laudo.getDataCriacao(),
                laudo.getEquipamento().getId(),
                laudo.getEquipamento().getDescricao(),
                laudo.getPaciente(),
                laudo.getIdade(),
                laudo.getNascimento(),
                laudo.getSexo(),
                laudo.getSolicitante().getId(),
                laudo.getSolicitante().getMedicoSolicitante(),
                laudo.getProcedencia().getId(),
                laudo.getProcedencia().getDescricao(),
                laudo.getPremedicacao().getId(),
                laudo.getPremedicacao().getDescricao(),
                laudo.getLocalExame().getId(),
                laudo.getLocalExame().getDescricao(),
                laudo.getMedicoExecutor().getCrm(),
                laudo.getMedicoExecutor().getMedicoExecutor(),
                laudo.getResumo().getId(),
                laudo.getResumo().getDescricao(),
                laudo.getObservacaoClinica(),
                laudo.getEsofago(),
                laudo.getEstomago(),
                laudo.getDuodeno(),
                laudo.getIntestino(),
                laudo.getPancreas(),
                laudo.getSolucao(),
                laudo.getConclusao(),
                laudo.getObservacao(),
                laudo.getTipoExame().getId(),
                laudo.getTipoExame().getDescricao());
    }

    public Laudo toEntityCreate(LaudoCreateDTO dto, LaudoRefs refs) {
        Laudo laudo = new Laudo();

        laudo.setDataCriacao(dto.dataCriacao());
        laudo.setEquipamento(refs.equipamento());
        laudo.setPaciente(dto.paciente());
        laudo.setIdade(dto.idade());
        laudo.setNascimento(dto.nascimento());
        laudo.setSexo(dto.sexo());

        laudo.setSolicitante(refs.solicitante());
        laudo.setProcedencia(refs.procedencia());
        laudo.setPremedicacao(refs.premedicacao());
        laudo.setLocalExame(refs.localExame());
        laudo.setTipoExame(refs.tipoExame());
        laudo.setMedicoExecutor(refs.medicoExecutor());
        laudo.setResumo(refs.resumo());

        laudo.setObservacaoClinica(dto.observacaoClinica());
        laudo.setEsofago(dto.esofago());
        laudo.setEstomago(dto.estomago());
        laudo.setDuodeno(dto.duodeno());
        laudo.setIntestino(dto.intestino());
        laudo.setPancreas(dto.pancreas());
        laudo.setSolucao(dto.solucao());
        laudo.setConclusao(dto.conclusao());
        laudo.setObservacao(dto.observacao());

        return laudo;
    }

    public void toEntityUpdate(Laudo laudo, LaudoUpdateDTO dto, LaudoRefs refs) {

        laudo.setDataCriacao(dto.dataCriacao());
        laudo.setEquipamento(refs.equipamento());
        laudo.setSolicitante(refs.solicitante());
        laudo.setProcedencia(refs.procedencia());
        laudo.setPremedicacao(refs.premedicacao());
        laudo.setLocalExame(refs.localExame());
        laudo.setResumo(refs.resumo());
        laudo.setTipoExame(refs.tipoExame());
        laudo.setMedicoExecutor(refs.medicoExecutor());

        laudo.setPaciente(dto.paciente());
        laudo.setIdade(dto.idade());
        laudo.setNascimento(dto.nascimento());
        laudo.setSexo(dto.sexo());
        laudo.setObservacaoClinica(dto.observacaoClinica());
        laudo.setEsofago(dto.esofago());
        laudo.setEstomago(dto.estomago());
        laudo.setDuodeno(dto.duodeno());
        laudo.setIntestino(dto.intestino());
        laudo.setPancreas(dto.pancreas());
        laudo.setSolucao(dto.solucao());
        laudo.setConclusao(dto.conclusao());
        laudo.setObservacao(dto.observacao());
    }
}
