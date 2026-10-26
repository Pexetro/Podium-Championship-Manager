package com.example.gerenciadordecampeonato.entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name= "jogo")
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_jogo;

    @ManyToOne
    @JoinColumn(name = "id_campeonato")
    private Campeonato campeonato;

    @ManyToOne
    @JoinColumn(name = "id_time_mandante")
    private Time timeMandante;


    @ManyToOne
    @JoinColumn(name = "id_time_visitante")
    private Time timeVisitante;

    private LocalDateTime data_hora;

    private Integer placar_mandante;

    private Integer placar_visitante;

    private String fase_rodada;
}
