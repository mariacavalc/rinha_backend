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
        if (tableExists()) {
            return;
        }
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
        jdbcTemplate.execute("CREATE INDEX idx_nome ON pessoa (nome);");
        jdbcTemplate.execute("CREATE INDEX idx_apelido ON pessoa (apelido);");
    }

    private boolean tableExists() {
        String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'pessoa'";
        return jdbcTemplate.queryForObject(query, Integer.class) > 0;
    }
}
