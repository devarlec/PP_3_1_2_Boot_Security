package org.pj.spring.boot_security.demo.configs;

import org.pj.spring.boot_security.demo.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final AuthenticationSuccessHandler successUserHandler;
    private final UserServiceImpl userServiceImpl;
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(AuthenticationSuccessHandler successUserHandler,
                             UserServiceImpl userServiceImpl,
                             PasswordEncoder passwordEncoder) {
        this.successUserHandler = successUserHandler;
        this.userServiceImpl = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);

        provider.setPreAuthenticationChecks(userDetails -> {
            //смотрим какие логин и хешированный пароль у пользователя
            System.out.println("=== PASSWORD CHECK IN CONFIG ===");
            System.out.println("Username from login form: " + userDetails.getUsername());
            System.out.println("Stored password hash: " + userDetails.getPassword());
        });

        provider.setUserDetailsService(userServiceImpl);
        provider.setHideUserNotFoundExceptions(false);  //запрещает скрывать исключение, когда пользователь не найден. Чтоб его посмотреть
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/login", "/").permitAll()
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .successHandler(successUserHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }
}
