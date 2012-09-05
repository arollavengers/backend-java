package arollavengers.core.repository;

import arollavengers.core.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserRepository {

  @PersistenceContext
  EntityManager manager;

  @Transactional
  public void save(User user){
    manager.persist(user);
  }

  @Transactional
  public User find(String id) {
    return manager.find(User.class,id);
  }

}
