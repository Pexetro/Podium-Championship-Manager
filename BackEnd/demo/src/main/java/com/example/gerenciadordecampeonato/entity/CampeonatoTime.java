package com.example.gerenciadordecampeonato.entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name= "campeonato_time")
public class CampeonatoTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_campeonato")
    private Campeonato campeonato;

    @ManyToOne
    @JoinColumn(name = "id_time")
    private Time time;


    private LocalDate data_inscricao;
}
