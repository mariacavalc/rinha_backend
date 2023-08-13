package org.madu.database.service;

import com.github.f4b6a3.ulid.UlidCreator;
import org.madu.entities.Pessoa;
import org.madu.exception.UnprocessableEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.UUID;

import static org.madu.util.UuidUtils.getBytesFromUuid;

@Component
public class PessoaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertPessoa(Pessoa pessoa) {
        try {
            pessoa.setId(getUuid());
            String insertPessoaSQL = "INSERT INTO pessoa (apelido, nome, nascimento, id) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(connection -> {
                        PreparedStatement ps = connection.prepareStatement(insertPessoaSQL);
                        ps.setString(1, pessoa.getApelido());
                        ps.setString(2, pessoa.getNome());
                        ps.setDate(3, Date.valueOf(pessoa.getNascimento()));
                        ps.setBytes(4, getBytesFromUuid(pessoa.getId()));
                        return ps;
                    }
            );
        } catch (DuplicateKeyException e){
            throw new UnprocessableEntityException();
        }
    }

    private static UUID getUuid() {
        return UlidCreator.getMonotonicUlid().toUuid();
    }
}
