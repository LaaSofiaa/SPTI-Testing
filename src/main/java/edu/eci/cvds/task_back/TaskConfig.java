package edu.eci.cvds.task_back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración para los repositorios de tareas.
 * Esta clase permite seleccionar la implementación del repositorio
 * a utilizar según el valor de la propiedad 'task.repository.type' en los archivos de configuración.
 * Dependiendo de si se especifica 'mongo' o 'text', se devolverá una implementación de
 * TaskMongoRepository o TaskTextRepository.
 */
@Configuration
public class TaskConfig {

    @Value("${task.repository.type}")
    private String repositoryType;

    private final TaskMongoRepository taskMongoRepository;
    private final TaskTextRepository taskTextRepository;

    /**
     * Constructor que inyecta las implementaciones de los repositorios.
     *
     * @param taskMongoRepository Repositorio de tareas basado en MongoDB.
     * @param taskTextRepository Repositorio de tareas basado en archivos de texto.
     */
    @Autowired
    public TaskConfig(TaskMongoRepository taskMongoRepository, TaskTextRepository taskTextRepository) {
        this.taskMongoRepository = taskMongoRepository;
        this.taskTextRepository = taskTextRepository;
    }

    /**
     * Define el bean que selecciona el repositorio a utilizar en función del tipo configurado.
     * @return Una instancia de TaskRepository (puede ser Mongo o Text).
     * @throws IllegalArgumentException Si el tipo de repositorio no es soportado.
     */
    @Bean
    public TaskRepository taskRepository() {
        if ("mongo".equalsIgnoreCase(repositoryType)) {
            return taskMongoRepository;
        } else if ("text".equalsIgnoreCase(repositoryType)) {
            return taskTextRepository;
        } else {
            throw new IllegalArgumentException("Tipo de repositorio no soportado");
        }
    }
}
