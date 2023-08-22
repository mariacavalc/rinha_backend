package org.madu.database.service;

import com.github.f4b6a3.ulid.UlidCreator;
import lombok.extern.slf4j.Slf4j;
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

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@Component
@Slf4j
public class PessoaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String insertPessoaSQL = "INSERT INTO pessoa (apelido, nome, nascimento, stack, id) " +
            "VALUES(?, ?, ?, ?, ?);";

    private static final String findPessoa = "SELECT p.id, p.apelido, p.nome, p.nascimento, p.stack FROM pessoa p WHERE p.id = ?";

    @Transactional
    public void insertPessoa(Pessoa pessoa) {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(insertPessoaSQL)) {

            pessoa.setId(getUuid());

            ps.setString(1, pessoa.getApelido());
            ps.setString(2, pessoa.getNome());
            ps.setDate(3, Date.valueOf(pessoa.getNascimento()));
            ps.setArray(4, connection.createArrayOf("varchar", pessoa.getStack()));
            ps.setObject(5, pessoa.getId());

            ps.executeUpdate();
            connection.commit();
        } catch (DuplicateKeyException e) {
            throw new UnprocessableEntityException();
        } catch (SQLException e) {
            log.info("Error inserting pessoa", e);
            throw new RuntimeException(e);
        }
    }

    public Pessoa getPessoa(UUID pessoaId) {
        try {
            String sql = "SELECT p.id, p.apelido, p.nome, p.nascimento, p.stack FROM pessoa p WHERE p.id = ?";

            return jdbcTemplate.queryForObject(sql, pessoaRowMapper, pessoaId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException();
        }
    }

    public List<Pessoa> queryPessoa(String searchTerm) {
        String sql = "SELECT p.id, p.apelido, p.nome, p.nascimento, p.stack " +
                "FROM pessoa p " +
                "WHERE p.apelido ILIKE ? OR p.nome ILIKE ? OR EXISTS ( " +
                "  SELECT 1 FROM unnest(p.stack) AS s(element) " +
                "  WHERE s.element ILIKE ? " +
                ") LIMIT 50";

        return jdbcTemplate.query(sql, pessoaRowMapper, "%" + searchTerm + "%", "%" + searchTerm + "%", "%" + searchTerm + "%");
    }

    public Integer countPessoas() {
        String sql = "SELECT count(*) FROM pessoa";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    private final RowMapper<Pessoa> pessoaRowMapper = (resultSet, rowNum) -> {
        UUID id = (UUID) resultSet.getObject("id");
        String apelido = resultSet.getString("apelido");
        String nome = resultSet.getString("nome");
        LocalDate nascimento = resultSet.getDate("nascimento").toLocalDate();
        String[] stack = isNull(resultSet.getArray("stack")) ? null : (String[]) resultSet.getArray("stack")
                .getArray();

        return Pessoa.builder().id(id).apelido(apelido).nome(nome).nascimento(nascimento).stack(stack).build();
    };

    private UUID getUuid() {
        return UlidCreator.getMonotonicUlid().toUuid();
    }
}
