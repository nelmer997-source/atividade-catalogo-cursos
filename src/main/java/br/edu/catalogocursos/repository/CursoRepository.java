package br.edu.catalogocursos.repository;

import br.edu.catalogocursos.domain.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    // Consulta obrigatória para buscar cursos do instrutor e ordenar
    List<Curso> findByInstrutorIdOrderByTituloAsc(Long instrutorId);
}