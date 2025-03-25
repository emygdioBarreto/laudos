package br.com.laudos.repository;

import br.com.laudos.domain.Intestino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IntestinoRepository extends JpaRepository<Intestino, Integer> {

    List<Intestino> findAllByOrderByIdAsc();
}
