package br.com.laudos.repository;

import br.com.laudos.domain.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicoRepository extends JpaRepository<Medico, String> {

    List<Medico> findAllByOrderByCrmAsc();

    @Query(value = "SELECT * FROM MEDICO ORDER BY medico asc", nativeQuery = true)
    List<Medico> findAllMedicos();

    Medico findByCrm(String crm);
}
