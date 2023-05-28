package com.example.demo.Controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dao.RoleRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.dao.UserRolesRepository;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.entities.UsersRoles;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


public class UserController {
	
	 	private final UserRepository userRepository;
	    private final RoleRepository roleRepository;
	    private final UserRolesRepository userRolesRepository;
	    private final HttpSession session;
	
	public UserController(UserRepository userRepository, RoleRepository roleRepository,
            UserRolesRepository userRolesRepository, HttpSession session) {
			this.userRepository = userRepository;
			this.roleRepository = roleRepository;
			this.userRolesRepository = userRolesRepository;
			this.session = session;
}
	
	 @GetMapping("/users")
	    public String index(Model model,
	                        @RequestParam(name = "p", defaultValue = "0") int page,
	                        @RequestParam(name = "s", defaultValue = "8") int size,
	                        @RequestParam(name = "mc", defaultValue = "") String mc) {
	        Pageable pageable = PageRequest.of(page, size);
	        Page<User> users = userRepository.findAll(pageable);

	        model.addAttribute("users", users.getContent());
	        model.addAttribute("pages", new int[users.getTotalPages()]);
	        model.addAttribute("size", size);
	        model.addAttribute("pageCourant", page);
	        model.addAttribute("mc", mc);

	        if (!model.containsAttribute("user")) {
	            model.addAttribute("user", new User());
	        }

	        model.addAttribute("roles", roleRepository.findAll());

	        HashMap<String, String> userRoles = (HashMap<String, String>) session.getAttribute("userRoles");
	        if (userRoles != null) {
	            model.addAttribute("rolesUser", userRoles.values());
	        }

	        return "users";
	    }
	 
	 @PostMapping("/users/add")
	    public String addUser(@Valid User user, BindingResult result, Model model) {
	        if (userRepository.findByUserName(user.getUsername()) != null) {
	            result.rejectValue("username", "error.user", "Nom d'utilisateur déjà existant !");
	        }

	        if (saveUser(user, result, model)) {
	            model.addAttribute("addOk", "Utilisateur ajouté !");
	        } else {
	            model.addAttribute("addFailed", true);
	        }

	        return "redirect:/users?p=0&s=8&mc=";
	    }
	 
	 @PostMapping("/users/update")
	    public String updateUser(@Valid User user, BindingResult result, Model model) {
	        User existingUser = userRepository.findByUserName(user.getUsername());
	        if (existingUser == null) {
	            result.rejectValue("username", "error.user", "Cet utilisateur n'existe pas !");
	        } else if (saveUser(user, result, model)) {
	            model.addAttribute("updateOk", "Utilisateur " + user.getUsername() + " est mis à jour !");
	        } else {
	            model.addAttribute("updateFailed", true);
	        }

	        return "redirect:/users?p=0&s=8&mc=";
	    }
	 
	 private boolean saveUser(User user, BindingResult result, Model model) {
	        HashMap<String, Role> userRoles = (HashMap<String, Role>) session.getAttribute("userRoles");
	        if (userRoles == null || userRoles.isEmpty()) {
	            result.rejectValue("roles", "error.user", "Vous devez affecter au moins une permission !");
	        }

	        if (result.hasErrors()) {
	            model.addAttribute("user", user);
	            return false;
	        }

	        User savedUser = userRepository.findByUserName(user.getUsername());
	        if (savedUser != null && savedUser.getRoles() != null) {
	            for (UsersRoles ur : savedUser.getRoles()) {
	                if (userRoles.get(ur.getRole().getRole()) == null) {
	                    userRolesRepository.delete(ur);
	                }
	            }
	        }
	        
	        user.setRoles(new ArrayList<>());
	        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
	        user = userRepository.save(user);
	        for (Role r : userRoles.values()) {
	            UsersRoles ur = userRolesRepository.findByRoleAndUser(r.getRole(), user.getUsername());
	            if (ur == null) {
	                ur = new UsersRoles();
	                ur.setUser(user);
	                ur.setRole(r);
	            }
	            ur = userRolesRepository.save(ur);
	            user.getRoles().add(ur);
	        }
	        userRepository.save(user);

	        clearRoles();
	        return true;
	    }
	 @PostMapping("/users/delete")
	    public String deleteUser(Model model, @RequestParam(name = "username", defaultValue = "0") String username) {
	        userRepository.deleteById(username);
	        model.addAttribute("deleteOk", "Utilisateur " + username + " est supprimé");
	        return "redirect:/users?p=0&s=8&mc=";
	    }

