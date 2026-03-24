package com.todolist.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.todolist.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> { //Task -> Entidade, Log -> Tipo do Id
	List<Task> findByDataOrderByHoraAsc(LocalDate data);

	List<Task> findByDataBetweenOrderByDataAscHoraAsc(LocalDate inicio, LocalDate fim);
}

/* Isso automaticamente fornece:

save() → criar / atualizar

findAll() → listar

findById() → buscar por id

deleteById() → deletar

@Repository não é obrigatório, mas ajuda na leitura.
-------------------------------*/