package br.com.laudos.service;

import br.com.laudos.auth.Usuario;
import br.com.laudos.dto.UsuarioDTO;
import br.com.laudos.dto.mapper.UsuarioMapper;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    private static final String REGISTRO_NAO_ENCONTRADO = "Usuário não encontrada pelo ID informado.";

    @InjectMocks
    private UsuarioService service;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private UsuarioMapper mapper;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;
    private Usuario usuario1;
    private UsuarioDTO usuario1DTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario(null, "paulo", "pv@gmail.com", "teste", "ADMIN");
        usuarioDTO = new UsuarioDTO( null, "paulo", "pv@gmail.com", "ADMIN");
        usuario1 = new Usuario(1L, "ana", "ana.botler@gmail.com", "$2a$12$ESCe4NPegTvkdugd7GJXAu//IP0lT/YkjO6g618sDhh.c.I6uRbRi", "MEDICO");
        usuario1DTO = new UsuarioDTO(1L, "ana2", "ana.botler@hotmail.com", "ADMIN");
    }

    @Test
    @DisplayName(value = "Salvar usuário com sucesso")
    void salvarUsuarioComSucesso() {
        when(mapper.toEntity(usuarioDTO)).thenReturn(usuario);
        when(repository.save(any())).thenReturn(usuario);
        when(mapper.toDTO(usuario)).thenReturn(usuarioDTO);

        var varUsuario = mapper.toEntity(usuarioDTO);
        var varUsuarioSaved = repository.save(varUsuario);
        var dtoConverter = mapper.toDTO(varUsuarioSaved);

        assertNotNull(varUsuario);
        assertEquals(UsuarioDTO.class, dtoConverter.getClass());
        assertEquals(usuarioDTO.username(), dtoConverter.username());
        assertEquals(usuarioDTO.email(), dtoConverter.email());
        assertEquals(usuarioDTO.role(), dtoConverter.role());

        verify(mapper, times(1)).toEntity(usuarioDTO);
        verify(repository, times(1)).save(usuario);
        verify(mapper, times(1)).toDTO(usuario);

        assertThat(dtoConverter).usingRecursiveComparison().isEqualTo(usuarioDTO);
        assertThat(dtoConverter.username()).isEqualTo(usuarioDTO.username());
        assertThat(dtoConverter.email()).isEqualTo(usuarioDTO.email());
        assertThat(dtoConverter.role()).isEqualTo(usuarioDTO.role());
    }

    @Test
    @DisplayName(value = "Atualizar o usuário com sucesso")
    void atualizarUsuarioComSucesso() {
        when(repository.findById(usuario1.getId())).thenReturn(Optional.ofNullable(usuario1));
        when(repository.save(usuario1)).thenReturn(usuario1);
        when(mapper.toDTO(usuario1)).thenReturn(usuario1DTO);

        var varUsuario = repository.findById(usuario1.getId());
        if (varUsuario.isPresent()) {
            varUsuario.map(usu -> {
                usu.setUsername(usuario1DTO.username());
                usu.setEmail(usuario1DTO.email());
                usu.setRole(usuario1DTO.role());
                return mapper.toDTO(repository.save(usu));
            });

            assertNotNull(varUsuario);
            assertEquals(UsuarioDTO.class, usuario1DTO.getClass());
            assertEquals(usuario1DTO.id(), varUsuario.get().getId());
            assertEquals(usuario1DTO.username(), varUsuario.get().getUsername());
            assertEquals(usuario1DTO.email(), varUsuario.get().getEmail());
            assertEquals(usuario1DTO.role(), varUsuario.get().getRole());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Apagar usuário por id informado.")
    void apagarUsuarioPorIdComSucesso() {
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(usuario1));
        doNothing().when(repository).delete(any());

        var varUsuario = repository.findById(usuario1.getId());
        if (varUsuario.isPresent()) {
            repository.delete(varUsuario.get());

            verify(repository, times(1)).delete(any());
        } else {
            throw new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO);
        }
    }

    @Test
    @DisplayName(value = "Listar todos os usuários com sucesso.")
    void listarTodosUsuariosComSucesso() {
        when(repository.findAll()).thenReturn(Collections.singletonList(new Usuario()));
        when(mapper.toDTO(usuario1)).thenReturn(usuario1DTO);

        List<Usuario> usuarioList = repository.findAll();
        List<UsuarioDTO> usuarios = new ArrayList<>();
        usuarios.add(mapper.toDTO(usuario1));

        assertNotNull(usuarios);
        assertEquals(usuarios.get(0).getClass(), UsuarioDTO.class);

        assertEquals(usuario1DTO.id(), usuarios.get(0).id());
        assertEquals(usuario1DTO.username(), usuarios.get(0).username());
        assertEquals(usuario1DTO.email(), usuarios.get(0).email());
        assertEquals(usuario1DTO.role(), usuarios.get(0).role());
    }

    @Test
    @DisplayName(value = "Recuperar usuário por Id com sucesso.")
    void recuperarUsuarioPorIdComSucesso() {
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(usuario1));
        when(mapper.toDTO(usuario1)).thenReturn(usuario1DTO);

        Optional<UsuarioDTO> response = repository.findById(usuario1.getId()).map(mapper::toDTO);
        if (response.isPresent()) {
            assertNotNull(response);

            assertEquals(usuario1DTO.id(), response.get().id());
            assertEquals(usuario1DTO.username(), response.get().username());
            assertEquals(usuario1DTO.email(), response.get().email());
            assertEquals(usuario1DTO.role(), response.get().role());
        } else {
            throw new RecordNotFoundException(usuario1.getId());
        }
    }

    @Test
    @DisplayName(value = "Gerar exceção quando pesquisar usuário por um ID invalido.")
    void gerarExcecaoQuandoPesquisarUsuarioPorIdInvalido() {
        when(repository.findById(anyLong())).thenThrow(new RecordNotFoundException(REGISTRO_NAO_ENCONTRADO));

        try{
            repository.findById(2L);
        } catch (Exception ex) {
            assertEquals(RecordNotFoundException.class, ex.getClass());
            assertEquals(REGISTRO_NAO_ENCONTRADO, ex.getMessage());
        }
    }
}