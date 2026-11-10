package com.example.gerenciadordecampeonato.controller;

import com.example.gerenciadordecampeonato.entity.Jogo;
import com.example.gerenciadordecampeonato.repository.JogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jogos")
public class JogoController {

    @Autowired
    private JogoRepository jogoRepository;

    @PostMapping
    public ResponseEntity<Jogo> criar(@RequestBody Jogo jogo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jogoRepository.save(jogo));
    }

    @PutMapping("/{id}/placar")
    public ResponseEntity<?> atualizarPlacar(@PathVariable Long id,
                                             @RequestBody Jogo dadosPlacar,
                                             @RequestParam Long usuarioId) {

        Jogo jogo = jogoRepository.findById(id).orElse(null);
        if (jogo == null) return ResponseEntity.notFound().build();

        boolean adminDoCampeonato = jogo.getCampeonato().getAdministradores().stream()
                .anyMatch(a -> a.getIdPessoa().equals(usuarioId));

        boolean adminDoMandante = jogo.getTimeMandante().getAdministradores().stream()
                .anyMatch(a -> a.getIdPessoa().equals(usuarioId));

        boolean adminDoVisitante = jogo.getTimeVisitante().getAdministradores().stream()
                .anyMatch(a -> a.getIdPessoa().equals(usuarioId));

        if (!adminDoCampeonato && !adminDoMandante && !adminDoVisitante) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas administradores do campeonato ou dos times envolvidos podem alterar o placar.");
        }

        jogo.setPontuacao(dadosPlacar.getPontuacao());


        if (dadosPlacar.getStatus() != null) {
            jogo.setStatus(dadosPlacar.getStatus());
        }

        return ResponseEntity.ok(jogoRepository.save(jogo));
    }

    @GetMapping
    public List<Jogo> listarTodos() {
        return jogoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jogo> buscarPorId(@PathVariable Long id) {
        return jogoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}