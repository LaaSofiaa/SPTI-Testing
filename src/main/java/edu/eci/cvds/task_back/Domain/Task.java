package edu.eci.cvds.task_back.Domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

/**
 * Representa una tarea en el sistema de gestión de tareas.
 * Esta clase almacena información sobre la tarea, incluyendo su nombre,
 * descripción, fechas de creación y vencimiento, estado de finalización.
 */
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Generación automática del ID
    //@Column(columnDefinition = "string")
    private String id;
    private String name;
    private String description;
    private String dueDate;
    private String creationDate;
    private Boolean isCompleted = false;
    private String difficulty;
    private Integer priority;
    private double estimatedTime;


    /**
     * Constructor para crear una nueva tarea.
     *
     * @param name        Nombre de la tarea.
     * @param description Descripción de la tarea.
     * @param dueDate     Fecha de vencimiento de la tarea.
     */
    public Task(String name, String description, String dueDate, String difficulty, Integer priority, double estimatedTime) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.difficulty = difficulty;
        this.priority = priority;
        this.estimatedTime = estimatedTime;

    }

    // Constructor predeterminado
    public Task() {
        this.creationDate = LocalDate.now().toString();
    }

    // Getters y setters
    public String getId() {
        return id;  // ID se generará automáticamente
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(double estimatedTime) {
        this.estimatedTime = estimatedTime;
    }


    /**
     * Asigna un ID aleatorio a una tarea si no lo tiene.
     * @return La tarea con el ID asignado.
     */
    private void setRandomId() {
        String CHARACTERS = "0123456789";
        Random random = new Random();
        String taskId = "";
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char character = CHARACTERS.charAt(index);
            taskId += character;
        }
        //Long newId = Long.parseLong(taskId);
        //setId(taskId);
    }
}
