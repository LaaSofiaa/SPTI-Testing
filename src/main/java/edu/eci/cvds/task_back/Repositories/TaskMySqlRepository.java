package edu.eci.cvds.task_back.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import edu.eci.cvds.task_back.Domain.Task;

import java.util.List;

public interface TaskMySqlRepository extends JpaRepository<Task, String>,TaskRepository{

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
