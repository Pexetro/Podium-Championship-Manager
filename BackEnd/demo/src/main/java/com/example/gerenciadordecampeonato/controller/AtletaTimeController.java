package com.example.gerenciadordecampeonato.controller;

import com.example.gerenciadordecampeonato.entity.AtletaTime;
import com.example.gerenciadordecampeonato.repository.AtletaTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atletastimes")
public class AtletaTimeController {

    @Autowired
    private AtletaTimeRepository repository;

    @GetMapping
    public List<AtletaTime> listarTodos() {
        return repository.findAll();
    }

    @PostMapping
    public AtletaTime criar(@RequestBody AtletaTime atletaTime) {
        return repository.save(atletaTime);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtletaTime> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtletaTime> atualizar(@PathVariable Long id, @RequestBody AtletaTime dadosAtualizados) {
        return repository.findById(id).map(atletaTime -> {
            atletaTime.setData_entrada(dadosAtualizados.getData_entrada());

            atletaTime.setAtleta(dadosAtualizados.getAtleta());
            atletaTime.setTime(dadosAtualizados.getTime());

            AtletaTime atualizado = repository.save(atletaTime);
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