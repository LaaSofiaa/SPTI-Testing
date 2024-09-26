# Laboratorio 04- SCRUM - DI/IOC - INTERNET
*Integrantes:*

*-Andrea Camila Torres Gonzales.*

*-Jorge Andrés Gamboa Sierra.*

*-Jaider David Vargas Noriega.*

*-Laura Sofia Gil Chaves.*

## Bitácora taskBack

###  1.  Planeación de un proyecto de software.

El proyecto consiste en una aplicación de gestión de tareas personales donde los usuarios podrán agregar, marcar como completadas, y eliminar tareas. La aplicación contará con una interfaz web y se conectará a un API REST desarrollado en Spring Boot. El backend permitirá la inyección de dependencias para el manejo de datos, pudiendo optar entre una base de datos en MongoDB Cloud o un archivo de texto plano para almacenar las tareas. Creamos un proyecto en GitHub para el back y para el front de manera independiente, a continuación mediante Spring Initializr  se crea un proyecto con Maven, Java 17 y dependencias requeridas para el trabajo. Se mantuvo el esquema de nombramiento  de los artefactos.

![image](https://github.com/user-attachments/assets/0551209e-8528-40b7-9434-2bbc3d29ac7f)


Se realizo toda la planificación que se requiere para poder dar inicio al proyecto planteado mediante Azure DevOps, por donde se asignó a cada integrante un función específica.


![image](https://github.com/user-attachments/assets/8443650e-d8ab-4d10-9910-71383ac668fc)



### 2.  Entender arquitectura cliente servidor.

La arquitectura cliente-servidor es un modelo donde el cliente envía solicitudes a un servidor, que las procesa y responde con los datos solicitados. El cliente inicia la comunicación y el servidor maneja las solicitudes, sirviendo a múltiples clientes al mismo tiempo. 
	
![image](https://github.com/user-attachments/assets/8e0892f4-c5be-4824-ba37-ba29e1d6a258)


### 3.Inyección de dependencias - Inversión de control.

 *Inversión de Control (IoC)*: Es un principio en el que el control del flujo del programa es "invertido". En lugar de que un objeto gestione sus propias dependencias (creando instancias de objetos de los que depende), se delega esta responsabilidad a un contenedor externo. 

*Inyección de Dependencias (DI)*: Es una forma específica de aplicar IoC. En DI, las dependencias  se proporcionan desde fuera,  generalmente por un contenedor. En lugar de que una clase cree sus propias dependencias, estas se "inyectan" a través de diferentes métodos. Esto permite cambiar las dependencias sin modificar el código de la clase. 

a) TaskService (Clase de Servicio): Esta clase maneja la lógica de negocio relacionada con las tareas.

 Inyección de dependencias es utilizada en la anotación `@Autowired` para inyectar la dependencia TaskRepository en el constructor de TaskService,  donde Spring gestiona la creación y vinculación del repositorio, aquí se presenta los métodos principales que se usaron:
    
    
-   `getTasks()`: Retorna una lista de todas las tareas almacenadas.
-   `saveTask(Task task)`: Guarda una tarea en la base de datos.
-   `markTaskAsCompleted(String id)`: Marca una tarea como completada y la guarda en la base de datos.
-   `deleteTask(String id)`: Elimina una tarea de la base de datos si existe
- `getTask(String id)`: Obtiene una tarea por su ID usando el repositorio.
    
   ```java
		package edu.eci.cvds.task_back;  
		import org.springframework.beans.factory.annotation.Autowired;  
		import org.springframework.stereotype.Service;  
		import java.util.List;  
  
		@Service  
		public class TaskService {  
		    private TaskRepository taskRepository;  
  
	    	@Autowired  
		  public TaskService(TaskRepository taskRepository) {  
		        this.taskRepository = taskRepository;  
		    }  
		   public Task getTask(String id){  
		        return taskRepository.findById(id).orElse(null);  
		    }  
		   public List<Task> getTasks(){  
		        return taskRepository.findAll();  
		    }  
		    public void saveTask(Task task){  
		        taskRepository.save(task);  
		    }  
		   public void markTaskAsCompleted(String id){  
			Task taskRepo = getTask(id);  
	        	taskRepo.setIsCompleted(true);  
	        	if (taskRepo != null){  
	            		taskRepository.save(taskRepo);  
	        	}  
	   	 }  
		 public void deleteTask(String id){  
	        	Task taskRepo = getTask(id);  
	        	if (taskRepo != null){  
	  			taskRepository.delete(taskRepo);  
	        	}  
	    }  
	}
   

b) Interfaz del Repositorio: El código implementa dos tipos de repositorios para manejar tareas: uno basado en un archivo JSON y otro usando MongoDB. La interfaz `TaskRepository` define las operaciones CRUD (Crear, Leer, Actualizar y Borrar), que son implementadas tanto por la clase `TaskTextRepository`, que interactúa con un archivo JSON, como por el repositorio `TaskRepository` basado en `MongoRepository`, que interactúa con una base de datos MongoDB. `TaskTextRepository` maneja las tareas almacenadas en `src/main/resources/tasks.json` usando `JSONParser`, mientras que el repositorio Mongo usa `findById`, `save`, y `delete` para gestionar tareas directamente en MongoDB, ofreciendo mayor escalabilidad y facilidad de uso en aplicaciones más complejas.  


```java 
	package edu.eci.cvds.task_back;

	import org.springframework.stereotype.Component;

	import java.util.List;
	@Component
	public interface TaskRepository {
 		void saveTask(Task task);
	    	List<Task> findAllTasks();
	    	void deleteTask(Task task);
		Task findTaskById(String id);
	    	void updateTask(Task task);
}	
```

```java 
	package edu.eci.cvds.task_back;  
	import org.springframework.data.mongodb.repository.MongoRepository;  
	import org.springframework.data.mongodb.repository.Query;  
	import org.springframework.stereotype.Repository;  
	import java.util.List;  
	  
	@Repository  
	public interface TaskMongoRepository extends MongoRepository<Task, String>, TaskTextRepository {
		   @Override
		    public default void saveTask(Task task){
		        save(task);
		    }
		    @Override
		    public default List<Task> findAllTasks(){
		        return findAll();
		    }
		    @Override
		    public default void deleteTask(Task task){
		        delete(task);
		    }
		    @Override
		    public default Task findTaskById(String id){
		        return findById(id).orElse(null);
		    }
		    @Override
		    public default void updateTask(Task task){ save(task); }
		}	
```
```java 
	package edu.eci.cvds.task_back;

	import org.json.simple.JSONObject;
	import org.json.simple.parser.ParseException;
	import org.springframework.stereotype.Repository;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.List;
	import java.io.FileReader;
	import java.util.Random;
	
	import org.json.simple.JSONArray;
	import org.json.simple.parser.JSONParser;
	@Repository
	public class TaskTextRepository implements TaskRepository {
	    private final static String filePath = "src/main/resources/tasks.json"; // Ruta del archivo JSON
	
	    @Override
	    public void saveTask(Task task) {
	        Task t = setRandomId(task);
	        List<Task> tasks = findAllTasks();
	        tasks.add(t);
	        saveAllTasks(tasks);
	    }
	    @Override
	    public List<Task> findAllTasks() {
	        JSONParser parser = new JSONParser();
	        try (FileReader fileReader = new FileReader(filePath)) {
	            Object obj = parser.parse(fileReader);
	            JSONArray tasks = (JSONArray) obj;
	            List<Task> taskList = new ArrayList<>();
	
	            for (Object task : tasks) {
	                JSONObject taskJson = (JSONObject) task;
	                String id = taskJson.get("id").toString();
	                String title = (String) taskJson.get("name");
	                String description = (String) taskJson.get("description");
	                // Convertir cadenas de fecha a LocalDate
	                String dueDate = (String) taskJson.get("dueDate");
	                String creationDate = (String) taskJson.get("creationDate");
	                boolean completed = (boolean) taskJson.get("isCompleted");
	                Task passedTask = new Task(id, title, description, dueDate);
	                passedTask.setCreationDate(creationDate);
	                passedTask.setIsCompleted(completed);
	                taskList.add(passedTask);
	            }
	
	            return taskList;
	        } catch (IOException e) {
	            return new ArrayList<>();
	        } catch (ParseException e) {
	            return new ArrayList<>();
	        }
	    }
	    @Override
	    public void deleteTask(Task task) {
	        List<Task> tasks = findAllTasks();
	        tasks.removeIf(t -> t.getId().equals(task.getId()));
	        saveAllTasks(tasks);
	    }
	    @Override
	    public void updateTask(Task task) {
	        List<Task> tasks = findAllTasks();
	        for (int i = 0; i < tasks.size(); i++) {
	            Task t = tasks.get(i);
	            if (t.getId().equals(task.getId())) {
	                tasks.set(i, task);
	                break;
	            }
	        }
	        saveAllTasks(tasks);
	    }
	
	    @Override
	    public Task findTaskById(String id) {
	        return findAllTasks().stream()
	                .filter(task -> task.getId().equals(id))
	                .findFirst()
	                .orElse(null);
	    }
	
	    private void saveAllTasks(List<Task> tasks) {
	        JSONArray jsonArray = new JSONArray();
	        for(Task task : tasks) {
	            JSONObject taskJson = new JSONObject();
	            taskJson.put("id", task.getId());
	            taskJson.put("name",task.getName());
	            taskJson.put("description", task.getDescription());
	            taskJson.put("dueDate", task.getDueDate());
	            taskJson.put("creationDate", task.getCreationDate());
	            taskJson.put("isCompleted", task.getIsCompleted());
	            jsonArray.add(taskJson);
	        }
	        System.out.println(jsonArray);
	        try(FileWriter file = new FileWriter(filePath)){
	            file.write(jsonArray.toJSONString());
	            file.flush();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	
	    private Task setRandomId(Task task) {
	        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	        Random random = new Random();
	        String taskId = "";
	        for (int i = 0; i < 6; i++) {
	            int index = random.nextInt(CHARACTERS.length());
	            char character = CHARACTERS.charAt(index);
	            taskId += character;
	        }
	        task.setId(taskId);
	        return task;
	    }
	}
		
```

c) TaskController (Controlador REST):  Expone la API REST para que los clientes  puedan interactuar con el servicio de tareas.
    
Anotaciones:

   -   `@RestController`: Define que esta clase es un controlador REST y puede manejar solicitudes HTTP.
     
   -  `@RequestMapping("/taskManager")`: Define que todas las rutas expuestas por este controlador estarán bajo el prefijo `/taskManager`.
     
   -   `@CrossOrigin(origins = "*")`: Permite peticiones desde cualquier origen (CORS), lo que permite el acceso desde cualquier dominio.
     
    
 Rutas principales:
    
   -   `POST /saveTask`: Permite guardar una tarea mediante una solicitud HTTP POST. Los datos de la tarea se reciben en el cuerpo de la solicitud (`@RequestBody`).

   -  `PATCH /markTaskAsCompleted`: Permite marcar una tarea como completada usando su ID mediante una solicitud HTTP PATCH con el parámetro `id`.

   -  `DELETE /delete`: Elimina una tarea por su ID con una solicitud HTTP DELETE.

   -  `GET /getTasks`: Retorna todas las tareas mediante una solicitud HTTP GET.
    
    
   ```java
		package edu.eci.cvds.task_back;  
		import org.springframework.beans.factory.annotation.Autowired;  
		import org.springframework.web.bind.annotation.*;  
		import org.springframework.web.bind.annotation.CrossOrigin;  
		import java.util.List;  
  
		@RestController  
		@RequestMapping("/taskManager")  
		public class taskController {  
  
	   	 @Autowired  
		 private TaskService taskService;  
	   	 @CrossOrigin(origins = "*")  
	    	@PostMapping("saveTask")  
	   	 public void saveTask(@RequestBody Task task){  
	        	taskService.saveTask(task);  
	    	}  
	   	 @CrossOrigin(origins = "*")  
	   	 @PatchMapping("/markTaskAsCompleted")  
	   	 public void markTaskAsCompleted(@RequestParam String id){  
	        	taskService.markTaskAsCompleted(id);  
	    	}  
	   	 @CrossOrigin(origins = "*")  
	   	 @DeleteMapping("/delete")  
	    	public void deleteTask(@RequestParam String id){  
	  		taskService.deleteTask(id);  
	   	 }  
	    	@CrossOrigin(origins = "*")  
	    	@GetMapping("getTasks")  
	    	public List<Task> getTasks(){  
	        	return taskService.getTasks();  
	   	 }  
	   	 }
```
			
 *Flujo de ejecución*:

1.  El usuario envía una solicitud HTTP al controlador `TaskController`.
2.  El controlador llama a `TaskService`, que gestiona la lógica de negocio.
3.  El servicio usa `TaskRepository` para interactuar con la base de datos MongoDB y realizar operaciones sobre las tareas.
4.  El resultado se devuelve al usuario.

### 4. Manejo de bases de datos no relacionales.

Ahora bien, se descarga e instala MongoDB una base de datos no relacional.

Primero, ejecutamos el instalador descargado, luego se selecciona la opción de instalación "Complete".

Durante la instalación, se activa la opción de instalar MongoDB como un servicio.

![image](https://github.com/user-attachments/assets/76592873-b4ea-48a2-901e-5da75cfd6648)
![image](https://github.com/user-attachments/assets/5960490e-6d75-4e08-98b9-f05ba2a62ab6)

Siguiente a la instalación, configuramos el MongoDB en nuestro proyecto, donde agregamos la dependencia en el `pom.xml`.

![image](https://github.com/user-attachments/assets/347fd7e8-5742-47ce-b020-e19aa5642c33)

Luego, en el paquete de `resources` y en el archivo `application.properties` agregamos la configuración de la base de datos.

![image](https://github.com/user-attachments/assets/7a0dfd8d-21b5-4531-baed-6b89a6315ff5)

### 5. Garantizar calidad del código y detección de deuda técnica (pruebas unitarias)

*TaskControllerTest*:

La clase TaskControllerTest es una prueba unitaria diseñada para validar el comportamiento del controlador TaskController, que gestiona las operaciones relacionadas con el gestor de tareas.

Componentes Clave:

`MockMvc`: Permite realizar solicitudes HTTP simuladas al controlador y verificar las respuestas.

`Mockito`: Biblioteca para crear objetos simulados (mocks) y verificar interacciones con ellos.

`@Mock`: Simula TaskService.

`@InjectMocks`: Crea una instancia de TaskController con el mock inyectado.

Pruebas Unitarias:

`testSaveTask`: Simula una solicitud POST para guardar una tarea y verifica que el servicio saveTask sea llamado una vez.

```java
	@Test
	    public void testSaveTask() throws Exception {
	        mockMvc.perform(post("/taskManager/saveTask")
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content("{\"id\":\"1\", \"name\":\"Test Task 1\", \"description\":\"Description 1\", \"dueDate\":\"2024-12-31\"}"))
	                .andExpect(status().isOk());
	
	        verify(taskService, times(1)).saveTask(any(Task.class));
	    }
```
`testMarkTaskAsCompleted`:Simula una solicitud PATCH para marcar una tarea como completada, verificando que markTaskAsCompleted se llame con el ID correcto.
```java
@Test
    public void testMarkTaskAsCompleted() throws Exception {
        mockMvc.perform(patch("/taskManager/markTaskAsCompleted")
                        .param("id", "1"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).markTaskAsCompleted("1");
    }
```

`testDeleteTask`:Simula una solicitud DELETE para eliminar una tarea, verificando que deleteTask se invoque con el ID de la tarea.
```java
@Test
    public void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/taskManager/delete")
                        .param("id", "1"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).deleteTask("1");
    }
```

`testGetTasks`:Simula una solicitud GET para obtener tareas y verifica que se devuelvan las tareas esperadas y que getTasks se llame una vez.
```java
@Test
    public void testGetTasks() throws Exception {
        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskService.getTasks()).thenReturn(tasks);

        mockMvc.perform(get("/taskManager/getTasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Task 1"))
                .andExpect(jsonPath("$[1].name").value("Test Task 2"));

        verify(taskService, times(1)).getTasks();
```
*TaskServiceTest*:

La clase TaskServiceTest se utiliza para validar el comportamiento del servicio TaskService, que gestiona las operaciones sobre las tareas.

Componentes Clave:

`Mockito`: Facilita la creación de objetos simulados y la verificación de interacciones.

`@Mock`: Simula TaskRepository para evitar interacciones con la base de datos real.

`@InjectMocks`: Crea una instancia de TaskService y le inyecta el mock del repositorio.

Pruebas Unitarias:
`testGetTask`: Simula la recuperación de una tarea por ID. Se verifica que el resultado no sea nulo y que los atributos de la tarea sean correctos al usar assertEquals.

```java
@Test
    void testGetTask() {
        Task task = new Task("1", "Test Task", "Description", LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        when(taskRepository.findTaskById("1")).thenReturn(task);
        Task result = taskService.getTask("1");
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Test Task", result.getName());
    }
```

`testGetTasks`: Simula la obtención de todas las tareas. Se comprueba que la lista devuelta no sea nula, tenga el tamaño esperado y que contenga las tareas correctas.
```java
 @Test
    public void testGetTasks() throws Exception {
        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskService.getTasks()).thenReturn(tasks);

        mockMvc.perform(get("/taskManager/getTasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Task 1"))
                .andExpect(jsonPath("$[1].name").value("Test Task 2"));

        verify(taskService, times(1)).getTasks();
    }
```

`testSaveTask`: Simula el guardado de una tarea. Se verifica que el método save del repositorio se llame una vez con la tarea correcta.
```java
@Test
    public void testSaveTask() throws Exception {
        mockMvc.perform(post("/taskManager/saveTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\", \"name\":\"Test Task 1\", \"description\":\"Description 1\", \"dueDate\":\"2024-12-31\"}"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).saveTask(any(Task.class));
    }
```

`testMarkTaskAsCompleted`:Simula la acción de marcar una tarea como completada. Se verifica que la tarea se haya actualizado correctamente y que el método save se invoque en el repositorio.

```java
@Test
    public void testMarkTaskAsCompleted() throws Exception {
        mockMvc.perform(patch("/taskManager/markTaskAsCompleted")
                        .param("id", "1"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).markTaskAsCompleted("1");
    }
```

`testDeleteTask`:Simula la eliminación de una tarea. Se verifica que el método delete del repositorio se llame con la tarea correcta.
```java
 @Test
    public void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/taskManager/delete")
                        .param("id", "1"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).deleteTask("1");
    }
```

*TaskTestRepositoryTest:*

La clase `TaskTextRepositoryTest` es responsable de probar el comportamiento del repositorio de tareas que interactúa con un archivo de texto en formato JSON. 

Componentes Clave:

`@Spy`: Se utiliza @Spy para crear un espía sobre TaskTextRepository, lo que permite verificar las llamadas a métodos reales.
`@InjectMocks`: inyectar dependencias necesarias.

Pruebas Unitarias:

`testSaveTask`: Verifica que al guardar una tarea, se llame al método findAllTasks. Se asegura de que las interacciones con el repositorio se realicen correctamente.

```java
@Test
    public void testSaveTask() {
        Task task = new Task("1", "Test Task", "Description",
LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        doReturn(new ArrayList<>()).when(taskTextRepository).findAllTasks();
        taskTextRepository.saveTask(task);
        verify(taskTextRepository, times(1)).findAllTasks();
    }
```

`testFindAllTasks`: Comprueba que se devuelven correctamente varias tareas, verificando tanto la cantidad como los valores específicos de cada tarea.

```java
@Test
    public void testFindAllTasks(){
        Task task1 = new Task("1", "Test Task 1", "Description 1",
  LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Task task2 = new Task("2", "Test Task 2", "Description 2",        LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        List<Task> tasks = Arrays.asList(task1, task2);
        doReturn(tasks).when(taskTextRepository).findAllTasks();
        List<Task> result = taskTextRepository.findAllTasks();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Task 1", result.get(0).getName());
        assertEquals("Test Task 2", result.get(1).getName());
    }
```

`testDeleteTask`: Valida que al eliminar una tarea, se llame a findAllTasks para obtener la lista actualizada de tareas.

```java
@Test
    public void testDeleteTask() {
        Task task = new Task("1", "Test Task", "Description",
LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        List<Task> tasks = new ArrayList<>();
        tasks.add(task); 
        doReturn(tasks).when(taskTextRepository).findAllTasks();
        taskTextRepository.deleteTask(task);
        verify(taskTextRepository, times(1)).findAllTasks();
    }
```

`testUpdateTask`: Asegura que al actualizar una tarea, se llamen a los métodos correctos para manejar la operación.

```java
@Test
    public void testUpdateTask() {
        Task task = new Task("1", "Test Task", "Description",
LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        doReturn(tasks).when(taskTextRepository).findAllTasks();
        task.setDescription("Updated Description");
        taskTextRepository.updateTask(task);
        verify(taskTextRepository, times(1)).findAllTasks();
    }
```

`testFindTaskById`: Verifica que al buscar una tarea por ID, se obtenga la tarea correcta.

```java
@Test
    public void testFindTaskById() {
        Task task = new Task("1", "Test Task", "Description",
LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        List<Task> tasks = new ArrayList<>();
        tasks.add(task); 
        doReturn(tasks).when(taskTextRepository).findAllTasks();
        Task result = taskTextRepository.findTaskById("1");
        assertNotNull(result); 
        assertEquals("1", result.getId()); .
        assertEquals("Test Task", result.getName()); 
    }
```
`testFindAllTasks_ValidJsonFile`: Simula la lectura de un archivo JSON válido, asegurando que las tareas se lean y se asignen correctamente a objetos Task.

```java
@Test
    public void testFindAllTasks_ValidJsonFile() throws IOException, ParseException {
        
        String jsonContent = "[{\"id\":\"1\",\"name\":\"Test Task 1\",\"description\":\"Description 1\",\"dueDate\":\"2024-09-26\",\"creationDate\":\"2024-09-25\",\"isCompleted\":false}," +
                "{\"id\":\"2\",\"name\":\"Test Task 2\",\"description\":\"Description 2\",\"dueDate\":\"2024-09-27\",\"creationDate\":\"2024-09-25\",\"isCompleted\":true}]";
        try (FileWriter fileWriter = new FileWriter("src/main/resources/tasks.json")) {
            fileWriter.write(jsonContent);
        }
        List<Task> tasks = taskTextRepository.findAllTasks();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertEquals("Test Task 1", tasks.get(0).getName());
        assertEquals("Description 1", tasks.get(0).getDescription());
        assertEquals("2024-09-26", tasks.get(0).getDueDate());
        assertFalse(tasks.get(0).getIsCompleted()); 
        assertEquals("Test Task 2", tasks.get(1).getName());
        assertEquals("Description 2", tasks.get(1).getDescription());
        assertEquals("2024-09-27", tasks.get(1).getDueDate());
        assertTrue(tasks.get(1).getIsCompleted()); 
    }

```

`testFindAllTasksEmptyJsonFile`: Comprueba el manejo de un archivo JSON vacío, asegurando que se devuelva una lista vacía sin errores.

```java
@Test
    public void testFindAllTasksEmptyJsonFile() throws IOException {
        try (FileWriter fileWriter = new FileWriter("src/main/resources/tasks.json")) {
            fileWriter.write("[]"); 
        }

        List<Task> tasks = taskTextRepository.findAllTasks();
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }
```

`testFindAllTasksInvalidJsonFile`: Valida el comportamiento del método al intentar leer un archivo JSON con formato inválido, asegurando que se maneje la excepción y se devuelva una lista vacía.

```java
@Test
    public void testFindAllTasksInvalidJsonFile() {
        try (FileWriter fileWriter = new FileWriter("src/main/resources/tasks.json")) {
            fileWriter.write("{invalidJson}"); 
        } catch (IOException e) {
            fail("No se pudo escribir en el archivo");
        }
        List<Task> tasks = taskTextRepository.findAllTasks();
        assertNotNull(tasks);
        
        assertTrue(tasks.isEmpty());
    }
```
*TaskCongigTest*:

La clase TaskConfigTest tiene como objetivo probar la configuración del repositorio de tareas, verificando que el tipo de repositorio seleccionado (Mongo o Texto) funcione correctamente y maneje adecuadamente casos no soportados.

Componentes clave:

`@Mock y @InjectMocks`: Se utilizan para crear simulaciones de TaskMongoRepository y TaskTextRepository, así como para inyectarlas en TaskConfig, permitiendo probar su lógica sin depender de implementaciones concretas.

`MockitoAnnotations.openMocks(this)`: Inicializa las anotaciones de Mockito en el método setUp, asegurando que los mocks estén listos para usar antes de cada prueba.

`testTaskRepositoryMongo`: Configura el tipo de repositorio como "mongo". Verifica que el método taskRepository() devuelva una instancia de TaskMongoRepository.

```java
 @Test
    public void testTaskRepositoryMongo() {
        repositoryType = "mongo";
        taskConfig = new TaskConfig(taskMongoRepository, taskTextRepository);
        ReflectionTestUtils.setField(taskConfig, "repositoryType", repositoryType);
        TaskRepository repository = taskConfig.taskRepository();
        assertNotNull(repository);
        assertTrue(repository instanceof TaskMongoRepository);
    }
```

`testTaskRepositoryText`:Configura el tipo de repositorio como "text".
Verifica que el método taskRepository() devuelva una instancia de TaskTextRepository.

```java
@Test
    public void testTaskRepositoryText() {
        repositoryType = "text";
        taskConfig = new TaskConfig(taskMongoRepository, taskTextRepository);
        ReflectionTestUtils.setField(taskConfig, "repositoryType", repositoryType);
        TaskRepository repository = taskConfig.taskRepository();
        assertNotNull(repository);
        assertTrue(repository instanceof TaskTextRepository);
    }
```

`testTaskRepositoryUnsupportedType`: Configura un tipo de repositorio no soportado. Asegura que al llamar a taskRepository() se lance una IllegalArgumentException con el mensaje "Tipo de repositorio no soportado".

```java
@Test
    public void testTaskRepositoryUnsupportedType() {
        repositoryType = "unsupported";
        taskConfig = new TaskConfig(taskMongoRepository, taskTextRepository);
        ReflectionTestUtils.setField(taskConfig, "repositoryType", repositoryType);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taskConfig.taskRepository();
        });
        assertEquals("Tipo de repositorio no soportado", exception.getMessage());
    }
```


Las clases `TaskControllerTest`, `TaskServiceTest`, `TaskTestRepositoryTest` y `TaskCongigTest`  no solo validan el comportamiento de las clases, sino que también permite evaluar la calidad del código utilizando JaCoCo. JaCoCo es una herramienta de cobertura de código que ayuda a identificar qué partes del código han sido ejecutadas durante las pruebas, proporcionando métricas clave sobre la calidad del código.

![image](https://github.com/user-attachments/assets/bf00df6a-0bea-450c-9076-357eca31f99e)
![image](https://github.com/user-attachments/assets/a4c42419-f710-41d4-92fd-5553fc143e80)


### Referencias

-https://start.spring.io

-https://dev.azure.com

-https://www.mongodb.com/try/download/community















