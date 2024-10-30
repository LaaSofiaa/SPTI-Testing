package edu.eci.cvds.task_back.repositories.mysql;
import edu.eci.cvds.task_back.domain.User;
import edu.eci.cvds.task_back.repositories.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import edu.eci.cvds.task_back.domain.Task;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TaskMySqlRepository extends JpaRepository<Task, String>, TaskRepository {

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
    public List<Task> findTasksByUser(User user);

    @Override
    public default void updateTask(Task task){ save(task); }
}
