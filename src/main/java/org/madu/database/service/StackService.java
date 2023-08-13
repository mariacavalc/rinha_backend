package org.madu.database.service;

import org.madu.entities.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.isNull;
import static org.madu.util.UuidUtils.getBytesFromUuid;

@Component
public class StackService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertStacks(Pessoa pessoa) {
        List<String> stack = pessoa.getStack();
        if (isNull(stack)){
            return;
        }

        String insertStackSQL = "INSERT INTO stack (id_pessoa, nome) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(insertStackSQL, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setBytes(1, getBytesFromUuid(pessoa.getId()));
                ps.setString(2, stack.get(i));
            }

            @Override
            public int getBatchSize() {
                return stack.size();
            }
        });
    }

    public void getStacks(Pessoa pessoa) {
        String sql = "SELECT s.nome FROM stack s WHERE s.id_pessoa = ?";

        List<String> stacks = jdbcTemplate.query(sql, stackRowMapper, new Object[]{getBytesFromUuid(pessoa.getId())});
        pessoa.setStack(stacks.isEmpty() ? null : stacks);
    }

    private final RowMapper<String> stackRowMapper = (resultSet, rowNum) -> resultSet.getString("nome");
}
