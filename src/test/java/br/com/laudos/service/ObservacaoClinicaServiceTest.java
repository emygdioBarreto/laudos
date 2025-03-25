package br.com.laudos.service;

import br.com.laudos.domain.ObservacaoClinica;
import br.com.laudos.dto.ObservacaoClinicaDTO;
import br.com.laudos.dto.mapper.ObservacaoClinicaMapper;
import br.com.laudos.dto.pages.ObservacaoClinicaPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.ObservacaoClinicaRepository;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObservacaoClinicaServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de observação Clínica não encontrada pelo ID informado.";
    private static final int size = 10;
    private static final int page = 0;

    @InjectMocks
    private ObservacaoClinicaService service;

    @Mock
    private ObservacaoClinicaRepository repository;

    @Mock
    private ObservacaoClinicaMapper mapper;

    @Mock
    private ObservacaoClinicaPageDTO pageDTO;

    private ObservacaoClinica observacaoClinica;
    private ObservacaoClinicaDTO observacaoClinicaDTO;
    private ObservacaoClinica observacaoClinica1;
    private ObservacaoClinicaDTO observacaoClinica1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        observacaoClinica = new ObservacaoClinica(null, "Teste de gravação de frase de ObservacaoClinica");
        observacaoClinicaDTO = new ObservacaoClinicaDTO(null, "Teste de gravação de frase de ObservacaoClinica");
        observacaoClinica1 = new ObservacaoClinica(6, "Gastrite ?");
        observacaoClinica1DTO = new ObservacaoClinicaDTO(6, "Gastrite ? Teste!");
    }

    @Test
    @DisplayName(value = "Salvar frase de observação Clínica com sucesso")
    void salvarFraseDeObservacaoClinicaComSucesso() {
        when(mapper.toEntity(observacaoClinicaDTO)).thenReturn(observacaoClinica);
        when(repository.save(any())).thenReturn(observacaoClinica);
        when(mapper.toDTO(observacaoClinica)).thenReturn(observacaoClinicaDTO);

        var varObservacaoClinica = mapper.toEntity(observacaoClinicaDTO);
        var varObservacaoClinicaSaved = repository.save(varObservacaoClinica);
        var dtoConverter = mapper.toDTO(varObservacaoClinicaSaved);

        assertNotNull(varObservacaoClinica);
        assertEquals(ObservacaoClinicaDTO.class, dtoConverter.getClass());
        assertEquals(observacaoClinicaDTO.descricao(), dtoConverter.descricao());

        verify(mapper, times(1)).toEntity(observacaoClinicaDTO);
        verify(repository, times(1)).save(observacaoClinica);
        verify(mapper, times(1)).toDTO(observacaoClinica);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(observacaoClinicaDTO);
        assertThat(dtoConverter.descricao()).isEqualTo(observacaoClinicaDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de observação Clínica com sucesso")
    void atualizarFraseObservacaoClinicaComSucesso() {
        when(repository.findById(observacaoClinica1.getId())).thenReturn(Optional.ofNullable(observacaoClinica1));
        when(repository.save(observacaoClinica1)).thenReturn(observacaoClinica1);
        when(mapper.toDTO(observacaoClinica1)).thenReturn(observacaoClinica1DTO);

        var varObservacaoClinica = repository.findById(observacaoClinica1.getId());
        if (varObservacaoClinica.isPresent()) {
            varObservacaoClinica.map(obc -> {
                obc.setDescricao(observacaoClinica1DTO.descricao());
                return mapper.toDTO(repository.save(obc));
            });

            assertNotNull(varObservacaoClinica);
            assertEquals(ObservacaoClinicaDTO.class, observacaoClinica1DTO.getClass());
            assertEquals(observacaoClinica1DTO.id(), varObservacaoClinica.get().getId());
            assertEquals(observacaoClinica1DTO.descricao(), varObservacaoClinica.get().getDescricao());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar frase de observação Clínica por id informado.")
    void apagarFraseObservacaoClinicaPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(observacaoClinica1));
        doNothing().when(repository).delete(any());

        var varObservacaoClinica = repository.findById(observacaoClinica1.getId());
        if (varObservacaoClinica.isPresent()) {
            repository.delete(varObservacaoClinica.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todas as frases de observação Clínica com sucesso.")
    void listarTodasFrasesObservacaoClinicaComSucesso() {
        Pageable pageObservacaoClinica = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        when(repository.findAll()).thenReturn(Collections.singletonList(new ObservacaoClinica()));
        when(mapper.toDTO(observacaoClinica1)).thenReturn(observacaoClinica1DTO);

        List<ObservacaoClinica> observacaoClinicaList = repository.findAll();
        List<ObservacaoClinicaDTO> observacoesClinicas = new ArrayList<>();
        observacoesClinicas.add(mapper.toDTO(observacaoClinica1));

        pageDTO = new ObservacaoClinicaPageDTO(observacoesClinicas,
                pageObservacaoClinica.getPageNumber(),
                pageObservacaoClinica.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), ObservacaoClinicaPageDTO.class);
        assertEquals(pageDTO.observacoesClinicas().get(0).getClass(), ObservacaoClinicaDTO.class);
        assertEquals(page, pageDTO.totalPages());
        assertEquals(size, pageDTO.totalElements());

        assertEquals(observacaoClinica1DTO.id(), pageDTO.observacoesClinicas().get(0).id());
        assertEquals(observacaoClinica1DTO.descricao(), pageDTO.observacoesClinicas().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de observação Clínica por Id com sucesso.")
    void recuperarFraseObservacaoClinicaPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(observacaoClinica1));
        when(mapper.toDTO(observacaoClinica1)).thenReturn(observacaoClinica1DTO);

        Optional<ObservacaoClinicaDTO> response = repository.findById(observacaoClinica1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(observacaoClinica1DTO.id(), response.get().id());
            assertEquals(observacaoClinica1DTO.descricao(), response.get().descricao());
        } else {
            throw new RecordNotFoundException(observacaoClinica1.getId());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar observação Clínica por um ID invalido.")
    void gerarExcecaoQuandoPesquisarObservacaoClinicaPorIdInvalido() {
        when(repository.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            repository.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}