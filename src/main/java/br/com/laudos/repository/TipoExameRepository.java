package br.com.laudos.repository;

import br.com.laudos.domain.TipoExame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TipoExameRepository extends JpaRepository<TipoExame, Integer> {

    List<TipoExame> findAllByOrderByIdAsc();

    @Query(value = "SELECT * FROM TIPO_EXAME ORDER BY tipoexame asc", nativeQuery = true)
    List<TipoExame> findAllTipoExames();
}
