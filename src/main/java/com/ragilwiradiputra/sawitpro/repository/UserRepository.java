package com.ragilwiradiputra.sawitpro.repository;

import com.ragilwiradiputra.sawitpro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findUserByUsername(String username);
  boolean existsUserByUsername(String username);

}
