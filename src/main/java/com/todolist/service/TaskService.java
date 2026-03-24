package com.todolist.service;

import com.todolist.model.Task;
import com.todolist.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.time.temporal.TemporalAdjusters;

@Service //Marca a classe como um bean de serviço
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) { //injeção por construtor, facilita testes evita @Autowired em campo
        this.taskRepository = taskRepository;
    }

    // CREATE / UPDATE
    public Task save(Task task) {
        validateDateTime(task);
        return taskRepository.save(task);
    }
    
    public boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }

    // READ - listar todas
    public List<Task> findAll() {
        return taskRepository.findAll()
                .stream()
                .sorted((a, b) -> {
                    int byDate = a.getData().compareTo(b.getData());
                    if (byDate != 0) {
                        return byDate;
                    }
                    return a.getHora().compareTo(b.getHora());
                })
                .toList();
    }

    public List<Task> findByDay(LocalDate data) {
        return taskRepository.findByDataOrderByHoraAsc(data);
    }

    public List<Task> findByWeek(LocalDate referencia) {
        LocalDate inicioSemana = referencia.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate fimSemana = referencia.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        return taskRepository.findByDataBetweenOrderByDataAscHoraAsc(inicioSemana, fimSemana);
    }

    public List<Task> findByPeriod(LocalDate inicio, LocalDate fim) {
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("A data de inicio nao pode ser maior que a data de fim");
        }
        return taskRepository.findByDataBetweenOrderByDataAscHoraAsc(inicio, fim);
    }

    // READ - buscar por id
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    // DELETE
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    private void validateDateTime(Task task) {
        if (task.getData() == null || task.getHora() == null) {
            throw new IllegalArgumentException("Data e hora sao obrigatorias");
        }

        LocalDateTime dateTime = LocalDateTime.of(task.getData(), task.getHora());
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Nao e permitido cadastrar compromisso no passado");
        }
    }
}