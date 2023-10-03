package ru.job4j.tracker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "j_user")
public class User {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "j_user_id")
    private List<UserMessenger> messengers = new ArrayList<>();
}
