package com.bookshop.services;

import com.bookshop.dto.UserEntityDTO;
import com.bookshop.entities.UserEntity;
import com.bookshop.entities.permissions.UserEnum;
import com.bookshop.entities.role.Role;
import com.bookshop.repositories.LoanRepository;
import com.bookshop.repositories.RoleRepository;
import com.bookshop.repositories.UserEntityRepository;
import com.bookshop.services.exceptions.BusinessException;
import com.bookshop.services.exceptions.DatabaseException;
import com.bookshop.services.exceptions.ResourceNotFoundException;
import com.bookshop.services.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserEntityService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LoanRepository loanRepository;

    public List<UserEntity> findAll() {
        try {
            return userEntityRepository.findAll();
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Erro ao buscar todos os usuários");
        }
    }

    public UserEntity findByIdWithLoans(Long id) {
        return userEntityRepository.findByIdWithLoans(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado para id: " + id));
    }

    @Transactional
    public UserEntity insert(UserEntityDTO dto) {

        if (userEntityRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("E-mail já cadastrado.");
        }
        if (userEntityRepository.existsByCpf(dto.getCpf())) {
            throw new BusinessException("CPF já cadastrado.");
        }
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new ValidationException("O nome de usuário é obrigatório.");
        }
        if (dto.getEmail() == null || !dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("E-mail inválido.");
        }
        if (dto.getPassword() == null || dto.getPassword().length() < 6) {
            throw new ValidationException("A senha deve ter pelo menos 6 caracteres.");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new ValidationException("As senhas devem ser iguais.");
        }
        if (dto.getCpf() == null || dto.getCpf().length() != 11) {
            throw new ValidationException("CPF inválido.");
        }

        UserEntity obj = new UserEntity();
        obj.setId(dto.getId());
        obj.setUsername(dto.getUsername());
        obj.setEmail(dto.getEmail());
        obj.setPassword(dto.getPassword());
        obj.setCpf(dto.getCpf());
        obj.setPhone(dto.getPhone());
        obj.setUserEnum(dto.getUserEnum());

        Role role = roleRepository.findByName("ROLE_" + dto.getUserEnum().name());
        if (role == null) {
            throw new ResourceNotFoundException("Role não encontrada para o tipo de usuário: " + dto.getUserEnum());
        }

        if (obj.getUserEnum() == UserEnum.CLIENT) {
            Role roleClient = roleRepository.findByName("ROLE_CLIENT");
            obj.setRoles(Set.of(roleClient));
        }
        if (obj.getUserEnum() == UserEnum.SELLER) {
            Role roleSeller = roleRepository.findByName("ROLE_SELLER");
            obj.setRoles(Set.of(roleSeller));
        }
        if (obj.getUserEnum() == UserEnum.MANAGER) {
            Role roleManager = roleRepository.findByName("ROLE_MANAGER");
            obj.setRoles(Set.of(roleManager));
        }

        try {
            return userEntityRepository.save(obj);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Erro ao salvar o usuário no banco de dados");
        }
    }

    @Transactional
    public UserEntity update(Long id, UserEntityDTO dto) {
        UserEntity entity = userEntityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));

        Optional<UserEntity> existingEmail = userEntityRepository.findByEmail(dto.getEmail());
        if (existingEmail.isPresent() && !existingEmail.get().getId().equals(id)) {
            throw new BusinessException("E-mail já cadastrado.");
        }

        Optional<UserEntity> existingCpf = userEntityRepository.findByCpf(dto.getCpf());
        if (existingCpf.isPresent() && !existingCpf.get().getId().equals(id)) {
            throw new BusinessException("CPF já cadastrado.");
        }

        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new ValidationException("O nome de usuário é obrigatório.");
        }
        if (dto.getEmail() == null || !dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("E-mail inválido.");
        }
        if (dto.getCpf() == null || dto.getCpf().length() != 11) {
            throw new ValidationException("CPF inválido.");
        }

        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setCpf(dto.getCpf());
        entity.setPhone(dto.getPhone());

        try {
            return userEntityRepository.save(entity);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Usuário não encontrado com o ID: " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Erro ao salvar o usuário no banco de dados.");
        }
    }

    @Transactional
    public void delete(Long id) {
        UserEntity entity = userEntityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID:" + id));

        if (entity.getUserEnum() == UserEnum.MANAGER) {
            throw new BusinessException("O administrador principal não pode ser excluído.");
        }

        if (loanRepository.existsByUserIdAndReturnedFalse(id)) {
            throw new BusinessException("Não é possível excluir o usuário, pois ele possui empréstimos não devolvidos.");
        }

        try {
            userEntityRepository.delete(entity);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Usuário não encontrado com o ID: " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Erro ao salvar o usuário no banco de dados.");
        }
    }
}
