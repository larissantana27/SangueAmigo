package com.example.SangueAmigo.service;

import com.example.SangueAmigo.infrastructure.DistanceCalculator;
import com.example.SangueAmigo.model.user.AddressInformation;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.LinkedHashMap;

@Service
public class BloodCenterService {

    private final Logger logger = LoggerFactory.getLogger(BloodCenterService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DistanceCalculator distanceCalculator;
    @Autowired
    private Gson gson = new Gson();

    public String getBloodCenterList(AddressInformation userAddressInfo) {
        logger.info("-Starting BloodCenterList GET-");

        String sql = "SELECT bc.id, bc.name, ai.street, ai.number, ai.city, ai.state, ai.cep FROM blood_center bc " +
                "INNER JOIN address_information ai ON bc.address_info_id = ai.id";

        List<Map<String, Object>> bloodCenters = jdbcTemplate.queryForList(sql);

        List<Map<String, Object>> bloodCentersWithDistance = new ArrayList<>();
        for (Map<String, Object> bloodCenter : bloodCenters) {
            logger.info("BloodCenter: {}", bloodCenter);

            String bloodCenterStreet = String.valueOf(bloodCenter.get("street"));
            String bloodCenterNumber = String.valueOf(bloodCenter.get("number"));
            String bloodCenterCity = String.valueOf(bloodCenter.get("city"));
            String bloodCenterState = String.valueOf(bloodCenter.get("state"));
            String bloodCenterCep =  String.valueOf(bloodCenter.get("cep"));

            AddressInformation bloodCentersAddressInfo = new AddressInformation(Integer.parseInt(bloodCenterCep), bloodCenterStreet,
                    Integer.parseInt(bloodCenterNumber), bloodCenterCity, bloodCenterState);

            double distance = distanceCalculator.getDistance(userAddressInfo, bloodCentersAddressInfo);
            bloodCenter.put("distance", distance);
            bloodCentersWithDistance.add(bloodCenter);
        }

        String result = convertListToJson(bloodCentersWithDistance);

        return Objects.requireNonNullElse(result, "{}");
    }

    public String getDetailedBloodCenterList() {
        logger.info("-Starting DetailedBloodCenterList GET-");

        String sql = "SELECT bc.id, bc.logo, bc.name, " +
                "ai.street, ai.number, ai.city, ai.state, " +
                "bc.operating_time, bc.phone_number, bc.website " +
                "FROM blood_center bc " +
                "INNER JOIN address_information ai ON bc.address_info_id = ai.id";

        List<Map<String, Object>> bloodCenters = jdbcTemplate.queryForList(sql);

        String result = convertListToJson(bloodCenters);

        return Objects.requireNonNullElse(result, "{}");
    }

    private String convertListToJson(List<Map<String, Object>> list) {
        return gson.toJson(list);
    }

    public String getBloodCenterStock(int bloodCenterId) {
        logger.info("-Starting BloodCenterStock GET-");

        String sql = "SELECT" +
                "       SUM(CASE WHEN bs.type = 'A+' THEN bs.quantity ELSE 0 END)," +
                "       SUM(CASE WHEN bs.type = 'B+' THEN bs.quantity ELSE 0 END)," +
                "       SUM(CASE WHEN bs.type = 'AB+' THEN bs.quantity ELSE 0 END)," +
                "       SUM(CASE WHEN bs.type = 'O+' THEN bs.quantity ELSE 0 END)," +
                "       SUM(CASE WHEN bs.type = 'A-' THEN bs.quantity ELSE 0 END)," +
                "       SUM(CASE WHEN bs.type = 'B-' THEN bs.quantity ELSE 0 END)," +
                "       SUM(CASE WHEN bs.type = 'AB-' THEN bs.quantity ELSE 0 END)," +
                "       SUM(CASE WHEN bs.type = 'O-' THEN bs.quantity ELSE 0 END)" +
                "FROM blood_stock bs " +
                "INNER JOIN blood_center bc ON bs.blood_center_id = bc.id " +
                "WHERE bs.blood_center_id = " + bloodCenterId;

        List<Map<String, Object>> bloodCenterStock = jdbcTemplate.queryForList(sql);

        String result = convertStockListToJson(bloodCenterStock);

        return Objects.requireNonNullElse(result, "{}");
    }

    private String convertStockListToJson(List<Map<String, Object>> list) {
        Map<String, Double> resultMap = new LinkedHashMap<>();

        for (Map<String, Object> entry : list) {
            entry.forEach((key, value) -> {
                String bloodType = key.replace("SUM(CASE WHEN bs.type = '", "")
                        .replace("' THEN bs.quantity ELSE 0 END)", "");
                resultMap.put(bloodType, (Double) value);
            });
        }

        return gson.toJson(resultMap);
    }
}