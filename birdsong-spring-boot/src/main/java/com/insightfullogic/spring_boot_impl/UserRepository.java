package com.insightfullogic.spring_boot_impl;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.stream.Stream;

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
    public void addUsers(User... users) {
        Stream.of(users).forEach(entities::persist);
    }

    public void checkUsersPassword(String name, String password) throws ForbiddenException {
        User user = lookupByName(name);
        if (!password.equals(user.getPassword())) {
            throw new ForbiddenException();
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
