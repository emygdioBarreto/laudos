package br.com.laudos.service;

import br.com.laudos.domain.Resumo;
import br.com.laudos.dto.ResumoDTO;
import br.com.laudos.dto.mapper.ResumoMapper;
import br.com.laudos.dto.pages.ResumoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.ResumoRepository;
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
class ResumoServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de resumo clínico não encontrada pelo ID informado.";
    private static final int size = 10;
    private static final int page = 0;

    @InjectMocks
    private ResumoService service;

    @Mock
    private ResumoRepository repository;

    @Mock
    private ResumoMapper mapper;

    @Mock
    private ResumoPageDTO pageDTO;

    private Resumo resumo;
    private ResumoDTO resumoDTO;
    private Resumo resumo1;
    private ResumoDTO resumo1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        resumo = new Resumo(null, "Teste de gravação de frase de Resumo");
        resumoDTO = new ResumoDTO(null, "Teste de gravação de frase de Resumo");
        resumo1 = new Resumo(2, "resumo clinico Inexistente 39");
        resumo1DTO = new ResumoDTO(2, "resumo clinico Inexistente 39 Teste!");
    }

    @Test
    @DisplayName(value = "Salvar frase de resumo clínico com sucesso")
    void salvarFraseDeResumoComSucesso() {
        when(mapper.toEntity(resumoDTO)).thenReturn(resumo);
        when(repository.save(any())).thenReturn(resumo);
        when(mapper.toDTO(resumo)).thenReturn(resumoDTO);

        var varResumo = mapper.toEntity(resumoDTO);
        var varResumoSaved = repository.save(varResumo);
        var dtoConverter = mapper.toDTO(varResumoSaved);

        assertNotNull(varResumo);
        assertEquals(ResumoDTO.class, dtoConverter.getClass());
        assertEquals(resumoDTO.descricao(), dtoConverter.descricao());

        verify(mapper, times(1)).toEntity(resumoDTO);
        verify(repository, times(1)).save(resumo);
        verify(mapper, times(1)).toDTO(resumo);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(resumoDTO);
        assertThat(dtoConverter.descricao()).isEqualTo(resumoDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de resumo clínico com sucesso")
    void atualizarFraseResumoComSucesso() {
        when(repository.findById(resumo1.getId())).thenReturn(Optional.ofNullable(resumo1));
        when(repository.save(resumo1)).thenReturn(resumo1);
        when(mapper.toDTO(resumo1)).thenReturn(resumo1DTO);

        var varResumo = repository.findById(resumo1.getId());
        if (varResumo.isPresent()) {
            varResumo.map(duo -> {
                duo.setDescricao(resumo1DTO.descricao());
                return mapper.toDTO(repository.save(duo));
            });

            assertNotNull(varResumo);
            assertEquals(ResumoDTO.class, resumo1DTO.getClass());
            assertEquals(resumo1DTO.id(), varResumo.get().getId());
            assertEquals(resumo1DTO.descricao(), varResumo.get().getDescricao());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar frase de resumo clínico por id informado.")
    void apagarFraseResumoPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(resumo1));
        doNothing().when(repository).delete(any());

        var varResumo = repository.findById(resumo1.getId());
        if (varResumo.isPresent()) {
            repository.delete(varResumo.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todas as frases de resumo clínico com sucesso.")
    void listarTodasFrasesResumoComSucesso() {
        Pageable pageResumo = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        when(repository.findAll()).thenReturn(Collections.singletonList(new Resumo()));
        when(mapper.toDTO(resumo1)).thenReturn(resumo1DTO);

        List<Resumo> resumoList = repository.findAll();
        List<ResumoDTO> resumos = new ArrayList<>();
        resumos.add(mapper.toDTO(resumo1));

        pageDTO = new ResumoPageDTO(resumos,
                pageResumo.getPageNumber(),
                pageResumo.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), ResumoPageDTO.class);
        assertEquals(pageDTO.resumos().get(0).getClass(), ResumoDTO.class);
        assertEquals(page, pageDTO.totalPages());
        assertEquals(size, pageDTO.totalElements());

        assertEquals(resumo1DTO.id(), pageDTO.resumos().get(0).id());
        assertEquals(resumo1DTO.descricao(), pageDTO.resumos().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de resumo clínico por Id com sucesso.")
    void recuperarFraseResumoPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(resumo1));
        when(mapper.toDTO(resumo1)).thenReturn(resumo1DTO);

        Optional<ResumoDTO> response = repository.findById(resumo1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(resumo1DTO.id(), response.get().id());
            assertEquals(resumo1DTO.descricao(), response.get().descricao());
        } else {
            throw new RecordNotFoundException(resumo1.getId());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar resumo por um ID invalido.")
    void gerarExcecaoQuandoPesquisarResumoPorIdInvalido() {
        when(repository.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            repository.findById(1);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}