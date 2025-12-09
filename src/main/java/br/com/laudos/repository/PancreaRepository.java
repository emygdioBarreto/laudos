package br.com.laudos.repository;

import br.com.laudos.domain.Pancrea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PancreaRepository extends JpaRepository<Pancrea, Integer> {

    List<Pancrea> findAllByOrderByIdAsc();
}
