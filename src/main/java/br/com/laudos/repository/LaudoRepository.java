package br.com.laudos.repository;

import br.com.laudos.domain.Laudo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LaudoRepository extends JpaRepository<Laudo, Long> {

    @Query("""
        select l from Laudo l
        left join fetch l.medicoExecutor
        left join fetch l.resumo
        left join fetch l.tipoExame
        left join fetch l.localExame
        where l.idLaudo = :idLaudo
        """)
    Optional<Laudo> buscarCompletoPorId(@Param("idLaudo") Long idLaudo);

    Optional<Laudo> findByHashValidacao(String hashValidacao);

}

