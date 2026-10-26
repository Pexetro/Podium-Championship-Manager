package com.example.gerenciadordecampeonato.repository;

import com.example.gerenciadordecampeonato.entity.CampeonatoTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampeonatoTimeRepository extends JpaRepository<CampeonatoTime, Long>{
}
