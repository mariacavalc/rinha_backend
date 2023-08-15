package org.madu.database.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contagem-pessoas")
public class ContagemController {

    @Autowired
    private PessoaService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Integer countPessoas() {
        return service.countPessoas();
    }
}
