package com.bookshop.services;

import com.bookshop.dto.UserEntityDTO;
import com.bookshop.entities.UserEntity;
import com.bookshop.entities.permissions.UserEnum;
import com.bookshop.entities.role.Role;
import com.bookshop.repositories.RoleRepository;
import com.bookshop.repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserEntityService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<UserEntity> findAll() {
        return userEntityRepository.findAll();
    }

    public UserEntity findByIdWithLoans(Long id) {
        return userEntityRepository.findByIdWithLoans(id)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado para empréstimos"));
    }

    public UserEntity insert(UserEntityDTO dto) {
        UserEntity obj = new UserEntity();
        obj.setId(dto.getId());
        obj.setUsername(dto.getUsername());
        obj.setEmail(dto.getEmail());
        obj.setPassword(dto.getPassword());
        obj.setCpf(dto.getCpf());
        obj.setPhone(dto.getPhone());
        obj.setUserEnum(dto.getUserEnum());

        if(obj.getUserEnum() == UserEnum.CLIENT) {
            Role roleClient = roleRepository.findByName("ROLE_CLIENT");
            obj.setRoles(Set.of(roleClient));
        }
        if(obj.getUserEnum() == UserEnum.SELLER) {
            Role roleSeller = roleRepository.findByName("ROLE_SELLER");
            obj.setRoles(Set.of(roleSeller));
        }
        if(obj.getUserEnum() == UserEnum.MANAGER) {
            Role roleManager = roleRepository.findByName("ROLE_MANAGER");
            obj.setRoles(Set.of(roleManager));
        }

        return userEntityRepository.save(obj);
    }

    public UserEntity update(Long id, UserEntityDTO dto) {
        UserEntity entity = userEntityRepository.getReferenceById(id);

        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setCpf(dto.getCpf());
        entity.setPhone(dto.getPhone());

        return userEntityRepository.save(entity);
    }

    public void delete(Long id) {
        userEntityRepository.deleteById(id);
    }
}
