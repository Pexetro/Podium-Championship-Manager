package com.example.gerenciadordecampeonato.entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Table(name= "atleta")
public class Atleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_atleta;

    @OneToOne
    @JoinColumn(name = "id_pessoa")
    private Pessoa pessoa;


}
