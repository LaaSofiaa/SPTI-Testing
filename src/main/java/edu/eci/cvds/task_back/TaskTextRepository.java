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

/**
 * Implementación del repositorio de tareas utilizando un archivo JSON para el almacenamiento.
 * Se encarga de realizar operaciones CRUD en el archivo de tareas.
 */
@Repository
public class TaskTextRepository implements TaskRepository {
    private final static String filePath = "src/main/resources/tasks.json"; // Ruta del archivo JSON

    /**
     * Guarda una nueva tarea en el archivo JSON. Si la tarea no tiene un ID,
     * se le asigna un ID aleatorio.
     * @param task La tarea a guardar.
     */
    @Override
    public void saveTask(Task task) {
        Task t = setRandomId(task);
        List<Task> tasks = findAllTasks();
        tasks.add(t);
        saveAllTasks(tasks);
    }

    /**
     * Recupera todas las tareas almacenadas en el archivo JSON.
     * @return Lista de todas las tareas.
     */
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

    /**
     * Elimina una tarea del archivo JSON.
     * @param task La tarea a eliminar.
     */
    @Override
    public void deleteTask(Task task) {
        List<Task> tasks = findAllTasks();
        tasks.removeIf(t -> t.getId().equals(task.getId()));
        saveAllTasks(tasks);
    }

    /**
     * Actualiza una tarea existente en el archivo JSON.
     * @param task La tarea a actualizar.
     */
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

    /**
     * Busca una tarea por su ID en el archivo JSON.
     * @param id El identificador de la tarea.
     * @return La tarea encontrada o {@code null} si no existe.
     */
    @Override
    public Task findTaskById(String id) {
        return findAllTasks().stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Guarda todas las tareas en el archivo JSON.
     * @param tasks Lista de tareas a guardar.
     */
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

    /**
     * Asigna un ID aleatorio a una tarea si no lo tiene.
     * @param task La tarea a la que se le asignará el ID.
     * @return La tarea con el ID asignado.
     */
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
