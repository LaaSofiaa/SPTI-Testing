# Laboratorio 05- SCRUM - CI/CD
*Integrantes:*

*-Andrea Camila Torres Gonzales.*

*-Jorge Andrés Gamboa Sierra.*

*-Jaider David Vargas Noriega.*

*-Laura Sofia Gil Chaves.*

## Parte I -Creando los Pipelines (CI - Continous Integration)

URL PÚBLICA: https://taskmanagercvds-bjdmg9hwaaa7erg0.eastus-01.azurewebsites.net

Usando el mismo código del proyecto realizado en el laboratorio 4 se generó un nuevo repositorio, para hacer los siguientes pasos:

1. Se configuró en github actions un workflow que contiene 3 jobs, el primer job se llamó build, el segundo test y el tercero deploy, además, este workflow disparó (events/trigger) on: pull_request, se tuvó que cumplir con lo siguientes requisitos.
   
    -build: realizar hasta la fase compile de maven.
    
    -test: realizar la fase de verify. 
    
    *¿Se puede lograr que se ejecute sin necesidad de compilar el proyecto?* : No es posible ejecutar mvn verify sin antes compilar el proyecto. Maven sigue una secuencia de fases que incluye la compilación antes de la verificación.
    
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
            -run: echo "En construccion..."


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

-Dado que tengo 1 tarea registrada, cuándo la elimino y consulto a nivel de servicio, entonces el resultado de la consulta no retornará ningún resultado.

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


### Desplegando en Azure usando CI/CD (Continous Deployment / Continous Delivery)

