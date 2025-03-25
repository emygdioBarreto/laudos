package br.com.laudos.repository;

import br.com.laudos.domain.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocalRepository extends JpaRepository<Local, Integer> {

    List<Local> findAllByOrderByIdAsc();
}
