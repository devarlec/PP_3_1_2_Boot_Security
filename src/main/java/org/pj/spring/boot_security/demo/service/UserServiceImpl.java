package org.pj.spring.boot_security.demo.service;

import org.pj.spring.boot_security.demo.dao.UserDao;
import org.pj.spring.boot_security.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.security.password-encoder.enabled:true}")
    private boolean passwordEncoderEnabled;

    @Autowired
    public UserServiceImpl(UserDao userDao, @Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
    }

    @Override
    public void updateUser(User user) {
        User existingUser = getUserById(user.getId());

        // Проверяем, изменился ли пароль
        if (!user.getPassword().equals(existingUser.getPassword())) {
            // Кодируем пароль только если он изменился
            if (passwordEncoderEnabled && !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }
        userDao.updateUser(user);
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    public void addUser(User user) {
        // Кодируем пароль только если кодирование включено
        if (passwordEncoderEnabled) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userDao.addUser(user);
    }

    @Override
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    public void removeUserById(Long id) {
        userDao.removeUserById(id);
    }

    @Override
    public List<User> listUsers() {
        return userDao.listUsers();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Email: " + email);
        System.out.println("Password encoder enabled: " + passwordEncoderEnabled);

        User user = userDao.getUserByEmail(email);

        System.out.println("User found: " + (user != null));

        if (user != null) {
            System.out.println("Stored password hash: " + user.getPassword());
            System.out.println("Hash length: " + user.getPassword().length());
        }
        return user;
    }
}
