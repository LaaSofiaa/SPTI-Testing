# Laboratorio 05- SCRUM - CI/CD
*Integrantes:*

*-Andrea Camila Torres Gonzales.*

*-Jorge Andrés Gamboa Sierra.*

*-Jaider David Vargas Noriega.*

*-Laura Sofia Gil Chaves.*

### Parte I -Creando los Pipelines (CI - Continous Integration)
Usando el mismo código del proyecto realizado en el laboratorio 4 se generó un nuevo repositorio, para hacer los siguientes pasos:

1. Se configuró en github actions un workflow que contiene 3 jobs, el primer job se llamará build, el segundo test y el tercero deploy, además, este workflow se disparará (events/trigger) on: pull_request, se tuvó que cumplir con lo siguientes requisitos.
   
    -build: realizar hasta la fase compile de maven.
    
    -test: realizar la fase de verify. 
    
    *¿se puede lograr que se ejecute sin necesidad de compilar el proyecto?* : No es posible ejecutar mvn verify sin antes compilar el proyecto. Maven sigue una secuencia de fases que incluye la compilación antes de la verificación.
    
    -deploy: por ahora deberá imprimir en consola "En construcción ...", necesita (needs) que se haya ejecutado test antes de iniciar.

   ```java
   
      name: Build and Test Java Spring Boot Application 
       
      on:
        push:
          branches: [ "main" ]
        pull_request:
          branches: [ "main" ]
       
      jobs:
        build:
          runs-on: ubuntu-latest
          steps:
            - uses: actions/checkout@v3
            - name: Set up JDK 21
              uses: actions/setup-java@v3
              with:
                java-version: '21'
                distribution: 'temurin'
                cache: maven
            
            - name: Build with Maven
              run: mvn compile
      
        test:
          runs-on: ubuntu-latest
          needs: build
          steps:
            - uses: actions/checkout@v3
            - name: Set up JDK 21
              uses: actions/setup-java@v3
              with:
                java-version: '21'
                distribution: 'temurin'
                cache: maven
      
            
            - name: Run tests
              run: mvn verify
      
        deploy:
          runs-on: ubuntu-latest
          needs: test
          steps:
            - uses: actions/checkout@v3
            - name: Set up JDK 21
              uses: actions/setup-java@v3
              with:
                java-version: '21'
                distribution: 'temurin'
                cache: maven
            - name: Build with Maven
              run: mvn package -DskipTests 
              - name: Deploy to Azure WebApp
                uses: azure/webapps-deploy@v2
                with:
                  app-name: nombre App Service
                  publish-profile: secreto creado en GitHub
                  package:  nombre archivo .jar
    ```
   
En deploy no se muestra los accesos por temas de confidencialidad del proyecto. 


2.Se agregaron los siguientes tests:
   
-Dado que tengo 1 tarea registrada, cuando lo consulto a nivel de servicio, entonces la consulta será exitosa validando el campo id.

  ```java
      @Test
      void SucessfullIDQuery() {
          Task task = new Task( "Test Task", "Description", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
          task.setId("2");
          when(taskRepository.findTaskById("2")).thenReturn(task);
          Task result = taskService.getTask("2");
          assertNotNull(result);
          assertEquals("2", result.getId());
      }
  ```

-Dado que no hay ninguna tarea registrada, cuándo la consulto a nivel de servicio, entonces la consulta no retornará ningún resultado.

  ```java
      @Test
      void UnsucessfullIDQuery() {
          Task result = taskService.getTask("2");
          List<Task> results = taskService.getTasks();
          assertEquals(results.size(),0);
          assertNull(result);
      }
  ```

-Dado que no hay ninguna tarea registrada, cuándo lo creo a nivel de servicio, entonces la creación será exitosa.

  ```java
       @Test
        void successfulTaskQuery() {
            when(taskRepository.findAllTasks()).thenReturn(Collections.emptyList());
            List<Task> results = taskService.getTasks();
            assertEquals(0, results.size());
            Task task = new Task( "Test Task", "Description", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            task.setId("2");
            taskService.saveTask(task);
            when(taskRepository.findTaskById("2")).thenReturn(task);
            Task result = taskService.getTask("2");
            assertNotNull(result);
            when(taskRepository.findAllTasks()).thenReturn(Collections.singletonList(task));
            List<Task> results2 = taskService.getTasks();
            assertEquals(1, results2.size());
        }

  ```

-Dado que tengo 1 tarea registrada, cuándo la elimino a nivel de servicio, entonces la eliminación será exitosa.

  ```java
        @Test
        void successfulTaskDelete() {
            Task task = new Task( "Test Task", "Description", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            task.setId("2");
            taskService.saveTask(task);
            when(taskRepository.findAllTasks()).thenReturn(Collections.singletonList(task));
            List<Task> results = taskService.getTasks();
            assertEquals(1, results.size());
            taskService.deleteTask("2");
            when(taskRepository.findAllTasks()).thenReturn(Collections.emptyList());
            List<Task> result2 = taskService.getTasks();
            assertEquals(0,result2.size());
        }

  ```

-Dado que tengo 1 tarea registrada, Cuándo la elimino y consulto a nivel de servicio, Entonces el resultado de la consulta no retornará ningún resultado.

  ```java
        @Test
        void successfulTaskDeleteAndGetNullwithID() {
            Task task = new Task( "Test Task", "Description", LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            task.setId("2");
            taskService.saveTask(task);
            when(taskRepository.findAllTasks()).thenReturn(Collections.singletonList(task));
            List<Task> results = taskService.getTasks();
            assertEquals(1, results.size());
            taskService.deleteTask("2");
            when(taskRepository.findAllTasks()).thenReturn(Collections.emptyList());
            List<Task> result2 = taskService.getTasks();
            assertEquals(0,result2.size());
            when(taskRepository.findTaskById("2")).thenReturn(null);
            Task taskFinal = taskService.getTask("2");
            assertNull(taskFinal);
        }
  ```
3. Verificar que la ejecución del workflow es exitosa.

![image](https://github.com/user-attachments/assets/5d182e90-da3f-4869-9bae-356cc16731b2)
