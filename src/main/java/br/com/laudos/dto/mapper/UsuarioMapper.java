package br.com.laudos.dto.mapper;

import br.com.laudos.auth.Usuario;
import br.com.laudos.dto.UsuarioDTO;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
     public UsuarioDTO toDTO(Usuario usuario) {
          if (usuario == null) {
               return null;
          }
          return new UsuarioDTO(usuario.getId(), usuario.getUsername(), usuario.getEmail(), usuario.getRole());
     }

     public Usuario toEntity(UsuarioDTO dto) {
          if (dto == null) {
               return null;
          }
          Usuario usuario = new Usuario();
          if (dto.id() != null) {
               usuario.setId(dto.id());
          }
          usuario.setUsername(dto.username());
          usuario.setEmail(dto.email());
          usuario.setRole(dto.role());
          return usuario;
     }
}
