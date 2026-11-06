package br.edu.catalogocursos.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instrutor")
public class Instrutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 160, unique = true)
    private String email;

    // Relacionamento Um-para-Muitos: Mapeado pelo atributo "instrutor" na classe Curso
    @OneToMany(mappedBy = "instrutor", fetch = FetchType.LAZY)
    private List<Curso> cursos = new ArrayList<>();

    public Instrutor() {}

    // Método obrigatório pela atividade para manter a consistência bidirecional
    public void adicionarCurso(Curso curso) {
        if (curso == null) {
            throw new IllegalArgumentException("O curso não pode ser nulo");
        }
        if (!this.cursos.contains(curso)) {
            curso.setInstrutor(this);
            this.cursos.add(curso);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Curso> getCursos() { return cursos; }
}