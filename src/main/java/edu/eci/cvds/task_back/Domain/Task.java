package edu.eci.cvds.task_back.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

/**
 * Representa una tarea en el sistema de gestión de tareas.
 * Esta clase almacena información sobre la tarea, incluyendo su nombre,
 * descripción, fechas de creación y vencimiento, estado de finalización.
 */
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Generación automática del ID
    private Long id;
    private String name;
    private String description;
    private String dueDate;
    private String creationDate;
    private Boolean isCompleted = false;

    /**
     * Constructor para crear una nueva tarea.
     * @param name Nombre de la tarea.
     * @param description Descripción de la tarea.
     * @param dueDate Fecha de vencimiento de la tarea.
     */
    public Task(String name, String description, String dueDate) {
        this.name = name;
        this.description = description;
        this.creationDate = LocalDate.now().toString();
        this.dueDate = dueDate;
    }

    // Constructor predeterminado
    public Task() {
    }

    // Getters y setters
    public Long getId() {
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
}
