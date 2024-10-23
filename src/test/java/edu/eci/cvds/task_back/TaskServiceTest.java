package edu.eci.cvds.task_back;


import edu.eci.cvds.task_back.Domain.Role;
import edu.eci.cvds.task_back.Domain.Task;
import edu.eci.cvds.task_back.Domain.User;
import edu.eci.cvds.task_back.Repositories.mysql.TaskMySqlRepository;
import edu.eci.cvds.task_back.Repositories.mysql.UserMySqlRepository;
import edu.eci.cvds.task_back.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    //@Mock
    //private TaskRepository taskRepository;
    @Mock
    private TaskMySqlRepository taskMySqlRepository;
    @Mock
    private UserMySqlRepository userMySqlRepository;


    @InjectMocks
    private UserService taskService;
    private String userId = "dsk123dcs";
    private String userId1 = "ddfs3456s";
    private User user,user1;
    private Task task1, task2;
    private String taskId1 = "id1", taskId2 = "id2";


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("Claudia", "clau@mail.com", "password");
        user1= new User("Oscar", "oscar@mail.com", "passwd");
        user.setId(userId);
        user1.setId(userId1);
        when(userMySqlRepository.getUser(userId)).thenReturn(user);
        when(userMySqlRepository.getUser(userId1)).thenReturn(user1);
        task1 = new Task("Task1", "Description1", "2024-10-01", "High", 1, 2.0);
        task1.setId(taskId1);
        task2 = new Task("Task2", "Description2", "2024-10-02", "Medium", 2, 3.0);
        task2.setId(taskId2);
    }

    @Test
    void testRandomTaskWithOutIdUser() {
        // Espiamos el objeto taskService
        UserService spyTaskService = spy(taskService);

        // Simulamos el método saveTask
        doNothing().when(spyTaskService).saveTask(any(Task.class));

        // Llamamos al método que estamos probando
        spyTaskService.RandomTask();

        // Verificamos que saveTask ha sido llamado al menos 100 y como máximo 1000 veces
        int numberOfInvocations = mockingDetails(spyTaskService).getInvocations().stream()
                .filter(invocation -> invocation.getMethod().getName().equals("saveTask"))
                .toArray().length;

        // Verificamos que el número de invocaciones está dentro del rango esperado
        assertTrue(numberOfInvocations >= 100 && numberOfInvocations <= 1000);
    }



    @Test
    public void testTaskConstructor() {
        // Crear un objeto User
        User user = new User("Jorge", "jorge@mail.com", "password");

        // Crear una instancia de Task utilizando el constructor
        Task task = new Task(user, "Test Task", "This is a test description", "2024-12-31", "High", 5, 7.5);

        // Verificar que los atributos se inicializan correctamente
        assertEquals(user, task.getUser());
        assertEquals("Test Task", task.getName());
        assertEquals("This is a test description", task.getDescription());
        assertEquals("2024-12-31", task.getDueDate());
        assertEquals("High", task.getDifficulty());
        assertEquals(Integer.valueOf(5), task.getPriority());  // Convertir a Integer explícitamente
        assertEquals(7.5, task.getEstimatedTime(), 0.01);
    }


    /*Pruebas Lab5*/




// Dado que no hay ninguna tarea registrada,
// Cuándo la consulto a nivel de servicio, Entonces la consulta no retornará ningún resultado
    @Test
    void UnsucessfullIDQuery() {

        // Se intenta obtener una tarea que no existe con su ID
        Task result = taskService.getTask("2");

        // Llamar al método getTasks en el servicio para obtener todas las tareas
        List<Task> results = taskService.getTasks();

        // Verificar que el resultado es nulo
        assertEquals(results.size(),0);

        // Verificar que la tarea consultada con el ID no existe
        assertNull(result);
    }

