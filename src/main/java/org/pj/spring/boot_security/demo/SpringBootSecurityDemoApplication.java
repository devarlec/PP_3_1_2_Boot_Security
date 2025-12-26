package org.pj.spring.boot_security.demo;

import org.pj.spring.boot_security.demo.model.Role;
import org.pj.spring.boot_security.demo.model.User;
import org.pj.spring.boot_security.demo.service.RoleService;
import org.pj.spring.boot_security.demo.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {

	public static void main(String[] args) {
		//SpringApplication.run(SpringBootSecurityDemoApplication.class, args);

		ApplicationContext context = SpringApplication.run(SpringBootSecurityDemoApplication.class, args);

		try {
			UserService userService = context.getBean(UserService.class);
			RoleService roleService = context.getBean(RoleService.class);
			PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);

			// Сначала создаем и сохраняем роли
			Role roleAdmin = new Role("ROLE_ADMIN");
			Role roleUser = new Role("ROLE_USER");

			roleService.add(roleAdmin);
			roleService.add(roleUser);

			// Получаем сохраненные роли (они теперь имеют ID)
			Role savedRoleAdmin = roleService.getRoleByName("ROLE_ADMIN");
			Role savedRoleUser = roleService.getRoleByName("ROLE_USER");

			// Создаем пользователей с ролями
			User user1 = new User("admin", "admin", 25, "admin@mail.ru",
					passwordEncoder.encode("admin"));
			user1.setRoles(new HashSet<>(Arrays.asList(savedRoleAdmin, savedRoleUser)));

			User user2 = new User("user", "user", 33, "user@mail.ru",
					passwordEncoder.encode("user"));
			user2.setRoles(new HashSet<>(Arrays.asList(savedRoleUser)));

			User user3 = new User("ivan", "ivan", 40, "ivan@mail.ru",
					passwordEncoder.encode("ivan"));
			user3.setRoles(new HashSet<>(Arrays.asList(savedRoleUser)));

			userService.addUser(user1);
			userService.addUser(user2);
			userService.addUser(user3);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}


