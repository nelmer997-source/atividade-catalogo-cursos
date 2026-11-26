package br.edu.catalogocursos.api.dto;

import br.edu.catalogocursos.domain.NivelCurso;

public record CursoResponse(
        Long id,
        String titulo,
        Integer cargaHoraria,
        NivelCurso nivel,
        InstrutorResumoDTO instrutor
) {
    public record InstrutorResumoDTO(Long id, String nome) {
    }
}