package br.com.laudos.repository;

import br.com.laudos.domain.Procedencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProcedenciaRepository extends JpaRepository<Procedencia, Integer> {

    List<Procedencia> findAllByOrderByIdAsc();

    @Query(value = "SELECT * FROM PROCEDENCIA ORDER BY procedencia asc", nativeQuery = true)
    List<Procedencia> findAllProcedencias();
}
