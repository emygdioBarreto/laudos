package br.com.laudos.service;

import br.com.laudos.dto.UsuarioDTO;
import br.com.laudos.dto.mapper.UsuarioMapper;
import br.com.laudos.exceptions.RecordNotFoundException;
import br.com.laudos.repository.UsuarioRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;

    public UsuarioDTO createUser(@NotNull @Valid UsuarioDTO userDto) {
        return mapper.toDTO(repository.save(mapper.toEntity(userDto)));
    }

    public UsuarioDTO update(@NotNull @Positive Long id, @NotNull @Valid UsuarioDTO dto) {
        return repository.findById(id)
                .map(user -> {
                    user.setUsername(dto.username());
                    user.setEmail(dto.email());
                    user.setRole(dto.role());
                    return mapper.toDTO(repository.save(user));
                }).orElseThrow(()-> new RecordNotFoundException(id));
    }

    public void delete(@NotNull @Positive Long id) {
        repository.delete(repository.findById(id)
                .orElseThrow(()-> new RecordNotFoundException(id)));
    }

    public List<UsuarioDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    public UsuarioDTO findById(@NotNull @Positive Long id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }
}
