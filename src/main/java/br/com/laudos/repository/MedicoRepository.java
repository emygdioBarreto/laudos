package br.com.laudos.repository;

import br.com.laudos.domain.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicoRepository extends JpaRepository<Medico, String> {

    List<Medico> findAllByOrderByCrmAsc();
}
