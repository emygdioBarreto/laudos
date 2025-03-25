package br.com.laudos.repository;

import br.com.laudos.domain.Concluir;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.ConcluirService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ConcluirRepositoryTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Frase de conclusão não encontrada pelo ID informado.";

    @InjectMocks
    private ConcluirService service;

    @Mock
    private ConcluirRepository repository;

    private Concluir newConcluir;
    private Concluir updateConcluir;
    private List<Concluir> conclusoes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        newConcluir = new Concluir(null, "Teste de gravação de frase de conclusão.");
        updateConcluir = new Concluir(2, "Teste de gravação de frase de conclusão atualizada.");
        Concluir concluir1 = new Concluir(1, "Exame normal do ponto de vista endoscópico.");
        Concluir concluir2 = new Concluir(2, "Exame normal do ponto de vista endoscópico 2.");
        conclusoes = List.of(concluir1, concluir2);
    }

    @Test
    @DisplayName(value = "Salvar nova frase de conclusão com sucesso")
    void salvarNovaFraseConclusaoComSucesso() {
        when(repository.save(newConcluir)).thenReturn(newConcluir);

        Concluir response = repository.save(newConcluir);

        assertNotNull(response);
        assertEquals(newConcluir.getClass(), response.getClass());
        assertEquals(newConcluir.getConclusao(), response.getConclusao());
    }

    @Test
    @DisplayName(value = "Atualizar frase de conclusão com sucesso")
    void atualizarFraseConclusaoComSucesso() {
        when(repository.save(updateConcluir)).thenReturn(updateConcluir);

        Concluir response = repository.save(updateConcluir);

        assertNotNull(response);
        assertEquals(updateConcluir.getClass(), response.getClass());
        assertEquals(updateConcluir.getConclusao(), response.getConclusao());
    }

    @Test
    @DisplayName(value = "apagar frase de conclusão com sucesso")
    void apagarFraseConclusaoComSucesso() {
        when(repository.findById(anyInt())).thenReturn(Optional.ofNullable(updateConcluir));
        doNothing().when(repository).delete(any());

        Optional<Concluir> varConcluir = repository.findById(updateConcluir.getId());
        if (varConcluir.isPresent()) {
            repository.delete(varConcluir.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Buscar todas as conclusões ordenadas por ID")
    void buscarTodasConclusoesOrdenadasPorId() {
        when(repository.findAll()).thenReturn(conclusoes);

        List<Concluir> response = repository.findAll();

        assertNotNull(response);
        assertEquals(conclusoes.getClass(), response.getClass());
        assertEquals(conclusoes.size(), response.size());

        assertThat(response).containsAll(conclusoes);

        assertEquals(conclusoes.get(0), response.get(0));
        assertEquals(conclusoes.get(0).getId(), response.get(0).getId());
        assertEquals(conclusoes.get(0).getConclusao(), response.get(0).getConclusao());

        assertEquals(conclusoes.get(1), response.get(1));
        assertEquals(conclusoes.get(1).getId(), response.get(1).getId());
        assertEquals(conclusoes.get(1).getConclusao(), response.get(1).getConclusao());
    }

    @Test
    @DisplayName(value = "Verificar se existe frase de conclusão pelo texto informado")
    void pesquisarFraseConclusaoPorTexto() {
        when(repository.existsByConclusao(anyString())).thenReturn(true);

        boolean response = repository.existsByConclusao("Exame normal do ponto de vista endoscópico.");

        assertTrue(response);
    }
}