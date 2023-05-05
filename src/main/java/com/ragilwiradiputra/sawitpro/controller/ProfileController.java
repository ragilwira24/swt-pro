package com.ragilwiradiputra.sawitpro.controller;

import com.ragilwiradiputra.sawitpro.records.ResponseRecord;
import com.ragilwiradiputra.sawitpro.records.UserRecord;
import com.ragilwiradiputra.sawitpro.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "Endpoints for get name and update name")
public class ProfileController {

  private final UserService userService;


  @GetMapping(value = "/name")
  public ResponseEntity<ResponseRecord<String>> getName(Authentication authentication){
    return ResponseEntity.ok(new ResponseRecord<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
        userService.getNameFromUser(authentication)));
  }

  @PutMapping(value = "/update/{name}")
  public ResponseEntity<ResponseRecord<UserRecord>> updateName(@PathVariable("name") String name,
                                                               Authentication authentication) {
    return ResponseEntity.ok(new ResponseRecord<>(HttpStatus.OK.value(),
        HttpStatus.OK.getReasonPhrase(), userService.updateNameInUser(authentication, name)));
  }

}
