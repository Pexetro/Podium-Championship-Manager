package com.example.gerenciadordecampeonato.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@Table(name= "pessoa")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pessoa;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @JsonIgnore
    @Column(nullable = false)
    private String hash;

    private String nome;

    private Long cpf;

    private String email;

    private LocalDate data_nascimento;
}