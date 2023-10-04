package com.example.SangueAmigo.infrastructure.controlers;

import com.example.SangueAmigo.service.security.BloodCenterService;
import com.example.SangueAmigo.service.security.TokenService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/bloodCenters")
public class BloodCentersController {

    private final Logger logger = LoggerFactory.getLogger(BloodCentersController.class);

    @Autowired
    private TokenService tokenService;
    @Autowired
    private BloodCenterService bloodCenterService;

    @GetMapping("/list")
    public ResponseEntity<String> getBloodCenterList(@NonNull HttpServletRequest request) {
        logger.info("-Starting BloodCenters List Getter-");

        String result = bloodCenterService.getBloodCenterList(request);
        logger.info("-Success Getting BloodCenters List-");

        return ResponseEntity.ok(result);
    }
}