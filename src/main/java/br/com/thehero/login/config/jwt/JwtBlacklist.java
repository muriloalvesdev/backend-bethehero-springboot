package br.com.thehero.login.config.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope("singleton")
public class JwtBlacklist {
  private static Map<String, String> blacklist = new ConcurrentHashMap<>();
  private static final Logger LOGGER = LoggerFactory.getLogger(JwtBlacklist.class);

  private String jwtSecret;

  public synchronized void add(String token) {
    if (!blacklist.containsKey(token)) {
      blacklist.putIfAbsent(token, String.valueOf(System.currentTimeMillis()));
    } else {
      LOGGER.info("TOKEN JA FOI INVALIDADO, TOKEN: {}", token);
    }
  }

  public boolean check(String token) {
    if (StringUtils.isNotBlank(token)) {
      return blacklist.containsKey(token);
    } else {
      return false;
    }
  }

  public synchronized void cleanBlacklist() {
    for (String token : blacklist.keySet()) {
      try {
        getDateExpirationToken(token);
      } catch (ExpiredJwtException e) {
        blacklist.remove(token);
        continue;
      }
    }
  }

  private LocalDateTime getDateExpirationToken(String token) {
    return Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody()
        .getExpiration()
        .toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
  }
}
