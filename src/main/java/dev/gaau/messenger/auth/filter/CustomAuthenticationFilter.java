package dev.gaau.messenger.auth.filter;

import dev.gaau.messenger.auth.jwt.JwtUtil;
import dev.gaau.messenger.dto.response.MemberDto;
import dev.gaau.messenger.service.MemberService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        Optional<String> parsedHeader = jwtUtil.parseAuthorizationHeader(authorizationHeader);

        // 인증 헤더가 없을 경우 비회원으로 간주
        if (parsedHeader.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = parsedHeader.get();

        if (!jwtUtil.isValidToken(accessToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            // response.sendRedirect("new URL for refreshToken reissue.");
            String body = """
            {
                "code": "INVALID_TOKEN",
                "message": "JWT Token is invalid."
            }
            """;

            response.getWriter().write(body);
            return;
        }

        Claims claims = jwtUtil.resolveToken(accessToken).get();

        Long memberId = Long.valueOf(claims.getSubject());

        MemberDto memberDto = memberService.findById(memberId).orElseThrow(
                () -> new RuntimeException("Cannot find member with ID : " + memberId)
        );

        String username = memberDto.getUsername();
        UserDetails userDetails = memberService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request,response);
    }
}
