package br.edu.ifpb.academico.Amantes_Cafezinho.controllers;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Role;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.User;
import br.edu.ifpb.academico.Amantes_Cafezinho.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import br.edu.ifpb.academico.Amantes_Cafezinho.dtos.UpdateRolesUser;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @GetMapping("/users")
    public ModelAndView getAllUsers(ModelAndView mav,@RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size) {
        Page<User> users = adminService.getAllUsers(page, size);

        mav.addObject("users", users);
        mav.setViewName("/admin/users-page");
        return mav;
    }

    @GetMapping("/users/roles")
    @ResponseBody
    public List<Role> getAllRoles(){
        return adminService.getAllRoles();
    }

    @GetMapping("/users/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return adminService.getUserById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UpdateRolesUser request) {
        try {
            adminService.updateUserRoles(id, request.rolesIds());
            return ResponseEntity.ok("Roles atualizadas com sucesso!");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao atualizar roles: " + e.getMessage());
        }
    }

    @PutMapping("/users/{id}/toggle-status")
    @ResponseBody
    public ResponseEntity<String> toggleUserStatus(@PathVariable Long id) {
        try {
            adminService.toggleUserStatus(id);
            return ResponseEntity.ok("Status atualizado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao alterar status: " + e.getMessage());
        }
    }


}
