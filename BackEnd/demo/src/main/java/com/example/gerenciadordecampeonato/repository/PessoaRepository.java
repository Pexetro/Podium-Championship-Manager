package com.example.gerenciadordecampeonato.repository;

import com.example.gerenciadordecampeonato.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}