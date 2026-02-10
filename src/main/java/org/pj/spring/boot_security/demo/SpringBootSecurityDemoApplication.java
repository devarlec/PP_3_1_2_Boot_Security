package org.pj.spring.boot_security.demo;

import org.pj.spring.boot_security.demo.model.Role;
import org.pj.spring.boot_security.demo.model.User;
import org.pj.spring.boot_security.demo.service.RoleService;
import org.pj.spring.boot_security.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@SpringBootApplication
public class SpringBootSecurityDemoApplication implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SpringBootSecurityDemoApplication(UserService userService, RoleService roleService,
                                             PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;

    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            // Сначала создаем и сохраняем роли
            Role roleAdmin = new Role("ROLE_ADMIN");
            Role roleUser = new Role("ROLE_USER");

            roleService.add(roleAdmin);
            roleService.add(roleUser);

            // Получаем сохраненные роли (они теперь имеют ID)
            Role savedRoleAdmin = roleService.getRoleByName("ROLE_ADMIN");
            Role savedRoleUser = roleService.getRoleByName("ROLE_USER");

            // Создаем пользователей с ролями
            User user1 = new User("admin", "admin", 43, "admin@mail.ru", "admin");
            user1.setRoles(new HashSet<>(Arrays.asList(savedRoleAdmin, savedRoleUser)));

            User user2 = new User("user", "user", 42, "user@mail.ru", "user");
            user2.setRoles(new HashSet<>(Collections.singletonList(savedRoleUser)));

            User user3 = new User("ivan", "ivan", 40, "ivan@mail.ru", "ivan");
            user3.setRoles(new HashSet<>(Collections.singletonList(savedRoleUser)));

            userService.addUser(user1);
            userService.addUser(user2);
            userService.addUser(user3);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


