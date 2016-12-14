package com.websystique.springmvc.service;

import com.websystique.springmvc.model.SchoolAdmin;

import java.util.List;

/**
 * Created by kevin on 2016/11/27.
 */
public interface SchoolAdminService extends BaseUserService<SchoolAdmin> {

    List<SchoolAdmin> findAll();

    List<SchoolAdmin> findByIsAudited(Boolean isAudited);

    SchoolAdmin update(SchoolAdmin schoolAdmin);

    Boolean deleteById(Long id);
}
