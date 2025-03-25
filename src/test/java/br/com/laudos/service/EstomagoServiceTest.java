package br.com.laudos.service;

import br.com.laudos.domain.Estomago;
import br.com.laudos.dto.EstomagoDTO;
import br.com.laudos.dto.mapper.EstomagoMapper;
import br.com.laudos.dto.pages.EstomagoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.EstomagoRepository;
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
class EstomagoServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de estômago não encontrada pelo ID informado.";
    private static final int size = 10;
    private static final int page = 0;

    @InjectMocks
    private EstomagoService service;

    @Mock
    private EstomagoRepository repository;

    @Mock
    private EstomagoMapper mapper;

    @Mock
    private EstomagoPageDTO pageDTO;

    private Estomago estomago;
    private EstomagoDTO estomagoDTO;
    private Estomago estomago1;
    private EstomagoDTO estomago1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        estomago = new Estomago(null, "Teste de gravação de frase de estomago");
        estomagoDTO = new EstomagoDTO(null, "Teste de gravação de frase de estomago");
        estomago1 = new Estomago(1, "Pequena quantidade de líquido de estase claro");
        estomago1DTO = new EstomagoDTO(1, "Pequena quantidade de líquido de estase claro. Teste");
    }

    @Test
    @DisplayName(value = "Salvar frase de estômago com sucesso")
    void salvarFraseEstomagoComSucesso() {
        when(mapper.toEntity(estomagoDTO)).thenReturn(estomago);
        when(repository.save(any())).thenReturn(estomago);
        when(mapper.toDTO(estomago)).thenReturn(estomagoDTO);

        var varEstomago = mapper.toEntity(estomagoDTO);
        var varEstomagoSaved = repository.save(varEstomago);
        var dtoConverter = mapper.toDTO(varEstomagoSaved);

        assertNotNull(varEstomago);
        assertEquals(EstomagoDTO.class, dtoConverter.getClass());
        assertEquals(estomagoDTO.descricao(), dtoConverter.descricao());

        verify(mapper, times(1)).toEntity(estomagoDTO);
        verify(repository, times(1)).save(estomago);
        verify(mapper, times(1)).toDTO(estomago);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(estomagoDTO);
        assertThat(dtoConverter.descricao()).isEqualTo(estomagoDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de estômago com sucesso")
    void atualizarFraseEstomagoComSucesso() {
        when(repository.findById(1)).thenReturn(Optional.ofNullable(estomago1));
        when(repository.save(estomago1)).thenReturn(estomago1);
        when(mapper.toDTO(estomago1)).thenReturn(estomago1DTO);

        var varEstomago = repository.findById(1);
        if (varEstomago.isPresent()) {
            varEstomago.map(est -> {
                est.setDescricao(estomago1DTO.descricao());
                return mapper.toDTO(repository.save(est));
            });

            assertNotNull(varEstomago);
            assertEquals(EstomagoDTO.class, estomago1DTO.getClass());
            assertEquals(estomago1DTO.id(), varEstomago.get().getId());
            assertEquals(estomago1DTO.descricao(), varEstomago.get().getDescricao());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar frase de estômago por id informado.")
    void apagarFraseEstomagoPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(estomago1));
        doNothing().when(repository).delete(any());

        var varEstomago = repository.findById(1);
        if (varEstomago.isPresent()) {
            repository.delete(varEstomago.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todas as frases de estômago com sucesso.")
    void listarTodasFrasesEstomagoComSucesso() {
        Pageable pageEstomago = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        when(repository.findAll()).thenReturn(Collections.singletonList(new Estomago()));
        when(mapper.toDTO(estomago1)).thenReturn(estomago1DTO);

        List<Estomago> estomagoList = repository.findAll();
        List<EstomagoDTO> estomagos = new ArrayList<>();
        estomagos.add(mapper.toDTO(estomago1));

        pageDTO = new EstomagoPageDTO(estomagos,
                pageEstomago.getPageNumber(),
                pageEstomago.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), EstomagoPageDTO.class);
        assertEquals(pageDTO.estomagos().get(0).getClass(), EstomagoDTO.class);
        assertEquals(0, pageDTO.totalPages());
        assertEquals(10, pageDTO.totalElements());

        assertEquals(estomago1DTO.id(), pageDTO.estomagos().get(0).id());
        assertEquals(estomago1DTO.descricao(), pageDTO.estomagos().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de estômago por Id com sucesso.")
    void recuperarFraseEstomagoPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(estomago1));
        when(mapper.toDTO(estomago1)).thenReturn(estomago1DTO);

        Optional<EstomagoDTO> response = repository.findById(estomago1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(estomago1DTO.id(), response.get().id());
            assertEquals(estomago1DTO.descricao(), response.get().descricao());
        } else {
            throw new RecordNotFoundException(estomago1.getId());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar estômago por um ID invalido.")
    void gerarExcecaoQuandoPesquisarEstomagoPorIdInvalido() {
        when(repository.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            repository.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}