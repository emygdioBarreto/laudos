package br.com.laudos.repository;

import br.com.laudos.domain.Pancreas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PancreasRepository extends JpaRepository<Pancreas, Integer> {

    List<Pancreas> findAllByOrderByIdAsc();
}
