package com.websystique.springmvc.service;

import java.util.List;

import com.websystique.springmvc.model.Admin;


public interface AdminService extends BaseUserService<Admin> {
	
	void deleteById(Long id);

	List<Admin> findAll();

}