//  Dado que tengo 1 tarea registrada, Cuando lo consulto a nivel de servicio,
//  Entonces la consulta será exitosa validando el campo id.
    @Test
    void SucessfullIDQuery() {
        // Crear un objeto Task con ID "7" y otros detalles
        Task task = new Task("Test Task", "Description", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), "high", 3, 3.8);
        task.setId("7");

        // Configurar el comportamiento del mock para devolver el task creado al buscar por ID "7"
        when(taskMySqlRepository.findTaskById("7")).thenReturn(task);

        // Llamar al método getTask en el servicio para obtener la tarea por ID
        Task result = taskService.getTask("7");

        // Verificar que el resultado no es nulo
        assertNotNull(result);
        // Comprobar que el ID de la tarea resultante es "7"
        assertEquals("7", result.getId());
    }

//  Dado que no hay ninguna tarea registrada,
//  Cuándo lo creo a nivel de servicio, Entonces la creación será exitosa.
    @Test
    void testSaveTask() {
        // Crear un objeto Task con un ID, nombre, descripción y fecha de vencimiento
        Task task = new Task( "Test Task", "Description", LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),"high",3,3.8);

        task.setId("1");

        // Llamar al método saveTask del servicio para guardar la tarea
        taskService.saveTask(task);

        // Verificar que el método saveTask del repositorio fue llamado una vez con el objeto Task
        verify(taskMySqlRepository, times(1)).saveTask(task);
    }

//  Dado que tengo 1 tarea registrada,
//  Cuándo la elimino a nivel de servicio, Entonces la eliminación será exitosa.
    @Test
    void successfulTaskDelete() {
        // Crear un objeto Task con ID "2" y otros detalles
        Task task = new Task( "Test Task", "Description", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),"high",3,3.8);

        task.setId("2");
        // Guardar la tarea
        taskService.saveTask(task);

        when(taskMySqlRepository.findAllTasks()).thenReturn(Collections.singletonList(task));

        // Llamar al método getTasks en el servicio para obtener todas las tareas
        List<Task> results = taskService.getTasks();

        // Verificar que la lista contiene una tarea
        assertEquals(1, results.size());

        taskService.deleteTask("2");

        // Configurar el comportamiento del mock para devolver una lista con la tarea creada
        when(taskMySqlRepository.findAllTasks()).thenReturn(Collections.emptyList());

        List<Task> result2 = taskService.getTasks();

        assertEquals(0,result2.size());
    }

