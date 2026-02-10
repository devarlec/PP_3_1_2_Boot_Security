package org.pj.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Value("${app.security.password-encoder.enabled:true}")
    private boolean passwordEncoderEnabled;

    @Value("${app.security.password-encoder.bcrypt-strength:12}")
    private int bcryptStrength;

    @Bean
    public PasswordEncoder passwordEncoder() {
        if (passwordEncoderEnabled) {
            return new BCryptPasswordEncoder(bcryptStrength) {
                // просто проверка в консоли, что пароли сравниваются нормально
                @Override
                public boolean matches(CharSequence rawPassword, String encodedPassword) {
                    System.out.println("=== PASSWORD MATCHING IN ENCODER ===");
                    System.out.println("Raw password: " + rawPassword);
                    System.out.println("Encoded Raw password: " + encode(rawPassword));
                    System.out.println("Encoded password from DB: " + encodedPassword);
                    boolean result = super.matches(rawPassword, encodedPassword);
                    System.out.println("Match result: " + result);

                    return result;
                }
            };
        } else {
            // если отключено кодирование паролей
            System.out.println("=== PASSWORD ENCODER DISABLED - USING NOOP ===");
            return NoOpPasswordEncoder.getInstance();
        }
    }
}