package com.example.SangueAmigo.infrastructure.controlers;

import com.example.SangueAmigo.model.bloodcenter.BloodCenterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bloodStock")
public class BloodStockController {

    private final Logger logger = LoggerFactory.getLogger(BloodStockController.class);

    @Autowired
    private BloodCenterRepository bloodCenterRepository;

    @GetMapping("/bloodTypeMostInNeed")
    public ResponseEntity<String> getMostNeedingBloodType() {
        logger.info("-Starting Getter-");

        String result = bloodCenterRepository.getMostNeedingBloodType();
        logger.info("Most Needing Blood Center: {}", result);

        logger.info("-Success Getting Most Needing Blood Type-");

        return ResponseEntity.ok(result);
    }
}