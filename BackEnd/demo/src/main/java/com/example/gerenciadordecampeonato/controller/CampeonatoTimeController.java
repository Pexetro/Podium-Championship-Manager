package com.example.gerenciadordecampeonato.controller;

import com.example.gerenciadordecampeonato.entity.CampeonatoTime;
import com.example.gerenciadordecampeonato.repository.CampeonatoTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campeonato-times")
public class CampeonatoTimeController {

    @Autowired
    private CampeonatoTimeRepository repository;

    @GetMapping
    public List<CampeonatoTime> listarTodas() {
        return repository.findAll();
    }

    @PostMapping
    public CampeonatoTime criar(@RequestBody CampeonatoTime campeonatoTime) {
        return repository.save(campeonatoTime);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampeonatoTime> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampeonatoTime> atualizar(@PathVariable Long id, @RequestBody CampeonatoTime dadosAtualizados) {
        return repository.findById(id).map(campeonatoTime -> {

            campeonatoTime.setData_inscricao(dadosAtualizados.getData_inscricao());

            campeonatoTime.setTime(dadosAtualizados.getTime());
            campeonatoTime.setCampeonato(dadosAtualizados.getCampeonato());

            CampeonatoTime atualizado = repository.save(campeonatoTime);
            return ResponseEntity.ok(atualizado);

        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}