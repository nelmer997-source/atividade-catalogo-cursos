package br.edu.catalogocursos.api.controller;

import br.edu.catalogocursos.api.dto.CursoResponse;
import br.edu.catalogocursos.api.dto.InstrutorRequest;
import br.edu.catalogocursos.api.dto.InstrutorResponse;
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

    public InstrutorController(InstrutorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InstrutorResponse> cadastrar(@RequestBody @Valid InstrutorRequest request) {
        Instrutor instrutor = new Instrutor();
        instrutor.setNome(request.nome());
        instrutor.setEmail(request.email());

        Instrutor salvo = service.cadastrar(instrutor);
        InstrutorResponse response = new InstrutorResponse(salvo.getId(), salvo.getNome(), salvo.getEmail());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(salvo.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}/cursos")
    public ResponseEntity<List<CursoResponse>> listarCursos(@PathVariable Long id) {
        List<CursoResponse> cursos = service.listarCursosDoInstrutor(id).stream()
                .map(c -> new CursoResponse(
                        c.getId(), c.getTitulo(), c.getCargaHoraria(), c.getNivel(),
                        new CursoResponse.InstrutorResumoDTO(c.getInstrutor().getId(), c.getInstrutor().getNome())
                )).toList();
        return ResponseEntity.ok(cursos);
    }
}