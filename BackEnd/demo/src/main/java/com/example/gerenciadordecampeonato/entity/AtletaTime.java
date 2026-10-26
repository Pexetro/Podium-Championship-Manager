package com.example.gerenciadordecampeonato.entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name= "atleta_time")
public class AtletaTime {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_atleta")
    private Atleta atleta;

    @ManyToOne
    @JoinColumn(name = "id_time")
    private Time time;

    private LocalDate data_entrada;
}
