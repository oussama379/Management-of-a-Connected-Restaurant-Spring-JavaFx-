package com.miola.mcr.Entities;

import io.github.palexdev.materialfx.filter.IFilterable;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="Users")
public class User implements IFilterable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name="username", unique=true)
    private String username;
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    @OneToMany(mappedBy="user", fetch = FetchType.EAGER)
    private Set<Order> orders;

    public User(Long id, String name, String username, String password, Role role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }
    public User(String name, String username, String password, Role role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public String getRoleName() {
        return this.getRole().getTitle();
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toFilterString() {
        return getId() + " " + this.getName() + " " + getUsername() + " " + getPassword();
    }
}