//  Dado que tengo 1 tarea registrada, Cuándo la elimino y consulto a nivel de servicio,
//  Entonces el resultado de la consulta no retornará ningún resultado.
    @Test
    void successfulTaskDeleteAndGetNullwithID() {

        // Crear un objeto Task con ID "2" y otros detalles
        Task task = new Task( "Test Task", "Description", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),"high",3,3.8);
        task.setId("2");
        // Guardar la tarea
        taskService.saveTask(task);

        when(taskMySqlRepository.findAllTasks()).thenReturn(Collections.singletonList(task));

        // Llamar al método getTasks en el servicio para obtener todas las tareas
        List<Task> results = taskService.getTasks();

        // Verificar que la lista contiene una tarea
        assertEquals(1, results.size());

        taskService.deleteTask("2");

        // Configurar el comportamiento del mock para devolver una lista con la tarea creada
        when(taskMySqlRepository.findAllTasks()).thenReturn(Collections.emptyList());

        List<Task> result2 = taskService.getTasks();

        assertEquals(0,result2.size());

        when(taskMySqlRepository.findTaskById("2")).thenReturn(null);

        Task taskFinal = taskService.getTask("2");
        assertNull(taskFinal);
    }


    @Test
    public void testGettersAndSetters() {
        // Crear una instancia de Task
        Task task = new Task();

        // Definir valores para los atributos
        String id = "123e4567-e89b-12d3-a456-426614174000";
        String name = "Test Task";
        String description = "This is a test description";
        String dueDate = "2024-12-31";
        String creationDate = "2024-01-01";
        Boolean isCompleted = true;
        String difficulty = "High";
        Integer priority = 5;
        double estimatedTime = 7.5;

        // Probar los setters
        task.setId(id);

        task.setDescription(description);

        task.setCreationDate(creationDate);
        task.setIsCompleted(isCompleted);
        task.setDifficulty(difficulty);
        task.setPriority(priority);
        task.setEstimatedTime(estimatedTime);

        // Probar los getters
        assertEquals(id, task.getId());

        assertEquals(description, task.getDescription());

        assertEquals(creationDate, task.getCreationDate());
        assertEquals(isCompleted, task.getIsCompleted());
        assertEquals(difficulty, task.getDifficulty());
        assertEquals(priority, task.getPriority());
        assertEquals(estimatedTime, task.getEstimatedTime(), 0.01);
    }


    //laboratorio 6

    @Test
    void testRandomTask() throws Exception {
        UserService spyTaskService = spy(taskService);

        // Simula el método saveTaskByUser
        doNothing().when(spyTaskService).saveTaskByUser(anyString(), any(Task.class));

        // Llamamos al método probado
        spyTaskService.RandomTask(userId);

        // Verificamos que saveTaskByUser ha sido llamado múltiples veces
        verify(spyTaskService, atLeastOnce()).saveTaskByUser(eq(userId), any(Task.class));
    }

    @Test
    void testRandomTask2() throws Exception {
        UserService spyTaskService = spy(taskService);

        // Simula el método saveTaskByUser
        doNothing().when(spyTaskService).saveTaskByUser(anyString(), any(Task.class));

        // Llamamos al método probado
        spyTaskService.RandomTask(userId);

        // Verificamos que saveTaskByUser ha sido llamado al menos una vez
        verify(spyTaskService, atLeastOnce()).saveTaskByUser(eq(userId), any(Task.class));

        // Capturamos el número de invocaciones a saveTaskByUser
        int numberOfInvocations = mockingDetails(spyTaskService).getInvocations().stream()
                .filter(invocation -> invocation.getMethod().getName().equals("saveTaskByUser"))
                .toArray().length;

        // Aseguramos que el número de invocaciones está dentro del rango esperado
        assertTrue(numberOfInvocations >= 100 && numberOfInvocations <= 1000);
    }

    @Test
    public void testCreateUserSuccessful() throws Exception {
        // Configura el comportamiento simulado para el repositorio de usuarios
        when(userMySqlRepository.findByEmail(user.getEmail())).thenReturn(null);
        // Llama al método que se va a probar
        taskService.createUser(user);
        // Verifica que se llame a userRepository.createUser() con el usuario correcto
        verify(userMySqlRepository).createUser(user);
    }

    @Test
    public void testAuthenticationSuccessful() throws Exception {
        // Configura el comportamiento simulado para el repositorio de usuarios
        when(userMySqlRepository.findByEmail(user.getEmail())).thenReturn(user);
        // Llama al método de autenticación
        String authUser = taskService.authentication(user.getEmail(),user.getPasswd());
        // Verifica que se retorna el usuario autenticado
        assertEquals(user.getId(), authUser);
        verify(userMySqlRepository).findByEmail(user.getEmail());
    }

    @Test
    public void testModifyUserSuccess() throws Exception {
        // Simulamos que el usuario ya existe
        when(userMySqlRepository.getUser(user.getId())).thenReturn(user);

        // Llamamos al método de modificar usuario
        boolean result = taskService.modifyUser(user1);

        // Verificamos que el usuario fue modificado correctamente
        assertTrue(result);
        verify(userMySqlRepository).modifyUser(user1);
    }

    @Test
    public void testDeleteUserSuccess() throws Exception {
        // Simulamos que el usuario ya existe
        when(userMySqlRepository.getUser(user.getId())).thenReturn(user);

        // Llamamos al método de eliminar usuario
        boolean result = taskService.deleteUser(user.getId());

        // Verificamos que el usuario fue eliminado correctamente
        assertTrue(result);
        verify(userMySqlRepository).deleteUser(user);
    }

    @Test
    public void testGetUsernameSuccess() throws Exception {
        // Simulamos que el usuario existe
        when(userMySqlRepository.getUserName(userId)).thenReturn(user.getUsername());

        // Llamamos al método getUsername
        String username = taskService.getUsername(userId);

        // Verificamos el resultado
        assertEquals(user.getUsername(), username);
        verify(userMySqlRepository).getUserName(userId);
    }
    @Test
    public void testSaveTaskWithNullUser() {
        // Configurar el comportamiento simulado para el repositorio de usuarios
        when(userMySqlRepository.getUser("invalidUserId")).thenReturn(null);

        // Intentar guardar una tarea con un ID de usuario que no existe y verificar la excepción
        Exception exception = assertThrows(Exception.class, () -> {
            taskService.saveTaskByUser("invalidUserId", task1);
        });

        // Verificar que el mensaje de la excepción sea el esperado
        assertEquals("The user doesn't exist", exception.getMessage());
    }
    @Test
    public void testSaveTaskByUserThrowsException() {
        // Simular que el repositorio de usuarios devuelve null para un ID de usuario inválido
        when(userMySqlRepository.getUser("invalidUserId")).thenReturn(null);

        // Intentar guardar una tarea y verificar que se lanza la excepción Exception
        Exception exception = assertThrows(Exception.class, () -> {
            taskService.saveTaskByUser("invalidUserId", task1);
        });

        // Verificar que el mensaje de la excepción sea el esperado
        assertEquals("The user doesn't exist", exception.getMessage());
    }

    //setters
    @Test
    public void testSetUsername() {
        String username = "Jorge";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testSetEmail() {
        String email = "jorge@mail.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testSetPasswd() {
        String password = "newPassword";
        user.setPasswd(password);
        assertEquals(password, user.getPasswd());
    }

    @Test
    public void testSetId() {
        String id = "dflkdnscls";
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    void testUserMethods() {
        // Prueba de getPassword
        assertEquals("password", user.getPassword());

        // Prueba de isAccountNonExpired
        assertTrue(user.isAccountNonExpired());

        // Prueba de isAccountNonLocked
        assertTrue(user.isAccountNonLocked());

        // Prueba de isCredentialsNonExpired
        assertTrue(user.isCredentialsNonExpired());

        // Prueba de isEnabled
        assertTrue(user.isEnabled());
    }

    @Test
    void testUserConstructor() {
        // Crear un nuevo usuario utilizando el constructor con parámetros
        User newUser = new User("Alice", "alice@mail.com", "securepass", Role.USER);

        // Verificar que los atributos se inicializaron correctamente
        assertEquals("Alice", newUser.getUsername());
        assertEquals("alice@mail.com", newUser.getEmail());
        assertEquals("securepass", newUser.getPassword());
    }
    @Test
    void testMarkTaskAsCompleted() throws Exception {
        // Configurar el mock para devolver la tarea
        when(taskMySqlRepository.findTaskById(taskId1)).thenReturn(task1);

        // Llamar al método
        taskService.markTaskAsCompleted(taskId1);

        // Verificar que la tarea fue marcada como completada
        assertTrue(task1.getIsCompleted());

        // Verificar que se actualizó la tarea en el repositorio
        verify(taskMySqlRepository, times(1)).updateTask(task1);
    }

    @Test
    void testGetTasksByUser() {
        // Crear una lista de tareas para el usuario
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);

        // Configurar el mock para devolver las tareas del usuario
        when(taskMySqlRepository.findTasksByUser(user)).thenReturn(tasks);

        // Llamar al método
        List<Task> resultTasks = taskService.getTasksByUser(userId);

        // Verificar que se devuelven las tareas correctas
        assertEquals(2, resultTasks.size());
        assertTrue(resultTasks.contains(task1));
        assertTrue(resultTasks.contains(task2));
    }
    
}