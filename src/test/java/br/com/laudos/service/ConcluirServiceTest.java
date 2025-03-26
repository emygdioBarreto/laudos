package br.com.laudos.service;

import br.com.laudos.domain.Concluir;
import br.com.laudos.dto.ConcluirDTO;
import br.com.laudos.dto.mapper.ConcluirMapper;
import br.com.laudos.dto.pages.ConcluirPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.ConcluirRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcluirServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de conclusão não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private ConcluirService service;

    @Mock
    private ConcluirRepository repository;

    @Mock
    private ConcluirMapper mapper;

    @Mock
    private ConcluirPageDTO pageDTO;

    private Concluir concluir;
    private ConcluirDTO concluirDTO;
    private Concluir concluir1;
    private ConcluirDTO concluir1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        concluir = new Concluir(null, "Teste de gravação de frase de conclusão.");
        concluirDTO = new ConcluirDTO(null, "Teste de gravação de frase de conclusão.");
        concluir1 = new Concluir(1, "Exame normal do ponto de vista endoscópico.");
        concluir1DTO = new ConcluirDTO(1, "Exame normal do ponto de vista endoscópico2.");
    }

    @Test
    @DisplayName(value = "Salvar frase de conclusão com sucesso")
    void salvarFraseDeConclusaoComSucesso() {
        boolean fraseJaExiste = repository.existsByConclusao(concluir1DTO.conclusao());
        if (fraseJaExiste) {
            throw new RecordNotFoundException("Frase de Conclusão já cadastrada! (" + concluir1DTO.conclusao() + ")");
        }

        when(mapper.toEntity(concluirDTO)).thenReturn(concluir);
        when(repository.save(any())).thenReturn(concluir);
        when(mapper.toDTO(concluir)).thenReturn(concluirDTO);

        Concluir varConcluir = mapper.toEntity(concluirDTO);
        Concluir varConcluirSaved = repository.save(varConcluir);
        ConcluirDTO dtoConverter = mapper.toDTO(varConcluirSaved);

        assertNotNull(varConcluir);
        assertEquals(ConcluirDTO.class, dtoConverter.getClass());
        assertEquals(concluirDTO.conclusao(), dtoConverter.conclusao());

        verify(mapper, times(1)).toEntity(concluirDTO);
        verify(repository, times(1)).save(concluir);
        verify(mapper, times(1)).toDTO(concluir);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(concluirDTO);
        assertThat(dtoConverter.conclusao()).isEqualTo(concluirDTO.conclusao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de conclusão com sucesso")
    void atualizarFraseConclusaoComSucesso() {
        when(repository.findById(1)).thenReturn(Optional.ofNullable(concluir1));
        when(repository.save(concluir1)).thenReturn(concluir1);
        when(mapper.toDTO(concluir1)).thenReturn(concluir1DTO);

        var varConcluir = repository.findById(1);
        if (varConcluir.isPresent()) {
            varConcluir.map(c -> {
                c.setConclusao(concluir1DTO.conclusao());
                return mapper.toDTO(repository.save(c));
            });

            assertNotNull(varConcluir);
            assertEquals(ConcluirDTO.class, concluir1DTO.getClass());
            assertEquals(1, varConcluir.get().getId());
            assertEquals("Exame normal do ponto de vista endoscópico2.", varConcluir.get().getConclusao());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar frase de conclusão por id informado.")
    void apagarFraseConcluirPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(concluir1));
        doNothing().when(repository).delete(any());

        var varConcluir = repository.findById(1);
        if (varConcluir.isPresent()) {
            repository.delete(varConcluir.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todas as frases de conclusão com sucesso.")
    void listarTodasFrasesConclusaoComSucesso() {
        Pageable pageConcluir = PageRequest.of(PAGE, SIZE, Sort.by(Sort.Direction.ASC, "id"));

        when(repository.findAll()).thenReturn(Collections.singletonList(concluir1));
        when(mapper.toDTO(concluir1)).thenReturn(concluir1DTO);

        List<ConcluirDTO> conclusoes = new ArrayList<>();
        Concluir concluirList = repository.findAll().get(0);
        conclusoes.add(mapper.toDTO(concluirList));

        pageDTO = new ConcluirPageDTO(conclusoes,
                pageConcluir.getPageNumber(),
                pageConcluir.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), ConcluirPageDTO.class);
        assertEquals(pageDTO.conclusoes().get(0).getClass(), ConcluirDTO.class);
        assertEquals(PAGE, pageDTO.totalPages());
        assertEquals(SIZE, pageDTO.totalElements());

        assertEquals(concluir1DTO.id(), pageDTO.conclusoes().get(0).id());
        assertEquals(concluir1DTO.conclusao(), pageDTO.conclusoes().get(0).conclusao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de conclusão por Id com sucesso.")
    void recuperarFraseConclusaoPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(concluir1));
        when(mapper.toDTO(concluir1)).thenReturn(concluir1DTO);

        Optional<ConcluirDTO> response = repository.findById(concluir1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(concluir1DTO.id(), response.get().id());
            assertEquals(concluir1DTO.conclusao(), response.get().conclusao());
        } else {
            throw new RecordNotFoundException(concluir1.getId());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar conclusao por um ID invalido.")
    void gerarExcecaoQuandoPesquisarConclusaoPorIdInvalido() {
        when(repository.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            repository.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}