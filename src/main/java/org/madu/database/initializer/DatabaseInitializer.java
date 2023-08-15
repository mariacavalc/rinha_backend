package org.madu.database.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Component
@Slf4j
public class DatabaseInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createTables() {
        createPessoaTable();
    }

    private void createPessoaTable() {
        if (tableExists()) {
            log.info("The 'pessoa' table already exists");
            return;
        }

        log.info("Creating 'pessoa' table");
        String createPessoaTable = "CREATE TABLE pessoa (" +
                "apelido varchar(32) NOT NULL," +
                "nome varchar(100) NOT NULL," +
                "id uuid NOT NULL DEFAULT uuid_generate_v4()," +
                "nascimento date NOT NULL," +
                "stack varchar(32)[] NULL," +
                "CONSTRAINT pessoa_pk PRIMARY KEY (id)," +
                "CONSTRAINT pessoa_un UNIQUE (apelido)" +
                ");";

        jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";");
        jdbcTemplate.execute(createPessoaTable);

        log.info("'pessoa' table created");
    }

    private boolean tableExists() {
        String checkTableExistsSQL = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
        Integer count = jdbcTemplate.queryForObject(checkTableExistsSQL, Integer.class, "pessoa");
        return !isNull(count) && count > 0;
    }
}
