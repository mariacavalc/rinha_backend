package org.madu.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.madu.database.service.PessoaService;
import org.madu.entities.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.madu.util.RequestValidator.validate;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    @Autowired
    private PessoaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Pessoa pessoa, HttpServletResponse response) {
        validate(pessoa);

        service.insertPessoa(pessoa);

        response.addHeader("Location", "/pessoas/" + pessoa.getId().toString());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Pessoa findById(@PathVariable("id") UUID pessoaId) {
        return service.getPessoa(pessoaId);
    }
}
