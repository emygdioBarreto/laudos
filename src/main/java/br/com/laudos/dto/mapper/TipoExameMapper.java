package br.com.laudos.dto.mapper;

import br.com.laudos.domain.TipoExame;
import br.com.laudos.dto.TipoExameDTO;
import org.springframework.stereotype.Component;

@Component
public class TipoExameMapper {

    public TipoExameDTO toDTO(TipoExame tipoExame) {
        if (tipoExame == null) {
            return null;
        }
        return new TipoExameDTO(tipoExame.getId(),
                tipoExame.getDescricao(),
                tipoExame.getOrdena(),
                tipoExame.isEsofago(),
                tipoExame.isEstomago(),
                tipoExame.isDuodeno(),
                tipoExame.isIntestino(),
                tipoExame.isPancreas());
    }

    public TipoExame toEntity(TipoExameDTO tipoExameDTO) {
        if (tipoExameDTO == null) {
            return null;
        }
        TipoExame tipoExame = new TipoExame();
        if (tipoExameDTO.id() != null) {
            tipoExame.setId(tipoExameDTO.id());
        }
        tipoExame.setDescricao(tipoExameDTO.descricao());
        tipoExame.setOrdena(tipoExameDTO.ordena());
        tipoExame.setEsofago(tipoExameDTO.esofago());
        tipoExame.setEstomago(tipoExameDTO.estomago());
        tipoExame.setDuodeno(tipoExameDTO.duodeno());
        tipoExame.setIntestino(tipoExameDTO.intestino());
        tipoExame.setPancreas(tipoExameDTO.pancreas());
        return tipoExame;
    }
}
