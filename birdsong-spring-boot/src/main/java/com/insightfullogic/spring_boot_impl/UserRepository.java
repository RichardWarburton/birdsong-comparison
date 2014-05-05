package com.insightfullogic.spring_boot_impl;

import com.insightfullogic.birdsong.Users;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

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

    public User lookupByNameAndPassword(String name, String password) throws ForbiddenException {
        try {
            return entities.createQuery("SELECT user FROM User user WHERE user.name = :name AND user.password = :password", User.class)
                           .setParameter("name", name)
                           .setParameter("password", password)
                           .getSingleResult();
        } catch (NoResultException e) {
            throw new ForbiddenException(e);
        }
    }

    @Transactional
    public void register(String username, String password) throws BadRequestException {
        try {
            entities.persist(new User(username, password));
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new BadRequestException(e);
            }

            throw new IllegalArgumentException(e);
        }
    }

    @Transactional
    public void follow(String username, String toFollowUsername) {
        User user = lookupByName(username);
        User toFollow = lookupByName(toFollowUsername);
        user.getFollowing().add(toFollow);
        entities.persist(user);
        entities.persist(toFollow);
    }

    public User lookupByName(String user) {
        try {
            return entities.createQuery("SELECT user FROM User user WHERE user.name = :name", User.class)
                           .setParameter("name", user)
                           .getSingleResult();
        } catch (NoResultException e) {
            throw new ForbiddenException(e);
        }
    }

}
