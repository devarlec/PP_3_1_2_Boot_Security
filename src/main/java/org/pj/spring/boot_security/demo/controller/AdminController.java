package org.pj.spring.boot_security.demo.controller;

import jakarta.validation.Valid;
import org.pj.spring.boot_security.demo.model.User;
import org.pj.spring.boot_security.demo.service.RoleService;
import org.pj.spring.boot_security.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


//    1. Пользователь заходит на: /admin
//    2. Срабатывает homeAdmin() → возвращает "redirect:/admin/users"
//    3. Браузер получает HTTP 302 и делает НОВЫЙ запрос на: /admin/users
//    4. Срабатывает printUsers() → добавляет данные в модель и возвращает "all_users"
//    5. Браузер отображает шаблон all_users.html с данными пользователей
    @GetMapping()
    //на список пользователей - начальная страница при входе одмином
    public String homeAdmin() {
        return "redirect:/admin/users";// Делает 302 редирект
    }


    @GetMapping("users")
    //список пользователей
    public String printUsers(Model model) {
        model.addAttribute("userSet", userService.listUsers());
        return "all_users";
    }


    @GetMapping(value = "users/add")
    //создаем пустой user и показываем форму для добавления
    public String newUserForm(@ModelAttribute("user") User user, Model model) {
        //"покажем" все роли по таблице из б.д.
        model.addAttribute("roles", roleService.getAllRoles());
        return "add_user";
    }


    @PostMapping(value = "users/add")
    //получаем (spring привязывает) данные от формы для полей user
    public String createNewUser(@Valid @ModelAttribute("user") User user
            , BindingResult bindingResult  //объект с результатами присваивания
            , @RequestParam(value = "roles") String[] roles
            , Model model) {

        // ПРОВЕРЯЕМ ЕСТЬ ЛИ ОШИБКИ
        if (bindingResult.hasErrors()) {
            // ЕСЛИ ЕСТЬ ОШИБКИ - ВОЗВРАЩАЕМСЯ НА ФОРМУ
            model.addAttribute("roles", roleService.getAllRoles());
            return "add_user"; // не redirect, а return на ту же страницу
        }

        // ЕСЛИ ОШИБОК НЕТ - СОХРАНЯЕМ
        user.setRoles(roleService.getSetOfRoles(roles));
        userService.addUser(user);
        return "redirect:/admin/users";
    }


    @GetMapping("users/{id}/edit")
    // "покажем" все роли по таблице из б.д. и заполним форму юзером
    public String editUserForm(Model model, @PathVariable("id") Long id) {
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", userService.getUserById(id));
        return "edit_user";
    }

    @PostMapping("users/{id}/edit")
    public String update(@Valid @ModelAttribute("user") User user
            , BindingResult bindingResult  //объект с результатами присваивания
            , @RequestParam(value = "roles") String[] roles
            , Model model) {

        // ПРОВЕРЯЕМ ЕСТЬ ЛИ ОШИБКИ
        if (bindingResult.hasErrors()) {
            // ЕСЛИ ЕСТЬ ОШИБКИ - ВОЗВРАЩАЕМСЯ НА ФОРМУ
            // Сохраняем выбранные роли, если они были
            if (roles != null) {
                user.setRoles(roleService.getSetOfRoles(roles));
            }
            model.addAttribute("roles", roleService.getAllRoles());
            return "edit_user"; // не redirect, а return на ту же страницу
        }

        // ЕСЛИ ОШИБОК НЕТ - ОБНОВЛЯЕМ
        user.setRoles(roleService.getSetOfRoles(roles));
        userService.updateUser(user);
        return "redirect:/admin/users";
    }


    @PostMapping("users/{id}/delete")
    public String deleteUserById(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin/users";
    }

    //вообще не используется
    @GetMapping("users/{id}")
    public String show(@PathVariable("id") Long id, ModelMap modelMap) {
        modelMap.addAttribute("user", userService.getUserById(id));
        return "user_info_by_id";
    }
}
