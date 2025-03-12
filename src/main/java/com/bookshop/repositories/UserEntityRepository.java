package com.bookshop.repositories;

import com.bookshop.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByCpf(String cpf);

}
