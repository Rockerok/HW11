package ru.gb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gb.entities.User;
import ru.gb.services.UserService;

import javax.sql.DataSource;
import java.security.Principal;

@RestController
public class MainController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping ("/")
    public String homePage(){
        return "home";
    }

    @GetMapping("/authenticated")
    public String pageForAuthenticatedUsers(Principal principal){
        User user = userService.findByUsername(principal.getName());
        return "secures part of web service" + user.getUsername()+" "+user.getEmail();
    }

    @GetMapping("/read_profile")
    public String pageForReadProfile(){
        return "read profile Page" ;
    }
    @GetMapping("/only_admins")
    public String pageForOnlyAdmins(){
        return "admins Page" ;
    }
}
