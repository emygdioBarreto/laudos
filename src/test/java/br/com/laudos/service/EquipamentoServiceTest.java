package br.com.laudos.service;

import br.com.laudos.domain.Equipamento;
import br.com.laudos.dto.EquipamentoDTO;
import br.com.laudos.dto.mapper.EquipamentoMapper;
import br.com.laudos.dto.pages.EquipamentoPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.EquipamentoRepository;
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
class EquipamentoServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de conclusão não encontrada pelo ID informado.";
    private static final int size = 10;
    private static final int page = 0;

    @InjectMocks
    private EquipamentoService service;

    @Mock
    private EquipamentoRepository repository;

    @Mock
    private EquipamentoMapper mapper;

    @Mock
    private EquipamentoPageDTO pageDTO;

    private Equipamento equipamento;
    private EquipamentoDTO equipamentoDTO;
    private Equipamento equipamento1;
    private EquipamentoDTO equipamento1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        equipamento = new Equipamento(null, "Teste de Gravação de equipamento", "30", "10", "10", "10", "1", "Recife", "C");
        equipamentoDTO = new EquipamentoDTO(null, "Teste de Gravação de equipamento", "30", "10", "10", "10", "1", "Recife", "C");
        equipamento1 = new Equipamento(1, "Pentax EG290I", "30", "10", "10", "10", "1", "Recife", "C");
        equipamento1DTO = new EquipamentoDTO(1, "Pentax EG290I Teste", "30", "10", "10", "10", "1", "Recife", "C");
    }

    @Test
    @DisplayName(value = "Salvar novo equipamento com sucesso")
    void salvarNovoEquipamentoComSucesso() {
        when(mapper.toEntity(equipamentoDTO)).thenReturn(equipamento);
        when(repository.save(any())).thenReturn(equipamento);
        when(mapper.toDTO(equipamento)).thenReturn(equipamentoDTO);

        var varEquipamento = mapper.toEntity(equipamentoDTO);
        var varEquipamentoSaved = repository.save(varEquipamento);
        var dtoConverter = mapper.toDTO(varEquipamentoSaved);

        assertNotNull(varEquipamento);
        assertEquals(EquipamentoDTO.class, dtoConverter.getClass());
        assertEquals("Teste de Gravação de equipamento", dtoConverter.descricao());

        verify(mapper, times(1)).toEntity(equipamentoDTO);
        verify(repository, times(1)).save(equipamento);
        verify(mapper, times(1)).toDTO(equipamento);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(equipamentoDTO);
        assertThat(dtoConverter.descricao()).isEqualTo(equipamentoDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar equipamento por id com sucesso")
    void atualizarEquipamentoPorIdComSucesso() {
        when(repository.findById(1)).thenReturn(Optional.ofNullable(equipamento1));
        when(repository.save(equipamento1)).thenReturn(equipamento1);
        when(mapper.toDTO(equipamento1)).thenReturn(equipamento1DTO);

        var varEquipamento = repository.findById(1);
        if (varEquipamento.isPresent()) {
            varEquipamento.map(eqp -> {
                eqp.setDescricao(equipamento1DTO.descricao());
                eqp.setSuperior(equipamento1DTO.superior());
                eqp.setInferior(equipamento1DTO.inferior());
                eqp.setDireita(equipamento1DTO.direita());
                eqp.setEsquerda(equipamento1DTO.esquerda());
                eqp.setModLaudo(equipamento1DTO.modLaudo());
                eqp.setCidade(equipamento1DTO.cidade());
                eqp.setOrdena(equipamento1DTO.ordena());
                return mapper.toDTO(repository.save(eqp));
            });

            assertNotNull(varEquipamento);
            assertEquals(EquipamentoDTO.class, equipamento1DTO.getClass());
            assertEquals(equipamento1DTO.id(), varEquipamento.get().getId());
            assertEquals(equipamento1DTO.descricao(), varEquipamento.get().getDescricao());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar equipamento por id informado.")
    void apagarEquipamentoPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(equipamento1));
        doNothing().when(repository).delete(any());

        var varEquipamento = repository.findById(1);
        if (varEquipamento.isPresent()) {
            repository.delete(varEquipamento.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todos os equipamentos com sucesso.")
    void listarTodosEquipamentosComSucesso() {
        Pageable pageEquipamento = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        when(repository.findAll()).thenReturn(Collections.singletonList(new Equipamento()));
        when(mapper.toDTO(equipamento1)).thenReturn(equipamento1DTO);

        List<Equipamento> equipamentoList = repository.findAll();
        List<EquipamentoDTO> equipamentos = new ArrayList<>();
        equipamentos.add(mapper.toDTO(equipamento1));

        pageDTO = new EquipamentoPageDTO(equipamentos,
                pageEquipamento.getPageNumber(),
                pageEquipamento.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), EquipamentoPageDTO.class);
        assertEquals(pageDTO.equipamentos().get(0).getClass(), EquipamentoDTO.class);
        assertEquals(0, pageDTO.totalPages());
        assertEquals(10, pageDTO.totalElements());

        assertEquals(equipamento1DTO.id(), pageDTO.equipamentos().get(0).id());
        assertEquals(equipamento1DTO.descricao(), pageDTO.equipamentos().get(0).descricao());
        assertEquals(equipamento1DTO.superior(), pageDTO.equipamentos().get(0).superior());
        assertEquals(equipamento1DTO.inferior(), pageDTO.equipamentos().get(0).inferior());
        assertEquals(equipamento1DTO.direita(), pageDTO.equipamentos().get(0).direita());
        assertEquals(equipamento1DTO.esquerda(), pageDTO.equipamentos().get(0).esquerda());
        assertEquals(equipamento1DTO.modLaudo(), pageDTO.equipamentos().get(0).modLaudo());
        assertEquals(equipamento1DTO.cidade(), pageDTO.equipamentos().get(0).cidade());
        assertEquals(equipamento1DTO.ordena(), pageDTO.equipamentos().get(0).ordena());
    }

    @Test
    @DisplayName(value = "Recuperar equipamento por Id com sucesso.")
    void recuperarEquipamentoPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(equipamento1));
        when(mapper.toDTO(equipamento1)).thenReturn(equipamento1DTO);

        Optional<EquipamentoDTO> response = repository.findById(equipamento1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(equipamento1DTO.id(), response.get().id());
            assertEquals(equipamento1DTO.descricao(), response.get().descricao());
            assertEquals(equipamento1DTO.superior(), response.get().superior());
            assertEquals(equipamento1DTO.inferior(), response.get().inferior());
            assertEquals(equipamento1DTO.direita(), response.get().direita());
            assertEquals(equipamento1DTO.esquerda(), response.get().esquerda());
            assertEquals(equipamento1DTO.modLaudo(), response.get().modLaudo());
            assertEquals(equipamento1DTO.cidade(), response.get().cidade());
            assertEquals(equipamento1DTO.ordena(), response.get().ordena());
        } else {
            throw new RecordNotFoundException(equipamento1.getId());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar equipamento por um ID invalido.")
    void gerarExcecaoQuandoPesquisarEquipamentoPorIdInvalido() {
        when(repository.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            repository.findById(2);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}