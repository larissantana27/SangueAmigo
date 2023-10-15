package com.example.SangueAmigo.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static org.apache.logging.log4j.util.Strings.concat;

@Service
public class AccountService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private Gson gson = new Gson();

    public String getAccountInfoList(int userId) {
        String sql = "SELECT U.firstname, U.lastname, U.email, " +
                "BI.birthday, BI.biological_sex, BI.blood_type, BI.rh_factor, BI.weight " +
                "FROM user U " +
                "INNER JOIN biological_information BI ON U.biological_info_id = BI.id " +
                "WHERE U.id = " + userId;

        Map<String, Object> accountInfo = jdbcTemplate.queryForObject(sql, new AccountInfoRowMapper());
        String result = convertMapToJson(accountInfo);
        return Objects.requireNonNullElse(result, "{}");
    }

    private String convertMapToJson(Map<String, Object> map) {
        return gson.toJson(map);
    }
}

@Service
class AccountInfoRowMapper implements RowMapper<Map<String, Object>> {
    @Override
    public Map<String, Object> mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Map<String, Object> accountInfo = new LinkedHashMap<>();

        String firstname = concat(resultSet.getString("firstname"), " ");
        String name = concat(firstname, resultSet.getString("lastname"));
        String booleanRhFactor = resultSet.getString("rh_factor");
        String rhFactor = booleanRhFactor.equals("1") ? "+" : "-";
        String blood_type = concat(resultSet.getString("blood_type"), rhFactor);

        accountInfo.put("name", name);
        accountInfo.put("email", resultSet.getString("email"));
        accountInfo.put("birthday", resultSet.getString("birthday"));
        accountInfo.put("biological_sex", resultSet.getString("biological_sex"));
        accountInfo.put("blood_type", blood_type);
        accountInfo.put("weight", resultSet.getString("weight"));

        return accountInfo;
    }
}