package com.websystique.springmvc.repository;

import com.websystique.springmvc.model.SchoolAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yangyma on 11/23/16.
 */
@Repository
public interface SchoolAdminRepository extends JpaRepository<SchoolAdmin, Long> {

    List<SchoolAdmin> findByIsAudited(Boolean isAudited);

    List<SchoolAdmin> findByName(String name);

    SchoolAdmin findByUsername(String username);

    SchoolAdmin findByEmailAddress(String email);

    SchoolAdmin findByMobilePhoneNumber(String mobile);

}
