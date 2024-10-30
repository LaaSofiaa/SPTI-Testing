package edu.eci.cvds.task_back.config;

import edu.eci.cvds.task_back.repositories.mongo.TaskMongoRepository;
import edu.eci.cvds.task_back.repositories.mysql.TaskMySqlRepository;
import edu.eci.cvds.task_back.repositories.TaskRepository;
import edu.eci.cvds.task_back.repositories.mysql.UserMySqlRepository;
import edu.eci.cvds.task_back.repositories.text.TaskTextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Clase de configuración para los repositorios de tareas.
 * Esta clase permite seleccionar la implementación del repositorio
 * a utilizar según el valor de la propiedad 'task.repository.type' en los archivos de configuración.
 * Dependiendo de si se especifica 'mongo' o 'text', se devolverá una implementación de
 * TaskMongoRepository o TaskTextRepository.
 */
@Configuration
@EnableJpaRepositories(basePackages = "edu.eci.cvds.task_back.repositories.mysql")
@EnableMongoRepositories(basePackages = "edu.eci.cvds.task_back.repositories.mongo")
public class TaskConfig {

    @Value("${task.repository.type}")
    private String repositoryType;

    private final TaskMongoRepository taskMongoRepository;
    private final TaskTextRepository taskTextRepository;
    private final TaskMySqlRepository taskMySqlRepository;
    private  final UserMySqlRepository userRepository;

    /**
     * Constructor que inyecta las implementaciones de los repositorios.
     *
     * @param taskMongoRepository Repositorio de tareas basado en MongoDB.
     * @param taskTextRepository Repositorio de tareas basado en archivos de texto.
     */
    @Autowired
    public TaskConfig(TaskMongoRepository taskMongoRepository, TaskTextRepository taskTextRepository,TaskMySqlRepository taskMySqlRepository,
                      UserMySqlRepository userRepository) {
        this.taskMongoRepository = taskMongoRepository;
        this.taskTextRepository = taskTextRepository;
        this.taskMySqlRepository = taskMySqlRepository;
        this.userRepository = userRepository;
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
        } else if ("MySql".equalsIgnoreCase(repositoryType)){
            return taskMySqlRepository;
        }
        else {
            throw new IllegalArgumentException("Tipo de repositorio no soportado");
        }
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailService(){
        return email -> this.userRepository.findByUsername(email);
    }
}
