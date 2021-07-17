package com.alessandro_molinaro.social_network.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginOutputDto {
    private String token;

}