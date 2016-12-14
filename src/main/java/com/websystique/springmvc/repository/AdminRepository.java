package com.websystique.springmvc.repository;

import java.util.List;
import com.websystique.springmvc.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by yangyma on 11/21/16.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    @Query("SELECT admin FROM Admin admin WHERE admin.username = ?1")
    Admin findByUsername(String username);

    Admin findByEmailAddress(String emailAddress);

    Admin findByMobilePhoneNumber(String mobilePhoneNumber);
}
