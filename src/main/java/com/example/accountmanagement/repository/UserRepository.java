package com.example.accountmanagement.repository;

import com.example.accountmanagement.model.Account;
import com.example.accountmanagement.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<User> findAll() {
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int create(User user){
        String sql = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql,user.getUsername(),user.getPassword(),user.getRole());
    }
}
