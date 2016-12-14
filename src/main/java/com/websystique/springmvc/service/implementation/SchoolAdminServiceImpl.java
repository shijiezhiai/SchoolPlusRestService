package com.websystique.springmvc.service.implementation;

import com.websystique.springmvc.model.SchoolAdmin;
import com.websystique.springmvc.repository.SchoolAdminRepository;
import com.websystique.springmvc.service.BaseUserService;
import com.websystique.springmvc.service.SchoolAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kevin on 2016/11/27.
 */
@Service
public class SchoolAdminServiceImpl implements SchoolAdminService {
    @Autowired
    SchoolAdminRepository schoolAdminRepository;

    @Override
    public List<SchoolAdmin> findAll() {
        return schoolAdminRepository.findAll();
    }

    @Override
    public List<SchoolAdmin> findByIsAudited(Boolean isAudited) {
        return schoolAdminRepository.findByIsAudited(isAudited);
    }

    @Override
    public SchoolAdmin findById(Long id) {
        return schoolAdminRepository.findOne(id);
    }

    @Override
    public SchoolAdmin findByUsername(String username) {
        return schoolAdminRepository.findByUsername(username);
    }

    @Override
    public SchoolAdmin findByEmailAddress(String email) {
        return schoolAdminRepository.findByEmailAddress(email);
    }

    @Override
    public SchoolAdmin findByMobilePhoneNumber(String mobile) {
        return schoolAdminRepository.findByMobilePhoneNumber(mobile);
    }

    @Override
    public SchoolAdmin update(SchoolAdmin schoolAdmin) {
        return schoolAdminRepository.save(schoolAdmin);
    }

    @Override
    public Boolean deleteById(Long id) {
        try {
            schoolAdminRepository.delete(id);
        } catch (Exception e) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    @Override
    public SchoolAdmin save(SchoolAdmin schoolAdmin) {
        return schoolAdminRepository.save(schoolAdmin);
    }
}
