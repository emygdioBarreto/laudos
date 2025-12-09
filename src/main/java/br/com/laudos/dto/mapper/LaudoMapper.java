package br.com.laudos.dto.mapper;

import br.com.laudos.domain.Laudo;
import br.com.laudos.dto.LaudoDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class LaudoMapper {

    public LaudoDTO toDTO(Laudo laudo) {
        if (laudo == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return new LaudoDTO(
                laudo.getId(),
                sdf.format(laudo.getData()),
                laudo.getEquipamento(),
                laudo.getPaciente(),
                laudo.getIdade(),
                sdf.format(laudo.getNascimento()),
                laudo.getSexo(),
                laudo.getSolicitante(),
                laudo.getProcedencia(),
                laudo.getPremedicacao(),
                laudo.getLocalExame(),
                laudo.getMedicoExecutor(),
                laudo.getResumo(),
                laudo.getObservacaoClinica(),
                laudo.getEsofago(),
                laudo.getEstomago(),
                laudo.getDuodeno(),
                laudo.getIntestino(),
                laudo.getPancreas(),
                laudo.getSolucao(),
                laudo.getConclusao(),
                laudo.getObservacao(),
                laudo.getTipoExame());
    }

    public Laudo toEntity(@Valid LaudoDTO laudoDTO) throws ParseException {
        if (laudoDTO == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Laudo laudo = new Laudo();
        if (laudoDTO.id() != null) {
            laudo.setId(laudoDTO.id());
        }
        laudo.setData(sdf.parse(laudoDTO.data()));
        laudo.setEquipamento(laudoDTO.equipamento());
        laudo.setPaciente(laudoDTO.paciente());
        laudo.setIdade(laudo.getIdade());
        laudo.setNascimento(sdf.parse(laudoDTO.nascimento()));
        laudo.setSexo(laudoDTO.sexo());
        laudo.setSolicitante(laudoDTO.solicitante());
        laudo.setProcedencia(laudoDTO.procedencia());
        laudo.setPremedicacao(laudoDTO.premedicacao());
        laudo.setLocalExame(laudoDTO.localExame());
        laudo.setMedicoExecutor(laudoDTO.medicoExecutor());
        laudo.setResumo(laudoDTO.resumo());
        laudo.setObservacaoClinica(laudoDTO.observacaoClinica());
        laudo.setEsofago(laudoDTO.esofago());
        laudo.setEstomago(laudoDTO.estomago());
        laudo.setDuodeno(laudoDTO.duodeno());
        laudo.setIntestino(laudoDTO.intestino());
        laudo.setPancreas(laudoDTO.pancreas());
        laudo.setSolucao(laudoDTO.solucao());
        laudo.setConclusao(laudoDTO.conclusao());
        laudo.setObservacao(laudoDTO.observacao());
        laudo.setTipoExame(laudoDTO.tipoExame());
         return laudo;
    }
}
