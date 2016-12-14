package com.websystique.springmvc.service;

import java.util.List;
import com.websystique.springmvc.model.SchoolSuper;

/**
 * Created by kevin on 2016/11/24.
 */
public interface SchoolSuperService extends BaseUserService<SchoolSuper> {

    List<SchoolSuper> findAll();

    List<SchoolSuper> findByIsAudited(Boolean isAudited);

    List<SchoolSuper> findBySchoolName(String schoolName);

    List<SchoolSuper> findBySchoolNameAndIsAudited(String school, Boolean isAudited);

    Boolean deleteById(Long id);

}
