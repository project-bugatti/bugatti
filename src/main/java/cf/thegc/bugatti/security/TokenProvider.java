package cf.thegc.bugatti.security;

import com.auth0.jwk.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TokenProvider {

    private final static Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private final static String AUTH0_DOMAIN = "https://thegc.auth0.com/.well-known/jwks.json";

    public boolean validateToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            JwkProvider provider = new JwkProviderBuilder(AUTH0_DOMAIN)
                    .cached(10, 24, TimeUnit.HOURS)
                    .build();
            Jwk jwk = provider.get(decodedJWT.getKeyId());
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            algorithm.verify(decodedJWT);

            // Checks token expiration time
            Long expiresAt = decodedJWT.getExpiresAt().getTime();
            Long currentTime = new Date().getTime();
            return expiresAt <= currentTime;
            // a JWT is valid if its time is less than or equal to the current time, so return true
        } catch (JwkException e) {
            logger.info("Invalid JWT: " + e.getMessage());
            logger.debug("Exception " + e.getMessage(), e);
            return false;
        }
    }

}
