package br.com.laudos.dto.mapper;

import br.com.laudos.domain.Esofago;
import br.com.laudos.dto.EsofagoDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class EsofagoMapper {

    public EsofagoDTO toDTO(Esofago esofago) {
        if (esofago == null) {
            return null;
        }
        return new EsofagoDTO(esofago.getId(), esofago.getDescricao());
    }

    public Esofago toEntity(@Valid EsofagoDTO esofagoDTO) {
        if (esofagoDTO == null) {
            return null;
        }
        Esofago esofago = new Esofago();
        if (esofagoDTO.id() != null) {
            esofago.setId(esofagoDTO.id());
        }
        esofago.setDescricao(esofagoDTO.descricao());
        return esofago;
    }
}
