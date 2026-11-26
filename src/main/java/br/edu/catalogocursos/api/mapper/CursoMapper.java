package br.edu.catalogocursos.api.mapper;

import br.edu.catalogocursos.api.dto.CursoRequest;
import br.edu.catalogocursos.api.dto.CursoResponse;
import br.edu.catalogocursos.api.dto.CursoResumoResponse;
import br.edu.catalogocursos.domain.Curso;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper {

    public Curso toEntity(CursoRequest request) {
        Curso curso = new Curso();
        curso.setTitulo(request.titulo());
        curso.setCargaHoraria(request.cargaHoraria());
        curso.setNivel(request.nivel());
        return curso;
    }

    public CursoResponse toResponse(Curso curso) {
        return new CursoResponse(
                curso.getId(),
                curso.getTitulo(),
                curso.getCargaHoraria(),
                curso.getNivel(),
                new CursoResponse.InstrutorResumoDTO(
                        curso.getInstrutor().getId(),
                        curso.getInstrutor().getNome()
                )
        );
    }

    public CursoResumoResponse toResumoResponse(Curso curso) {
        return new CursoResumoResponse(
                curso.getId(),
                curso.getTitulo(),
                curso.getCargaHoraria(),
                curso.getNivel()
        );
    }
}