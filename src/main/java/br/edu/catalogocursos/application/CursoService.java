package br.edu.catalogocursos.application;

import br.edu.catalogocursos.domain.Curso;
import br.edu.catalogocursos.domain.Instrutor;
import br.edu.catalogocursos.repository.CursoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final InstrutorService instrutorService;

    public CursoService(CursoRepository cursoRepository, InstrutorService instrutorService) {
        this.cursoRepository = cursoRepository;
        this.instrutorService = instrutorService;
    }

    @Transactional
    public Curso cadastrar(Curso curso, Long instrutorId) {
        Instrutor instrutor = instrutorService.buscarPorId(instrutorId);
        instrutor.adicionarCurso(curso);
        return cursoRepository.save(curso);
    }

    @Transactional(readOnly = true)
    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso n\u00e3o encontrado"));
    }
}