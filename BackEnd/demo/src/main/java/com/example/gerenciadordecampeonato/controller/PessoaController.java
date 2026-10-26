package com.example.gerenciadordecampeonato.controller;

import com.example.gerenciadordecampeonato.entity.Pessoa;
import com.example.gerenciadordecampeonato.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public List<Pessoa> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Pessoa criar(@RequestBody Pessoa pessoa){

        String senhaPura = pessoa.getSenha();

        if (senhaPura != null && !senhaPura.isEmpty()){
            String hashGerado = encoder.encode(senhaPura);
            pessoa.setHash(hashGerado);
        }

        pessoa.setSenha(null);
        return repository.save(pessoa);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping ("/{id}")
    public ResponseEntity<Pessoa> atualizar(@PathVariable Long id, @RequestBody Pessoa dadosAtualizados){
        return repository.findById(id).map(pessoa ->{
            pessoa.setNome(dadosAtualizados.getNome());
            pessoa.setData_nascimento(dadosAtualizados.getData_nascimento());
            pessoa.setEmail(dadosAtualizados.getEmail());
            pessoa.setCpf(dadosAtualizados.getCpf());

            if (dadosAtualizados.getSenha() != null && !dadosAtualizados.getSenha().isEmpty()) {
                String hashGerado = encoder.encode(dadosAtualizados.getSenha());
                pessoa.setHash(hashGerado);
            }

            Pessoa atualizado = repository.save(pessoa);
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
