package br.com.laudos.service;

import br.com.laudos.domain.Pancrea;
import br.com.laudos.dto.PancreaDTO;
import br.com.laudos.dto.mapper.PancreaMapper;
import br.com.laudos.dto.pages.PancreaPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.PancreaRepository;
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
class PancreaServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de pancreas não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private PancreaService service;

    @Mock
    private PancreaRepository repository;

    @Mock
    private PancreaMapper mapper;

    @Mock
    private PancreaPageDTO pageDTO;

    private Pancrea pancrea;
    private PancreaDTO pancreaDTO;
    private Pancrea pancrea1;
    private PancreaDTO pancrea1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pancrea = new Pancrea(null, "Teste de gravação de frase de Pancreas");
        pancreaDTO = new PancreaDTO(null, "Teste de gravação de frase de Pancreas");
        pancrea1 = new Pancrea(5, "teste de frase de pancreas");
        pancrea1DTO = new PancreaDTO(5, "teste de frase de pancreas Teste!");
    }

    @Test
    @DisplayName(value = "Salvar frase de pancreas com sucesso")
    void salvarFraseDePancreasComSucesso() {
        when(mapper.toEntity(pancreaDTO)).thenReturn(pancrea);
        when(repository.save(any())).thenReturn(pancrea);
        when(mapper.toDTO(pancrea)).thenReturn(pancreaDTO);

        var varPancrea = mapper.toEntity(pancreaDTO);
        var varPancreaSaved = repository.save(varPancrea);
        var dtoConverter = mapper.toDTO(varPancreaSaved);

        assertNotNull(varPancrea);
        assertEquals(PancreaDTO.class, dtoConverter.getClass());
        assertEquals(pancreaDTO.descricao(), dtoConverter.descricao());

        verify(mapper, times(1)).toEntity(pancreaDTO);
        verify(repository, times(1)).save(pancrea);
        verify(mapper, times(1)).toDTO(pancrea);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(pancreaDTO);
        assertThat(dtoConverter.descricao()).isEqualTo(pancreaDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de pancreas com sucesso")
    void atualizarFrasePancreasComSucesso() {
        when(repository.findById(pancrea1.getId())).thenReturn(Optional.ofNullable(pancrea1));
        when(repository.save(pancrea1)).thenReturn(pancrea1);
        when(mapper.toDTO(pancrea1)).thenReturn(pancrea1DTO);

        var varPancreas = repository.findById(pancrea1.getId());
        if (varPancreas.isPresent()) {
            varPancreas.map(duo -> {
                duo.setDescricao(pancrea1DTO.descricao());
                return mapper.toDTO(repository.save(duo));
            });

            assertNotNull(varPancreas);
            assertEquals(PancreaDTO.class, pancrea1DTO.getClass());
            assertEquals(pancrea1DTO.id(), varPancreas.get().getId());
            assertEquals(pancrea1DTO.descricao(), varPancreas.get().getDescricao());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar frase de pancreas por id informado.")
    void apagarFrasePancreasPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(pancrea1));
        doNothing().when(repository).delete(any());

        var varPancrea = repository.findById(pancrea1.getId());
        if (varPancrea.isPresent()) {
            repository.delete(varPancrea.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todas as frases de pancreas com sucesso.")
    void listarTodasFrasesPancreasComSucesso() {
        Pageable pagePancreas = PageRequest.of(PAGE, SIZE, Sort.by(Sort.Direction.ASC, "id"));

        when(repository.findAll()).thenReturn(Collections.singletonList(pancrea1));
        when(mapper.toDTO(pancrea1)).thenReturn(pancrea1DTO);

        List<PancreaDTO> pancreas = new ArrayList<>();
        Pancrea pancreaList = repository.findAll().get(0);
        pancreas.add(mapper.toDTO(pancreaList));

        pageDTO = new PancreaPageDTO(pancreas,
                pagePancreas.getPageNumber(),
                pagePancreas.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), PancreaPageDTO.class);
        assertEquals(pageDTO.pancreas().get(0).getClass(), PancreaDTO.class);
        assertEquals(PAGE, pageDTO.totalPages());
        assertEquals(SIZE, pageDTO.totalElements());

        assertEquals(pancrea1DTO.id(), pageDTO.pancreas().get(0).id());
        assertEquals(pancrea1DTO.descricao(), pageDTO.pancreas().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de pancreas por Id com sucesso.")
    void recuperarFrasePancreasPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(pancrea1));
        when(mapper.toDTO(pancrea1)).thenReturn(pancrea1DTO);

        Optional<PancreaDTO> response = repository.findById(pancrea1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(pancrea1DTO.id(), response.get().id());
            assertEquals(pancrea1DTO.descricao(), response.get().descricao());
        } else {
            throw new RecordNotFoundException(pancrea1.getId());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar pancreas por um ID invalido.")
    void gerarExcecaoQuandoPesquisarPancreasPorIdInvalido() {
        when(repository.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            repository.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}