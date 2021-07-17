package com.alessandro_molinaro.social_network.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "account_utente")
public class AccountUtente {

  @Id @NotNull String username;

  @Column(nullable = false)
  @NotNull
  @Length(min = 8)
  String password;

  @OneToOne
  @JoinColumn(nullable = false)
  Utente datiUtente;

  public AccountUtente() {}

  public AccountUtente(@NotNull String username, @NotNull @Length(min = 8) String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Utente getDatiUtente() {
    return datiUtente;
  }

  public void setDatiUtente(Utente datiUtente) {
    this.datiUtente = datiUtente;
  }
}
