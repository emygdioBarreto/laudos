package br.com.laudos.service;

import br.com.laudos.domain.Premedicacao;
import br.com.laudos.dto.PremedicacaoDTO;
import br.com.laudos.dto.mapper.PremedicacaoMapper;
import br.com.laudos.dto.pages.PremedicacaoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.PremedicacaoRepository;
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
class PremedicacaoServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de premedicação não encontrada pelo ID informado.";
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @InjectMocks
    private PremedicacaoService service;

    @Mock
    private PremedicacaoRepository repository;

    @Mock
    private PremedicacaoMapper mapper;

    @Mock
    private PremedicacaoPageDTO pageDTO;

    private Premedicacao premedicacao;
    private PremedicacaoDTO premedicacaoDTO;
    private Premedicacao premedicacao1;
    private PremedicacaoDTO premedicacao1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        premedicacao = new Premedicacao(null, "Teste de gravação de frase de Premedicacao");
        premedicacaoDTO = new PremedicacaoDTO(null, "Teste de gravação de frase de Premedicacao");
        premedicacao1 = new Premedicacao(1, "Xylocaína tópica à 10%.");
        premedicacao1DTO = new PremedicacaoDTO(1, "Xylocaína tópica à 10%. Teste!");
    }

    @Test
    @DisplayName(value = "Salvar frase de premedicação com sucesso")
    void salvarFraseDePremedicacaoComSucesso() {
        when(mapper.toEntity(premedicacaoDTO)).thenReturn(premedicacao);
        when(repository.save(any())).thenReturn(premedicacao);
        when(mapper.toDTO(premedicacao)).thenReturn(premedicacaoDTO);

        var varPremedicacao = mapper.toEntity(premedicacaoDTO);
        var varPremedicacaoSaved = repository.save(varPremedicacao);
        var dtoConverter = mapper.toDTO(varPremedicacaoSaved);

        assertNotNull(varPremedicacao);
        assertEquals(PremedicacaoDTO.class, dtoConverter.getClass());
        assertEquals(premedicacaoDTO.analgesia(), dtoConverter.analgesia());

        verify(mapper, times(1)).toEntity(premedicacaoDTO);
        verify(repository, times(1)).save(premedicacao);
        verify(mapper, times(1)).toDTO(premedicacao);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(premedicacaoDTO);
        assertThat(dtoConverter.analgesia()).isEqualTo(premedicacaoDTO.analgesia());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de premedicação com sucesso")
    void atualizarFrasePremedicacaoComSucesso() {
        when(repository.findById(premedicacao1.getId())).thenReturn(Optional.ofNullable(premedicacao1));
        when(repository.save(premedicacao1)).thenReturn(premedicacao1);
        when(mapper.toDTO(premedicacao1)).thenReturn(premedicacao1DTO);

        var varPremedicacao = repository.findById(premedicacao1.getId());
        if (varPremedicacao.isPresent()) {
            varPremedicacao.map(duo -> {
                duo.setAnalgesia(premedicacao1DTO.analgesia());
                return mapper.toDTO(repository.save(duo));
            });

            assertNotNull(varPremedicacao);
            assertEquals(PremedicacaoDTO.class, premedicacao1DTO.getClass());
            assertEquals(premedicacao1DTO.id(), varPremedicacao.get().getId());
            assertEquals(premedicacao1DTO.analgesia(), varPremedicacao.get().getAnalgesia());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar frase de premedicação por id informado.")
    void apagarFrasePremedicacaoPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(premedicacao1));
        doNothing().when(repository).delete(any());

        var varPremedicacao = repository.findById(premedicacao1.getId());
        if (varPremedicacao.isPresent()) {
            repository.delete(varPremedicacao.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todas as frases de premedicação com sucesso.")
    void listarTodasFrasesPremedicacaoComSucesso() {
        Pageable pagePremedicacao = PageRequest.of(PAGE, SIZE, Sort.by(Sort.Direction.ASC, "id"));

        when(repository.findAll()).thenReturn(Collections.singletonList(premedicacao1));
        when(mapper.toDTO(premedicacao1)).thenReturn(premedicacao1DTO);

        List<PremedicacaoDTO> premedicacoes = new ArrayList<>();
        Premedicacao premedicacaoList = repository.findAll().get(0);
        premedicacoes.add(mapper.toDTO(premedicacaoList));

        pageDTO = new PremedicacaoPageDTO(premedicacoes,
                pagePremedicacao.getPageNumber(),
                pagePremedicacao.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), PremedicacaoPageDTO.class);
        assertEquals(pageDTO.premedicacoes().get(0).getClass(), PremedicacaoDTO.class);
        assertEquals(PAGE, pageDTO.totalPages());
        assertEquals(SIZE, pageDTO.totalElements());

        assertEquals(premedicacao1DTO.id(), pageDTO.premedicacoes().get(0).id());
        assertEquals(premedicacao1DTO.analgesia(), pageDTO.premedicacoes().get(0).analgesia());
    }

    @Test
    @DisplayName(value = "Recuperar frase de premedicação por Id com sucesso.")
    void recuperarFrasePremedicacaoPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(premedicacao1));
        when(mapper.toDTO(premedicacao1)).thenReturn(premedicacao1DTO);

        Optional<PremedicacaoDTO> response = repository.findById(premedicacao1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(premedicacao1DTO.id(), response.get().id());
            assertEquals(premedicacao1DTO.analgesia(), response.get().analgesia());
        } else {
            throw new RecordNotFoundException(premedicacao1.getId());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar premedicacao por um ID invalido.")
    void gerarExcecaoQuandoPesquisarPremedicacaoPorIdInvalido() {
        when(repository.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            repository.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}