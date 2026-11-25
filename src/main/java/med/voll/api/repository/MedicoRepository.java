package med.voll.api.repository;

import med.voll.api.domain.medico.Medico;
import med.voll.api.enums.Especialidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Page<Medico> findAllByAtivoTrue(Pageable pageable);

    @Query(value = """
            select m.* from medicos m
            where m.ativo = 1
            and m.especialidade = :especialidade
            and m.id not in (
                select c.medico_id from consultas c
                where c.data = :data
            )
            order by rand()
            limit 1
            """, nativeQuery = true)
    Medico escolherMedicoAleatorioLivreNaData(String especialidade, LocalDateTime data);

    @Query("""
            SELECT m.ativo
            FROM Medico m
            WHERE m.id = :id
            """)
    Boolean findAtivoById(Long id);
}