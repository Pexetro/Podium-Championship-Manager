package com.example.gerenciadordecampeonato.controller;

import com.example.gerenciadordecampeonato.entity.Atleta;
import com.example.gerenciadordecampeonato.repository.AtletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atletas")
public class AtletaController {

    @Autowired
    private AtletaRepository repository;

    @GetMapping
    public List<Atleta> listarTodos() {
        return repository.findAll();
    }

    @PostMapping
    public Atleta criar(@RequestBody Atleta atleta) {
        return repository.save(atleta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Atleta> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Atleta> atualizar(@PathVariable Long id, @RequestBody Atleta dadosAtualizados) {
        return repository.findById(id).map(atleta -> {
            atleta.setPessoa(dadosAtualizados.getPessoa());

            Atleta atualizado = repository.save(atleta);
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