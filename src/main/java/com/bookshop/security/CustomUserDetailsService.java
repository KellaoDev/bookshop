package com.bookshop.security;

import com.bookshop.entities.UserEntity;
import com.bookshop.repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        UserEntity user = userEntityRepository.findByCpf(cpf);

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        if(user.getRoles().isEmpty()) {
            throw new UsernameNotFoundException("Usuário não tem roles atribuídas");
        }
        return new CustomUserDetails(user);
    }

}
