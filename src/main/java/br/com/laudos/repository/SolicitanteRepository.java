package br.com.laudos.repository;

import br.com.laudos.domain.Solicitante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitanteRepository extends JpaRepository<Solicitante, Integer> {

    List<Solicitante> findAllByOrderByIdAsc();
}