1. En Azure se creó un servicio de App Service con recursos que facturen 0 dólares.

   a) Primero se ingresó a Azure con nuestra cuenta institucional y se accede a AzureDevOps.

   ![image](https://github.com/user-attachments/assets/1cab2829-9e07-4a10-8499-b785cc8cf31c)

   b)Se ingresa a servicos gratuitos.

   ![image](https://github.com/user-attachments/assets/24626185-c332-4707-a6b8-fb77e7d1b0cb)

   c)Aquí debe aparecer soluciones nube para estudiantes.

   ![image](https://github.com/user-attachments/assets/dc06a244-4206-46d6-9901-37cedfe94b9e)
   ![image](https://github.com/user-attachments/assets/4c2b254a-86fa-4c04-a368-1a304efe099e)

   d) Se dirige a AppServices y luego crear Aplicación Web, donde se llena los espacios obligatorios para el proyecto.

   ![image](https://github.com/user-attachments/assets/b74cb51b-4a41-4d8f-8d9e-cb4798fc08f1)

   e) Ahora, pasamos a la opción Revisar y Crear

   ![image](https://github.com/user-attachments/assets/b3a6c157-34e1-4f7d-a0b2-58791f5bc9d8)

   f) Nos dirigimos al inicio de la página para mirar si se creo correctamnete el dominio del proyecto.

   ![image](https://github.com/user-attachments/assets/1e028340-22d5-403f-b959-11988fb3e53b)
   
   g) Ahora, se cambia a la pestaña de Centro de Implentación para conectar correctamnete el proyecto.

   ![image](https://github.com/user-attachments/assets/c4dc79e1-e993-4309-8543-063cf209c1eb)
   ![image](https://github.com/user-attachments/assets/407535fd-ae3d-4ff3-ac8c-dbd89831da32)


   h) En este momento, debería salirnos una nueva ventana para terminar de hacer la configuración del despliegue de la aplicación con las opciones que son necesarias para la 0 facturacióon.

    ![image](https://github.com/user-attachments/assets/e3dc89f0-854c-401f-b9b6-daf12a2533c2)
   ![image](https://github.com/user-attachments/assets/5799b61f-8502-4b69-a5da-adcee7de8ca0)

   i) Aquí nos saldrá una ventana que nos dirá que la implementación fue exitosa.

   ![image](https://github.com/user-attachments/assets/c425a751-e766-41d1-adc6-ad91345afd3c)


3. Se configura el job deploy que se creó en el paso 2, usando el action azure/webapps-deploy@v2 se despliega el jar generado al servicio de App Service. Las llaves necesarias para poder tener la conexión con Azure y el proyecto las da GitHub.

![image](https://github.com/user-attachments/assets/cf00f95e-d92b-4296-925d-d443041dbf0b)
![image](https://github.com/user-attachments/assets/c3e0387a-9bd5-4f30-9d35-1b56d874b938)
![image](https://github.com/user-attachments/assets/73674320-4250-45bf-8dcd-d7e1ee7902c7)



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

4. Se verifica qué el endpoint de la aplicación generado en App Service . En este punto la aplicación no debería funcionar.

![image](https://github.com/user-attachments/assets/5564e23b-e164-449a-b9b5-5b1702766b97)

*¿Donde se puede ver el mensaje de error de la aplicación o logs?* : 
Para ver los mensajes de error o logs de la aplicación, se revisa los detalles en la pestaña Actions de GitHub cuando el pipeline falle, donde cada step del workflow mostrará sus logs. Si se ejecuta la aplicación localmente, los logs aparecerán en la consola. Si el problema es el puerto incorrecto, se ajusta el archivo application.properties para usar el puerto adecuado  server.port=80.

5. Se crea una base de datos MySQL con facturación de 0 dólares, se hace que los datos de conexión sean con una o varias variables de entorno tanto en App Service como en el archivo application.properties del proyecto.

   a) En este caso, se tomo la base de satos MySql de Azure, donde se configura la conexión, el usuario y la contraseña.

   ![image](https://github.com/user-attachments/assets/b2c84479-3cc4-4dab-8b82-51cba928e970)

   b)Con la configuración anteriormente hecha, en azure ya se establece la base de datos.
   
   ![image](https://github.com/user-attachments/assets/8a9a3ea7-85c5-46ed-9bc3-e5a22a1f7926)

   c) Ahora vamos a variables de entorno, donde agregamos o editamos la conexión de esta.

   ![image](https://github.com/user-attachments/assets/e0af26fa-3dee-4b1f-9cd9-b6ab4ca4b2b7)
   ![image](https://github.com/user-attachments/assets/7fef1f50-187b-4ae6-acf9-e3cce110622e)


## PARTE II. GRÁFICOS

### Generación de datos por procedimientos

1. Se agregaron 3 nuevos campos, nivel de dificultad (Alto, medio, Bajo), prioridad(de 1 a 5) donde 1 es baja prioridad y 5 alta y tiempo promedio de desarrollo a la entidad Tarea existente. Aqui se agregó un nuevo método que genera "proceduralmente" nuevas tareas, aleatoriamente entre 100 y 1000.

    ```java
       public void RandomTask(){
           Random random = new Random();
           int randomTasks = random.nextInt(100,1001);
           String[] difficulties = {"High", "Middle", "Low"};
   
           for(int i =0; i< randomTasks;i++){
               String name = "Task" + (i+1);
               String description = "Description" +(i+1);
               String dueDate = LocalDate.now().plusDays(random.nextInt(30) + 1).toString();
               String difficulty = difficulties[random.nextInt(difficulties.length)];
               Integer priority = random.nextInt(1,6);
               double estimatedTime = random.nextDouble()*10;
               Task task = new Task(name,description,dueDate,difficulty,priority,estimatedTime);
               saveTask(task);
           }
    }
   
   ```
2. Se escogió la bibloteca Chart.js gracias a:

![image](https://github.com/user-attachments/assets/658a63bf-5664-4d91-a6a0-befc1e764278)

En el contexto de nuestro proyecto de gestión de tareas, donde se esten creando gráficos, los contras de chart.js no parecen ser una limitación significativa. La capacidad de crear gráficos básicos y responsivos, junto con su facilidad de uso y buen soporte para animaciones, lo hace una opción adecuada para implementar sin necesidad de funcionalidades avanzadas o alta interactividad.

3. Se realizó una página nueva en la estructura TaskManager -Analytics donde se visulizan las siguientes gráficas:
   
*Histograma de Dificultad*

![image](https://github.com/user-attachments/assets/9c648d60-37b5-4749-98d1-262042f45534)

*Número de tareas finalizadas por tiempo*

![image](https://github.com/user-attachments/assets/5603ccdf-cfb3-4652-ac48-b5fe549b03ef)

*Promedios de tareas por prioridad*

![image](https://github.com/user-attachments/assets/cedc5baa-9008-49e5-9d8b-3abc44a3019f)


*Tiempo total invertido por tareas realizadas*

![image](https://github.com/user-attachments/assets/7446f669-d61c-4b7b-bda3-3e100df315a4)

