package br.com.laudos.service;

import br.com.laudos.domain.Observacao;
import br.com.laudos.dto.ObservacaoDTO;
import br.com.laudos.dto.mapper.ObservacaoMapper;
import br.com.laudos.dto.pages.ObservacaoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.ObservacaoRepository;
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
class ObservacaoServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de observação não encontrada pelo ID informado.";
    private static final int size = 10;
    private static final int page = 0;

    @InjectMocks
    private ObservacaoService service;

    @Mock
    private ObservacaoRepository repository;

    @Mock
    private ObservacaoMapper mapper;

    @Mock
    private ObservacaoPageDTO pageDTO;

    private Observacao observacao;
    private ObservacaoDTO observacaoDTO;
    private Observacao observacao1;
    private ObservacaoDTO observacao1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        observacao = new Observacao(null, "Teste de gravação de frase de Observacao");
        observacaoDTO = new ObservacaoDTO(null, "Teste de gravação de frase de Observacao");
        observacao1 = new Observacao(8, "Não foram realizadas biópsias.");
        observacao1DTO = new ObservacaoDTO(8, "Não foram realizadas biópsias. Teste!");
    }

    @Test
    @DisplayName(value = "Salvar frase de observação com sucesso")
    void salvarFraseDeObservacaoComSucesso() {
        when(mapper.toEntity(observacaoDTO)).thenReturn(observacao);
        when(repository.save(any())).thenReturn(observacao);
        when(mapper.toDTO(observacao)).thenReturn(observacaoDTO);

        var varObservacao = mapper.toEntity(observacaoDTO);
        var varObservacaoSaved = repository.save(varObservacao);
        var dtoConverter = mapper.toDTO(varObservacaoSaved);

        assertNotNull(varObservacao);
        assertEquals(ObservacaoDTO.class, dtoConverter.getClass());
        assertEquals(observacaoDTO.descricao(), dtoConverter.descricao());

        verify(mapper, times(1)).toEntity(observacaoDTO);
        verify(repository, times(1)).save(observacao);
        verify(mapper, times(1)).toDTO(observacao);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(observacaoDTO);
        assertThat(dtoConverter.descricao()).isEqualTo(observacaoDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de observação com sucesso")
    void atualizarFraseObservacaoComSucesso() {
        when(repository.findById(observacao1.getId())).thenReturn(Optional.ofNullable(observacao1));
        when(repository.save(observacao1)).thenReturn(observacao1);
        when(mapper.toDTO(observacao1)).thenReturn(observacao1DTO);

        var varObservacao = repository.findById(observacao1.getId());
        if (varObservacao.isPresent()) {
            varObservacao.map(obs -> {
                obs.setDescricao(observacao1DTO.descricao());
                return mapper.toDTO(repository.save(obs));
            });

            assertNotNull(varObservacao);
            assertEquals(ObservacaoDTO.class, observacao1DTO.getClass());
            assertEquals(observacao1DTO.id(), varObservacao.get().getId());
            assertEquals(observacao1DTO.descricao(), varObservacao.get().getDescricao());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar frase de observação por id informado.")
    void apagarFraseObservacaoPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(observacao1));
        doNothing().when(repository).delete(any());

        var varObservacao = repository.findById(observacao1.getId());
        if (varObservacao.isPresent()) {
            repository.delete(varObservacao.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todas as frases de observação com sucesso.")
    void listarTodasFrasesObservacaoComSucesso() {
        Pageable pageObservacao = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        when(repository.findAll()).thenReturn(Collections.singletonList(new Observacao()));
        when(mapper.toDTO(observacao1)).thenReturn(observacao1DTO);

        List<Observacao> observacaoList = repository.findAll();
        List<ObservacaoDTO> observacoes = new ArrayList<>();
        observacoes.add(mapper.toDTO(observacao1));

        pageDTO = new ObservacaoPageDTO(observacoes,
                pageObservacao.getPageNumber(),
                pageObservacao.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), ObservacaoPageDTO.class);
        assertEquals(pageDTO.observacoes().get(0).getClass(), ObservacaoDTO.class);
        assertEquals(page, pageDTO.totalPages());
        assertEquals(size, pageDTO.totalElements());

        assertEquals(observacao1DTO.id(), pageDTO.observacoes().get(0).id());
        assertEquals(observacao1DTO.descricao(), pageDTO.observacoes().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de observação por Id com sucesso.")
    void recuperarFraseObservacaoPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(observacao1));
        when(mapper.toDTO(observacao1)).thenReturn(observacao1DTO);

        Optional<ObservacaoDTO> response = repository.findById(observacao1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(observacao1DTO.id(), response.get().id());
            assertEquals(observacao1DTO.descricao(), response.get().descricao());
        } else {
            throw new RecordNotFoundException(observacao1.getId());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar observação por um ID invalido.")
    void gerarExcecaoQuandoPesquisarObservacaoPorIdInvalido() {
        when(repository.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            repository.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}