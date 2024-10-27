package edu.eci.cvds.task_back.Controller;

import edu.eci.cvds.task_back.Domain.Task;
import edu.eci.cvds.task_back.Domain.User;
import edu.eci.cvds.task_back.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
/**
 * Controlador REST para gestionar las operaciones relacionadas con usuarios.
 * Proporciona endpoints para crear, actualizar, eliminar y obtener usuarios.
 */
@RestController
@RequestMapping("/taskManager")
public class UserController {
    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "https://taskManager.com")
    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam String userId) {
        try {
            if (userService.deleteUser(userId)) {
                return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User doesn't exist", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PatchMapping("/modifyUser")
    public ResponseEntity<?> modifyUser(@RequestBody User user) {
        try {
            if (userService.modifyUser(user)) {
                return new ResponseEntity<>("User modified successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User doesn't exist", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
    /**
     * Endpoint para guardar una nueva tarea.
     * @param task Objeto de tipo Task recibido en el cuerpo de la solicitud.
     * Esta operación permite crear una nueva tarea.
     * La anotación {@code @CrossOrigin} permite solicitudes de origen cruzado de cualquier dominio.
     */

    @PostMapping("/saveTaskByUser")
    public ResponseEntity<?> saveTaskByUser(@RequestParam String userId, @RequestBody Task task){
        try{
            userService.saveTaskByUser(userId, task);
            return new ResponseEntity<>("Task saved succesfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/saveTask")
    public ResponseEntity<?> saveTask(@RequestBody Task task) {
        try {
            userService.saveTask(task);
            return new ResponseEntity<>("Task saved succesfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para marcar una tarea como completada.
     * @param id Identificador de la tarea a marcar como completada, recibido como parámetro de solicitud.
     * Permite actualizar el estado de la tarea para indicar que ha sido completada.
     * La anotación {@code @CrossOrigin} permite solicitudes de origen cruzado de cualquier dominio.
     */

    @PatchMapping("/markTaskAsCompleted")
    public ResponseEntity<?> markTaskAsCompleted(@RequestParam String id) {
        try {
            userService.markTaskAsCompleted(id);
            return new ResponseEntity<>("Task successfully completed ", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para eliminar una tarea.
     * @param id Identificador de la tarea a eliminar, recibido como parámetro de solicitud.
     * Permite eliminar una tarea específica del sistema.
     * La anotación {@code @CrossOrigin} permite solicitudes de origen cruzado de cualquier dominio.
     */

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTask(@RequestParam String id) {
        try{
            userService.deleteTask(id);
            return new ResponseEntity<>("Task successfully deleted",HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para obtener todas las tareas.
     * @return Lista de tareas almacenadas en el sistema.
     * Permite obtener todas las tareas creadas. Las tareas se retornan en formato JSON.
     * La anotación {@code @CrossOrigin} permite solicitudes de origen cruzado de cualquier dominio.
     */


    @GetMapping("getTasks")
    public List<Task> getTasks(){
        return userService.getTasks();
    }


    @GetMapping("getTasksByUser")
    public List<Task> getTasksByUser(@RequestParam String userId){
        return userService.getTasksByUser(userId);
    }

    /**
     * Endpoint para obtener todas las tareas.
     * @return Lista de tareas almacenadas en el sistema.
     * Permite obtener todas las tareas creadas. Las tareas se retornan en formato JSON.
     * La anotación {@code @CrossOrigin} permite solicitudes de origen cruzado de cualquier dominio.
     */
    @CrossOrigin(origins = "*")
    @PostMapping ("generateTasks")
    public ResponseEntity<?> generateTasks(@RequestParam String idUser) {
        try {
            userService.RandomTask(idUser);
            return new ResponseEntity<>("Tasks being generated", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Genera un número aleatorio de tareas y las almacena en el sistema.
     * @return Una respuesta indicando que las tareas están en proceso de generación.
     */

    @PostMapping ("generateTasksss")
    public ResponseEntity<?> generateTasks() {
        try {
            userService.RandomTask();
            return new ResponseEntity<>("Tasks being generated", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Recupera el nombre de usuario basado en su ID.
     * @param idUser El ID del usuario cuyo nombre se desea recuperar.
     * @return El nombre del usuario o un mensaje de error si no se encuentra.
     */

    @GetMapping("getUser")
    public ResponseEntity<?> getUser(@RequestParam String idUser) {
        try{
            String user = userService.getUsername(idUser);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
