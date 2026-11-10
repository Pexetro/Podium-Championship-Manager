package com.example.gerenciadordecampeonato.controller;

import com.example.gerenciadordecampeonato.entity.Pessoa;
import com.example.gerenciadordecampeonato.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    @Autowired
    private PessoaRepository repository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@RequestBody Pessoa novaPessoa) {

        if (repository.findByEmail(novaPessoa.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Este e-mail já está cadastrado.");
        }

        String senhaPura = novaPessoa.getSenha();
        if (senhaPura == null || senhaPura.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("A senha é obrigatória.");
        }

        novaPessoa.setHash(encoder.encode(senhaPura));

        novaPessoa.setSenha(null);

        Pessoa pessoaSalva = repository.save(novaPessoa);
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Pessoa loginRequest) {

        Pessoa pessoa = repository.findByEmail(loginRequest.getEmail()).orElse(null);

        if (pessoa == null || !encoder.matches(loginRequest.getSenha(), pessoa.getHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha incorretos.");
        }

        return ResponseEntity.ok(pessoa);
    }

    @GetMapping
    public List<Pessoa> listarTodos() {
        return repository.findAll();
    }
}