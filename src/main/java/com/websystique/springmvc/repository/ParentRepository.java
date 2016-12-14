package com.websystique.springmvc.repository;

import com.websystique.springmvc.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yangyma on 11/23/16.
 */
@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    Parent findByUsername(String username);

    Parent findByEmailAddress(String emailAddress);

    Parent findByMobilePhoneNumber(String mobilePhoneNumber);
}
