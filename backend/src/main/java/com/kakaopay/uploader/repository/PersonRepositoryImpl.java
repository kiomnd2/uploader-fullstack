package com.kakaopay.uploader.repository;

import com.kakaopay.uploader.domain.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PersonRepositoryImpl implements PersonRepository {

    final private JdbcTemplate jdbcTemplate;

    @Override
    public void batchInsert(List<Person> list) {
        String sql = "insert into Person (id, firstname, lastname, email) values (?,?,?,?)".trim();

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, list.get(i).getId());
                ps.setString(2, list.get(i).getFirstname());
                ps.setString(3, list.get(i).getLastname());
                ps.setString(4, list.get(i).getEmail());
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    @Override
    public boolean deleteAll() {
        String sql = "delete from Person".trim();
        jdbcTemplate.update(sql);
        return false;
    }

    @Override
    public int count() {
        String sql = "select count(1) from Person";
        return jdbcTemplate.queryForObject(sql, int.class);
    }
}
