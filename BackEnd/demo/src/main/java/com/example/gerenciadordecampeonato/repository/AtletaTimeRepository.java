package com.example.gerenciadordecampeonato.repository;

import com.example.gerenciadordecampeonato.entity.Atleta;
import com.example.gerenciadordecampeonato.entity.AtletaTime;
import com.example.gerenciadordecampeonato.entity.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtletaTimeRepository extends JpaRepository<AtletaTime, Long>{

    List<AtletaTime> findByTime_IdTime(Long timeId);

    boolean existsByAtletaAndTime(Atleta atleta, Time time);
}
