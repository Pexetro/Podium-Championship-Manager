package com.example.gerenciadordecampeonato.entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name= "campeonato")
public class Campeonato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCampeonato;

    private String nomeCampeonato;

    private String descricao;

    private String modalidade;

    private LocalDate dataInicio;

    private LocalDate dataFim;
}
