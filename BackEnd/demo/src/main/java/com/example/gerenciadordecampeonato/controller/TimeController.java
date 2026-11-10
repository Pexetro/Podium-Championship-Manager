package com.example.gerenciadordecampeonato.controller;

import com.example.gerenciadordecampeonato.entity.Pessoa;
import com.example.gerenciadordecampeonato.entity.Time;
import com.example.gerenciadordecampeonato.repository.PessoaRepository;
import com.example.gerenciadordecampeonato.repository.TimeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/times")
public class TimeController {

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @GetMapping
    public List<Time> listarTodos() {
        return timeRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Time time, @RequestParam Long criadorId) {
        Pessoa criador = pessoaRepository.findById(criadorId).orElse(null);

        if (criador == null) {
            return ResponseEntity.badRequest().body("Usuário criador não encontrado.");
        }

        time.setCriador(criador);
        time.getAdministradores().add(criador); // Adiciona o criador como admin

        Time novoTime = timeRepository.save(time);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoTime);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @RequestBody Time dadosAtualizados,
                                       @RequestParam Long usuarioId) {

        Time timeExistente = timeRepository.findById(id).orElse(null);
        if (timeExistente == null) {
            return ResponseEntity.notFound().build();
        }

        boolean temPermissao = timeExistente.getAdministradores().stream()
                .anyMatch(admin -> admin.getIdPessoa().equals(usuarioId));

        if (!temPermissao) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para editar este time.");
        }

        timeExistente.setNomeTime(dadosAtualizados.getNomeTime());


        Time atualizado = timeRepository.save(timeExistente);
        return ResponseEntity.ok(atualizado);
    }

    @PostMapping("/{id}/administradores")
    public ResponseEntity<?> adicionarAdmin(@PathVariable Long id,
                                            @RequestParam Long novoAdminId,
                                            @RequestParam Long usuarioSolicitanteId) {

        Time time = timeRepository.findById(id).orElse(null);
        Pessoa novoAdmin = pessoaRepository.findById(novoAdminId).orElse(null);

        if (time == null || novoAdmin == null) {
            return ResponseEntity.notFound().build();
        }

        boolean solicitanteEhAdmin = time.getAdministradores().stream()
                .anyMatch(admin -> admin.getIdPessoa().equals(usuarioSolicitanteId));

        if (!solicitanteEhAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas administradores podem adicionar novos administradores.");
        }

        if (!time.getAdministradores().contains(novoAdmin)) {
            time.getAdministradores().add(novoAdmin);
            timeRepository.save(time);
        }

        return ResponseEntity.ok(time);
    }
}