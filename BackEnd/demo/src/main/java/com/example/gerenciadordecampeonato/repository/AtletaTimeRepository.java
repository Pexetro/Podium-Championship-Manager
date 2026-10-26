package com.example.gerenciadordecampeonato.repository;

import com.example.gerenciadordecampeonato.entity.AtletaTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtletaTimeRepository extends JpaRepository<AtletaTime, Long>{
}
