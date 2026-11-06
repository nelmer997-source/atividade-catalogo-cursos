package br.edu.catalogocursos.api.controller;

import br.edu.catalogocursos.api.dto.CursoRequest;
import br.edu.catalogocursos.api.dto.CursoResponse;
import br.edu.catalogocursos.application.CursoService;
import br.edu.catalogocursos.domain.Curso;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService service;

    public CursoController(CursoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CursoResponse> cadastrar(@RequestBody @Valid CursoRequest request) {
        Curso curso = new Curso();
        curso.setTitulo(request.titulo());
        curso.setCargaHoraria(request.cargaHoraria());
        curso.setNivel(request.nivel());

        Curso salvo = service.cadastrar(curso, request.instrutorId());
        CursoResponse response = new CursoResponse(
                salvo.getId(), salvo.getTitulo(), salvo.getCargaHoraria(), salvo.getNivel(),
                new CursoResponse.InstrutorResumoDTO(salvo.getInstrutor().getId(), salvo.getInstrutor().getNome())
        );

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(salvo.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponse> consultar(@PathVariable Long id) {
        Curso c = service.buscarPorId(id);
        CursoResponse response = new CursoResponse(
                c.getId(), c.getTitulo(), c.getCargaHoraria(), c.getNivel(),
                new CursoResponse.InstrutorResumoDTO(c.getInstrutor().getId(), c.getInstrutor().getNome())
        );
        return ResponseEntity.ok(response);
    }
}