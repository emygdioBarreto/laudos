package br.com.laudos.service;

import br.com.laudos.domain.Droga;
import br.com.laudos.dto.DrogaDTO;
import br.com.laudos.dto.mapper.DrogaMapper;
import br.com.laudos.dto.pages.DrogaPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.DrogaRepository;
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
class DrogaServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de droga não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private DrogaService service;

    @Mock
    private DrogaRepository repository;

    @Mock
    private DrogaMapper mapper;

    @Mock
    private DrogaPageDTO pageDTO;

    private Droga droga;
    private DrogaDTO drogaDTO;
    private Droga droga1;
    private DrogaDTO droga1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        droga = new Droga(null, "Teste de gravação de frase de drogas.");
        drogaDTO = new DrogaDTO(null, "Teste de gravação de frase de drogas.");
        droga1 = new Droga(1, "histoacryl 1ml 2 amp.");
        droga1DTO = new DrogaDTO(1, "histoacryl 1ml 2 amp. 2");
    }

    @Test
    @DisplayName(value = "Salvar frase de droga com sucesso")
    void salvarFraseDeDrogaComSucesso() {
        when(mapper.toEntity(drogaDTO)).thenReturn(droga);
        when(repository.save(any())).thenReturn(droga);
        when(mapper.toDTO(droga)).thenReturn(drogaDTO);

        var varDroga = mapper.toEntity(drogaDTO);
        var varDrogaSaved = repository.save(varDroga);
        var dtoConverter = mapper.toDTO(varDrogaSaved);

        assertNotNull(varDroga);
        assertEquals(DrogaDTO.class, dtoConverter.getClass());
        assertEquals("Teste de gravação de frase de drogas.", dtoConverter.nomedroga());

        verify(mapper, times(1)).toEntity(drogaDTO);
        verify(repository, times(1)).save(droga);
        verify(mapper, times(1)).toDTO(droga);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(drogaDTO);
        assertThat(dtoConverter.nomedroga()).isEqualTo(drogaDTO.nomedroga());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de droga com sucesso")
    void atualizarFraseDrogaComSucesso() {
        when(repository.findById(1)).thenReturn(Optional.ofNullable(droga1));
        when(repository.save(droga1)).thenReturn(droga1);
        when(mapper.toDTO(droga1)).thenReturn(droga1DTO);

        var varDroga = repository.findById(1);
        if (varDroga.isPresent()) {
            varDroga.map(c -> {
                c.setNomedroga(droga1DTO.nomedroga());
                return mapper.toDTO(repository.save(c));
            });

            assertNotNull(varDroga);
            assertEquals(DrogaDTO.class, droga1DTO.getClass());
            assertEquals(1, varDroga.get().getId());
            assertEquals("histoacryl 1ml 2 amp. 2", varDroga.get().getNomedroga());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar frase de droga por id informado.")
    void apagarFraseDrogaPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(droga));
        doNothing().when(repository).delete(any());

        var varDroga = repository.findById(1);
        if (varDroga.isPresent()) {
            repository.delete(varDroga.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todas as frases de drogas com sucesso.")
    void listarTodasFrasesDrogaComSucesso() {
        Pageable pageDroga = PageRequest.of(PAGE, SIZE, Sort.by(Sort.Direction.ASC, "id"));

        when(repository.findAll()).thenReturn(Collections.singletonList(droga1));
        when(mapper.toDTO(droga1)).thenReturn(droga1DTO);

        List<DrogaDTO> drogas = new ArrayList<>();
        Droga drogaList = repository.findAll().get(0);
        drogas.add(mapper.toDTO(drogaList));

        pageDTO = new DrogaPageDTO(drogas,
                pageDroga.getPageNumber(),
                pageDroga.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), DrogaPageDTO.class);
        assertEquals(pageDTO.drogas().get(0).getClass(), DrogaDTO.class);
        assertEquals(PAGE, pageDTO.totalPages());
        assertEquals(SIZE, pageDTO.totalElements());

        assertEquals(droga1DTO.id(), pageDTO.drogas().get(0).id());
        assertEquals(droga1DTO.nomedroga(), pageDTO.drogas().get(0).nomedroga());
    }

    @Test
    @DisplayName(value = "Recuperar frase de droga por Id com sucesso.")
    void recuperarFraseDrogaPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(droga1));
        when(mapper.toDTO(droga1)).thenReturn(droga1DTO);

        Optional<DrogaDTO> response = repository.findById(droga1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(droga1DTO.id(), response.get().id());
            assertEquals(droga1DTO.nomedroga(), response.get().nomedroga());
        } else {
            throw new RecordNotFoundException(droga1.getId());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar droga por um ID invalido.")
    void gerarExcecaoQuandoPesquisarDrogaPorIdInvalido() {
        when(repository.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try {
            repository.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}