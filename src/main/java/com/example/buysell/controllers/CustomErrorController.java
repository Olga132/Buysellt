package com.example.buysell.controllers;

import com.example.buysell.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class CustomErrorController implements ErrorController {
    @Autowired
    private UserService userService;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model, Principal principal) {
        model.addAttribute("messageError", "Error : " + request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "error";
    }

}