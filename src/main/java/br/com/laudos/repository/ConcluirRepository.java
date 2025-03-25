package br.com.laudos.repository;

import br.com.laudos.domain.Concluir;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcluirRepository extends JpaRepository<Concluir, Integer> {

    List<Concluir> findAllByOrderByIdAsc();

    boolean existsByConclusao(String conclusao);
}
