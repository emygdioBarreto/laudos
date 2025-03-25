package br.com.laudos.controller;

import br.com.laudos.auth.Usuario;
import br.com.laudos.dto.UsuarioDTO;
import br.com.laudos.dto.mapper.UsuarioMapper;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Usuário não encontrado pelo ID informado.";

    @InjectMocks
    private UsuarioController controller;

    @Mock
    private UsuarioService service;

    @Mock
    private UsuarioMapper mapper;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;
    private Usuario usuario1;
    private UsuarioDTO usuario1DTO;

    private final List<UsuarioDTO> usuarios = new ArrayList<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario(null, "paulo", "pv@gmail.com", "teste", "ADMIN");
        usuarioDTO = new UsuarioDTO( null, "paulo", "pv@gmail.com", "ADMIN");
        usuario1 = new Usuario(1L, "ana", "ana.botler@gmail.com", "$2a$12$ESCe4NPegTvkdugd7GJXAu//IP0lT/YkjO6g618sDhh.c.I6uRbRi", "MEDICO");
        usuario1DTO = new UsuarioDTO(1L, "ana2", "ana.botler@hotmail.com", "ADMIN");
    }

    @Test
    @DisplayName(value = "Salvar novo usuário com sucesso")
    void salvarNovoUsuarioComSucesso() {
        when(service.createUser(any())).thenReturn(usuarioDTO);

        UsuarioDTO response = service.createUser(mapper.toDTO(usuario));

        assertNotNull(response);
        assertEquals(usuarioDTO.username(), response.username());
        assertEquals(usuarioDTO.email(), response.email());
        assertEquals(usuarioDTO.role(), response.role());

        verify(mapper, times(1)).toDTO(usuario);

        assertThat(response).usingRecursiveComparison().isEqualTo(usuarioDTO);
        assertThat(response.username()).isEqualTo(usuarioDTO.username());
        assertThat(response.email()).isEqualTo(usuarioDTO.email());
        assertThat(response.role()).isEqualTo(usuarioDTO.role());
    }

    @Test
    @DisplayName(value = "Atualizar usuário com sucesso")
    void atuaizarUsuarioComSucesso() {
        when(service.update(anyLong(), any())).thenReturn(usuario1DTO);

        UsuarioDTO response = service.update(usuario1DTO.id(), usuario1DTO);

        assertNotNull(response);
        assertEquals(UsuarioDTO.class, usuario1DTO.getClass());
        assertEquals(usuario1DTO.id(), response.id());
        assertEquals(usuario1DTO.username(), response.username());
        assertEquals(usuario1DTO.email(), response.email());
        assertEquals(usuario1DTO.role(), response.role());

        assertThat(response).usingRecursiveComparison().isEqualTo(usuario1DTO);
        assertThat(response.username()).isEqualTo(usuario1DTO.username());
        assertThat(response.email()).isEqualTo(usuario1DTO.email());
        assertThat(response.role()).isEqualTo(usuario1DTO.role());
    }

    @Test
    @DisplayName(value = "Apagar usuário por id informado.")
    void apagarUsuarioPorIdComSucesso() {
        doNothing().when(service).delete(any());

        service.delete(usuario1DTO.id());

        verify(service, times(1)).delete(any());
    }

    @Test
    @DisplayName(value = "Lista todos os usuários.")
    void listaTodosUsuarios() {
        when(service.findAll()).thenReturn(usuarios);

        usuarios.add(usuario1DTO);
        List<UsuarioDTO> usuariosList = service.findAll();

        assertNotNull(usuarios);
        assertEquals(UsuarioDTO.class, usuariosList.get(0).getClass());

        assertEquals(usuario1DTO.id(), usuarios.get(0).id());
        assertEquals(usuario1DTO.username(), usuarios.get(0).username());
        assertEquals(usuario1DTO.email(), usuarios.get(0).email());
        assertEquals(usuario1DTO.role(), usuarios.get(0).role());
    }

    @Test
    @DisplayName(value = "Recuperar usuário por Id com sucesso.")
    void recuperarUsuarioPorIdComSucesso() {
        when(service.findById(anyLong())).thenReturn(usuario1DTO);

        UsuarioDTO response = service.findById(usuario1.getId());

        assertEquals(usuario1DTO.id(), response.id());
        assertEquals(usuario1DTO.username(), response.username());
        assertEquals(usuario1DTO.email(), response.email());
        assertEquals(usuario1DTO.role(), response.role());
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar usuário por um ID invalido.")
    void gerarExcecaoQuandoPesquisarUsuarioPorIdInvalido() {
        when(service.findById(anyLong())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            service.findById(2L);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}