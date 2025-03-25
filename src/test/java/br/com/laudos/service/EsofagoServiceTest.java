package br.com.laudos.service;

import br.com.laudos.domain.Esofago;
import br.com.laudos.dto.EsofagoDTO;
import br.com.laudos.dto.mapper.EsofagoMapper;
import br.com.laudos.dto.pages.EsofagoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.EsofagoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EsofagoServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de esôfago não encontrada pelo ID informado.";
    private static final int size = 10;
    private static final int page = 0;

    @InjectMocks
    private EsofagoService service;

    @Mock
    private EsofagoRepository repository;

    @Mock
    private EsofagoMapper mapper;

    @Mock
    private EsofagoPageDTO pageDTO;

    private Esofago esofago;
    private EsofagoDTO esofagoDTO;
    private Esofago esofago1;
    private EsofagoDTO esofago1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        esofago = new Esofago(null, "Teste de gravação de frase de Esôfago");
        esofagoDTO = new EsofagoDTO(null, "Teste de gravação de frase de Esôfago");
        esofago1 = new Esofago(2, "Morfologia conservada");
        esofago1DTO = new EsofagoDTO(2, "Morfologia conservada!");
    }

    @Test
    @DisplayName(value = "Salvar frase de esôfago com sucesso")
    void salvarFraseDeEsofagoComSucesso() {
        when(mapper.toEntity(esofagoDTO)).thenReturn(esofago);
        when(repository.save(any())).thenReturn(esofago);
        when(mapper.toDTO(esofago)).thenReturn(esofagoDTO);

        var varEsofago = mapper.toEntity(esofagoDTO);
        var varEsofagoSaved = repository.save(varEsofago);
        var dtoConverter = mapper.toDTO(varEsofagoSaved);

        assertNotNull(varEsofago);
        assertEquals(EsofagoDTO.class, dtoConverter.getClass());
        assertEquals(esofagoDTO.descricao(), dtoConverter.descricao());

        verify(mapper, times(1)).toEntity(esofagoDTO);
        verify(repository, times(1)).save(esofago);
        verify(mapper, times(1)).toDTO(esofago);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(esofagoDTO);
        assertThat(dtoConverter.descricao()).isEqualTo(esofagoDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de esôfago com sucesso")
    void atualizarFraseEsofagoComSucesso() {
        when(repository.findById(2)).thenReturn(Optional.ofNullable(esofago1));
        when(repository.save(esofago1)).thenReturn(esofago1);
        when(mapper.toDTO(esofago1)).thenReturn(esofago1DTO);

        var varEsofago = repository.findById(2);
        if (varEsofago.isPresent()) {
            varEsofago.map(eso -> {
                eso.setDescricao(esofago1DTO.descricao());
                return mapper.toDTO(repository.save(eso));
            });

            assertNotNull(varEsofago);
            assertEquals(EsofagoDTO.class, esofago1DTO.getClass());
            assertEquals(esofago1DTO.id(), varEsofago.get().getId());
            assertEquals(esofago1DTO.descricao(), varEsofago.get().getDescricao());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar frase de esofago por id informado.")
    void apagarFraseEsofagoPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(esofago1));
        doNothing().when(repository).delete(any());

        var varEsofago = repository.findById(2);
        if (varEsofago.isPresent()) {
            repository.delete(varEsofago.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todas as frases de esôfago com sucesso.")
    void listarTodasFrasesEsofagoComSucesso() {
        Pageable pageEsofago = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        when(repository.findAll()).thenReturn(Collections.singletonList(new Esofago()));
        when(mapper.toDTO(esofago1)).thenReturn(esofago1DTO);

        List<Esofago> esofagoList = repository.findAll();
        List<EsofagoDTO> esofagos = new ArrayList<>();
        esofagos.add(mapper.toDTO(esofago1));

        pageDTO = new EsofagoPageDTO(esofagos,
                pageEsofago.getPageNumber(),
                pageEsofago.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), EsofagoPageDTO.class);
        assertEquals(pageDTO.esofagos().get(0).getClass(), EsofagoDTO.class);
        assertEquals(0, pageDTO.totalPages());
        assertEquals(10, pageDTO.totalElements());

        assertEquals(esofago1DTO.id(), pageDTO.esofagos().get(0).id());
        assertEquals(esofago1DTO.descricao(), pageDTO.esofagos().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de esôfago por Id com sucesso.")
    void recuperarFraseEsofagoPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(esofago1));
        when(mapper.toDTO(esofago1)).thenReturn(esofago1DTO);

        Optional<EsofagoDTO> response = repository.findById(esofago1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(esofago1DTO.id(), response.get().id());
            assertEquals(esofago1DTO.descricao(), response.get().descricao());
        } else {
            throw new RecordNotFoundException(esofago1.getId());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar esôfago por um ID invalido.")
    void gerarExcecaoQuandoPesquisarEsofagoPorIdInvalido() {
        when(repository.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            repository.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}