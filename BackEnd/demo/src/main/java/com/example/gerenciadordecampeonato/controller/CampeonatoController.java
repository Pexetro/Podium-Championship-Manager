package com.example.gerenciadordecampeonato.controller;

import com.example.gerenciadordecampeonato.entity.Campeonato;
import com.example.gerenciadordecampeonato.entity.Pessoa;
import com.example.gerenciadordecampeonato.repository.CampeonatoRepository;
import com.example.gerenciadordecampeonato.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campeonatos")
public class CampeonatoController {

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @GetMapping
    public List<Campeonato> listarTodos() {
        return campeonatoRepository.findAll();
    }


    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Campeonato campeonato, @RequestParam Long criadorId) {
        Pessoa criador = pessoaRepository.findById(criadorId).orElse(null);
        if (criador == null) return ResponseEntity.badRequest().body("Usuário não encontrado.");

        campeonato.setCriador(criador);
        campeonato.getAdministradores().add(criador);

        return ResponseEntity.status(HttpStatus.CREATED).body(campeonatoRepository.save(campeonato));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                       @RequestBody Campeonato dadosAtualizados,
                                       @RequestParam Long usuarioId) {

        Campeonato campeonato = campeonatoRepository.findById(id).orElse(null);
        if (campeonato == null) return ResponseEntity.notFound().build();


        boolean temPermissao = campeonato.getAdministradores().stream()
                .anyMatch(admin -> admin.getIdPessoa().equals(usuarioId));

        if (!temPermissao) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas administradores podem editar o campeonato.");
        }

        campeonato.setNomeCampeonato(dadosAtualizados.getNomeCampeonato());
        campeonato.setDescricao(dadosAtualizados.getDescricao());
        campeonato.setModalidade(dadosAtualizados.getModalidade());
        campeonato.setDataInicio(dadosAtualizados.getDataInicio());
        campeonato.setDataFim(dadosAtualizados.getDataFim());

        return ResponseEntity.ok(campeonatoRepository.save(campeonato));
    }


    @PostMapping("/{id}/administradores")
    public ResponseEntity<?> adicionarAdmin(@PathVariable Long id,
                                            @RequestParam Long novoAdminId,
                                            @RequestParam Long usuarioSolicitanteId) {

        Campeonato campeonato = campeonatoRepository.findById(id).orElse(null);
        Pessoa novoAdmin = pessoaRepository.findById(novoAdminId).orElse(null);

        if (campeonato == null || novoAdmin == null) return ResponseEntity.notFound().build();

        boolean solicitanteEhAdmin = campeonato.getAdministradores().stream()
                .anyMatch(admin -> admin.getIdPessoa().equals(usuarioSolicitanteId));

        if (!solicitanteEhAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas administradores podem adicionar novos administradores.");
        }

        if (!campeonato.getAdministradores().contains(novoAdmin)) {
            campeonato.getAdministradores().add(novoAdmin);
            campeonatoRepository.save(campeonato);
        }

        return ResponseEntity.ok(campeonato);
    }
}

