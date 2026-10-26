package com.example.gerenciadordecampeonato.repository;

import com.example.gerenciadordecampeonato.entity.Atleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtletaRepository extends JpaRepository<Atleta, Long>{
}
