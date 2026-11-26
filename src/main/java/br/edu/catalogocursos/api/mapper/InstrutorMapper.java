package br.edu.catalogocursos.api.mapper;

import br.edu.catalogocursos.api.dto.InstrutorRequest;
import br.edu.catalogocursos.api.dto.InstrutorResponse;
import br.edu.catalogocursos.domain.Instrutor;
import org.springframework.stereotype.Component;

@Component
public class InstrutorMapper {

    public Instrutor toEntity(InstrutorRequest request) {
        Instrutor instrutor = new Instrutor();
        instrutor.setNome(request.nome());
        instrutor.setEmail(request.email());
        return instrutor;
    }

    public InstrutorResponse toResponse(Instrutor instrutor) {
        return new InstrutorResponse(
                instrutor.getId(),
                instrutor.getNome(),
                instrutor.getEmail()
        );
    }
}