package com.example.gerenciadordecampeonato.entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "time")
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_time;

    private String nome_time;

    @ManyToOne
    @JoinColumn(name = "id_delegado")
    private Pessoa delegado;
}
