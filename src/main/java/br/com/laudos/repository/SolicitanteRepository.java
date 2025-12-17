package br.com.laudos.repository;

import br.com.laudos.domain.Solicitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SolicitanteRepository extends JpaRepository<Solicitante, Integer> {

    List<Solicitante> findAllByOrderByIdAsc();

    @Query(value = "SELECT * FROM SOLICITANTE ORDER BY medico_solicitante asc", nativeQuery = true)
    List<Solicitante> findAllSolicitantes();
}
