package com.ragilwiradiputra.sawitpro.controller;

import com.ragilwiradiputra.sawitpro.configuration.JwtComponent;
import com.ragilwiradiputra.sawitpro.exception.UserException;
import com.ragilwiradiputra.sawitpro.model.User;
import com.ragilwiradiputra.sawitpro.records.AuthenticationRecord;
import com.ragilwiradiputra.sawitpro.records.ResponseRecord;
import com.ragilwiradiputra.sawitpro.records.UserRecord;
import com.ragilwiradiputra.sawitpro.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for authenticate and register")
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final JwtComponent jwtComponent;

  @PostMapping(value = "/login")
  public ResponseEntity<ResponseRecord<Object>> authenticate(
      @Valid @RequestBody AuthenticationRecord authenticationRequest) {

    return Try.of(() -> {
      String username = authenticationRequest.username();
      authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(username,
              authenticationRequest.password(), new ArrayList<>()));

      final User user = (User) userService.loadUserByUsername(username);

      if (user != null) {
        String jwt = jwtComponent.generateToken(user);
        return ResponseEntity.ok()
            .body(new ResponseRecord<Object>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                Map.of("token", jwt)));
      }

      throw new UserException("There's internal server error, please check again later");
    }).getOrElseGet(e -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseRecord<Object>(HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage())));
  }

  @PostMapping(value = "/register")
  public ResponseEntity<ResponseRecord<UserRecord>> register(@Valid @RequestBody UserRecord userRequest) {
    return Try.of(() -> {
      UserRecord userRecord = userService.registerNewUser(userRequest);
      return ResponseEntity.ok(new ResponseRecord<>(HttpStatus.OK.value(),
          HttpStatus.OK.getReasonPhrase(), userRecord));
    }).getOrElse(ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
        .body(new ResponseRecord<>(HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(), null)));
  }

}
