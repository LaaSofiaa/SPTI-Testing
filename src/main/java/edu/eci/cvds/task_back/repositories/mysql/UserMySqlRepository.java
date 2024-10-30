package edu.eci.cvds.task_back.repositories.mysql;

import edu.eci.cvds.task_back.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
    User findByUsername(String userName);
    User findByEmail(String email);
    default String getUserName(String idUser){
        User user = getUser(idUser);
        if(user != null){
            return user.getUsername();
        }
        return null;
    }
}
