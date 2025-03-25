package br.com.laudos.service;

import br.com.laudos.domain.TipoExame;
import br.com.laudos.dto.TipoExameDTO;
import br.com.laudos.dto.mapper.TipoExameMapper;
import br.com.laudos.dto.pages.TipoExamePageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.TipoExameRepository;
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
class TipoExameServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Tipo de exame não encontrado pelo ID informado.";
    private static final int size = 10;
    private static final int page = 0;

    @InjectMocks
    private TipoExameService service;

    @Mock
    private TipoExameRepository repository;

    @Mock
    private TipoExameMapper mapper;

    @Mock
    private TipoExamePageDTO pageDTO;

    private TipoExame tipoExame;
    private TipoExameDTO tipoExameDTO;
    private TipoExame tipoExame1;
    private TipoExameDTO tipoExame1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tipoExame = new TipoExame(null, "Teste de gravação de Tipos de Exames", "C", false, false, false, false, false);
        tipoExameDTO = new TipoExameDTO(null, "Teste de gravação de Tipos de Exames", "C", false, false, false, false, false);
        tipoExame1 = new TipoExame(1, "Endoscopia Digestiva Alta", "C", true, true, true, false, false);
        tipoExame1DTO = new TipoExameDTO(1, "Endoscopia Digestiva Alta Teste!", "C", true, true, true, false, false);
    }

    @Test
    @DisplayName(value = "Salvar tipo de exame com sucesso")
    void salvarFraseDeTipoExameComSucesso() {
        when(mapper.toEntity(tipoExameDTO)).thenReturn(tipoExame);
        when(repository.save(any())).thenReturn(tipoExame);
        when(mapper.toDTO(tipoExame)).thenReturn(tipoExameDTO);

        var varTipoExame = mapper.toEntity(tipoExameDTO);
        var varTipoExameSaved = repository.save(varTipoExame);
        var dtoConverter = mapper.toDTO(varTipoExameSaved);

        assertNotNull(varTipoExame);
        assertEquals(TipoExameDTO.class, dtoConverter.getClass());
        assertEquals(tipoExameDTO.descricao(), dtoConverter.descricao());

        verify(mapper, times(1)).toEntity(tipoExameDTO);
        verify(repository, times(1)).save(tipoExame);
        verify(mapper, times(1)).toDTO(tipoExame);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(tipoExameDTO);
        assertThat(dtoConverter.descricao()).isEqualTo(tipoExameDTO.descricao());
        assertThat(dtoConverter.ordena()).isEqualTo(tipoExameDTO.ordena());
        assertThat(dtoConverter.esofago()).isEqualTo(tipoExameDTO.esofago());
        assertThat(dtoConverter.estomago()).isEqualTo(tipoExameDTO.estomago());
        assertThat(dtoConverter.duodeno()).isEqualTo(tipoExameDTO.duodeno());
        assertThat(dtoConverter.intestino()).isEqualTo(tipoExameDTO.intestino());
        assertThat(dtoConverter.pancreas()).isEqualTo(tipoExameDTO.pancreas());
    }

    @Test
    @DisplayName(value = "Atualizar tipo de exame com sucesso")
    void atualizarFraseTipoExameComSucesso() {
        when(repository.findById(tipoExame1.getId())).thenReturn(Optional.ofNullable(tipoExame1));
        when(repository.save(tipoExame1)).thenReturn(tipoExame1);
        when(mapper.toDTO(tipoExame1)).thenReturn(tipoExame1DTO);

        var varTipoExame = repository.findById(tipoExame1.getId());
        if (varTipoExame.isPresent()) {
            varTipoExame.map(tpe -> {
                tpe.setDescricao(tipoExame1DTO.descricao());
                tpe.setOrdena(tipoExame1DTO.ordena());
                tpe.setEsofago(tipoExame1DTO.esofago());
                tpe.setEstomago(tipoExame1DTO.estomago());
                tpe.setDuodeno(tipoExame1DTO.duodeno());
                tpe.setIntestino(tipoExame1DTO.intestino());
                tpe.setPancreas(tipoExame1DTO.pancreas());
                return mapper.toDTO(repository.save(tpe));
            });

            assertNotNull(varTipoExame);
            assertEquals(TipoExameDTO.class, tipoExame1DTO.getClass());
            assertEquals(tipoExame1DTO.id(), varTipoExame.get().getId());
            assertEquals(tipoExame1DTO.descricao(), varTipoExame.get().getDescricao());
            assertEquals(tipoExame1DTO.ordena(), varTipoExame.get().getOrdena());
            assertEquals(tipoExame1DTO.esofago(), varTipoExame.get().isEsofago());
            assertEquals(tipoExame1DTO.estomago(), varTipoExame.get().isEstomago());
            assertEquals(tipoExame1DTO.duodeno(), varTipoExame.get().isDuodeno());
            assertEquals(tipoExame1DTO.intestino(), varTipoExame.get().isIntestino());
            assertEquals(tipoExame1DTO.pancreas(), varTipoExame.get().isPancreas());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar tipo de exame por id informado.")
    void apagarFraseTipoExamePorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(tipoExame1));
        doNothing().when(repository).delete(any());

        var varTipoExame = repository.findById(tipoExame1.getId());
        if (varTipoExame.isPresent()) {
            repository.delete(varTipoExame.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todos os tipo de exame com sucesso.")
    void listarTodasFrasesTipoExameComSucesso() {
        Pageable pageTipoExame = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        when(repository.findAll()).thenReturn(Collections.singletonList(new TipoExame()));
        when(mapper.toDTO(tipoExame1)).thenReturn(tipoExame1DTO);

        List<TipoExame> tipoExameList = repository.findAll();
        List<TipoExameDTO> tipoexames = new ArrayList<>();
        tipoexames.add(mapper.toDTO(tipoExame1));

        pageDTO = new TipoExamePageDTO(tipoexames,
                pageTipoExame.getPageNumber(),
                pageTipoExame.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), TipoExamePageDTO.class);
        assertEquals(pageDTO.tipoexames().get(0).getClass(), TipoExameDTO.class);
        assertEquals(page, pageDTO.totalPages());
        assertEquals(size, pageDTO.totalElements());

        assertEquals(tipoExame1DTO.id(), pageDTO.tipoexames().get(0).id());
        assertEquals(tipoExame1DTO.descricao(), pageDTO.tipoexames().get(0).descricao());
        assertEquals(tipoExame1DTO.ordena(), pageDTO.tipoexames().get(0).ordena());
        assertEquals(tipoExame1DTO.esofago(), pageDTO.tipoexames().get(0).esofago());
        assertEquals(tipoExame1DTO.estomago(), pageDTO.tipoexames().get(0).estomago());
        assertEquals(tipoExame1DTO.duodeno(), pageDTO.tipoexames().get(0).duodeno());
        assertEquals(tipoExame1DTO.intestino(), pageDTO.tipoexames().get(0).intestino());
        assertEquals(tipoExame1DTO.pancreas(), pageDTO.tipoexames().get(0).pancreas());
    }

    @Test
    @DisplayName(value = "Recuperar tipo de exame por Id com sucesso.")
    void recuperarFraseTipoExamePorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(tipoExame1));
        when(mapper.toDTO(tipoExame1)).thenReturn(tipoExame1DTO);

        Optional<TipoExameDTO> response = repository.findById(tipoExame1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(tipoExame1DTO.id(), response.get().id());
            assertEquals(tipoExame1DTO.descricao(), response.get().descricao());
            assertEquals(tipoExame1DTO.ordena(), response.get().ordena());
            assertEquals(tipoExame1DTO.esofago(), response.get().esofago());
            assertEquals(tipoExame1DTO.estomago(), response.get().estomago());
            assertEquals(tipoExame1DTO.duodeno(), response.get().duodeno());
            assertEquals(tipoExame1DTO.intestino(), response.get().intestino());
            assertEquals(tipoExame1DTO.pancreas(), response.get().pancreas());
        } else {
            throw new RecordNotFoundException(tipoExame1.getId());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar tipo de exame por um ID invalido.")
    void gerarExcecaoQuandoPesquisarTipoExamePorIdInvalido() {
        when(repository.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            repository.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}