	    @PostMapping(value = "/users/get", produces = "application/json")
	    public @ResponseBody User getUser(@RequestParam(name = "username") String username) {
	        return userRepository.findByUserName(username);
	    }
	    
	    @PostMapping(value = "/users/storeuserrole", produces = "application/json")
	    public @ResponseBody String[] storeUserRole(@RequestParam(name = "role") String r) {
	        Role role = roleRepository.findByRole(r);
	        if (role != null) {
	            HashMap<String, Role> userRoles = (HashMap<String, Role>) session.getAttribute("userRoles");
	            if (userRoles == null) {
	                userRoles = new HashMap<>();
	            }
	            userRoles.put(role.getRole(), role);
	            session.setAttribute("userRoles", userRoles);
	            return new String[]{};
	        }
	        return new String[]{"Role n'existe pas !"};
	    }
	    
	    @PostMapping(value = "/users/removeuserrole", produces = "application/json")
	    public @ResponseBody HashMap<String, Role> removeUserRole(@RequestParam(name = "role", defaultValue = "") String r) {
	        HashMap<String, Role> userRoles = (HashMap<String, Role>) session.getAttribute("userRoles");
	        if (userRoles != null && !r.isEmpty()) {
	            userRoles.remove(r);
	        }
	        return new HashMap<>();
	    }
	    @PostMapping("/users/clearroles")
	    public String clearRoles() {
	        session.removeAttribute("userRoles");
	        return "redirect:/users?p=0&s=8&mc=";
	    }
	    
	    @PostMapping(value = "/users/updaterolee", produces = "application/json")
	    public @ResponseBody String[] updateRole(@RequestParam(name = "id") Long id, @RequestParam(name = "newrole") String nr) {
	        if (nr.length() <= 3) {
	            return new String[]{"Le rôle doit comporter au moins 4 caractères."};
	        }

	        Role existingRole = roleRepository.findByRole(nr);
	        if (existingRole != null) {
	            return new String[]{"Le rôle existe déjà!"};
	        }

	        Role role = roleRepository.findByIdRole(id);
	        if (role == null) {
	            return new String[]{"Le rôle n'existe pas !"};
	        }

	        role.setRole(nr);
	        roleRepository.save(role);

	        return new String[]{};
	    }

	    @PostMapping(value = "/users/addrole", produces = "application/json")
	    public @ResponseBody String[] addRole(@RequestParam(name = "role") String r) {
	        if (r.length() <= 3) {
	            return new String[]{"Le rôle doit comporter au moins 4 caractères."};
	        }

	        Role existingRole = roleRepository.findByRole(r);
	        if (existingRole != null) {
	            return new String[]{"Le rôle existe déjà!"};
	        }

	        Role role = roleRepository.save(new Role(r));
	        return new String[]{"", role.getId() + ""};
	    }
	    
	    @PostMapping("/users/updaterole")
	    public String updateRole(Model model, @RequestParam("id") Long id, @RequestParam("name") String name, @RequestParam("des") String des) {
	        Role role = roleRepository.findByIdAndName(id, name);
	        if (role == null) {
	            model.addAttribute("updateFailed", "Le rôle n'existe pas !");
	        } else if (des.length() <= 2) {
	            model.addAttribute("updateDes", "La désignation doit comporter au moins 3 caractères.");
	        } else {
	            role.setDesignation(des);
	            roleRepository.save(role);
	            model.addAttribute("updateDesOk", "La désignation a été modifiée !");
	        }
	        return getRoles(model);
	    }
	    
	    @PostMapping(value = "/users/deleterole", produces = "application/json")
	    public @ResponseBody String[] deleteRole(@RequestParam(name = "id") Long id) {
	        roleRepository.deleteById(id);
	        return new String[]{};
	    }
	    @GetMapping("/roles")
	    public String getRoles(Model model) {
	        List<Role> roles = roleRepository.findAll();
	        model.addAttribute("roles", roles);
	        return "roles";
	    }
	    @GetMapping("/color")
	    public @ResponseBody String color(@RequestParam(name = "c", defaultValue = "1") Integer c) {
	        if (c == 0) {
	            return ".montheme{background-color: #343a40 !important;}";
	        } else {
	            return ".montheme{background-color: #FFF;}";
	        }
	    }
	
	
}
