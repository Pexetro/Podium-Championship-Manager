package com.example.gerenciadordecampeonato.entity;
import com.example.gerenciadordecampeonato.dto.DetalhesPontuacao;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    private LocalDateTime dataHora;

    private String status;
    private String faseRodada;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private DetalhesPontuacao pontuacao;
}
