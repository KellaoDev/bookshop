package com.bookshop.resources;

import com.bookshop.dto.UserEntityDTO;
import com.bookshop.entities.UserEntity;
import com.bookshop.services.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserEntityService userEntityService;

    @GetMapping
    public ResponseEntity<List<UserEntity>> findAll() {
        List<UserEntity> list = userEntityService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/loan/{id}")
    public ResponseEntity<UserEntity> findByIdWithLoans(@PathVariable Long id) {
        UserEntity obj = userEntityService.findByIdWithLoans(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<UserEntityDTO> insert(@RequestBody UserEntityDTO dto) {
        userEntityService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserEntity> update(@PathVariable Long id, @RequestBody UserEntityDTO dto) {
        UserEntity update = userEntityService.update(id, dto);
        return ResponseEntity.ok().body(update);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userEntityService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
