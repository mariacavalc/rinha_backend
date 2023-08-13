package org.madu.database.service;

import org.madu.entities.Pessoa;
import org.madu.util.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.madu.util.UuidUtils.getBytesFromUuid;

@Component
public class PessoaStackService {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private StackService stackService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void insertPessoaAndStacks(Pessoa pessoa) {
        pessoaService.insertPessoa(pessoa);
        stackService.insertStacks(pessoa);
    }

    @Transactional
    public Pessoa getPessoaAndStacks(UUID pessoaId) {
        Pessoa pessoa = pessoaService.getPessoa(pessoaId);
        stackService.getStacks(pessoa);

        return pessoa;
    }
}
