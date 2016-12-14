package com.websystique.springmvc.service.implementation;

import com.websystique.springmvc.controller.ParentRestController;
import com.websystique.springmvc.model.Parent;
import com.websystique.springmvc.repository.ParentRepository;
import com.websystique.springmvc.service.BaseUserService;
import com.websystique.springmvc.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yangyma on 11/27/16.
 */
@Service
public class ParentServiceImpl implements ParentService {

    @Autowired
    ParentRepository parentRepository;

    @Override
    public Parent findByUsername(String username) {
        return parentRepository.findByUsername(username);
    }

    @Override
    public Parent findById(Long id) {
        return parentRepository.findOne(id);
    }

    @Override
    public Parent findByEmailAddress(String emailAddress) {
        return parentRepository.findByEmailAddress(emailAddress);
    }

    @Override
    public Parent findByMobilePhoneNumber(String mobilePhoneNumber) {
        return parentRepository.findByMobilePhoneNumber(mobilePhoneNumber);
    }

    @Override
    public Parent save(Parent parent) {
        return parentRepository.save(parent);
    }
}
