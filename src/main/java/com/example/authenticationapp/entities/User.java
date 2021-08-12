package com.example.authenticationapp.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import com.example.authenticationapp.entities.Role;

@Getter @Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns=@JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roleList;

}
