package com.insightfullogic.spring_boot_impl;

import com.insightfullogic.birdsong.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 * .
 */
@Repository
public class UserRepository {

    private final EntityManager entities;

    @Autowired
    public UserRepository(EntityManager entities) {
        this.entities = entities;
    }

    @Transactional
    public void addInitialData() {
        entities.persist(new User(Users.richard, Users.richardsPass));
        entities.persist(new User(Users.bob, Users.bobsPass));
    }

    public User lookupByName(String name, String password) throws ForbiddenException {
        try {
            return entities.createQuery("SELECT user FROM User user WHERE user.name = :name AND user.password = :password", User.class)
                    .setParameter("name", name)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
            throw new ForbiddenException(e);
        }
    }

}
