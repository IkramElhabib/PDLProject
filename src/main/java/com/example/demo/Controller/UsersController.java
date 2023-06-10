package com.example.demo.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.RoleRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.dao.UserRolesRepository;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.entities.UsersRoles;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UsersController{

private final UserRepository userRepository;
private final RoleRepository roleRepository;
private final UserRolesRepository userRolesRepository;

public UsersController(UserRepository userRepository, RoleRepository roleRepository, UserRolesRepository userRolesRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.userRolesRepository = userRolesRepository;
}
@GetMapping("/add")
public String showAddUserForm(Model model) {
    User user = new User();
    model.addAttribute("user", user);
    model.addAttribute("roles", roleRepository.findAll());
    return "adduser";
}

@PostMapping("/add")
public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                      @RequestParam("roles") List<Long> roleIds) {
    if (bindingResult.hasErrors()) {
        return "adduser";
    }

    // Set active flag to true
    user.setActive(true);

    // Save the User entity first
    userRepository.save(user);

    // Create UsersRoles entities and associate them with the user
    for (Long roleId : roleIds) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role != null) {
            UsersRoles usersRole = new UsersRoles();
            usersRole.setUser(user);
            usersRole.setRole(role);
            userRolesRepository.save(usersRole);
        }
    }

    return "redirect:/users";
}






}