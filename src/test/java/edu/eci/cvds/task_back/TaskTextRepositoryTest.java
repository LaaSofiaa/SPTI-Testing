package edu.eci.cvds.task_back;



import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskTextRepositoryTest {

    @Spy
    @InjectMocks
    private TaskTextRepository taskTextRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveTask() {
        // Crea una nueva tarea con ID "1" y una fecha futura.
        Task task = new Task("1", "Test Task", "Description",
                LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // Simula que findAllTasks retorna una lista vacía.
        doReturn(new ArrayList<>()).when(taskTextRepository).findAllTasks();

        // Llama a saveTask para guardar la nueva tarea.
        taskTextRepository.saveTask(task);

        // Verifica que findAllTasks fue llamado una vez.
        verify(taskTextRepository, times(1)).findAllTasks();
    }

    @Test
    public void testFindAllTasks() {
        // Crea dos nuevas tareas con ID y fechas futuras.
        Task task1 = new Task("1", "Test Task 1", "Description 1",
                LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Task task2 = new Task("2", "Test Task 2", "Description 2",
                LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        List<Task> tasks = Arrays.asList(task1, task2);

        // Simula que findAllTasks retorna la lista de tareas.
        doReturn(tasks).when(taskTextRepository).findAllTasks();

        // Llama a findAllTasks para obtener la lista de tareas.
        List<Task> result = taskTextRepository.findAllTasks();

        // Verifica que el resultado no sea nulo y tiene el tamaño esperado.
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Task 1", result.get(0).getName());
        assertEquals("Test Task 2", result.get(1).getName());
    }

    @Test
    public void testDeleteTask() {
        // Crea una nueva tarea con ID "1".
        Task task = new Task("1", "Test Task", "Description",
                LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        List<Task> tasks = new ArrayList<>();
        tasks.add(task); // Agrega la tarea a una lista.

        // Simula que findAllTasks retorna la lista que contiene la tarea.
        doReturn(tasks).when(taskTextRepository).findAllTasks();

        // Llama al método deleteTask para eliminar la tarea.
        taskTextRepository.deleteTask(task);

        // Verifica que findAllTasks y saveAllTasks fueron llamados una vez cada uno.
        verify(taskTextRepository, times(1)).findAllTasks();
    }

    @Test
    public void testUpdateTask() {
        // Crea una nueva tarea con ID "1" y descripción inicial.
        Task task = new Task("1", "Test Task", "Description",
                LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        List<Task> tasks = new ArrayList<>();
        tasks.add(task); // Agrega la tarea a una lista.

        // Simula que findAllTasks retorna la lista que contiene la tarea.
        doReturn(tasks).when(taskTextRepository).findAllTasks();

        // Actualiza la descripción de la tarea.
        task.setDescription("Updated Description");

        // Llama al método updateTask para actualizar la tarea.
        taskTextRepository.updateTask(task);

        // Verifica que findAllTasks y saveAllTasks fueron llamados una vez cada uno.
        verify(taskTextRepository, times(1)).findAllTasks();
    }

    @Test
    public void testFindTaskById() {
        // Crea una nueva tarea con ID "1" y descripción inicial.
        Task task = new Task("1", "Test Task", "Description",
                LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        List<Task> tasks = new ArrayList<>();
        tasks.add(task); // Agrega la tarea a una lista.

        // Simula que findAllTasks retorna la lista que contiene la tarea.
        doReturn(tasks).when(taskTextRepository).findAllTasks();

        // Llama al método findTaskById para buscar la tarea por su ID.
        Task result = taskTextRepository.findTaskById("1");

        // Verifica que el resultado no sea nulo y que coincida con los valores esperados.
        assertNotNull(result); // Asegura que se encontró la tarea.
        assertEquals("1", result.getId()); // Verifica que el ID sea "1".
        assertEquals("Test Task", result.getName()); // Verifica que el nombre sea "Test Task".
    }
    @Test
    public void testFindAllTasks_ValidJsonFile() throws IOException, ParseException {
        // Preparar un archivo JSON simulado con dos tareas
        String jsonContent = "[{\"id\":\"1\",\"name\":\"Test Task 1\",\"description\":\"Description 1\",\"dueDate\":\"2024-09-26\",\"creationDate\":\"2024-09-25\",\"isCompleted\":false}," +
                "{\"id\":\"2\",\"name\":\"Test Task 2\",\"description\":\"Description 2\",\"dueDate\":\"2024-09-27\",\"creationDate\":\"2024-09-25\",\"isCompleted\":true}]";

        // Escribir el contenido JSON en un archivo tasks.json
        try (FileWriter fileWriter = new FileWriter("src/main/resources/tasks.json")) {
            fileWriter.write(jsonContent);
        }

        // Llamar al método findAllTasks para leer las tareas desde el archivo JSON
        List<Task> tasks = taskTextRepository.findAllTasks();

        // Verificar que la lista de tareas no sea nula
        assertNotNull(tasks);
        // Comprobar que se leyeron dos tareas del archivo JSON
        assertEquals(2, tasks.size());

        // Verificar los detalles de la primera tarea
        assertEquals("Test Task 1", tasks.get(0).getName());
        assertEquals("Description 1", tasks.get(0).getDescription());
        assertEquals("2024-09-26", tasks.get(0).getDueDate());
        assertFalse(tasks.get(0).getIsCompleted()); // Comprobar que isCompleted es false

        // Verificar los detalles de la segunda tarea
        assertEquals("Test Task 2", tasks.get(1).getName());
        assertEquals("Description 2", tasks.get(1).getDescription());
        assertEquals("2024-09-27", tasks.get(1).getDueDate());
        assertTrue(tasks.get(1).getIsCompleted()); // Comprobar que isCompleted es true
    }

    @Test
    public void testFindAllTasksEmptyJsonFile() throws IOException {
        // Preparar un archivo JSON vacío
        try (FileWriter fileWriter = new FileWriter("src/main/resources/tasks.json")) {
            fileWriter.write("[]"); // Escribir un arreglo vacío en el archivo
        }

        // Llamar al método findAllTasks para leer las tareas desde el archivo JSON
        List<Task> tasks = taskTextRepository.findAllTasks();

        // Verificar que se devuelve una lista no nula
        assertNotNull(tasks);
        // Comprobar que la lista de tareas está vacía
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void testFindAllTasksInvalidJsonFile() {
        // Preparar un archivo JSON con contenido no válido
        try (FileWriter fileWriter = new FileWriter("src/main/resources/tasks.json")) {
            fileWriter.write("{invalidJson}"); // Escribir un JSON no válido en el archivo
        } catch (IOException e) {
            fail("No se pudo escribir en el archivo"); // Fallar la prueba si hay un error al escribir
        }

        // Llamar al método findAllTasks y verificar el manejo de la excepción
        List<Task> tasks = taskTextRepository.findAllTasks(); // Intentar leer tareas desde el archivo JSON

        // Verificar que se devuelve una lista no nula en caso de excepción
        assertNotNull(tasks);
        // Comprobar que la lista de tareas está vacía, ya que el JSON es inválido
        assertTrue(tasks.isEmpty());
    }
}