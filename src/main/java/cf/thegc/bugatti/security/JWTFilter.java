package cf.thegc.bugatti.security;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class JWTFilter implements Filter {

    private final static Logger logger = LoggerFactory.getLogger(JWTFilter.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";
    private final static String AUTH0_DOMAIN = "https://thegc.auth0.com/.well-known/jwks.json";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = this.resolveToken(httpServletRequest);
        if (StringUtils.hasText(jwt)) {
            if (this.validateToken(jwt)) {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
    }


    private String resolveToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        logger.debug("Authorization header improperly formatted or missing");
        return null;
    }

    private boolean validateToken(String token) {
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
            if (expiresAt >= currentTime) {
                logger.debug("JWT is valid");
                return true;
            } else {
                logger.debug("JWT expired");
                return false;
            }
        } catch (JwkException e) {
            logger.info("Invalid JWT: " + e.getMessage());
            logger.debug("Exception " + e.getMessage(), e);
            return false;
        }
    }
}
