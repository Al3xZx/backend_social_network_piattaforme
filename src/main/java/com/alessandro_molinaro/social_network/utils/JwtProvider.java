package com.alessandro_molinaro.social_network.utils;

import com.alessandro_molinaro.social_network.configuration.SecurityConfig;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.util.Map;

/** The type Jwt provider. */
@Slf4j
public class JwtProvider {

  private static final String issuer = "demo-service";

  /**
   * Create jwt string.
   *
   * @param subject the subject
   * @param payloadClaims the payload claims
   * @return the JWT string
   */
  public static String createJwt(String subject, Map<String, String> payloadClaims) {
    JWTCreator.Builder creator =
        JWT.create()
            .withSubject(subject)
            .withIssuer(issuer)
            .withIssuedAt(DateTime.now().toDate())
            .withExpiresAt(DateTime.now().plusMonths(1).toDate());

    if (payloadClaims != null && !payloadClaims.isEmpty()) {
      for (Map.Entry<String, String> entry : payloadClaims.entrySet()) {
        creator.withClaim(entry.getKey(), entry.getValue());
      }
    } else {
      log.warn("You are building a JWT without header claims!");
    }
    return creator.sign(Algorithm.HMAC256(SecurityConfig.secret));
  }

  /**
   * Verify jwt decoded.
   *
   * @param jwt the JWT string
   * @return the decoded JWT
   */
  public static DecodedJWT verifyJwt(String jwt) {
    return JWT.require(Algorithm.HMAC256(SecurityConfig.secret)).build().verify(jwt);
  }

  private JwtProvider() {}
}
