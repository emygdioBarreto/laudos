package br.com.laudos.dto.mapper;

import br.com.laudos.domain.*;

public record LaudoRefs(
        Equipamento equipamento,
        Solicitante solicitante,
        Procedencia procedencia,
        Premedicacao premedicacao,
        Local localExame,
        TipoExame tipoExame,
        Medico medicoExecutor,
        Resumo resumo
) {
}
