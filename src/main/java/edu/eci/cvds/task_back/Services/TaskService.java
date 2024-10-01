package edu.eci.cvds.task_back.Services;

import edu.eci.cvds.task_back.Domain.Task;
import edu.eci.cvds.task_back.Repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Servicio encargado de gestionar la lógica de negocio relacionada con las tareas.
 * Interactúa con un repositorio TaskRepository para realizar operaciones CRUD sobre las tareas.
 */
@Service
public class TaskService {
    private TaskRepository taskRepository;

    /**
     * Constructor que inyecta el repositorio de tareas mediante dependencia.
     * @param taskRepository El repositorio que se utilizará para la gestión de tareas.
     */
    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Recupera una tarea por su ID.
     * @param id El identificador de la tarea.
     * @return La tarea encontrada o {@code null} si no existe.
     */
    public Task getTask(String id){
        return taskRepository.findTaskById(id);
    }

    /**
     * Recupera todas las tareas almacenadas.
     * @return Lista de todas las tareas almacenadas.
     */
    public List<Task> getTasks(){
        return taskRepository.findAllTasks();
    }

    /**
     * Guarda una nueva tarea en el repositorio.
     * @param task La tarea a guardar.
     */
    public void saveTask(Task task){
        taskRepository.saveTask(task);
    }

    /**
     * Marca una tarea como completada cambiando su estado.
     * @param id El identificador de la tarea a marcar como completada.
     */
    public void markTaskAsCompleted(String id){
        Task taskRepo = getTask(id);
        if (taskRepo != null){
            taskRepo.setIsCompleted(true);
            taskRepository.updateTask(taskRepo);
        }
    }

    /**
     * Elimina una tarea del repositorio.
     * @param id El identificador de la tarea a eliminar.
     */
    public void deleteTask(String id){
        Task taskRepo = getTask(id);
        if (taskRepo != null){
            taskRepository.deleteTask(taskRepo);
        }
    }

//    public void RandomTask(){
//        Random random = new Random();
//        int randomTasks = random.nextInt(100,1000);
//        String[] difficulties = {"Alto", "Medio", "Bajo"};
//        List<Task> tasks = new ArrayList<>();
//
//        for(int i =0; i< randomTasks;i++){
//
//
//           // Task task = new Task(name,description,dueDate,difficulty,priority,estimatedTime);
//            // tasks.add(task);
//
//        }
//
//
//    }

}
