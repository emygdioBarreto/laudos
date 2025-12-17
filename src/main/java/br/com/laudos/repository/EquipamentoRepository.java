package br.com.laudos.repository;

import br.com.laudos.domain.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Integer> {

    List<Equipamento> findAllByOrderByIdAsc();

    @Query(value = "SELECT * FROM EQUIPAMENTO ORDER BY descricao asc", nativeQuery = true)
    List<Equipamento> findAllEquipamentos();

}
