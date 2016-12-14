package com.websystique.springmvc.service;

import com.websystique.springmvc.types.BaseUser;

/**
 * Created by yangyma on 12/1/16.
 */
public interface BaseUserService<T extends BaseUser> {

    T findById(Long id);

    T findByUsername(String username);

    T findByEmailAddress(String emailAddress);

    T findByMobilePhoneNumber(String mobilePhoneNumber);

    T save(T user);

}
