package br.com.laudos.repository;

import br.com.laudos.domain.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Integer> {

    List<Equipamento> findAllByOrderByIdAsc();
}
