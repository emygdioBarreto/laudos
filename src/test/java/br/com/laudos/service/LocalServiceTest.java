package br.com.laudos.service;

import br.com.laudos.domain.Local;
import br.com.laudos.dto.LocalDTO;
import br.com.laudos.dto.mapper.LocalMapper;
import br.com.laudos.dto.pages.LocalPageDTO;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.LocalRepository;
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
class LocalServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Local não encontrado pelo ID informado.";
    private static final int size = 10;
    private static final int page = 0;

    @InjectMocks
    private LocalService service;

    @Mock
    private LocalRepository repository;

    @Mock
    private LocalMapper mapper;

    @Mock
    private LocalPageDTO pageDTO;

    private Local local;
    private LocalDTO localDTO;
    private Local local1;
    private LocalDTO local1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        local = new Local(null, "Teste de gravação de frase de Local");
        localDTO = new LocalDTO(null, "Teste de gravação de frase de Local");
        local1 = new Local(2, "Papila anatômica");
        local1DTO = new LocalDTO(2, "Papila anatômica teste!");
    }

    @Test
    @DisplayName(value = "Salvar frase de local com sucesso")
    void salvarFraseDeLocalComSucesso() {
        when(mapper.toEntity(localDTO)).thenReturn(local);
        when(repository.save(any())).thenReturn(local);
        when(mapper.toDTO(local)).thenReturn(localDTO);

        var varLocal = mapper.toEntity(localDTO);
        var varLocalSaved = repository.save(varLocal);
        var dtoConverter = mapper.toDTO(varLocalSaved);

        assertNotNull(varLocal);
        assertEquals(LocalDTO.class, dtoConverter.getClass());
        assertEquals(localDTO.descricao(), dtoConverter.descricao());

        verify(mapper, times(1)).toEntity(localDTO);
        verify(repository, times(1)).save(local);
        verify(mapper, times(1)).toDTO(local);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(localDTO);
        assertThat(dtoConverter.descricao()).isEqualTo(localDTO.descricao());
    }

    @Test
    @DisplayName(value = "Atualizar a frase de local com sucesso")
    void atualizarFraseLocalComSucesso() {
        when(repository.findById(2)).thenReturn(Optional.ofNullable(local1));
        when(repository.save(local1)).thenReturn(local1);
        when(mapper.toDTO(local1)).thenReturn(local1DTO);

        var varLocal = repository.findById(2);
        if (varLocal.isPresent()) {
            varLocal.map(duo -> {
                duo.setDescricao(local1DTO.descricao());
                return mapper.toDTO(repository.save(duo));
            });

            assertNotNull(varLocal);
            assertEquals(LocalDTO.class, local1DTO.getClass());
            assertEquals(local1DTO.id(), varLocal.get().getId());
            assertEquals(local1DTO.descricao(), varLocal.get().getDescricao());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar frase de local por id informado.")
    void apagarFraseLocalPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(local1));
        doNothing().when(repository).delete(any());

        var varLocal = repository.findById(2);
        if (varLocal.isPresent()) {
            repository.delete(varLocal.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todas as frases de local com sucesso.")
    void listarTodasFrasesLocalComSucesso() {
        Pageable pageLocal = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        when(repository.findAll()).thenReturn(Collections.singletonList(new Local()));
        when(mapper.toDTO(local1)).thenReturn(local1DTO);

        List<Local> localList = repository.findAll();
        List<LocalDTO> locais = new ArrayList<>();
        locais.add(mapper.toDTO(local1));

        pageDTO = new LocalPageDTO(locais,
                pageLocal.getPageNumber(),
                pageLocal.getPageSize());

        assertNotNull(pageDTO);
        assertEquals(pageDTO.getClass(), LocalPageDTO.class);
        assertEquals(pageDTO.locais().get(0).getClass(), LocalDTO.class);
        assertEquals(page, pageDTO.totalPages());
        assertEquals(size, pageDTO.totalElements());

        assertEquals(local1DTO.id(), pageDTO.locais().get(0).id());
        assertEquals(local1DTO.descricao(), pageDTO.locais().get(0).descricao());
    }

    @Test
    @DisplayName(value = "Recuperar frase de local por Id com sucesso.")
    void recuperarFraseLocalPorIdComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(local1));
        when(mapper.toDTO(local1)).thenReturn(local1DTO);

        Optional<LocalDTO> response = repository.findById(local1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(local1DTO.id(), response.get().id());
            assertEquals(local1DTO.descricao(), response.get().descricao());
        } else {
            throw new RecordNotFoundException(local1.getId());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar local por um ID invalido.")
    void gerarExcecaoQuandoPesquisarLocalPorIdInvalido() {
        when(repository.findById(anyInt())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            repository.findById(1);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}