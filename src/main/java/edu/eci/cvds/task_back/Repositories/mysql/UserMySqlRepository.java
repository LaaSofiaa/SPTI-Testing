package edu.eci.cvds.task_back.Repositories.mysql;

import edu.eci.cvds.task_back.Domain.Task;
import edu.eci.cvds.task_back.Domain.User;
import edu.eci.cvds.task_back.Repositories.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMySqlRepository extends JpaRepository<User, String>{

    default void createUser(User user){
        save(user);
    }
    default void deleteUser(User user){
        delete(user);
    }
    default User getUser(String idUser){
        return findById(idUser).orElse(null);
    }
    default void modifyUser(User user){
        save(user);
    }
    User findByUserName(String userName);
    User findByEmail(String email);
}
