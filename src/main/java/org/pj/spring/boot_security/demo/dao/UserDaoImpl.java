package org.pj.spring.boot_security.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.pj.spring.boot_security.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private final EntityManager em;

    @Autowired
    public UserDaoImpl(EntityManager entityManager) {

        this.em = entityManager;
    }


    @Override
    public User getUserByEmail(String email) {
        try {
            return em.createQuery(
                            "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email",
                            User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }

    @Override
    public void addUser(User user) {
        em.persist(user);
    }

    @Override
    public User getUserById(Long id) {
        return em.find(User.class, id);
    }

    @Override
    public void updateUser(User user) {
        em.merge(user);
    }

    @Override
    public void removeUserById(Long id) {
        em.remove(em.find(User.class, id));
    }

    @Override
    public List<User> listUsers() {
        return em.createQuery(
                        "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles",
                        User.class)
                .getResultList();
    }
}
