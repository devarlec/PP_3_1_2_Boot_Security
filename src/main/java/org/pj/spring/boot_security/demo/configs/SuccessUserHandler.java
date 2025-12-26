package org.pj.spring.boot_security.demo.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {
    // Spring Security использует объект Authentication, пользователя авторизованной сессии.

    private static final Logger logger = LoggerFactory.getLogger(SuccessUserHandler.class);


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        logger.info("User {} authenticated with roles: {}",
                authentication.getName(), roles);

        if (roles.contains("ROLE_ADMIN")) {
            // У пользователя admin есть роль ADMIN, перенаправляем на страницу админа
            httpServletResponse.sendRedirect("/admin");
        } else if (roles.contains("ROLE_USER")) {
            // У пользователя user есть только роль USER, перенаправляем на страницу пользователя
            httpServletResponse.sendRedirect("/user");
        } else {
            // На случай, если у пользователя нет известных ролей
            httpServletResponse.sendRedirect("/login?error");
        }
    }

}
