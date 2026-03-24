package com.todolist.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity //Esta classe vira uma tabela no banco de dados
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //O banco gera o id de forma automática

    @NotNull(message = "A data e obrigatoria")
    @FutureOrPresent(message = "A data deve ser hoje ou futura")
    private LocalDate data;

    @NotNull(message = "A hora e obrigatoria")
    private LocalTime hora;

    @NotBlank(message = "A descricao e obrigatoria")
    private String descricao;

    // Construtor padrão (obrigatório para o JPA) -- sem ele a aplicação não sobe
    public Task() {
    }

    // Construtor de conveniência
    public Task(LocalDate data, LocalTime hora, String descricao) {
        this.data = data;
        this.hora = hora;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}