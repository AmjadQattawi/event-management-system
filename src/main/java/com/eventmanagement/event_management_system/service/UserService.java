package com.eventmanagement.event_management_system.service;

import com.eventmanagement.event_management_system.dto.UserDTO;
import com.eventmanagement.event_management_system.entity.User;
import com.eventmanagement.event_management_system.interfaceService.IUserService;
import com.eventmanagement.event_management_system.mapper.UserMapper;
import com.eventmanagement.event_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;


    @Override
    public UserDTO create(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO update(Long id, UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO findById(Long aLong) {
        return null;
    }

    @Override
    public List<UserDTO> findAll() {
        return List.of();
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public Page<UserDTO> findByPage(int page, int size, String sortBy, String direction) {
        return null;
    }
}
