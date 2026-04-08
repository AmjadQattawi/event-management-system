package com.eventmanagement.event_management_system.controller;

import com.eventmanagement.event_management_system.dto.EventDTO;
import com.eventmanagement.event_management_system.service.BaseService;
import com.eventmanagement.event_management_system.searchCriteria.EventSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class BaseController <D,ID>{

    protected abstract BaseService<D, ID> getService();

    @GetMapping("/findById")
    public ResponseEntity<D> findById( ID id) {
        return ResponseEntity.ok(getService().findById(id));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<D>> findAll() {
        return ResponseEntity.ok(getService().findAll());
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete( ID id) {
        getService().delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findByPage")
    public ResponseEntity<Page<D>> findByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){
        return ResponseEntity.ok(getService().findByPage(page,size,sortBy,direction));
    }


}
