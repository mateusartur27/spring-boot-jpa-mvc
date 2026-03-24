package com.todolist.controller;

// Importa a entidade Task (modelo da aplicação)
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;

import com.todolist.model.Task;
import com.todolist.service.TaskService;
import jakarta.validation.Valid;

// Indica que a classe é um Controller REST (retorna JSON automaticamente)
@RestController

// Define o endpoint base da API → /tasks
@RequestMapping("/tasks")
public class TaskController {

    // Injeta o serviço responsável pelas operações da Task
    private final TaskService taskService;

    // Construtor para injeção de dependência (boa prática)
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // =========================
    // CREATE (Criar nova tarefa)
    // =========================
    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody Task task) {
        Task saved = taskService.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ==================================
    // READ (Listar todos os compromissos)
    // ==================================
    @GetMapping
    public ResponseEntity<List<Task>> listAll(
            @RequestParam(value = "data", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,

            @RequestParam(value = "inicio", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,

            @RequestParam(value = "fim", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {

        if (data != null) {
            return ResponseEntity.ok(taskService.findByDay(data));
        }

        if (inicio != null && fim != null) {
            return ResponseEntity.ok(taskService.findByPeriod(inicio, fim));
        }

        // Retorna 200 OK com a lista de tarefas
        return ResponseEntity.ok(taskService.findAll());
    }

    @GetMapping("/semana")
    public ResponseEntity<List<Task>> listByWeek(
            @RequestParam(value = "data", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        LocalDate referencia = (data == null) ? LocalDate.now() : data;
        return ResponseEntity.ok(taskService.findByWeek(referencia));
    }

    // ==================================
    // READ (Buscar tarefa por ID)
    // ==================================
    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {

        // Busca a tarefa pelo ID
        // Se encontrar → retorna 200 OK
        // Se não encontrar → retorna 404 Not Found
        return taskService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // UPDATE (Atualizar tarefa)
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<Task> update(
            @PathVariable("id") Long id,  // ID vindo da URL
            @Valid @RequestBody Task task        // Dados enviados no corpo da requisição
    ) {

        // Primeiro verifica se a tarefa existe
        return taskService.findById(id)
                .map(existing -> {

                    // Atualiza os campos da tarefa existente
                    existing.setData(task.getData());
                    existing.setHora(task.getHora());
                    existing.setDescricao(task.getDescricao());

                    // Salva a tarefa atualizada no banco
                    return ResponseEntity.ok(taskService.save(existing));
                })
                // Caso não exista, retorna 404
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================
    // DELETE (Excluir tarefa)
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {

        // Verifica se a tarefa existe antes de deletar
        if (!taskService.existsById(id)) {
            return ResponseEntity.notFound().build(); // 404
        }

        // Remove a tarefa pelo ID
        taskService.deleteById(id);

        // Retorna 204 No Content (sucesso sem corpo na resposta)
        return ResponseEntity.noContent().build();
    }
}