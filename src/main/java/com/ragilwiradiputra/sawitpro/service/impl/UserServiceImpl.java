package com.ragilwiradiputra.sawitpro.service.impl;

import com.ragilwiradiputra.sawitpro.exception.UserException;
import com.ragilwiradiputra.sawitpro.model.User;
import com.ragilwiradiputra.sawitpro.records.UserRecord;
import com.ragilwiradiputra.sawitpro.repository.UserRepository;
import com.ragilwiradiputra.sawitpro.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserRecord registerNewUser(UserRecord userRecord) {
    String phone = userRecord.phoneNumber();

    if (userRepository.existsUserByUsername(phone)) {
      throw new UserException("User already present");
    }

    User user = User.builder()
        .name(userRecord.name())
        .password(passwordEncoder.encode(userRecord.password()))
        .username(phone)
        .phone(phone)
        .enabled(true)
        .roles("AUTHENTICATED")
        .build();

    return Optional.of(userRepository.save(user)).map(newUser -> new UserRecord(phone, newUser.getName(), null))
        .orElseThrow(() -> new UserException("Failed to register the user"));
  }

  @Override
  public String getNameFromUser(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return user.getName();
  }

  @Override
  public UserRecord updateNameInUser(Authentication authentication, String name) {
    User authenticatedUser = (User) authentication.getPrincipal();
    return userRepository.findById(authenticatedUser.getId())
        .map(user -> {
          user.setName(name);
          return Optional.of(userRepository.save(user)).map(newUser -> new UserRecord(user.getPhone(), newUser.getName(), null))
              .orElseThrow(() -> new UserException("Failed to update the user"));
        }).orElseThrow(() -> new UserException("Not found user"));
  }

  @Override
  public User loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findUserByUsername(username)
        .orElseThrow(() -> new UserException("Username or password wrong"));
  }

}
