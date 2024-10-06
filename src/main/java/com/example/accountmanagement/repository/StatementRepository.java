package com.example.accountmanagement.repository;

import com.example.accountmanagement.model.Statement;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatementRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<Statement> findAll(){
        String sql = "SELECT * FROM statement";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Statement.class));
    }

    public Statement findById(Long id) {
        String sql = "SELECT * FROM statement WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Statement.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Statement> findByAccountId(Long accountId) {
        String sql = "SELECT * FROM statement WHERE account_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Statement.class), accountId);
    }
}
