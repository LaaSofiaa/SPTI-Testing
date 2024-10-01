package edu.eci.cvds.task_back.Domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Representa una tarea en el sistema de gestión de tareas.
 * Esta clase almacena información sobre la tarea, incluyendo su nombre,
 * descripción, fechas de creación y vencimiento,  estado de finalización.
 */
@Document("tasks")
public class Task {

    @Id
    private String id;
    private String name;
    private String description;
    private String dueDate;
    private String creationDate;
    private Boolean isCompleted = false;
    private String difficulty;
    private int priority;
    private double estimatedTime;

    /**
     * Constructor para crear una nueva tarea.
     * @param id Identificador único de la tarea.
     * @param name Nombre de la tarea.
     * @param description Descripción de la tarea.
     * @param dueDate Fecha de vencimiento de la tarea.
     */
    public Task(String id, String name, String description, String dueDate, String difficulty, int priority, double estimatedTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creationDate = LocalDate.now().toString();
        this.dueDate = dueDate;
        this.difficulty = difficulty;
        this.priority = priority;
        this.estimatedTime =estimatedTime;
    }

    // Getters y setters
    public String getId() {
        return id;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(double estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
}

