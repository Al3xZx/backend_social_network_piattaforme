package com.alessandro_molinaro.social_network.controller;

import com.alessandro_molinaro.social_network.dto.LoginInputDto;
import com.alessandro_molinaro.social_network.dto.LoginOutputDto;
import com.alessandro_molinaro.social_network.entity.Utente;
import com.alessandro_molinaro.social_network.mapper.UtenteMapper;
import com.alessandro_molinaro.social_network.service.AccountingService;
import com.alessandro_molinaro.social_network.utils.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("public/authentication")
public class AuthenticationController {

  private final AccountingService accountingService;

  private final UtenteMapper utenteMapper;

  public AuthenticationController(
      @Lazy AccountingService accountingService, @Lazy UtenteMapper utenteMapper) {
    this.accountingService = accountingService;
    this.utenteMapper = utenteMapper;
  }

  @PostMapping
  public ResponseEntity<LoginOutputDto> signin(@RequestBody LoginInputDto body) {
    // verifica se l'utente è registrato su db
    Utente user =
        accountingService.getByUsernameAndPassword(body.getUsername(), body.getPassword());
    if (user == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Map claimMap = new HashMap<>();
    claimMap.put("user", new ObjectMapper().valueToTree(utenteMapper.toDto(user)));

    String jwt = JwtProvider.createJwt(user.getEmail(), claimMap);
    LoginOutputDto dto = new LoginOutputDto();
    dto.setToken(jwt);
    return ResponseEntity.ok(dto);
  }
}
