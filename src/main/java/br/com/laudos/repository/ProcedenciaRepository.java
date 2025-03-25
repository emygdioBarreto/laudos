package br.com.laudos.repository;

import br.com.laudos.domain.Procedencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcedenciaRepository extends JpaRepository<Procedencia, Integer> {

    List<Procedencia> findAllByOrderByIdAsc();
}
