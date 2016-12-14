package com.websystique.springmvc.service.implementation;

import java.util.List;

import com.websystique.springmvc.model.SchoolSuper;
import com.websystique.springmvc.repository.SchoolSuperRepository;
import com.websystique.springmvc.service.BaseUserService;
import com.websystique.springmvc.service.SchoolSuperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yangyma on 11/24/16.
 */
@Service
public class SchoolSuperServiceImpl implements SchoolSuperService {

    @Autowired
    SchoolSuperRepository schoolSuperRepository;

    @Override
    public SchoolSuper findById(Long id) {
        return schoolSuperRepository.findOne(id);
    }

    @Override
    public List<SchoolSuper> findAll() {
        return schoolSuperRepository.findAll();
    }

    @Override
    public SchoolSuper findByUsername(String username) {
        return schoolSuperRepository.findByUsername(username);
    }

    @Override
    public List<SchoolSuper> findByIsAudited(Boolean isAudited) {
        return schoolSuperRepository.findByIsAudited(isAudited);
    }

    @Override
    public List<SchoolSuper> findBySchoolName(String schoolName) {
        return schoolSuperRepository.findBySchoolName(schoolName);
    }

    @Override
    public List<SchoolSuper> findBySchoolNameAndIsAudited(String school, Boolean isAudited) {
        return schoolSuperRepository.findBySchoolNameAndIsAudited(school, isAudited);
    }

    @Override
    public Boolean deleteById(Long id) {
        try {
            schoolSuperRepository.delete(id);
        } catch (Exception e) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    @Override
    public SchoolSuper findByEmailAddress(String emailAddress) {
        return schoolSuperRepository.findByEmailAddress(emailAddress);
    }

    @Override
    public SchoolSuper findByMobilePhoneNumber(String mobilePhoneNumber) {
        return schoolSuperRepository.findByMobilePhoneNumber(mobilePhoneNumber);
    }

    @Override
    public SchoolSuper save(SchoolSuper schoolSuper) {
        return schoolSuperRepository.save(schoolSuper);
    }
}
