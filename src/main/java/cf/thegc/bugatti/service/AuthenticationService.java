package cf.thegc.bugatti.service;

import cf.thegc.bugatti.exception.UnauthorizedException;
import com.auth0.jwk.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class AuthenticationService {

    private final static String AUTHORIZATION_HEADER_KEY = "Authorization";
    private final static String BEARER = "BEARER";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void verifyJWT(HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION_HEADER_KEY);

        // Splits the string by a space
        String[] splitAuthorizationHeader = authorizationHeader.split("\\s+");
        if (splitAuthorizationHeader.length != 2 || !splitAuthorizationHeader[0].toUpperCase().equals(BEARER)) {
            throwUnauthorized("Malformed Authorization header");
        }

        try {
            String token = splitAuthorizationHeader[1];
            DecodedJWT decodedJWT = JWT.decode(token);
            JwkProvider provider = new JwkProviderBuilder("https://thegc.auth0.com/.well-known/jwks.json")
                    .cached(10, 24, TimeUnit.HOURS)
                    .build();
            Jwk jwk = provider.get(decodedJWT.getKeyId());
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            algorithm.verify(decodedJWT);

            // Checks token expiration time
            Long expiresAt = decodedJWT.getExpiresAt().getTime();
            Long currentTime = new Date().getTime();
            if (expiresAt < currentTime) {
                throwUnauthorized("Token expired");
            }
        } catch (JWTDecodeException e) {
            throwUnauthorized("Unable to decode JWT");
        } catch (SignatureVerificationException e) {
            logger.debug(e.getMessage());
            throwUnauthorized("Token signature unverified");
        } catch (JwkException e) {
            throwUnauthorized("Unable to verify JWT");
        }
    }

    private void throwUnauthorized(String details) {
        logger.info("Unauthorized");
        logger.info(details);
        throw new UnauthorizedException(details);
        //throw new UnauthorizedException(details);
    }

}
