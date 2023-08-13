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

        createStackTable();
    }

    private void createPessoaTable() {
        if (tableExists("pessoa")) {
            log.info("The 'pessoa' table already exists");
            return;
        }

        log.info("Creating 'pessoa' table");
        String createPessoaTable = "CREATE TABLE `pessoa` (" +
                "  `apelido` varchar(32) NOT NULL," +
                "  `nome` varchar(100) NOT NULL," +
                "  `nascimento` date NOT NULL," +
                "  `id` binary(16) NOT NULL," +
                "  PRIMARY KEY (`id`)," +
                "  UNIQUE KEY `pessoa_UN` (`apelido`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

        jdbcTemplate.execute(createPessoaTable);
    }

    private void createStackTable() {
        if (tableExists("stack")) {
            log.info("The 'stack' table already exists");
            return;
        }

        log.info("Creating 'stack' table");
        String createStackTable = "CREATE TABLE `stack` (" +
                "  `nome` varchar(32) NOT NULL," +
                "  `id_pessoa` binary(16) NOT NULL," +
                "  KEY `stack_FK` (`id_pessoa`) USING BTREE," +
                "  CONSTRAINT `stack_FK` FOREIGN KEY (`id_pessoa`) REFERENCES `pessoa` (`id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

        jdbcTemplate.execute(createStackTable);
    }

    private boolean tableExists(String tableName) {
        String checkTableExistsSQL = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
        Integer count = jdbcTemplate.queryForObject(checkTableExistsSQL, Integer.class, tableName);
        return !isNull(count) && count > 0;
    }
}
