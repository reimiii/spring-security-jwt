package franxx.code.security.web.token;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class JwtServiceTest {

  // create secret key


  @Test
  void generateSecretKey() {
    SecretKey secretKey = Jwts.SIG.HS512.key().build();
    String hexBinary = DatatypeConverter.printHexBinary(secretKey.getEncoded());
    log.info("key = [{}]", hexBinary);
  }
}