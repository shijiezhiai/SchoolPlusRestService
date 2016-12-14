package com.websystique.springmvc.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.websystique.springmvc.service.AdminService;
import com.websystique.springmvc.model.Admin;
import com.websystique.springmvc.repository.AdminRepository;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminRepository adminRepo;


    @Override
    public List<Admin> findAll() {
        return adminRepo.findAll();
    }

	@Override
	public Admin findById(Long id) {
		return adminRepo.findOne(id);
	}

    @Override
    public Admin findByUsername(String username) {
        return adminRepo.findByUsername(username);
    }

    @Override
    public Admin findByEmailAddress(String emailAddress) {
        return adminRepo.findByEmailAddress(emailAddress);
    }

    @Override
    public Admin findByMobilePhoneNumber(String mobilePhoneNumber) {
        return adminRepo.findByMobilePhoneNumber(mobilePhoneNumber);
    }

    @Override
	@Transactional
	public Admin save(Admin admin) {
		return adminRepo.save(admin);
	}

	@Transactional
	public void deleteById(Long id) {
		adminRepo.delete(id);
	}

}
