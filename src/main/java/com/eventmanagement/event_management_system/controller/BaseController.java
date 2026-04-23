package com.eventmanagement.event_management_system.controller;

import com.eventmanagement.event_management_system.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class BaseController <D,ID>{

    protected abstract BaseService<D, ID> getService();

    @GetMapping("/findById")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<D> findById( ID id) {
        return ResponseEntity.ok(getService().findById(id));
    }

    @GetMapping("/findAll")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<D>> findAll() {
        return ResponseEntity.ok(getService().findAll());
    }
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete( ID id) {
        getService().delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findByPage")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<D>> findByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){
        return ResponseEntity.ok(getService().findByPage(page,size,sortBy,direction));
    }

}
