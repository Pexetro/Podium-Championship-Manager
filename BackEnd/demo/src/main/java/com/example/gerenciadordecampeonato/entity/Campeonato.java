package com.example.gerenciadordecampeonato.entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "campeonato")
public class Campeonato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCampeonato;

    private String nomeCampeonato;

    private String descricao;

    private String modalidade;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    @ManyToOne
    @JoinColumn(name = "id_criador")
    private Pessoa criador;

    @ManyToMany
    @JoinTable(
            name = "campeonato_administradores",
            joinColumns = @JoinColumn(name = "id_campeonato"),
            inverseJoinColumns = @JoinColumn(name = "id_pessoa")
    )
    private List<Pessoa> administradores = new ArrayList<>();
}
