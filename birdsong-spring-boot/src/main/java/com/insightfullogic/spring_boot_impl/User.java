package com.insightfullogic.spring_boot_impl;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * .
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable=false, unique=true)
    private String name;

    @Column(nullable=false)
    private String password;

    @ManyToMany
    private Set<User> following;

    @ManyToMany(mappedBy="following")
    private Set<User> followers;

    protected User() {

    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        following = new HashSet<>();
        followers = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Set<User> getFollowing() {
        return following;
    }

    public Set<User> getFollowers() {
        return followers;
    }
}
