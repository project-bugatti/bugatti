package cf.thegc.bugatti.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Service
public class AuthenticationService {

    private final static String AUTHORIZATION_HEADER_KEY = "Authorization";
    private final static String BEARER = "BEARER";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void verifyJWT(HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION_HEADER_KEY);

        // Split a string by a space - https://stackoverflow.com/questions/7899525/how-to-split-a-string-by-space
        String[] splitAuthorizationHeader = authorizationHeader.split("\\s+");
        if (splitAuthorizationHeader.length != 2) {
            throwUnauthorized();
        }

        if (!splitAuthorizationHeader[0].toUpperCase().equals(BEARER)) {
            throwUnauthorized();
        }

        String token = splitAuthorizationHeader[1];

        RSAPublicKey publicKey = null;
        RSAPrivateKey privateKey = null;
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
        }

    }

    private void throwUnauthorized() {
        throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
    }

}
