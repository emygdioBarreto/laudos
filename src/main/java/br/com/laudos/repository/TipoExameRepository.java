package br.com.laudos.repository;

import br.com.laudos.domain.TipoExame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoExameRepository extends JpaRepository<TipoExame, Integer> {

    List<TipoExame> findAllByOrderByIdAsc();
}
