package ru.cards.SpringCard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cards.SpringCard.model.User;
import ru.cards.SpringCard.service.UserService;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверный логин или пароль");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user, @RequestParam String passwordConfirm, Model model) {
        if (!user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("error", "Пароли не совпадают");
            return "register";
        }
        if (!userService.saveUser(user)) {
            model.addAttribute("error", "Пользователь с таким именем уже существует");
            return "register";
        }
        return "redirect:/login";
    }

    @GetMapping("/user/home")
    public String userHome(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("userDetails", user);
        return "index";
    }
}
