package org.pj.spring.boot_security.demo.controller;

import org.pj.spring.boot_security.demo.model.User;
import org.pj.spring.boot_security.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userInfo(Principal principal, Model model) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "current_user";
    }
}
