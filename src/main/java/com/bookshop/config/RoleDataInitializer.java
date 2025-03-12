package com.bookshop.config;

import com.bookshop.entities.role.Role;
import com.bookshop.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleDataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName("ROLE_CLIENT") == null) {
            Role roleClient = new Role();
            roleClient.setName("ROLE_CLIENT");
            roleRepository.save(roleClient);
        }
        if (roleRepository.findByName("ROLE_SELLER") == null) {
            Role roleSeller = new Role();
            roleSeller.setName("ROLE_SELLER");
            roleRepository.save(roleSeller);
        }
        if (roleRepository.findByName("ROLE_MANAGER") == null) {
            Role roleManager = new Role();
            roleManager.setName("ROLE_MANAGER");
            roleRepository.save(roleManager);
        }
    }

}
