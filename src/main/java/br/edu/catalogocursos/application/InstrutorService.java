package br.edu.catalogocursos.application;

import br.edu.catalogocursos.domain.Curso;
import br.edu.catalogocursos.domain.Instrutor;
import br.edu.catalogocursos.repository.CursoRepository;
import br.edu.catalogocursos.repository.InstrutorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InstrutorService {

    private final InstrutorRepository repository;
    private final CursoRepository cursoRepository;

    public InstrutorService(InstrutorRepository repository, CursoRepository cursoRepository) {
        this.repository = repository;
        this.cursoRepository = cursoRepository;
    }

    @Transactional
    public Instrutor cadastrar(Instrutor instrutor) {
        if (repository.existsByEmail(instrutor.getEmail())) {
            throw new IllegalArgumentException("Email já utilizado"); // Será mapeado para erro 409 depois
        }
        return repository.save(instrutor);
    }

    @Transactional(readOnly = true)
    public Instrutor buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instrutor não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Curso> listarCursosDoInstrutor(Long instrutorId) {
        if (!repository.existsById(instrutorId)) {
            throw new EntityNotFoundException("Instrutor não encontrado");
        }
        return cursoRepository.findByInstrutorIdOrderByTituloAsc(instrutorId);
    }
}