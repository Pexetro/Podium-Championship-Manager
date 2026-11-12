package com.example.gerenciadordecampeonato.controller;

import com.example.gerenciadordecampeonato.entity.Atleta;
import com.example.gerenciadordecampeonato.repository.AtletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> criar(@RequestBody Atleta atleta) {
        // 1. Verifica se o Atleta já existe para essa Pessoa
        if (atleta.getPessoa() != null && atleta.getPessoa().getIdPessoa() != null) {
            Atleta atletaExistente = repository.findByPessoaIdPessoa(atleta.getPessoa().getIdPessoa());

            if (atletaExistente != null) {
                // Se já existe, retorna o atleta existente em vez de dar erro 500
                return ResponseEntity.ok(atletaExistente);
            }
        }

        // 2. Se não existe, cria um novo
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(atleta));
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