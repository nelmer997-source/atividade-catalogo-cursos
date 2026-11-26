package br.edu.catalogocursos;

import br.edu.catalogocursos.application.CursoService;
import br.edu.catalogocursos.application.InstrutorService;
import br.edu.catalogocursos.domain.Curso;
import br.edu.catalogocursos.domain.Instrutor;
import br.edu.catalogocursos.domain.NivelCurso;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    void deveAssociarCursoEmanterConsistencia() {
        Instrutor instrutor = new Instrutor();
        instrutor.setNome("Jo\u00e3o");
        instrutor.setEmail("joao@teste.com");

        Curso curso = new Curso();
        curso.setTitulo("Java");

        instrutor.adicionarCurso(curso);

        assertTrue(instrutor.getCursos().contains(curso));
        assertEquals(instrutor, curso.getInstrutor());
    }

    @Test
    void deveRejeitarCursoComInstrutorInexistente() {
        Curso curso = new Curso();
        curso.setTitulo("Spring");
        curso.setCargaHoraria(40);
        curso.setNivel(NivelCurso.INICIANTE);

        assertThrows(EntityNotFoundException.class, () -> cursoService.cadastrar(curso, 999L));
    }

    @Test
    void deveRetornar400ParaInstrutorInvalido() throws Exception {
        String jsonInvalido = "{\"nome\": \"\", \"email\": \"emailinvalido\"}";

        mockMvc.perform(post("/api/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value("/api/instrutores"));
    }

    @Test
    void deveRetornar409ParaEmailDuplicado() throws Exception {
        Instrutor instrutor = new Instrutor();
        instrutor.setNome("Carlos Silva");
        instrutor.setEmail("carlos@teste.com");
        instrutorService.cadastrar(instrutor);

        String jsonDuplicado = "{\"nome\": \"Outro Carlos\", \"email\": \"carlos@teste.com\"}";

        mockMvc.perform(post("/api/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDuplicado))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.path").value("/api/instrutores"));
    }

    @Test
    void deveListarCursosOrdenados() throws Exception {
        Instrutor instrutor = new Instrutor();
        instrutor.setNome("Maria");
        instrutor.setEmail("maria@teste.com");
        instrutor = instrutorService.cadastrar(instrutor);

        Curso c1 = new Curso();
        c1.setTitulo("Zookeeper");
        c1.setCargaHoraria(10);
        c1.setNivel(NivelCurso.AVANCADO);

        Curso c2 = new Curso();
        c2.setTitulo("Angular");
        c2.setCargaHoraria(20);
        c2.setNivel(NivelCurso.INICIANTE);

        cursoService.cadastrar(c1, instrutor.getId());
        cursoService.cadastrar(c2, instrutor.getId());

        mockMvc.perform(get("/api/instrutores/" + instrutor.getId() + "/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Angular"))
                .andExpect(jsonPath("$[1].titulo").value("Zookeeper"));
    }
}