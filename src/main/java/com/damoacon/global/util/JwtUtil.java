package com.damoacon.global.util;

import com.damoacon.domain.member.dto.LoginResponseDto;
import com.damoacon.domain.member.entity.Member;
import com.damoacon.domain.member.repository.MemberRepository;
import com.damoacon.domain.model.TokenClaimVo;
import com.damoacon.global.constant.ErrorCode;
import com.damoacon.global.exception.GeneralException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.*;

import static io.jsonwebtoken.Jwts.*;

@RequiredArgsConstructor
@Component
public class JwtUtil {
    private final MemberRepository memberRepository;

    @Value("${jwt.issuer}")
    private String ISSUER;

    @Value("${jwt.secret}")
    private String JWT_SECRET_KEY;

    public LoginResponseDto generateTokens(Member member) {
        TokenClaimVo vo = new TokenClaimVo(member.getId(), member.getEmail());

        // 토큰 발급
        String accessToken = generateToken(true, vo);
        String refreshToken = generateToken(false, vo);

        return new LoginResponseDto(member.getId(), accessToken, refreshToken);
    }

    // 토큰 생성
    public String generateToken(boolean isAccessToken, TokenClaimVo vo) {
        // Payloads 생성
        Map<String, Object> payloads = new LinkedHashMap<>();
        payloads.put("memberId", vo.getId());
        payloads.put("email", vo.getEmail());

        // Expiration time (access: 30m / refresh: 14d)
        Date now = new Date();
        Duration duration = isAccessToken ? Duration.ofMinutes(30) : Duration.ofDays(14);
        Date expiration = new Date(now.getTime() + duration.toMillis());

        // Subject
        String subject = isAccessToken ? "access" : "refresh";

        // Build
        return builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(payloads)
                .setIssuer(ISSUER)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setSubject(subject)
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate Token
    public boolean validateToken(String token) throws ExpiredJwtException {
        // 토큰 검증
        parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return true;
    }

    public Member getMember(String token) {
        // 검증 및 payload 추출
        Map<String, Object> payloads = parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        // member 조회
        return memberRepository.findById(((Number) payloads.get("memberId")).longValue())
                .orElseThrow(() -> new GeneralException(ErrorCode.INVALID_TOKEN));
    }

    // Decode Request
    public String decodeHeader(boolean isAccessToken, HttpServletRequest request) {
        final String ACCESS_HEADER = "Authorization";
        final String REFRESH_HEADER = "Authorization-refresh";
        final String header = isAccessToken ? ACCESS_HEADER : REFRESH_HEADER;

        String headerValue = request.getHeader(header);
        if(isAccessToken && headerValue == null) {
            return null;
        } else if(!isAccessToken && headerValue == null) {
            throw new GeneralException(ErrorCode.REFRESH_TOKEN_REQUIRED);
        }

        return decodeBearer(headerValue);
    }

    // Decode Bearer
    public String decodeBearer(String bearerToken) {
        final String BEARER = "Bearer ";
        List<String> tokenParts = Arrays.asList(bearerToken.split(BEARER));

        if (tokenParts.size() < 2) {
            throw new GeneralException(ErrorCode.INVALID_TOKEN);
        }

        return tokenParts.get(1);
    }

    // secretKey 로드
    private Key getSigninKey() {
        byte[] keyBytes = JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
