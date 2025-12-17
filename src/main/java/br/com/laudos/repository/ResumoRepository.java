package br.com.laudos.repository;

import br.com.laudos.domain.Resumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResumoRepository extends JpaRepository<Resumo, Integer> {

    List<Resumo> findAllByOrderByIdAsc();

    @Query(value = "SELECT * FROM RESUMO_CLINICO ORDER BY descricao asc", nativeQuery = true)
    List<Resumo> findAllResumos();
}
