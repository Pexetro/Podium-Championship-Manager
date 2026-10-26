package com.example.gerenciadordecampeonato.repository;
import com.example.gerenciadordecampeonato.entity.Campeonato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampeonatoRepository extends JpaRepository<Campeonato, Long> {


}
