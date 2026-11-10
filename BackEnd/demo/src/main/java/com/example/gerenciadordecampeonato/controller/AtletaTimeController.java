package com.example.gerenciadordecampeonato.controller;

import com.example.gerenciadordecampeonato.entity.Atleta;
import com.example.gerenciadordecampeonato.entity.AtletaTime;
import com.example.gerenciadordecampeonato.entity.Time;
import com.example.gerenciadordecampeonato.repository.AtletaRepository;
import com.example.gerenciadordecampeonato.repository.AtletaTimeRepository;
import com.example.gerenciadordecampeonato.repository.TimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/atletas-times")
public class AtletaTimeController {

    @Autowired
    private AtletaTimeRepository atletaTimeRepository;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private AtletaRepository atletaRepository;

    @GetMapping("/time/{timeId}")
    public ResponseEntity<List<AtletaTime>> listarAtletasDoTime(@PathVariable Long timeId) {
        return ResponseEntity.ok(atletaTimeRepository.findByTime_IdTime(timeId));
    }

    @PostMapping
    public ResponseEntity<?> adicionarAtleta(
            @RequestParam Long timeId,
            @RequestParam Long atletaId,
            @RequestParam Long usuarioSolicitanteId) {

        Time time = timeRepository.findById(timeId).orElse(null);
        Atleta atleta = atletaRepository.findById(atletaId).orElse(null);

        if (time == null || atleta == null) {
            return ResponseEntity.badRequest().body("Time.java ou Atleta não encontrado.");
        }

        boolean temPermissao = time.getAdministradores().stream()
                .anyMatch(admin -> admin.getIdPessoa().equals(usuarioSolicitanteId));

        if (!temPermissao) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas administradores do time podem adicionar atletas.");
        }

        boolean jaEstaNoTime = atletaTimeRepository.existsByAtletaAndTime(atleta, time);
        if (jaEstaNoTime) {
            return ResponseEntity.badRequest().body("Este atleta já está neste time.");
        }


        AtletaTime novoVinculo = new AtletaTime();
        novoVinculo.setAtleta(atleta);
        novoVinculo.setTime(time);
        novoVinculo.setDataEntrada(LocalDate.now());

        return ResponseEntity.status(HttpStatus.CREATED).body(atletaTimeRepository.save(novoVinculo));
    }


@PutMapping("/{id}")
public ResponseEntity<?> atualizarVinculo(
        @PathVariable Long id,
        @RequestBody AtletaTime dadosAtualizados,
        @RequestParam Long usuarioSolicitanteId) {

    AtletaTime vinculo = atletaTimeRepository.findById(id).orElse(null);
    if (vinculo == null) return ResponseEntity.notFound().build();

    boolean temPermissao = vinculo.getTime().getAdministradores().stream()
            .anyMatch(admin -> admin.getIdPessoa().equals(usuarioSolicitanteId));

    if (!temPermissao) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Apenas administradores do time podem alterar este vínculo.");
    }

    if (dadosAtualizados.getDataEntrada() != null) {
        vinculo.setDataEntrada(dadosAtualizados.getDataEntrada());
    }

    return ResponseEntity.ok(atletaTimeRepository.save(vinculo));
}

@DeleteMapping("/{id}")
public ResponseEntity<?> removerAtleta(
        @PathVariable Long id,
        @RequestParam Long usuarioSolicitanteId) {

    AtletaTime vinculo = atletaTimeRepository.findById(id).orElse(null);
    if (vinculo == null) return ResponseEntity.notFound().build();

    boolean temPermissao = vinculo.getTime().getAdministradores().stream()
            .anyMatch(admin -> admin.getIdPessoa().equals(usuarioSolicitanteId));

        if (!temPermissao) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas administradores do time podem remover atletas.");
        }

        atletaTimeRepository.delete(vinculo);
        return ResponseEntity.noContent().build();
    }
}