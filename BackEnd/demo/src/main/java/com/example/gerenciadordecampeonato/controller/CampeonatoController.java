package com.example.gerenciadordecampeonato.controller;

import com.example.gerenciadordecampeonato.entity.Campeonato;
import com.example.gerenciadordecampeonato.repository.CampeonatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/campeonatos")
public class CampeonatoController {

    @Autowired
    private CampeonatoRepository repository;

    @GetMapping
    public List<Campeonato> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Campeonato criar(@RequestBody Campeonato campeonato) {
        return repository.save(campeonato);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campeonato> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping ("/{id}")
    public ResponseEntity<Campeonato> atualizar(@PathVariable Long id, @RequestBody Campeonato dadosAtualizados){
        return repository.findById(id).map(campeonato ->{
            campeonato.setNomeCampeonato(dadosAtualizados.getNomeCampeonato());
            campeonato.setDescricao(dadosAtualizados.getDescricao());
            campeonato.setModalidade(dadosAtualizados.getModalidade());
            campeonato.setDataInicio(dadosAtualizados.getDataInicio());
            campeonato.setDataFim(dadosAtualizados.getDataFim());

            Campeonato atualizado = repository.save(campeonato);
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
