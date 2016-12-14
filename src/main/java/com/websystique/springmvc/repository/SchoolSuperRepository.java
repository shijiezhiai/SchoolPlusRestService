package com.websystique.springmvc.repository;

import java.util.List;
import com.websystique.springmvc.model.SchoolSuper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by yangyma on 11/24/16.
 */
@Repository
public interface SchoolSuperRepository extends JpaRepository<SchoolSuper, Long> {
    List<SchoolSuper> findByIsAudited(Boolean isAudited);

    @Query("SELECT schoolSuper FROM SchoolSuper schoolSuper WHERE schoolSuper.school.name = ?1")
    List<SchoolSuper> findBySchoolName(String schoolName);

    @Query("SELECT schoolSuper FROM SchoolSuper schoolSuper " +
            "WHERE schoolSuper.school.name = ?1 AND schoolSuper.isAudited = ?2")
    List<SchoolSuper> findBySchoolNameAndIsAudited(String school, Boolean isAudited);

    SchoolSuper findByUsername(String username);

    SchoolSuper findByEmailAddress(String emailAddress);

    SchoolSuper findByMobilePhoneNumber(String mobilePhoneNumber);
}
