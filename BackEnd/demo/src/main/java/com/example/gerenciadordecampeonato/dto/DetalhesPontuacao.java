package com.example.gerenciadordecampeonato.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DetalhesPontuacao implements Serializable {

    private Integer pontosMandante;
    private Integer pontosVisitante;

    private Integer setsMandante;
    private Integer setsVisitante;

    private String tempoAtual;
}