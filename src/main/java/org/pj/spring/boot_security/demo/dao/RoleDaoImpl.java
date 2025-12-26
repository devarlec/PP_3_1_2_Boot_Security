package org.pj.spring.boot_security.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.pj.spring.boot_security.demo.model.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Set<Role> getAllRoles() {
        List<Role> roleList = em.createQuery("select r from Role r ", Role.class).getResultList();
        return new HashSet<>(roleList);
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleByName(String roleName) {
        try {
            return em.createQuery(
                            "SELECT r FROM Role r WHERE r.name = :roleName",
                            Role.class)
                    .setParameter("roleName", roleName)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new RuntimeException("Role not found: " + roleName);
        }
    }

    @Override
    public Set<Role> getSetOfRoles(String[] roleNames) {
        Set<Role> roleSet = new HashSet<>();
        for (String role : roleNames) {
            roleSet.add(getRoleByName(role));
        }
        return roleSet;
    }

    @Override
    public void add(Role role) {
        em.persist(role);
    }

    @Override
    public void edit(Role role) {
        em.merge(role);
    }

    @Override
    public Role getById(Long id) {
        return em.find(Role.class, id);
    }
}
