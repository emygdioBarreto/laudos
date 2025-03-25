package br.com.laudos.service;

import br.com.laudos.domain.Pancreas;
import br.com.laudos.dto.PancreasDTO;
import br.com.laudos.dto.mapper.PancreasMapper;
import br.com.laudos.dto.pages.PancreasPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.PancreasRepository;
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
class PancreasServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de pancreas não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private PancreasService service;

    @Mock
    private PancreasRepository repository;

    @Mock
    private PancreasMapper mapper;

    @Mock
    private PancreasPageDTO pageDTO;

    private Pancreas pancreas;
    private PancreasDTO pancreasDTO;
    private Pancreas pancreas1;
    private PancreasDTO pancreas1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pancreas = new Pancreas(null, "Teste de gravação de frase de Pancreas");
        pancreasDTO = new PancreasDTO(null, "Teste de gravação de frase de Pancreas");
        pancreas1 = new Pancreas(5, "teste de frase de pancreas");
        pancreas1DTO = new PancreasDTO(5, "teste de frase de pancreas Teste!");
    }

    @Test
    @DisplayName(value = "Salvar frase de pancreas com sucesso")
    void salvarFraseDePancreasComSucesso() {
        when(mapper.toEntity(pancreasDTO)).thenReturn(pancreas);
        when(repository.save(any())).thenReturn(pancreas);
        when(mapper.toDTO(pancreas)).thenReturn(pancreasDTO);

        var varPancreas = mapper.toEntity(pancreasDTO);
        var varPancreasSaved = repository.save(varPancreas);
        var dtoConverter = mapper.toDTO(varPancreasSaved);

        assertNotNull(varPancreas);
        assertEquals(PancreasDTO.class, dtoConverter.getClass());
        assertEquals(pancreasDTO.descricao(), dtoConverter.descricao());

        verify(mapper, times(1)).toEntity(pancreasDTO);
        verify(repository, times(1)).save(pancreas);
        verify(mapper, times(1)).toDTO(pancreas);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(pancreasDTO);
        assertThat(dtoConverter.descricao()).isEqualTo(pancreasDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de pancreas com sucesso")
    void atualizarFrasePancreasComSucesso() {
        when(repository.findById(pancreas1.getId())).thenReturn(Optional.ofNullable(pancreas1));
        when(repository.save(pancreas1)).thenReturn(pancreas1);
        when(mapper.toDTO(pancreas1)).thenReturn(pancreas1DTO);

        var varPancreas = repository.findById(pancreas1.getId());
        if (varPancreas.isPresent()) {
            varPancreas.map(duo -> {
                duo.setDescricao(pancreas1DTO.descricao());
                return mapper.toDTO(repository.save(duo));
            });

            assertNotNull(varPancreas);
            assertEquals(PancreasDTO.class, pancreas1DTO.getClass());
            assertEquals(pancreas1DTO.id(), varPancreas.get().getId());
            assertEquals(pancreas1DTO.descricao(), varPancreas.get().getDescricao());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar frase de pancreas por id informado.")
    void apagarFrasePancreasPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(pancreas1));
        doNothing().when(repository).delete(any());

        var varPancreas = repository.findById(pancreas1.getId());
        if (varPancreas.isPresent()) {
            repository.delete(varPancreas.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todas as frases de pancreas com sucesso.")
    void listarTodasFrasesPancreasComSucesso() {
        Pageable pagePancreas = PageRequest.of(PAGE, SIZE, Sort.by(Sort.Direction.ASC, "id"));

        when(repository.findAll()).thenReturn(Collections.singletonList(new Pancreas()));
        when(mapper.toDTO(pancreas1)).thenReturn(pancreas1DTO);

        List<Pancreas> pancreasList = repository.findAll();
        List<PancreasDTO> pancreas = new ArrayList<>();
        pancreas.add(mapper.toDTO(pancreas1));

        pageDTO = new PancreasPageDTO(pancreas,
                pagePancreas.getPageNumber(),
                pagePancreas.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), PancreasPageDTO.class);
        assertEquals(pageDTO.pancreass().get(0).getClass(), PancreasDTO.class);
        assertEquals(PAGE, pageDTO.totalPages());
        assertEquals(SIZE, pageDTO.totalElements());

        assertEquals(pancreas1DTO.id(), pageDTO.pancreass().get(0).id());
        assertEquals(pancreas1DTO.descricao(), pageDTO.pancreass().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de pancreas por Id com sucesso.")
    void recuperarFrasePancreasPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(pancreas1));
        when(mapper.toDTO(pancreas1)).thenReturn(pancreas1DTO);

        Optional<PancreasDTO> response = repository.findById(pancreas1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(pancreas1DTO.id(), response.get().id());
            assertEquals(pancreas1DTO.descricao(), response.get().descricao());
        } else {
            throw new RecordNotFoundException(pancreas1.getId());
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