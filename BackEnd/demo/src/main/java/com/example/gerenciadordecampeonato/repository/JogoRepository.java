package com.example.gerenciadordecampeonato.repository;

import com.example.gerenciadordecampeonato.entity.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JogoRepository extends JpaRepository<Jogo, Long> {
    List<Jogo> findByCampeonatoIdCampeonato(Long id);
}