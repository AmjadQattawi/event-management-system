package com.eventmanagement.event_management_system.service;

import org.springframework.data.domain.Page;

import java.util.List;

public interface BaseService <D,ID>{

    D findById(ID id);
    List<D> findAll();
    void delete(ID id);
    Page<D> findByPage(int page, int size, String sortBy, String direction);


}
