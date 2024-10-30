package edu.eci.cvds.task_back.repositories;

import edu.eci.cvds.task_back.domain.Task;
import edu.eci.cvds.task_back.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Interfaz que define los métodos básicos para gestionar tareas.
 * Las clases que implementan esta interfaz deben proporcionar
 * la lógica para almacenar, actualizar, eliminar y recuperar tareas.
 */
@Component
public interface TaskRepository {
    void saveTask(Task task);
    List<Task> findAllTasks();
    void deleteTask(Task task);
    Task findTaskById(String id);
    void updateTask(Task task);
    List<Task> findTasksByUser(User user);

}
