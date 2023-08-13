package org.madu.controller;

import org.madu.database.service.PessoaStackService;
import org.madu.entities.Pessoa;
import org.madu.util.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.madu.util.RequestValidator.validate;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    @Autowired
    private PessoaStackService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Pessoa pessoa) {
        validate(pessoa);
        service.insertPessoaAndStacks(pessoa);
    }
}
