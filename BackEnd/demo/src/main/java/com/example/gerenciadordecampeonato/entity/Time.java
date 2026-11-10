package com.example.gerenciadordecampeonato.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "time")
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTime;

    private String nomeTime;

    @ManyToOne
    @JoinColumn(name = "id_criador")
    private Pessoa criador;

    @ManyToMany
    @JoinTable(
            name = "time_administradores",
            joinColumns = @JoinColumn(name = "id_time"),
            inverseJoinColumns = @JoinColumn(name = "id_pessoa")
    )
    private List<Pessoa> administradores = new ArrayList<>();
}