package com.websystique.springmvc.service.implementation;

import com.websystique.springmvc.service.BaseUserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.websystique.springmvc.model.SuperAdmin;
import com.websystique.springmvc.repository.SuperAdminRepository;
import com.websystique.springmvc.service.SuperAdminService;
import org.springframework.stereotype.Service;

/**
 * Created by yangyma on 11/24/16.
 */
@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    @Autowired
    SuperAdminRepository superRepo;

    @Override
    public SuperAdmin findByEmailAddress(String emailAddress) {
        return superRepo.findByEmailAddress(emailAddress);
    }

    @Override
    public SuperAdmin findByMobilePhoneNumber(String mobilePhoneNumber) {
        return superRepo.findByMobilePhoneNumber(mobilePhoneNumber);
    }

    @Override
    public SuperAdmin findById(Long id) {
        return superRepo.findOne(id);
    }

    @Override
    public SuperAdmin findByUsername(String username) {
        return superRepo.findByUsername(username);
    }

    @Override
    public SuperAdmin save(SuperAdmin superAdmin) {
        return superRepo.save(superAdmin);
    }
}
