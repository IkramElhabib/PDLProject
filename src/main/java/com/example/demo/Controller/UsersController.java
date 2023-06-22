package com.example.demo.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    return "addUser";
}

@PostMapping("/add")
public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                      @RequestParam("roles") List<Long> roleIds) {
    if (bindingResult.hasErrors()) {
        return "addUser";
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
	@GetMapping
	public String getUsers(Model model) {
	    List<User> users = userRepository.findAll();
	    model.addAttribute("users", users);
	    return "listUser";
	}
	
	@GetMapping("/{userId}/permissions")
    public String getPermissionPage(@PathVariable("userId") String userId, Model model) {
        User user = userRepository.findByUsername(userId);
        List<Role> allRoles = roleRepository.findAll();

        // Add the user and all roles to the model
        model.addAttribute("user", user);
        model.addAttribute("roles", allRoles);

        return "updatepermission";
    }
	
	@PostMapping("/{userId}/roles/update")
	public String updateRoleForUser(@PathVariable("userId") String userId, @RequestParam("roleId") Long roleId, @RequestParam("action") String action) {
	    // Get the user and role based on the provided IDs
	    User user = userRepository.findByUsername(userId);
	    Role role = roleRepository.findByIdRole(roleId);

	    try {
	        if ("update".equals(action)) {
	            // Update the existing role for the user
	            UsersRoles userRole = userRolesRepository.findByUser(user);
	            if (userRole != null) {
	                userRole.setRole(role);
	                userRolesRepository.save(userRole);
	            }
	        } else if ("add".equals(action)) {
	            // Add a new role for the user
	            UsersRoles newUserRole = new UsersRoles(user, role);
	            userRolesRepository.save(newUserRole);
	        } else {
	            throw new IllegalArgumentException("Invalid action: " + action);
	        }

	        return "redirect:/users";
	    } catch (Exception e) {
	        System.out.println("Error updating role for user: " + e.getMessage());
	        return "redirect:/users"; // Remplacer "error-page" par le nom de votre page d'erreur personnalisée
	    }
	}
	
	@PostMapping("/{userId}/roles/delete")
	public String deleteRoleForUser(@PathVariable("userId") String userId, @RequestParam("roleId") Long roleId) {
	    // Récupérer l'utilisateur et le rôle en fonction des IDs fournis
		 User user = userRepository.findByUsername(userId);
		    Role role = roleRepository.findByIdRole(roleId);
		    
	    // Supprimer la relation rôle-utilisateur de la base de données
		    UsersRoles userRole = userRolesRepository.findByUserAndRole(user, role);
		    if (userRole != null) {
		        userRolesRepository.delete(userRole);
		    }

	    return "redirect:/users";
	}
}