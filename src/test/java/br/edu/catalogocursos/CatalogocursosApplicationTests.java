package br.edu.catalogocursos;

import br.edu.catalogocursos.domain.Curso;
import br.edu.catalogocursos.domain.Instrutor;
import br.edu.catalogocursos.domain.NivelCurso;
import br.edu.catalogocursos.application.CursoService;
import br.edu.catalogocursos.application.InstrutorService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CatalogocursosApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private InstrutorService instrutorService;

    // 1. Instrutor associa um curso e mantém os dois lados consistentes[cite: 9]
    @Test
    void deveAssociarCursoEmanterConsistencia() {
        Instrutor instrutor = new Instrutor();
        instrutor.setNome("João");
        instrutor.setEmail("joao@teste.com");

        Curso curso = new Curso();
        curso.setTitulo("Java");

        instrutor.adicionarCurso(curso);

        assertTrue(instrutor.getCursos().contains(curso));
        assertEquals(instrutor, curso.getInstrutor());
    }

    // 2. Cadastro de curso com instrutor inexistente é rejeitado[cite: 9]
    @Test
    void deveRejeitarCursoComInstrutorInexistente() {
        Curso curso = new Curso();
        curso.setTitulo("Spring");
        curso.setCargaHoraria(40);
        curso.setNivel(NivelCurso.INICIANTE);

        assertThrows(EntityNotFoundException.class, () -> {
            cursoService.cadastrar(curso, 999L);
        });
    }

    // 3. Cadastro HTTP de instrutor inválido retorna 400[cite: 9]
    @Test
    void deveRetornar400ParaInstrutorInvalido() throws Exception {
        String jsonInvalido = "{\"nome\": \"\", \"email\": \"emailinvalido\"}";

        mockMvc.perform(post("/api/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest());
    }

    // 4. Listagem por instrutor retorna somente seus cursos, ordenados por título[cite: 9]
    @Test
    void deveListarCursosOrdenados() throws Exception {
        Instrutor instrutor = new Instrutor();
        instrutor.setNome("Maria");
        instrutor.setEmail("maria@teste.com");
        instrutor = instrutorService.cadastrar(instrutor);

        Curso c1 = new Curso(); c1.setTitulo("Zookeeper"); c1.setCargaHoraria(10); c1.setNivel(NivelCurso.AVANCADO);
        Curso c2 = new Curso(); c2.setTitulo("Angular"); c2.setCargaHoraria(20); c2.setNivel(NivelCurso.INICIANTE);

        cursoService.cadastrar(c1, instrutor.getId());
        cursoService.cadastrar(c2, instrutor.getId());

        mockMvc.perform(get("/api/instrutores/" + instrutor.getId() + "/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Angular"))
                .andExpect(jsonPath("$[1].titulo").value("Zookeeper"));
    }
}