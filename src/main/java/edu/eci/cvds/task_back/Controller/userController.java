package edu.eci.cvds.task_back.Controller;

import edu.eci.cvds.task_back.Domain.Task;
import edu.eci.cvds.task_back.Domain.User;
import edu.eci.cvds.task_back.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class userController {
    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "*")
    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody User user){
        try{
            userService.createUser(user);
            return new ResponseEntity<>("User created succesfully",HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam String userId){
        try{
            if(userService.deleteUser(userId)){
                return new ResponseEntity<>("User deleted succesfully",HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("User doesn't exist",HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @CrossOrigin(origins = "*")
    @PatchMapping("/modifyUser")
    public ResponseEntity<?> modifyUser(@RequestBody User user){
        try{
            if(userService.modifyUser(user)){
                return new ResponseEntity<>("User modified succesfully",HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("User doesn't exist",HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
    @CrossOrigin(origins = "*")
    @GetMapping("/authentication")
    public void deleteUser(@RequestParam String email,@RequestParam String passwd){
        userService.authentication(email,passwd);
    }
    /**
     * Endpoint para guardar una nueva tarea.
     * @param task Objeto de tipo Task recibido en el cuerpo de la solicitud.
     * Esta operación permite crear una nueva tarea.
     * La anotación {@code @CrossOrigin} permite solicitudes de origen cruzado de cualquier dominio.
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/saveTask")
    public void saveTask(@RequestBody Task task){
        userService.saveTask(task);
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
        userService.markTaskAsCompleted(id);
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
        userService.deleteTask(id);
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
        return userService.getTasks();
    }

    /**
     * Endpoint para obtener todas las tareas.
     * @return Lista de tareas almacenadas en el sistema.
     * Permite obtener todas las tareas creadas. Las tareas se retornan en formato JSON.
     * La anotación {@code @CrossOrigin} permite solicitudes de origen cruzado de cualquier dominio.
     */
    @CrossOrigin(origins = "*")
    @GetMapping("generateTasks")
    public void generateTasks(){
        userService.RandomTask();
    }
}
