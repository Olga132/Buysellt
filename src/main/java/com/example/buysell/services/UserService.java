package com.example.buysell.services;

import com.example.buysell.models.User;
import com.example.buysell.models.enums.Role;
import com.example.buysell.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSender;

    // создание пользователя
    public boolean createUser(User user) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) return false;
        user.setActive(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        user.setActivationCode(UUID.randomUUID().toString());
        if (!user.getEmail().isEmpty()) {
            String message = String.format("Здравствуйте, %s! \n" +
                            "Добро пожаловать на BuySell. " +
                            "Для активации аккаунта, пожалуйста, перейдите по ссылке: http://localhost:8082/activate/%s",
                    user.getName(), user.getActivationCode()
            );
            mailSender.sendEmail(user.getEmail(), "Код активации аккаунта BuySell", message);
        }
        log.info("Saving new User, email: {}", email);
        userRepository.save(user);
        return true;
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    // блокировка/активация пользователя
    public void banUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (user.isActive()) {
                user.setActive(false);
                log.info("Ban user with id = {}; email: {}", user.getId(), user.getEmail());
            } else {
                user.setActive(true);
                log.info("Unban user with id = {}; email: {}", user.getId(), user.getEmail());
            }
        }
        userRepository.save(user);
    }

    // изменение ролей
    public void changeUserRoles(User user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
        log.info("User with id = {} has roles set {}", user.getId(), user.getRoles());
    }

    // получение пользователя из контекста безопасности
    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    // активация при регистрации
    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);
        log.info("User with id = {} activated", user.getId());
        return true;
    }

    // удаление пользователя
    public void deleteUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(userRepository::delete);
        log.info("User with id = {} deleted", id);
    }
}
