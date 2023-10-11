package com.example.SangueAmigo.service.security;

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
        String sql = "SELECT bc.id, bc.name, ai.street, ai.number, ai.city, ai.state FROM blood_center bc " +
                "INNER JOIN address_information ai ON bc.address_info_id = ai.id";

        List<Map<String, Object>> bloodCenters = jdbcTemplate.queryForList(sql);

        List<Map<String, Object>> bloodCentersWithDistance = new ArrayList<>();
        for (Map<String, Object> bloodCenter : bloodCenters) {
            logger.info("BloodCenter: {}", bloodCenter);

            String bloodCenterStreet = String.valueOf(bloodCenter.get("street"));
            String bloodCenterNumber = String.valueOf(bloodCenter.get("number"));
            String bloodCenterCity = String.valueOf(bloodCenter.get("city"));
            String bloodCenterState = String.valueOf(bloodCenter.get("state"));

            AddressInformation bloodCentersAddressInfo = new AddressInformation(bloodCenterStreet,
                    Integer.parseInt(bloodCenterNumber), bloodCenterCity, bloodCenterState);

            double distance = distanceCalculator.getDistance(userAddressInfo, bloodCentersAddressInfo);
            bloodCenter.put("distance", distance);
            bloodCentersWithDistance.add(bloodCenter);
        }

        String result = convertListToJson(bloodCentersWithDistance);

        return Objects.requireNonNullElse(result, "{}");
    }

    private String convertListToJson(List<Map<String, Object>> list) {
        return gson.toJson(list);
    }
}