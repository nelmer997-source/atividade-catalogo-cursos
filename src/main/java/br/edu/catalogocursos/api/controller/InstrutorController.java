package br.edu.catalogocursos.api.controller;

import br.edu.catalogocursos.api.dto.CursoResumoResponse;
import br.edu.catalogocursos.api.dto.InstrutorRequest;
import br.edu.catalogocursos.api.dto.InstrutorResponse;
import br.edu.catalogocursos.api.mapper.CursoMapper;
import br.edu.catalogocursos.api.mapper.InstrutorMapper;
import br.edu.catalogocursos.application.InstrutorService;
import br.edu.catalogocursos.domain.Instrutor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/instrutores")
public class InstrutorController {

    private final InstrutorService service;
    private final InstrutorMapper instrutorMapper;
    private final CursoMapper cursoMapper;

    public InstrutorController(
            InstrutorService service,
            InstrutorMapper instrutorMapper,
            CursoMapper cursoMapper
    ) {
        this.service = service;
        this.instrutorMapper = instrutorMapper;
        this.cursoMapper = cursoMapper;
    }

    @PostMapping
    public ResponseEntity<InstrutorResponse> cadastrar(@RequestBody @Valid InstrutorRequest request) {
        Instrutor salvo = service.cadastrar(instrutorMapper.toEntity(request));
        InstrutorResponse response = instrutorMapper.toResponse(salvo);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvo.getId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}/cursos")
    public ResponseEntity<List<CursoResumoResponse>> listarCursos(@PathVariable Long id) {
        List<CursoResumoResponse> cursos = service.listarCursosDoInstrutor(id).stream()
                .map(cursoMapper::toResumoResponse)
                .toList();
        return ResponseEntity.ok(cursos);
    }
}