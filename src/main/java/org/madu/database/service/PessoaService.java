package org.madu.database.service;

import com.github.f4b6a3.ulid.UlidCreator;
import org.madu.entities.Pessoa;
import org.madu.exception.NotFoundException;
import org.madu.exception.UnprocessableEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.UUID;

import static java.util.Objects.isNull;

@Component
public class PessoaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void insertPessoa(Pessoa pessoa) {
        try {
            pessoa.setId(getUuid());
            String insertPessoaSQL = "INSERT INTO pessoa (apelido, nome, nascimento, stack, id) " +
                    "VALUES(?, ?, ?, ?, ?);";
            jdbcTemplate.update(conn -> {
                        PreparedStatement ps = conn.prepareStatement(insertPessoaSQL);
                        ps.setString(1, pessoa.getApelido());
                        ps.setString(2, pessoa.getNome());
                        ps.setDate(3, Date.valueOf(pessoa.getNascimento()));
                        ps.setArray(4, conn.createArrayOf("varchar", pessoa.getStack()));
                        ps.setObject(5, pessoa.getId());
                        return ps;
                    }
            );
        } catch (DuplicateKeyException e){
            throw new UnprocessableEntityException();
        }
    }

    @Transactional
    public Pessoa getPessoa(UUID pessoaId) {
        try {
            String sql = "SELECT p.id, p.apelido, p.nome, p.nascimento, p.stack FROM pessoa p WHERE p.id = ?";

            return jdbcTemplate.queryForObject(sql, pessoaRowMapper, pessoaId);
        }catch (EmptyResultDataAccessException e){
            throw new NotFoundException();
        }
    }

    private final RowMapper<Pessoa> pessoaRowMapper = (resultSet, rowNum) -> {
        UUID id = (UUID) resultSet.getObject("id");
        String apelido = resultSet.getString("apelido");
        String nome = resultSet.getString("nome");
        LocalDate nascimento = resultSet.getDate("nascimento").toLocalDate();
        String [] stack = isNull(resultSet.getArray("stack")) ? null : (String[]) resultSet.getArray("stack")
                .getArray();

        return Pessoa.builder().id(id).apelido(apelido).nome(nome).nascimento(nascimento).stack(stack).build();
    };

    private UUID getUuid() {
        return UlidCreator.getMonotonicUlid().toUuid();
    }
}
