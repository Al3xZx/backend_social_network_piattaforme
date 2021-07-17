package com.alessandro_molinaro.social_network.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UtenteDTO {
  private Long id;

  private String nome;

  private String cognome;

  private String email;

  private Boolean genere;
}
