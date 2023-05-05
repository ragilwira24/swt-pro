package com.ragilwiradiputra.sawitpro.service;

import com.ragilwiradiputra.sawitpro.records.UserRecord;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  UserRecord registerNewUser(UserRecord userRecord);
  String getNameFromUser(Authentication authentication);
  UserRecord updateNameInUser(Authentication authentication, String name);

}
