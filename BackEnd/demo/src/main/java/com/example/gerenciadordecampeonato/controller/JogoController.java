package com.example.gerenciadordecampeonato.controller;

import com.example.gerenciadordecampeonato.entity.Jogo;
import com.example.gerenciadordecampeonato.repository.JogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/jogos")
public class JogoController {

    @Autowired
    private JogoRepository repository;

    @GetMapping
    public List<Jogo> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Jogo criar(@RequestBody Jogo jogo) {
        return repository.save(jogo);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<Jogo> atualizar(@PathVariable Long id, @RequestBody Jogo dadosAtualizados){
        return repository.findById(id).map(jogo ->{
            jogo.setData_hora(dadosAtualizados.getData_hora());
            jogo.setFase_rodada(dadosAtualizados.getFase_rodada());
            jogo.setTimeMandante(dadosAtualizados.getTimeMandante());
            jogo.setPlacar_mandante(dadosAtualizados.getPlacar_mandante());
            jogo.setTimeVisitante(dadosAtualizados.getTimeVisitante());
            jogo.setPlacar_visitante(dadosAtualizados.getPlacar_visitante());
            Jogo atualizado = repository.save(jogo);
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
