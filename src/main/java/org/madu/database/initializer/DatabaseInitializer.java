package org.madu.database.initializer;

import jakarta.persistence.LockModeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class DatabaseInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    public void createTables() {
        createPessoaTable();
    }

    private void createPessoaTable() {
        String createPessoaTable = "CREATE TABLE IF NOT EXISTS pessoa (" +
                "apelido varchar(32) NOT NULL," +
                "nome varchar(100) NOT NULL," +
                "id uuid NOT NULL," +
                "nascimento date NOT NULL," +
                "stack varchar(32)[] NULL," +
                "CONSTRAINT pessoa_pk PRIMARY KEY (id)," +
                "CONSTRAINT pessoa_un UNIQUE (apelido)" +
                ");";

        jdbcTemplate.execute(createPessoaTable);
    }
}
