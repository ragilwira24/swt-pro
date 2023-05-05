package com.ragilwiradiputra.sawitpro.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "user", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class User implements UserDetails {

  @Id
  @GeneratedValue(generator = "system-uuid")
  @GenericGenerator(name = "system-uuid", strategy = "uuid")
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @CreatedDate
  @Column(name = "created_date", updatable = false)
  private Date createdDate;

  @LastModifiedDate
  @Column(name = "modified_date")
  private Date modifiedDate;

  private String username;
  private String name;
  private String phone;
  private String password;
  private String roles;

  @Transient
  private Collection<? extends GrantedAuthority> authorities;
  private boolean enabled;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    User user = (User) o;
    return getId() != null && Objects.equals(getId(), user.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  @Transient
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Optional.ofNullable(roles)
        .map(role -> Arrays.stream(this.roles.split(","))
            .map(SimpleGrantedAuthority::new).toList())
        .orElse(Collections.emptyList());
  }

  @Override
  @Transient
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  @Transient
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  @Transient
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @PostLoad
  void onLoad(){
    this.authorities = getAuthorities();
  }

  @PrePersist
  void onCreate() {
    this.createdDate = new Date();
    this.modifiedDate = new Date();
  }

  @PreUpdate
  void onPersist() {
    this.modifiedDate = new Date();
  }

}
