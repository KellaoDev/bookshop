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
import java.util.Optional;
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

    public UserEntity findById(Long id) {
        Optional<UserEntity> obj = userEntityRepository.findById(id);
        return obj.orElseThrow();
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

    public void delete (Long id) {
        userEntityRepository.deleteById(id);
    }

    public UserEntity update(Long id, UserEntity obj) {
        UserEntity entity = userEntityRepository.getReferenceById(id);
        updateData(entity, obj);
        return userEntityRepository.save(entity);
    }

    private void updateData(UserEntity entity, UserEntity obj) {
        entity.setUsername(obj.getUsername());
        entity.setEmail(obj.getEmail());
        entity.setPassword(obj.getPassword());
        entity.setCpf(obj.getCpf());
        entity.setPhone(obj.getPhone());
    }

}
