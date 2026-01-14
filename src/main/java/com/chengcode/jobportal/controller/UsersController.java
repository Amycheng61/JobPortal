package com.chengcode.jobportal.controller;

import com.chengcode.jobportal.entity.Users;
import com.chengcode.jobportal.entity.UsersType;
import com.chengcode.jobportal.service.UsersService;
import com.chengcode.jobportal.service.UsersTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class UsersController {
    private final UsersTypeService usersTypeService;
    private final UsersService usersService;
    @Autowired
    public UsersController(UsersTypeService usersTypeService,UsersService usersService){
        this.usersTypeService=usersTypeService;
        this.usersService=usersService;
    }
    @GetMapping("/register")
    public String register(Model model){
        List<UsersType> usersTypes = usersTypeService.getAll();
        model.addAttribute("getAllTypes",usersTypes);
        model.addAttribute("user",new Users());
        return "register";
    }

    @PostMapping("/register/new")
    public String registerNewUser(@Valid Users user,Model model){
        System.out.println("---------11111111111");
        Optional<Users> optionalUsers=usersService.getUserByEmail(user.getEmail());
        System.out.println("----22="+optionalUsers);
        if(optionalUsers.isPresent()){
            System.out.println("2222222222222222222");
            model.addAttribute("error","Email already registered, try to login or register with other Email");
            List<UsersType> usersTypes=usersTypeService.getAll();
            model.addAttribute("getAllTypes",usersTypes);
            model.addAttribute("user",new Users());
            return "register";
        }
        this.usersService.addNew(user);
        return "dashboard";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        return "redirect:/";
    }
}
