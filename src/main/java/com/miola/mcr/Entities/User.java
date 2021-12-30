package com.miola.mcr.Entities;

import io.github.palexdev.materialfx.filter.IFilterable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@ToString
@Table(name="Users")
public class User implements IFilterable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name="username", unique=true)
    private String username;
    private String password;

    @ManyToOne
    private Role role;

    public User(Long id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }
    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toFilterString() {
        return getId() + " " + this.getName() + " " + getUsername() + " " + getPassword();
    }
}