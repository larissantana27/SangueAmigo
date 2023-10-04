package com.example.SangueAmigo.service.security;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Service
public class BloodCenterService {

    String bloodCentersQuery = "SELECT " +
            "JSON_OBJECT( " +
                "'bloodCenters', JSON_ARRAYAGG( " +
                    "JSON_OBJECT( " +
                        "'blood_center', JSON_OBJECT( " +
                            "'logo', bc.logo, " +
                            "'name', bc.name, " +
                            "'address_information', JSON_OBJECT( " +
                            "       'cep', ai.cep," +
                            "       'street', ai.street, " +
                            "       'number', ai.number, " +
                            "       'city', ai.city, " +
                            "       'state', ai.state " +
                            "), " +
                            "'distance', 0" +
                        ")" +
                    ")" +
                ")" +
            ") AS result " +
            "FROM blood_center bc " +
            "INNER JOIN address_information ai " +
            "ON bc.address_info_id = ai.id;";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getBloodCenterList(@NonNull HttpServletRequest request) {

        String sql = bloodCentersQuery;

        try {
            String result = jdbcTemplate.queryForObject(sql, String.class);
            return Objects.requireNonNullElse(result, "{}");
        } catch (EmptyResultDataAccessException e) {
            return "{}";
        }
    }
}