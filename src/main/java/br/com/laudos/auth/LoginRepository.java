package br.com.laudos.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String login);
}
