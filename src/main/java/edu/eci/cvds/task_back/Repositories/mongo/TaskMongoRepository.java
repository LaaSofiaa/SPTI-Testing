package edu.eci.cvds.task_back.Repositories.mongo;

import edu.eci.cvds.task_back.Domain.Task;
import edu.eci.cvds.task_back.Repositories.TaskRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la gestión de tareas utilizando MongoDB como almacenamiento.
 * Implementa los métodos definidos en {@link TaskRepository} y los combina con
 * las funcionalidades proporcionadas por {@link MongoRepository}.
 */
@Repository
public interface TaskMongoRepository extends TaskRepository,MongoRepository<Task,String>{

    @Override
    public default void saveTask(Task task){
        //task.setMongoId(task.getId().toString());
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
    public default void updateTask(Task task){
        saveTask(task);
    }
}
