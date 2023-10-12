package com.example.SangueAmigo.infrastructure.controlers;

import com.example.SangueAmigo.model.user.User;
import com.example.SangueAmigo.model.user.UserRepository;
import com.example.SangueAmigo.service.security.AccountService;
import com.example.SangueAmigo.service.security.TokenService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountService accountService;

    @GetMapping("/list")
    public ResponseEntity<String> getAccountInfoList(@NonNull HttpServletRequest request) {
        logger.info("-Starting Account List Getter-");

        String token = tokenService.recoverToken(request);
        String userEmail = tokenService.getEmailFromToken(token);
        User user = (User) userRepository.findByEmail(userEmail);

        String result = accountService.getAccountInfoList(user.getId());

        logger.info("-Success Getting Account List-");

        return ResponseEntity.ok(result);
    }
}