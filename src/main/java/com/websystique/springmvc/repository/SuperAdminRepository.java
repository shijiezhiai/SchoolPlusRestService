package com.websystique.springmvc.repository;

import com.websystique.springmvc.model.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by yangyma on 11/24/16.
 */
@Repository
public interface SuperAdminRepository extends JpaRepository<SuperAdmin, Long> {

    SuperAdmin findByUsername(String username);

    SuperAdmin findByEmailAddress(String emailAddress);

    SuperAdmin findByMobilePhoneNumber(String mobilePhoneNumber);

    @Query("SELECT super from SuperAdmin super WHERE super.username = ?1 and super.passwdMd5 = ?2")
    SuperAdmin findByUsernameAndPasswd(String username, String passwd);

}
