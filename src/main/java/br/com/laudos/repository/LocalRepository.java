package br.com.laudos.repository;

import br.com.laudos.domain.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocalRepository extends JpaRepository<Local, Integer> {

    List<Local> findAllByOrderByIdAsc();

    @Query(value = "SELECT * FROM LOCAL ORDER BY descricao asc", nativeQuery = true)
    List<Local> findAllLocais();
}
