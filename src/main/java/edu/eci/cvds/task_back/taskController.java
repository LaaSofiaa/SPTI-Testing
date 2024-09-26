package edu.eci.cvds.task_back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

/**
 * Controlador REST para gestionar las operaciones relacionadas con tareas.
 * Proporciona endpoints para crear, actualizar, eliminar y obtener tareas.
 */
@RestController
@RequestMapping("/taskManager")
public class taskController {

    @Autowired
    private TaskService taskService;

    /**
     * Endpoint para guardar una nueva tarea.
     * @param task Objeto de tipo Task recibido en el cuerpo de la solicitud.
     * Esta operación permite crear una nueva tarea.
     * La anotación {@code @CrossOrigin} permite solicitudes de origen cruzado de cualquier dominio.
     */
    @CrossOrigin(origins = "*")
    @PostMapping("saveTask")
    public void saveTask(@RequestBody Task task){
        taskService.saveTask(task);
    }

    /**
     * Endpoint para marcar una tarea como completada.
     * @param id Identificador de la tarea a marcar como completada, recibido como parámetro de solicitud.
     * Permite actualizar el estado de la tarea para indicar que ha sido completada.
     * La anotación {@code @CrossOrigin} permite solicitudes de origen cruzado de cualquier dominio.
     */
    @CrossOrigin(origins = "*")
    @PatchMapping("/markTaskAsCompleted")
    public void markTaskAsCompleted(@RequestParam String id){
        taskService.markTaskAsCompleted(id);
    }

    /**
     * Endpoint para eliminar una tarea.
     * @param id Identificador de la tarea a eliminar, recibido como parámetro de solicitud.
     * Permite eliminar una tarea específica del sistema.
     * La anotación {@code @CrossOrigin} permite solicitudes de origen cruzado de cualquier dominio.
     */
    @CrossOrigin(origins = "*")
    @DeleteMapping("/delete")
    public void deleteTask(@RequestParam String id){
        taskService.deleteTask(id);
    }

    /**
     * Endpoint para obtener todas las tareas.
     * @return Lista de tareas almacenadas en el sistema.
     * Permite obtener todas las tareas creadas. Las tareas se retornan en formato JSON.
     * La anotación {@code @CrossOrigin} permite solicitudes de origen cruzado de cualquier dominio.
     */
    @CrossOrigin(origins = "*")
    @GetMapping("getTasks")
    public List<Task> getTasks(){
        return taskService.getTasks();
    }

}

