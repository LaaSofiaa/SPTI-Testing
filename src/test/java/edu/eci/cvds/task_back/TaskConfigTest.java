package edu.eci.cvds.task_back;

import edu.eci.cvds.task_back.Config.TaskConfig;
import edu.eci.cvds.task_back.Repositories.mongo.TaskMongoRepository;
import edu.eci.cvds.task_back.Repositories.mysql.TaskMySqlRepository;
import edu.eci.cvds.task_back.Repositories.text.TaskTextRepository;
import edu.eci.cvds.task_back.Repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class TaskConfigTest {

    @Mock
    private TaskMongoRepository taskMongoRepository;

    @Mock
    private TaskTextRepository taskTextRepository;

    @Mock
    private TaskMySqlRepository taskMySqlRepository;

    @InjectMocks
    private TaskConfig taskConfig;

    private String repositoryType;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTaskRepositoryMongo() {
        // Configurar el tipo de repositorio a "mongo"
        repositoryType = "mongo";
        taskConfig = new TaskConfig(taskMongoRepository, taskTextRepository, taskMySqlRepository);

        // Aquí se usa reflexión para establecer el valor de la propiedad
        ReflectionTestUtils.setField(taskConfig, "repositoryType", repositoryType);

        TaskRepository repository = taskConfig.taskRepository();

        assertNotNull(repository);
        assertTrue(repository instanceof TaskMongoRepository);
    }

    @Test
    public void testTaskRepositoryText() {
        // Configurar el tipo de repositorio a "text"
        repositoryType = "text";
        taskConfig = new TaskConfig(taskMongoRepository, taskTextRepository, taskMySqlRepository);

        // Aquí se usa reflexión para establecer el valor de la propiedad
        ReflectionTestUtils.setField(taskConfig, "repositoryType", repositoryType);

        TaskRepository repository = taskConfig.taskRepository();

        assertNotNull(repository);
        assertTrue(repository instanceof TaskTextRepository);
    }

    @Test
    public void testTaskRepositoryMySql() {
        // Configurar el tipo de repositorio a "MySql"
        repositoryType = "MySql";
        taskConfig = new TaskConfig(taskMongoRepository, taskTextRepository, taskMySqlRepository);

        // Aquí se usa reflexión para establecer el valor de la propiedad
        ReflectionTestUtils.setField(taskConfig, "repositoryType", repositoryType);

        TaskRepository repository = taskConfig.taskRepository();

        assertNotNull(repository);
        assertTrue(repository instanceof TaskMySqlRepository);
    }

    @Test
    public void testTaskRepositoryUnsupportedType() {
        // Configurar el tipo de repositorio a un valor no soportado
        repositoryType = "unsupported";
        taskConfig = new TaskConfig(taskMongoRepository, taskTextRepository, taskMySqlRepository);

        // Aquí se usa reflexión para establecer el valor de la propiedad
        ReflectionTestUtils.setField(taskConfig, "repositoryType", repositoryType);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taskConfig.taskRepository();
        });

        assertEquals("Tipo de repositorio no soportado", exception.getMessage());
    }
}