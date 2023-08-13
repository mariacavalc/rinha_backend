package org.madu.database.service;

import org.madu.entities.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PessoaStackService {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private StackService stackService;

    @Transactional
    public void insertPessoaAndStacks(Pessoa pessoa) {
        pessoaService.insertPessoa(pessoa);
        stackService.insertStacks(pessoa);
    }
}
